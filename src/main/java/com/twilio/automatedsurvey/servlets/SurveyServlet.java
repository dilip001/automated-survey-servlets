package com.twilio.automatedsurvey.servlets;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.twilio.automatedsurvey.survey.Question;
import com.twilio.automatedsurvey.survey.Survey;
import com.twilio.automatedsurvey.survey.loader.SurveyLoader;
import com.twilio.automatedsurvey.survey.SurveyRepository;
import com.twilio.sdk.verbs.*;

import javax.inject.Singleton;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

@Singleton
public class SurveyServlet extends HttpServlet{
    private SurveyRepository surveyRepo;
    private TwilioResponseFactory twilioResponseFactory;
    private ResponseWriter responseWriter;

    @Inject
    public SurveyServlet(SurveyRepository surveyRepo, TwilioResponseFactory twilioResponseFactory,
                         ResponseWriter responseWriter) {
        this.surveyRepo = surveyRepo;
        this.twilioResponseFactory = twilioResponseFactory;
        this.responseWriter = responseWriter;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);

        try {
            if (isSmsAnswer(request)) {
                String survey = session.getAttribute("lastSurvey").toString();
                String lastQuestion = session.getAttribute("lastQuestion").toString();

                redirectToAnswerEndpoint(response, survey, lastQuestion);
            } else {
                Survey newSurvey = createSurveyInstance();
                TwiMLResponse twilioResponse;

                String message = String.format("Welcome to the %s survey", newSurvey.getTitle());
                Verb welcomeMessage = isSms(request) ? new Message(message) : new Say(message);
                twilioResponse = twilioResponseFactory.build(newSurvey, welcomeMessage);
                this.responseWriter.writeIn(response, twilioResponse.toEscapedXML());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    private Survey createSurveyInstance() {
        SurveyLoader loader = new SurveyLoader("survey.json");
        return surveyRepo.add(loader.load());
    }

    private TwiMLResponse buildWelcomeMessage(HttpServletRequest request, Optional<Survey> lastSurvey) throws TwiMLException {
        TwiMLResponse twilioResponse;
        String message = String.format("Welcome to the %s survey", lastSurvey.map((Survey s) -> s.getTitle()).orElse(""));
        Verb welcomeMessage = isSms(request) ? new Message(message) : new Say(message);
        twilioResponse = twilioResponseFactory.build(lastSurvey.orElse(null), welcomeMessage);
        return twilioResponse;
    }

    private void redirectToAnswerEndpoint(HttpServletResponse response, String survey, String lastQuestion) throws TwiMLException, IOException {
        TwiMLResponse twiMLResponse = new TwiMLResponse();
        Redirect redirect = new Redirect(String.format("survey?survey=%s&question=%s", survey, lastQuestion));
        redirect.setMethod("POST");
        twiMLResponse.append(redirect);
        this.responseWriter.writeIn(response, twiMLResponse.toEscapedXML());
    }

    private boolean isSmsAnswer(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        return session.getAttribute("lastSurvey") != null;
    }

    private boolean isSms(HttpServletRequest request) {
        return request.getParameter("MessageSid") != null;
    }

    @Override
    @Transactional
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long surveyId = Long.parseLong(request.getParameter("survey"));

        Survey survey = surveyRepo.find(surveyId).orElseThrow(() -> new RuntimeException("Survey was not found"));

        Question answeredQuestion = isSms(request) ? survey.answerSMS(request.getParameterMap()) :
                survey.answerCall(request.getParameterMap());

        surveyRepo.update(survey);

        Optional<Question> nextQuestion = survey.getNextQuestion(answeredQuestion);

        TwiMLResponse twiMLResponse = nextQuestion.map((Question q) -> buildRedirectTwiMLMessage(surveyId, q))
                .orElse(buildThankYouTwiMLResponse(survey.getTitle(), request));

        responseWriter.writeIn(response, twiMLResponse.toEscapedXML());
    }

    private TwiMLResponse buildThankYouTwiMLResponse(String surveyTitle, HttpServletRequest request) {
        try {
            TwiMLResponse response = new TwiMLResponse();

            Verb message;
            String realMessage = String.format("Thank you for taking the %s survey. Good bye.", surveyTitle);
            if (isSms(request)) {
                message = new Message(realMessage);
            } else {
                message = new Say(realMessage);
            }

            response.append(message);
            return response;
        } catch (TwiMLException e) {
            throw new RuntimeException(e);
        }
    }

    private TwiMLResponse buildRedirectTwiMLMessage(Long surveyId, Question q) {
        try {
            TwiMLResponse response = new TwiMLResponse();
            Redirect redirect = new Redirect(String.format("question?survey=%s&question=%s", surveyId, q.getId()));
            redirect.setMethod("GET");
            response.append(redirect);
            return response;
        } catch (TwiMLException e) {
            throw new RuntimeException(e);
        }
    }


}
