package com.twilio.automatedsurvey.servlets;

import com.twilio.automatedsurvey.survey.Survey;
import com.twilio.sdk.verbs.Redirect;
import com.twilio.sdk.verbs.Say;
import com.twilio.sdk.verbs.TwiMLException;
import com.twilio.sdk.verbs.TwiMLResponse;

public class TwilioResponseFactory {
    public TwiMLResponse build(Survey survey) throws TwiMLException {
        String welcomeMessage = String.format("Welcome to the %s survey", survey.getTitle());
        Say sayWelcome = new Say(welcomeMessage);

        String url = String.format("/question?survey=%s&question=1", survey.getId());
        Redirect redirectQuestion = new Redirect(url);

        TwiMLResponse twiMLResponse = new TwiMLResponse();
        twiMLResponse.append(sayWelcome);
        twiMLResponse.append(redirectQuestion);

        return twiMLResponse;
    }
}
