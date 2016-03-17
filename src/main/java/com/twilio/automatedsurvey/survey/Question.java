package com.twilio.automatedsurvey.survey;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Question {
    @Id
    @GeneratedValue
    private Long id;
    private String body;
    private String type;

    private Question() { /* Used by the ORM */ }

    public Question(String body, String type) {
        this.body = body;
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public String getType() {
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
