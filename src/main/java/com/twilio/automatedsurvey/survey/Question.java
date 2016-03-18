package com.twilio.automatedsurvey.survey;

import com.twilio.automatedsurvey.servlets.NumericQuestion;
import com.twilio.automatedsurvey.servlets.TwiMLQuestion;
import com.twilio.automatedsurvey.servlets.VoiceQuestion;

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

    public enum QuestionTypes {
        voice {
            @Override
            public TwiMLQuestion getTwiMLQuestion(Question question) {
                return new VoiceQuestion(question);
            }
        },
        numeric {
            @Override
            public TwiMLQuestion getTwiMLQuestion(Question question) {
                return new NumericQuestion(question);
            }
        };


        public abstract TwiMLQuestion getTwiMLQuestion(Question question);
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
        return String.format("[Body: %s, Type: %s]", this.getBody(), this.getType());
    }

    public Long getId() {
        return id;
    }
}
