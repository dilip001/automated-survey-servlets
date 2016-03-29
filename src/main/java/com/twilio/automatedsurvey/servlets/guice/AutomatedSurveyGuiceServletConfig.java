package com.twilio.automatedsurvey.servlets.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.GuiceServletContextListener;
import com.twilio.automatedsurvey.servlets.guice.AutomatedSurveyServletModule;

import java.util.HashMap;

public class AutomatedSurveyGuiceServletConfig extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {

        JpaPersistModule persistModule = new JpaPersistModule("jpaUnit");
        persistModule.properties(new HashMap<String, String>() {{
            put("javax.persistence.jdbc.url", System.getenv("DATABASE_URL"));
            put("javax.persistence.jdbc.user", System.getenv("DATABASE_USER"));
            put("javax.persistence.jdbc.password", System.getenv("DATABASE_PASSWORD"));
        }});

        return Guice.createInjector(persistModule, new AutomatedSurveyServletModule());
    }
}