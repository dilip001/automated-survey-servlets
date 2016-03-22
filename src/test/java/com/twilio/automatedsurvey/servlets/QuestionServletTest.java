package com.twilio.automatedsurvey.servlets;

import com.twilio.automatedsurvey.servlets.twimlquestions.NumericQuestion;
import com.twilio.automatedsurvey.servlets.twimlquestions.VoiceQuestion;
import com.twilio.automatedsurvey.survey.Question;
import com.twilio.automatedsurvey.survey.Survey;
import com.twilio.automatedsurvey.survey.SurveyRepository;
import com.twilio.sdk.verbs.TwiMLException;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class QuestionServletTest {

    private ResponseWriter responseWriter;
    private SurveyRepository surveyRepository;
    private HttpServletResponse servletResponse;

    @Before
    public void setup() {
        responseWriter = mock(ResponseWriter.class);
        surveyRepository = mock(SurveyRepository.class);
        servletResponse = mock(HttpServletResponse.class);
    }

    @Test
    public void shouldRespondWithSpecifiedQuestion() throws TwiMLException, IOException {
        Question voiceQuestion = new Question(1L, "Is that a question?", Question.QuestionTypes.valueOf("voice"));
        when(surveyRepository.find(anyLong())).thenReturn(Optional.of(surveyWithQuestion(voiceQuestion)));

        HttpServletRequest servletRequest = MockedHttpServletRequestFactory.getMockedRequestWithParameters(new HashMap() {{
            put("question", "1");
            put("survey", "1");
        }});

        QuestionServlet questionServlet = new QuestionServlet(surveyRepository, responseWriter);

        questionServlet.doGet(servletRequest, servletResponse);

        String expectedXmlResponse = new VoiceQuestion(1L, voiceQuestion).toEscapedXML();
        verify(responseWriter, times(1)).writeIn(eq(servletResponse), eq(expectedXmlResponse));
    }

    @Test
    public void shouldRespondWithFirstQuestionWhenNoQuestionIdIsProvided() throws TwiMLException, IOException {
        Question numericQuestion = new Question("Is that a question?", Question.QuestionTypes.valueOf("numeric"));
        when(surveyRepository.find(anyLong())).thenReturn(Optional.of(surveyWithQuestion(numericQuestion)));

        HttpServletRequest servletRequest = MockedHttpServletRequestFactory.getMockedRequestWithParameters(new HashMap() {{
            put("survey", "1");
        }});

        QuestionServlet questionServlet = new QuestionServlet(surveyRepository, responseWriter);

        questionServlet.doGet(servletRequest, servletResponse);

        String expectedXmlResponse = new NumericQuestion(1L, numericQuestion).toEscapedXML();
        verify(responseWriter, times(1)).writeIn(eq(servletResponse), eq(expectedXmlResponse));
    }

    private Survey surveyWithQuestion(Question... questions) {
        Survey survey = new Survey(1L, "a new survey");
        Arrays.stream(questions).forEach((Question question) -> survey.addQuestion(question));
        return survey;
    }

}
