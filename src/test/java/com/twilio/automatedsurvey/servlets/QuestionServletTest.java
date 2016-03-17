package com.twilio.automatedsurvey.servlets;

import com.twilio.automatedsurvey.survey.Question;
import com.twilio.automatedsurvey.survey.Survey;
import com.twilio.automatedsurvey.survey.SurveyRepository;
import com.twilio.sdk.verbs.TwiMLException;
import com.twilio.sdk.verbs.TwiMLResponse;
import org.junit.Before;
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
    public void shouldRespondWhenSurveyHasOnlyOneQuestion() throws TwiMLException, IOException {
        Question voiceQuestion = new Question("Is that a question?", "voice");
        when(surveyRepository.find(anyLong())).thenReturn(Optional.of(surveyWithQuestion(voiceQuestion)));

        HttpServletRequest servletRequest = createMockedValidRequest();

        QuestionServlet questionServlet = new QuestionServlet(surveyRepository, responseWriter);

        questionServlet.doGet(servletRequest, servletResponse);

        String expectedXmlResponse = new VoiceResponse(voiceQuestion).toEscapedXML();
        verify(responseWriter, times(1)).writeIn(eq(servletResponse), eq(expectedXmlResponse));
    }

    private HttpServletRequest createMockedValidRequest() {
        return getMockedRequestWithParameters(new HashMap(){{
                put("question", "1");
                put("survey", "1");
            }});
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
