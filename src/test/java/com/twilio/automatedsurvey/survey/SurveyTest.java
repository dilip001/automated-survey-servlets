package com.twilio.automatedsurvey.survey;

import com.twilio.automatedsurvey.servlets.MockedHttpServletRequestFactory;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SurveyTest {

    @Test
    public void shouldAnswerToNumericQuestion() {
        Survey survey = new Survey("Survey");
        survey.addQuestion(new Question(1L, "Question?", QuestionTypes.numeric));

        HttpServletRequest request = MockedHttpServletRequestFactory.getMockedRequestWithParameters(new HashMap<String, String>() {{
            put("question", "1");
            put("Digits", "1");
        }});

        Question question = survey.answer(request);

        assertThat(question.getAnswer(), is("1"));
    }

    @Test
    public void shouldAnswerToYesNoQuestion() {
        Survey survey = new Survey("Survey");
        survey.addQuestion(new Question(1L, "Question?", QuestionTypes.yesno));

        HttpServletRequest request = MockedHttpServletRequestFactory.getMockedRequestWithParameters(new HashMap<String, String>() {{
            put("question", "1");
            put("Digits", "1");
        }});

        Question question = survey.answer(request);

        assertThat(question.getAnswer(), is("1"));
    }

    @Test
    public void shouldAnswerToVoiceQuestion() {
        Survey survey = new Survey("Survey");
        survey.addQuestion(new Question(1L, "Question?", QuestionTypes.voice));

        HttpServletRequest request = MockedHttpServletRequestFactory.getMockedRequestWithParameters(new HashMap<String, String>() {{
            put("question", "1");
            put("RecordingUrl", "answer");
        }});

        Question question = survey.answer(request);

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


}
