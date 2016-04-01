package com.twilio.automatedsurvey.servlets.twimlquestions;

import com.twilio.automatedsurvey.servlets.TelephoneTwiMLQuestionFactory;
import com.twilio.automatedsurvey.survey.Question;
import com.twilio.sdk.verbs.TwiMLResponse;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractTwiMLQuestionFactory {

    public static AbstractTwiMLQuestionFactory getInstance(HttpServletRequest request) {
        if (request.getParameter("MessageSid")==null){
            return new TelephoneTwiMLQuestionFactory();
        } else {
            return new SMSTwiMLQuestionFactory();
        }
    }

    public abstract TwiMLResponse build(Long surveyId, Question question);
}
