package com.twilio.automatedsurvey.servlets;

import com.twilio.automatedsurvey.survey.Question;
import com.twilio.sdk.verbs.*;

public class VoiceResponse {

    private Question internalQuestion;

    public VoiceResponse(Question question) {
        this.internalQuestion = question;
    }

    public TwiMLResponse getVoiceResponse() {
        TwiMLResponse twiMLResponse;
        try {
            twiMLResponse = new TwiMLResponse();
            twiMLResponse.append(new Say("Record your answer after the beep and press the pound key when you are done."));
            twiMLResponse.append(new Say(internalQuestion.getBody()));
            twiMLResponse.append(new Pause());
            Record record = new Record();
            record.setAction("/save_response?qid=" + internalQuestion.getId());
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
