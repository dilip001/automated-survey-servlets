package com.twilio.automatedsurvey.servlets.twimlquestions;

import com.twilio.automatedsurvey.survey.Question;
import com.twilio.sdk.verbs.*;

public class NumericQuestion implements TwiMLQuestion {
    private Long surveyId;
    private Question numericQuestion;

    public NumericQuestion(Long surveyId, Question numericQuestion) {
        this.surveyId = surveyId;
        this.numericQuestion = numericQuestion;
    }

    public String toEscapedXML() {
        try {
            TwiMLResponse response = new TwiMLResponse();
            response.append(new Say("For the next question select a number with the dial pad " +
                    "and then press the pound key"));
            response.append(new Say(numericQuestion.getBody()));
            response.append(new Pause());

            Gather gather = new Gather();
            gather.setAction(String.format("survey?survey=%s&amp;question=%s", surveyId, numericQuestion.getId()));
            gather.setMethod("POST");
            gather.setFinishOnKey("#");
            response.append(gather);

            return response.toEscapedXML();
        } catch (TwiMLException e) {
            throw new RuntimeException(e);
        }
    }
}
