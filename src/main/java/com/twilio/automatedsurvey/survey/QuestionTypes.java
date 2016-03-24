package com.twilio.automatedsurvey.survey;

import com.twilio.automatedsurvey.servlets.twimlquestions.NumericQuestion;
import com.twilio.automatedsurvey.servlets.twimlquestions.TwiMLQuestion;
import com.twilio.automatedsurvey.servlets.twimlquestions.VoiceQuestion;
import com.twilio.automatedsurvey.servlets.twimlquestions.YesNoQuestion;

public enum QuestionTypes {
    voice("RecordingUrl") {
        @Override
        public TwiMLQuestion getTwiMLQuestion(Long surveyId, Question question) {
            return new VoiceQuestion(surveyId, question);
        }

        @Override
        public String format(String answer) {
            return answer;
        }
    },
    numeric("Digits") {
        @Override
        public TwiMLQuestion getTwiMLQuestion(Long surveyId, Question question) {
            return new NumericQuestion(surveyId, question);
        }

        @Override
        public String format(String answer) {
            return answer;
        }
    }, yesno("Digits") {
        @Override
        TwiMLQuestion getTwiMLQuestion(Long surveyId, Question question) {
            return new YesNoQuestion(surveyId, question);
        }

        @Override
        public String format(String answer) {
            if (answer != null) {
                return Integer.parseInt(answer) > 0 ? "Yes" : "No";
            }

            return "";
        }
    };

    private final String answerKey;

    QuestionTypes(String answerKey) {
        this.answerKey = answerKey;
    }

    public String getAnswerKey() {
        return answerKey;
    }

    abstract TwiMLQuestion getTwiMLQuestion(Long surveyId, Question question);

    public abstract String format(String answer);
}