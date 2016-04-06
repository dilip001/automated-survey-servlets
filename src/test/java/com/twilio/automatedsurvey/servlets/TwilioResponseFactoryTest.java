package com.twilio.automatedsurvey.servlets;

import com.twilio.automatedsurvey.survey.Survey;
import com.twilio.sdk.verbs.*;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertThat;

public class TwilioResponseFactoryTest {

    @Test
    public void shouldBuildAWelcomingMessage() throws TwiMLException {
        Survey survey = new Survey(1L, "Survey title");
        String expectedWelcomeMessage = String.format("Welcome to the %s survey", survey.getTitle());
        TwilioResponseFactory twilioResponseFactory = new TwilioResponseFactory();

        String url = String.format("question?survey=%s", survey.getId());
        Redirect redirectQuestion = new Redirect(url);
        redirectQuestion.setMethod("GET");
        TwiMLResponse twiMLResponse1 = new TwiMLResponse();
        twiMLResponse1.append(new Say(String.format("Welcome to the %s survey", survey.getTitle())));
        twiMLResponse1.append(redirectQuestion);

        TwiMLResponse twiMLResponse = twiMLResponse1;

        assertThat(twiMLResponse.getChildren(), hasItem(hasProperty("body", is(expectedWelcomeMessage))));
    }

    @Test
    public void shouldBuildARedirectMessage() throws TwiMLException {
        Survey survey = new Survey(1L, "Survey title");
        String expectedUrl = String.format("question?survey=%s", survey.getId());
        TwilioResponseFactory twilioResponseFactory = new TwilioResponseFactory();

        String url = String.format("question?survey=%s", survey.getId());
        Redirect redirectQuestion = new Redirect(url);
        redirectQuestion.setMethod("GET");
        TwiMLResponse twiMLResponse1 = new TwiMLResponse();
        twiMLResponse1.append(new Say(String.format("Welcome to the %s survey", survey.getTitle())));
        twiMLResponse1.append(redirectQuestion);

        TwiMLResponse twiMLResponse = twiMLResponse1;

        assertThat(twiMLResponse.getChildren(), hasItem(hasProperty("body", is(expectedUrl))));
    }
}
