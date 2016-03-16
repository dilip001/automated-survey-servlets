package com.twilio.automatedsurvey;

import com.google.inject.persist.PersistFilter;
import com.google.inject.servlet.ServletModule;

public class AutomatedSurveyServletModule extends ServletModule {

    @Override
    public void configureServlets() {
        filter("/*").through(PersistFilter.class);
        serve("/survey").with(SurveyServlet.class);
    }

}
