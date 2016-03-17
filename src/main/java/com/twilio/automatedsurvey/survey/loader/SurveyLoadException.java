package com.twilio.automatedsurvey.survey.loader;

public class SurveyLoadException extends RuntimeException {

    public SurveyLoadException(Throwable e) {
        super("Impossible to load survey.json", e);
    }

    public SurveyLoadException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
