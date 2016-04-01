package com.twilio.automatedsurvey.survey;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SurveyTest {

    @Test
    public void shouldAnswerToNumericQuestion() {
        Survey survey = new Survey("Survey");
        survey.addQuestion(new Question(1L, "Question?", QuestionTypes.numeric));

        Map<String, String[]> parameterMap = new HashMap<String, String[]>() {{
            put("question", new String[]{"1"});
            put("Digits", new String[]{"1"});
        }};

        Question question = survey.answerCall(parameterMap);

        assertThat(question.getAnswer(), is("1"));
    }

    @Test
    public void shouldAnswerToYesNoQuestion() {
        Survey survey = new Survey("Survey");
        survey.addQuestion(new Question(1L, "Question?", QuestionTypes.yesno));


        Map<String, String[]> parameterMap = new HashMap<String, String[]>() {{
            put("question", new String[]{"1"});
            put("Digits", new String[]{"1"});
        }};

        Question question = survey.answerCall(parameterMap);

        assertThat(question.getAnswer(), is("1"));
    }

    @Test
    public void shouldAnswerToVoiceQuestion() {
        Survey survey = new Survey("Survey");
        survey.addQuestion(new Question(1L, "Question?", QuestionTypes.voice));


        Map<String, String[]> parameterMap = new HashMap<String, String[]>() {{
            put("question", new String[]{"1"});
            put("RecordingUrl", new String[]{"answer"});
        }};

        Question question = survey.answerCall(parameterMap);

        assertThat(question.getAnswer(), is("answer"));
    }

    @Test
    public void shouldReturnNextQuestion()  {
        Survey survey = new Survey("Survey");
        Question question1 = new Question(1L, "Question?", QuestionTypes.voice);
        Question question2 = new Question(2L, "Question 2?", QuestionTypes.voice);

        survey.addQuestion(question1);
        survey.addQuestion(question2);

        Optional<Question> nextQuestion = survey.getNextQuestion(question1);

        assertThat(nextQuestion.get(), is(question2));
    }

    @Test
    public void shouldReturnEmptyIfTheresNoNextQuestion() {
        Survey survey = new Survey("Survey");
        Question question1 = new Question(1L, "Question?", QuestionTypes.voice);

        survey.addQuestion(question1);

        Optional<Question> nextQuestion = survey.getNextQuestion(question1);

        assertThat(nextQuestion.isPresent(), is(false));
    }

    @Test
    public void shouldReturnFirstQuestion() {
        Survey survey = new Survey("Survey");
        Question question1 = new Question(1L, "Question?", QuestionTypes.voice);

        survey.addQuestion(question1);

        Optional<Question> nextQuestion = survey.getFirstQuestion();

        assertThat(nextQuestion.get(), is(question1));
    }

    @Test
    public void shouldAnswerUsingTranscriptionWhenPresent() {
        Survey survey = new Survey("Survey");
        Question question = new Question(1L, "question?", QuestionTypes.voice);

        survey.addQuestion(question);

        final String expectedAnswer = "transcription";

        Map<String, String[]> parameters = new HashMap<String, String[]>(){{
            put("question", new String[]{"1"});
            put("RecordingUrl", new String[]{"recording url"});
            put("TranscriptionText", new String[]{expectedAnswer});
        }};

        Question answeredQuestion = survey.answerCall(parameters);

        assertThat(answeredQuestion.getAnswer(), is(expectedAnswer));
    }


}
