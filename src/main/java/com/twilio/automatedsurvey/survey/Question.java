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
    private QuestionTypes type;
    private String answer;

    private Question() { /* Used by the ORM */ }

    public Question(String body, QuestionTypes type) {
        this(null, body, type);
    }

    public Question(Long id, String body, QuestionTypes type) {
        this.id = id;
        this.body = body;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getFormatedAnswer(){
        return type.format(answer);
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
        return String.format("[Id: %s, Body: %s, Type: %s]", this.getId(),
                this.getBody(), this.getType());
    }

}
