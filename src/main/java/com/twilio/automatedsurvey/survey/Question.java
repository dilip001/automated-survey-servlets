package com.twilio.automatedsurvey.survey;

import com.twilio.automatedsurvey.servlets.twimlquestions.NumericQuestion;
import com.twilio.automatedsurvey.servlets.twimlquestions.TwiMLQuestion;
import com.twilio.automatedsurvey.servlets.twimlquestions.VoiceQuestion;
import com.twilio.automatedsurvey.servlets.twimlquestions.YesNoQuestion;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Question {
    @Id
    @GeneratedValue
    private Long id;
    private String body;
    private QuestionTypes type;
    private String answer;

    Question(long id, String body, QuestionTypes type) {
        this.id = id;
        this.body = body;
        this.type = type;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }


    public enum QuestionTypes {
        voice("RecordingUrl") {
            @Override
            public TwiMLQuestion getTwiMLQuestion(Question question) {
                return new VoiceQuestion(question);
            }
        },
        numeric("Digits") {
            @Override
            public TwiMLQuestion getTwiMLQuestion(Question question) {
                return new NumericQuestion(question);
            }
        }, yesno("Digits") {
            @Override
            TwiMLQuestion getTwiMLQuestion(Question question) {
                return new YesNoQuestion(question);
            }
        };

        private final String answerKey;

        QuestionTypes(String answerKey) {
            this.answerKey = answerKey;
        }

        public String getAnswerKey() {
            return answerKey;
        }

        abstract TwiMLQuestion getTwiMLQuestion(Question question);
    }

    private Question() { /* Used by the ORM */ }

    public Question(String body, QuestionTypes type) {
        this.body = body;
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public QuestionTypes getType() {
        return type;
    }

    public TwiMLQuestion toTwiML() {
        return type.getTwiMLQuestion(this);
    }

    @Override
    public boolean equals(Object o) {
        boolean result = false;

        if (o instanceof Question) {
            Question that = (Question) o;
            result = this.getBody().equals(that.getBody()) &&
                    this.getType().equals(that.getType());
        }

        return result;
    }

    @Override
    public String toString() {
        return String.format("[Id: %s, Body: %s, Type: %s]", this.getId(),
                this.getBody(), this.getType());
    }

    public Long getId() {
        return id;
    }
}
