package com.twilio.automatedsurvey;

import com.twilio.sdk.verbs.TwiMLResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseWriter {
    public void writeIn(HttpServletResponse response, TwiMLResponse lastSurvey) throws IOException {
        response.setContentType("application/xml");
        response.getWriter().write(lastSurvey.toEscapedXML());
    }
}
