package com.twilio.automatedsurvey.servlets.guice;

import com.google.inject.persist.PersistFilter;
import com.google.inject.servlet.ServletModule;
import com.twilio.automatedsurvey.servlets.IndexServlet;
import com.twilio.automatedsurvey.servlets.QuestionServlet;
import com.twilio.automatedsurvey.servlets.SurveyServlet;

public class AutomatedSurveyServletModule extends ServletModule {

    @Override
    public void configureServlets() {
        filter("/*").through(PersistFilter.class);
        serve("/").with(IndexServlet.class);
        serve("/survey").with(SurveyServlet.class);
        serve("/question").with(QuestionServlet.class);
    }

}
