package com.twilio.automatedsurvey.servlets.twimlquestions;

import com.twilio.automatedsurvey.survey.Question;
import com.twilio.sdk.verbs.*;

public class VoiceQuestion implements TwiMLQuestion {

    private Long surveyId;
    private Question internalQuestion;

    public VoiceQuestion(Long surveyId, Question question) {
        this.surveyId = surveyId;
        this.internalQuestion = question;
    }

    public TwiMLResponse getVoiceResponse() {
        TwiMLResponse twiMLResponse;
        try {
            twiMLResponse = new TwiMLResponse();
            twiMLResponse.append(new Say("Record your answer after the beep and press the pound key when you are done."));
            twiMLResponse.append(new Pause());
            twiMLResponse.append(new Say(internalQuestion.getBody()));
            Record record = new Record();
            record.setAction("survey?survey="+ surveyId +"&amp;question="+internalQuestion.getId());
            record.setMethod("POST");
            record.setFinishOnKey("#");
            twiMLResponse.append(record);

        } catch (TwiMLException e) {
            throw new RuntimeException(e);
        }
        return twiMLResponse;
    }

    public String toEscapedXML() {
        return getVoiceResponse().toEscapedXML();
    }
}
