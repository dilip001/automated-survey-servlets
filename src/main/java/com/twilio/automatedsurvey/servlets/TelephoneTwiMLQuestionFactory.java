package com.twilio.automatedsurvey.servlets;

import com.twilio.automatedsurvey.servlets.twimlquestions.*;
import com.twilio.automatedsurvey.survey.Question;

public class TelephoneTwiMLQuestionFactory extends AbstractTwiMLQuestionFactory {
    public TwiMLQuestion build(Long surveyId, Question question) {
        TwiMLQuestion twiMLQuestion = null;
        switch (question.getType()){
            case voice:
                twiMLQuestion = new VoiceQuestion(surveyId, question);
                break;
            case numeric:
                twiMLQuestion = new NumericQuestion(surveyId, question);
                break;
            case yesno:
                twiMLQuestion = new YesNoQuestion(surveyId, question);
                break;
        }

        return twiMLQuestion;
    }
}
