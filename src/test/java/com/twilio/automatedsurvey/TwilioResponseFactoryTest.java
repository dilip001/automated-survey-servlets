package com.twilio.automatedsurvey;

import com.twilio.sdk.verbs.Redirect;
import com.twilio.sdk.verbs.TwiMLException;
import com.twilio.sdk.verbs.TwiMLResponse;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertThat;

public class TwilioResponseFactoryTest {

    @Test
    public void shouldBuildAWelcomingMessage() throws TwiMLException {
        Survey survey = new Survey(1, "Survey title");
        String expectedWelcomeMessage = String.format("Welcome to the %s survey", survey.getTitle());
        TwilioResponseFactory twilioResponseFactory = new TwilioResponseFactory();

        TwiMLResponse twiMLResponse = twilioResponseFactory.build(survey);

        assertThat(twiMLResponse.getChildren(), hasItem(hasProperty("body", is(expectedWelcomeMessage))));
    }

    @Test
    public void shouldBuildARedirectMessage() throws TwiMLException {
        Survey survey = new Survey(1, "Survey title");
        String expectedUrl = String.format("/question?survey=%s&question=1", survey.getId());
        TwilioResponseFactory twilioResponseFactory = new TwilioResponseFactory();

        TwiMLResponse twiMLResponse = twilioResponseFactory.build(survey);

        assertThat(twiMLResponse.getChildren(), hasItem(hasProperty("body", is(expectedUrl))));
    }
}
