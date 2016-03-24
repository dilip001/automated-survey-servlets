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
    @Transactional
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SurveyLoader loader = new SurveyLoader("survey.json");
        surveyRepo.add(loader.load());
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        Optional<Survey> lastSurvey = surveyRepo.findLast();
        TwiMLResponse twilioResponse;
        try {
            String message = String.format("Welcome to the %s survey", lastSurvey.map((Survey s) -> s.getTitle()).orElse(""));
            Verb welcomeMessage = isSms(request) ? new Message(message) : new Say(message);
            twilioResponse = twilioResponseFactory.build(lastSurvey.orElse(null), welcomeMessage);
            this.responseWriter.writeIn(response, twilioResponse.toEscapedXML());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isSms(HttpServletRequest request) {
        return request.getParameter("MessageSid") != null;
    }

    @Override
    @Transactional
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long surveyId = Long.parseLong(request.getParameter("survey"));

        Survey survey = surveyRepo.find(surveyId).orElseThrow(() -> new RuntimeException("Survey was not found"));

        Question answeredQuestion = survey.answer(request);
        surveyRepo.update(survey);

        Optional<Question> nextQuestion = survey.getNextQuestion(answeredQuestion);

        TwiMLResponse twiMLResponse = nextQuestion.map((Question q) -> buildRedirectTwiMLMessage(surveyId, q))
                .orElse(buildThankYouTwiMLResponse(survey.getTitle()));

        responseWriter.writeIn(response, twiMLResponse.toEscapedXML());
    }

    private TwiMLResponse buildThankYouTwiMLResponse(String surveyTitle) {
        try {
            TwiMLResponse response = new TwiMLResponse();
            response.append(new Say(String.format("Thank you for taking the %s survey. Good bye.", surveyTitle)));
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
