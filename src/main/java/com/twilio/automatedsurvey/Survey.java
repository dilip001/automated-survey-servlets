package com.twilio.automatedsurvey;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Survey {
    @Id
    @GeneratedValue
    private Long id;
    private String title;

    private Survey() { /* needed by the ORM */ }

    public Survey(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Survey(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public Long getId() {
        return id;
    }
}
