package com.twilio.automatedsurvey;

public class SurveyLoadException extends RuntimeException {

    public SurveyLoadException(Throwable e) {
        super("Impossible to load survey.json", e);
    }

    public SurveyLoadException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
