package com.twilio.automatedsurvey.servlets;

import com.twilio.automatedsurvey.survey.Question;
import com.twilio.automatedsurvey.survey.Survey;
import com.twilio.automatedsurvey.survey.SurveyRepository;
import com.twilio.sdk.verbs.TwiMLException;
import com.twilio.sdk.verbs.TwiMLResponse;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class QuestionServletTest {

    @Test
    public void shouldRespondWhenSurveyHasOnlyOneQuestion() throws TwiMLException, IOException {
        ResponseWriter responseWriter = mock(ResponseWriter.class);
        SurveyRepository surveyRepository = mock(SurveyRepository.class);

        Question voiceQuestion = new Question("Is that a question?", "voice");
        when(surveyRepository.find(anyLong())).thenReturn(Optional.of(surveyWithQuestion(voiceQuestion)));

        HttpServletResponse servletResponse = mock(HttpServletResponse.class);
        HttpServletRequest servletRequest = getMockedRequestWithParameters(new HashMap(){{
            put("question", "1");
            put("survey", "1");
        }});

        QuestionServlet questionServlet = new QuestionServlet(surveyRepository, responseWriter);

        questionServlet.doGet(servletRequest, servletResponse);

        verify(responseWriter, times(1)).writeIn(eq(servletResponse), any(TwiMLResponse.class));
    }

    private HttpServletRequest getMockedRequestWithParameters(Map<String, String> parameters) {
        HttpServletRequest servletRequest = mock(HttpServletRequest.class);

        for (String key : parameters.keySet()) {
            when(servletRequest.getParameter(key)).thenReturn(parameters.get(key));
        }
        return servletRequest;
    }

    private Survey surveyWithQuestion(Question... questions) {
        Survey survey = new Survey(1L, "a new survey");
        Arrays.stream(questions).forEach((Question question) -> survey.addQuestion(question));
        return survey;
    }

}
