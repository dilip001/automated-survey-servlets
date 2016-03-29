package com.twilio.automatedsurvey.survey;

public enum QuestionTypes {
    voice("RecordingUrl") {
        @Override
        public String format(String answer) {
            return answer;
        }
    },

    numeric("Digits") {
        @Override
        public String format(String answer) {
            return answer;
        }
    },

    yesno("Digits") {
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

    public abstract String format(String answer);
}