package com.twilio.automatedsurvey.servlets;

import com.twilio.automatedsurvey.survey.Survey;
import com.twilio.sdk.verbs.*;

public class TwilioResponseFactory {
    public TwiMLResponse build(Survey survey, Verb welcomeMessage) throws TwiMLException {
        String url = String.format("question?survey=%s", survey.getId());
        Redirect redirectQuestion = new Redirect(url);
        redirectQuestion.setMethod("GET");
        TwiMLResponse twiMLResponse = new TwiMLResponse();
        twiMLResponse.append(welcomeMessage);
        twiMLResponse.append(redirectQuestion);

        return twiMLResponse;
    }
}
