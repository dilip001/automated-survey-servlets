package com.twilio.automatedsurvey.survey;

import com.twilio.automatedsurvey.survey.loader.SurveyLoadException;
import com.twilio.automatedsurvey.survey.loader.SurveyLoader;
import org.junit.Test;

import java.util.Set;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertThat;

public class SurveyLoaderTest {

    @Test
    public void shouldReadSurveyFromJsonFile() {
        Survey survey = new SurveyLoader("survey.json").load();

        assertThat(survey.getTitle(), is("test survey"));
    }

    @Test
    public void shouldReadSurveysQuestionFromJsonFile() {
        Survey survey = new SurveyLoader("survey.json").load();
        Set<Question> questions = survey.getQuestions();

        assertThat(questions.size(), is(2));
        assertThat(questions, hasItem(new Question("What is your full name?", QuestionTypes.voice)));
        assertThat(questions, hasItem(new Question("In a scale of 1 to 9, how would you rate the quality of this call?",
                QuestionTypes.numeric)));
    }

    @Test(expected = SurveyLoadException.class)
    public void shouldThrowAnExceptionWhenImpossibleToReadFile() {
        new SurveyLoader("/non_existent_file.json").load();
    }

}
