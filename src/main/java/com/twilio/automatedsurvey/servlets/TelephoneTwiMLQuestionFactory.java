package com.twilio.automatedsurvey.servlets;

import com.twilio.automatedsurvey.servlets.twimlquestions.*;
import com.twilio.automatedsurvey.survey.Question;
import com.twilio.sdk.verbs.*;

public class TelephoneTwiMLQuestionFactory extends AbstractTwiMLQuestionFactory {
    public TwiMLResponse build(Long surveyId, Question question) {

        TwiMLResponse twiMLResponse;
        switch (question.getType()){
            case voice:
                twiMLResponse = buildVoiceMessage(surveyId, question,
                        "Record your answer after the beep and press the pound key when you are done.");
                break;
            case numeric:
                twiMLResponse = buildNumericMessage(surveyId, question,
                        "For the next question select a number with the dial pad " +
                        "and then press the pound key");
                break;
            case yesno:
                twiMLResponse = buildNumericMessage(surveyId, question,
                        "For the next question, press 1 for yes, and 0 for no. Then press the pound key.");
                break;
            default:
                throw new RuntimeException("Invalid question type");
        }

        return twiMLResponse;
    }

    private TwiMLResponse buildVoiceMessage(Long surveyId, Question question, String message) {
        TwiMLResponse twiMLResponse = new TwiMLResponse();
        try {
            twiMLResponse.append(new Say(message));
            twiMLResponse.append(new Pause());
            twiMLResponse.append(new Say(question.getBody()));
            Record record = new Record();
            record.setTranscribe(true);
            record.setTranscribeCallback("survey?survey="+ surveyId +"&amp;question="+question.getId());
            record.setAction("survey?survey="+ surveyId +"&amp;question="+question.getId());
            record.setMethod("POST");
            record.setMaxLength(6);
            twiMLResponse.append(record);

        } catch (TwiMLException e) {
            throw new RuntimeException(e);
        }

        return twiMLResponse;
    }

    private TwiMLResponse buildNumericMessage(Long surveyId, Question question, String message) {
        TwiMLResponse twiMLResponse = new TwiMLResponse();
        try {
            twiMLResponse.append(new Say(message));
            twiMLResponse.append(new Pause());
            twiMLResponse.append(new Say(question.getBody()));
            Gather gather = new Gather();
            gather.setAction("survey?survey="+ surveyId +"&amp;question="+question.getId());
            gather.setMethod("POST");
            gather.setFinishOnKey("#");
            twiMLResponse.append(gather);

        } catch (TwiMLException e) {
            throw new RuntimeException(e);
        }

        return twiMLResponse;
    }
}
