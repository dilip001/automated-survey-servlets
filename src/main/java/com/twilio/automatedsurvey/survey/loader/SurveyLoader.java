package com.twilio.automatedsurvey.survey.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.automatedsurvey.survey.Survey;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.Optional;

public class SurveyLoader {

    private final String resourceName;

    public SurveyLoader(String fileName) {
        resourceName = String.format("/%s", fileName);
    }

    public Survey load() {
        try {
            File surveyJsonFile = new File(getResourceURI());

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(surveyJsonFile, Survey.class);
        } catch (Exception e) {
            throw new SurveyLoadException(e);
        }
    }

    private URI getResourceURI() {
        Optional<URL> url = Optional.ofNullable(this.getClass().getResource(resourceName));
        return url.map((URL u) -> {
            try{
                return u.toURI();
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        }).orElseThrow(() -> new SurveyLoadException("not possible to retrieve resource: "+ resourceName));
    }
}
