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
        survey.addQuestion(new Question("a question?", Question.QuestionTypes.valueOf("voice")));

        Optional<Question> question = survey.getQuestionByNumber(1);

        assertThat(question.isPresent(), is(true));
    }

    @Test
    public void shouldReturnTheSecondQuestionWhenRequested() {
        Survey survey = new Survey("a new survey");
        Question firstQuestion = new Question("a question?", Question.QuestionTypes.valueOf("voice"));
        Question expectedQuestion = new Question("two questions?", Question.QuestionTypes.valueOf("numeric"));
        survey.addQuestion(firstQuestion);
        survey.addQuestion(expectedQuestion);

        Optional<Question> question = survey.getQuestionByNumber(2);

        assertThat(question.isPresent(), is(true));
        assertThat(question.get().getBody(), is(expectedQuestion.getBody()));
        assertThat(question.get().getType(), is(expectedQuestion.getType()));
    }

    @Test
    public void shouldReturnQuestionsAnswerKeyForNumericQuestion() {
        Survey survey = new Survey("Survey");
        survey.addQuestion(new Question(1L, "Question?", Question.QuestionTypes.numeric));

        Optional<String> questionsAnswerKey = survey.getQuestionsAnswerKey(1L);

        assertThat(questionsAnswerKey.get(), is("Digits"));
    }

    @Test
    public void shouldReturnQuestionsAnswerKeyForYesNoQuestion() {
        Survey survey = new Survey("Survey");
        survey.addQuestion(new Question(1L, "Question?", Question.QuestionTypes.yesno));

        Optional<String> questionsAnswerKey = survey.getQuestionsAnswerKey(1L);

        assertThat(questionsAnswerKey.get(), is("Digits"));
    }

    @Test
    public void shouldReturnQuestionsAnswerKeyForVoiceQuestion() {
        Survey survey = new Survey("Survey");
        survey.addQuestion(new Question(1L, "Question?", Question.QuestionTypes.voice));

        Optional<String> questionsAnswerKey = survey.getQuestionsAnswerKey(1L);

        assertThat(questionsAnswerKey.get(), is("RecordingUrl"));
    }

    @Test
    public void shouldAnswerToSpecificQuestion() {
        Survey survey = new Survey("Survey");
        survey.addQuestion(new Question(1L, "Question?", Question.QuestionTypes.voice));

        Optional<Question> question = survey.answer(1L, "The answer");

        assertThat(question.get().getAnswer(), is("The answer"));
    }


}
