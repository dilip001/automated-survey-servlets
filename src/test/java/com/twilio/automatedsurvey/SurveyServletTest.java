package com.twilio.automatedsurvey;

import com.twilio.sdk.verbs.TwiMLException;
import com.twilio.sdk.verbs.TwiMLResponse;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;

public class SurveyServletTest {

    @Test
    public void shouldWelcomeAndRedirectRequest() throws TwiMLException {
        SurveyRepository surveyRepo = mock(SurveyRepository.class);

        TwilioResponseFactory twilioResponseFactory = mock(TwilioResponseFactory.class);
        TwiMLResponse twiMLResponse = new TwiMLResponse();
        when(twilioResponseFactory.build(any(Survey.class))).thenReturn(twiMLResponse);

        ResponseWriter responseWriter = mock(ResponseWriter.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        SurveyServlet surveyServlet = new SurveyServlet(surveyRepo, twilioResponseFactory, responseWriter);

        surveyServlet.doGet(mock(HttpServletRequest.class), response);

        verify(responseWriter, times(1)).writeIn(response, twiMLResponse);
    }
}
