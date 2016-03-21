package com.twilio.automatedsurvey.servlets;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.twilio.automatedsurvey.survey.Survey;
import com.twilio.automatedsurvey.survey.loader.SurveyLoader;
import com.twilio.automatedsurvey.survey.SurveyRepository;
import com.twilio.sdk.verbs.TwiMLResponse;

import javax.inject.Singleton;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
            twilioResponse = twilioResponseFactory.build(lastSurvey.orElse(null));
            this.responseWriter.writeIn(response, twilioResponse.toEscapedXML());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        Long surveyId = Long.parseLong(request.getParameter("survey"));
        Long questionId = Long.parseLong(request.getParameter("question"));

        Survey survey = surveyRepo.find(surveyId).orElseThrow(() -> new RuntimeException("Survey was not found"));

        String answerKey = survey.getQuestionsAnswerKey(questionId)
                .orElseThrow(() -> new RuntimeException("Impossible to find answer key"));

        survey.answer(questionId, request.getParameter(answerKey));
    }


}
