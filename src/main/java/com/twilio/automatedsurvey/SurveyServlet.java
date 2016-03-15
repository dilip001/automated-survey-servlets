package com.twilio.automatedsurvey;

import com.twilio.sdk.verbs.TwiMLException;
import com.twilio.sdk.verbs.TwiMLResponse;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SurveyServlet extends HttpServlet{
    private SurveyRepository surveyRepo;
    private TwilioResponseFactory twilioResponseFactory;
    private ResponseWriter responseWriter;

    public SurveyServlet(SurveyRepository surveyRepo, TwilioResponseFactory twilioResponseFactory,
                         ResponseWriter responseWriter) {
        this.surveyRepo = surveyRepo;
        this.twilioResponseFactory = twilioResponseFactory;
        this.responseWriter = responseWriter;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        Survey lastSurvey = surveyRepo.findLast();
        TwiMLResponse twilioResponse = null;
        try {
            twilioResponse = twilioResponseFactory.build(lastSurvey);
        } catch (TwiMLException e) {
            e.printStackTrace();
        }
        this.responseWriter.writeIn(response, twilioResponse);
    }
}
