package com.twilio.automatedsurvey.servlets.twimlquestions;

import com.twilio.automatedsurvey.survey.Question;
import com.twilio.sdk.verbs.*;

public class YesNoQuestion implements TwiMLQuestion {
    private Long surveyId;
    private Question question;

    public YesNoQuestion(Long surveyId, Question question) {
        this.surveyId = surveyId;
        this.question = question;
    }

    @Override
    public String toEscapedXML() {
        TwiMLResponse response = new TwiMLResponse();
        try {
            response.append(new Say("For the next question, press 1 for yes, and 0 for no. Then press the pound key."));
            response.append(new Say(question.getBody()));
            response.append(new Pause());

            Gather gather = new Gather();
            gather.setAction(String.format("survey?survey=%s&amp;question=%s", surveyId, question.getId()));
            gather.setMethod("POST");
            gather.setFinishOnKey("#");

            response.append(gather);
        } catch (TwiMLException e) {
            throw new RuntimeException(e);
        }
        return response.toEscapedXML();
    }
}
