package com.twilio.automatedsurvey;

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

    public String getBody() {
        return body;
    }

    public String getType() {
        return type;
    }
}
