package com.twilio.automatedsurvey;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
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
            this.responseWriter.writeIn(response, twilioResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
