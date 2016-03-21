package com.twilio.automatedsurvey.servlets;

import com.twilio.automatedsurvey.IntegrationTestHelper;
import com.twilio.automatedsurvey.survey.Question;
import com.twilio.automatedsurvey.survey.Survey;
import com.twilio.automatedsurvey.survey.SurveyRepository;
import com.twilio.sdk.verbs.TwiMLException;
import com.twilio.sdk.verbs.TwiMLResponse;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class SurveyServletTest {

    @Test
    public void shouldWelcomeAndRedirectRequest() throws TwiMLException, IOException {
        SurveyRepository surveyRepo = mock(SurveyRepository.class);
        when(surveyRepo.findLast()).thenReturn(Optional.empty());

        TwilioResponseFactory twilioResponseFactory = mock(TwilioResponseFactory.class);
        TwiMLResponse twiMLResponse = new TwiMLResponse();
        when(twilioResponseFactory.build(any(Survey.class))).thenReturn(twiMLResponse);

        ResponseWriter responseWriter = mock(ResponseWriter.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        SurveyServlet surveyServlet = new SurveyServlet(surveyRepo, twilioResponseFactory, responseWriter);

        surveyServlet.doGet(mock(HttpServletRequest.class), response);

        verify(responseWriter, times(1)).writeIn(response, twiMLResponse.toEscapedXML());
    }

    @Test
    public void shouldAllowTheUserToAnswerAVoiceSurveyQuestion() throws IOException {
        SurveyRepository surveyRepository = mock(SurveyRepository.class);
        Survey mockedSurvey = mock(Survey.class);
        when(surveyRepository.find(anyLong())).thenReturn(Optional.of(mockedSurvey));
        when(mockedSurvey.getQuestionsAnswerKey(1L)).thenReturn(Optional.of("RecordingUrl"));

        SurveyServlet surveyServlet = new SurveyServlet(surveyRepository, mock(TwilioResponseFactory.class),
                mock(ResponseWriter.class));

        HttpServletRequest request = MockedHttpServletRequestFactory.getMockedRequestWithParameters(new HashMap<String, String>() {{
            put("survey", "1");
            put("question", "1");
            put("RecordingUrl", "the answer");
        }});

        surveyServlet.doPost(request, mock(HttpServletResponse.class));

        verify(mockedSurvey, times(1)).answer(1L, "the answer");
    }

    @Test
    public void shouldAllowTheUserToAnswerANumericSurveyQuestion() throws IOException {
        SurveyRepository surveyRepository = mock(SurveyRepository.class);
        Survey mockedSurvey = mock(Survey.class);
        when(surveyRepository.find(anyLong())).thenReturn(Optional.of(mockedSurvey));
        when(mockedSurvey.getQuestionsAnswerKey(anyLong())).thenReturn(Optional.of("Digits"));

        SurveyServlet surveyServlet = new SurveyServlet(surveyRepository, mock(TwilioResponseFactory.class),
                mock(ResponseWriter.class));

        HttpServletRequest request = MockedHttpServletRequestFactory.getMockedRequestWithParameters(new HashMap<String, String>() {{
            put("survey", "1");
            put("question", "1");
            put("Digits", "1");
        }});

        surveyServlet.doPost(request, mock(HttpServletResponse.class));

        verify(mockedSurvey, times(1)).answer(1L, "1");
    }

    @Test
    public void shouldAllowTheUserToAnswerAYesNoSurveyQuestion() throws IOException {
        SurveyRepository surveyRepository = mock(SurveyRepository.class);
        Survey mockedSurvey = mock(Survey.class);
        when(surveyRepository.find(anyLong())).thenReturn(Optional.of(mockedSurvey));
        when(mockedSurvey.getQuestionsAnswerKey(anyLong())).thenReturn(Optional.of("Digits"));

        SurveyServlet surveyServlet = new SurveyServlet(surveyRepository, mock(TwilioResponseFactory.class),
                mock(ResponseWriter.class));

        HttpServletRequest request = MockedHttpServletRequestFactory.getMockedRequestWithParameters(new HashMap<String, String>() {{
            put("survey", "1");
            put("question", "1");
            put("Digits", "1");
        }});

        surveyServlet.doPost(request, mock(HttpServletResponse.class));

        verify(mockedSurvey, times(1)).answer(1L, "1");
    }
}
