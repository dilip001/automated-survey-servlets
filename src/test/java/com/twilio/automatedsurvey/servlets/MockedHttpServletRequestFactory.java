package com.twilio.automatedsurvey.servlets;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockedHttpServletRequestFactory {
    static HttpServletRequest getMockedRequestWithParameters(Map<String, String> parameters) {
        HttpServletRequest servletRequest = mock(HttpServletRequest.class);

        for (String key : parameters.keySet()) {
            when(servletRequest.getParameter(key)).thenReturn(parameters.get(key));
        }
        return servletRequest;
    }
}
