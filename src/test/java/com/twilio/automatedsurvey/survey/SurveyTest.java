package com.twilio.automatedsurvey.survey;

import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SurveyTest {

    @Test
    public void shouldntReturnAnythingWhenSurveyHasNoQuestion() {
        Survey survey = new Survey("a new survey");

        Optional<Question> question = survey.getQuestionByNumber(1);

        assertThat(question.isPresent(), is(false));
    }

    @Test
    public void shouldReturnTheQuestionWhenItsTheOnlyOneInTheSurvey() {
        Survey survey = new Survey("a new survey");
        survey.addQuestion(new Question("a question?", "voice"));

        Optional<Question> question = survey.getQuestionByNumber(1);

        assertThat(question.isPresent(), is(true));
    }

    @Test
    public void shouldReturnTheSecondQuestionWhenRequested() {
        Survey survey = new Survey("a new survey");
        Question firstQuestion = new Question("a question?", "voice");
        Question expectedQuestion = new Question("two questions?", "numeric");
        survey.addQuestion(firstQuestion);
        survey.addQuestion(expectedQuestion);

        Optional<Question> question = survey.getQuestionByNumber(2);

        assertThat(question.isPresent(), is(true));
        assertThat(question.get().getBody(), is(expectedQuestion.getBody()));
        assertThat(question.get().getType(), is(expectedQuestion.getType()));
    }


}
