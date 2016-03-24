package com.twilio.automatedsurvey.servlets.twimlquestions;

import com.twilio.automatedsurvey.survey.Question;
import com.twilio.sdk.verbs.Message;
import com.twilio.sdk.verbs.TwiMLException;
import com.twilio.sdk.verbs.TwiMLResponse;

public class SMSTwiMLQuestionFactory extends AbstractTwiMLQuestionFactory {
    @Override
    public TwiMLQuestion build(Long surveyId, Question question) {
        TwiMLResponse response = new TwiMLResponse();
        try {
            response.append(new Message(question.getBody()));
        } catch (TwiMLException e) {
            e.printStackTrace();
        }
        return () -> response.toEscapedXML();
    }
}
