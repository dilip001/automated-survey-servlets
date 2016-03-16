package com.twilio.automatedsurvey;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SurveyLoaderTest {

    @Test
    public void shouldReadSurveyFromJsonFile() {
        Survey survey = new SurveyLoader("survey.json").load();

        assertThat(survey.getTitle(), is("test survey"));
    }

    @Test(expected = SurveyLoadException.class)
    public void shouldThrowAnExceptionWhenImpossibleToReadFile() {
        new SurveyLoader("/non_existent_file.json").load();
    }

}
