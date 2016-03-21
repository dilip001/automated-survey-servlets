package com.twilio.automatedsurvey.survey;

import javax.persistence.*;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Entity
public class Survey {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Question> questions;

    {
        questions = new HashSet<>();
    }

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

    public Set<Question> getQuestions() {
        return questions;
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public Optional<Question> getQuestionByNumber(Integer questionNumber) {
        int questionIndex = questionNumber - 1;

        if (questionIndex >= questions.size()){
            return Optional.empty();
        } else {
            Comparator<Question> questionIdComparator = (elem1, elem2) -> elem1.getBody().compareTo(elem2.getBody());
            Stream<Question> sortedQuestionsList = questions.stream().sorted(questionIdComparator);
            return Optional.of((Question) sortedQuestionsList.toArray()[questionIndex]);
        }
    }


    public Optional<Question> answer(long questionId, String answer) {
        Optional<Question> question = questionById(questionId);

        return question.map((Question q) -> {
            q.setAnswer(answer);
            return q;
        });
    }

    public Optional<String> getQuestionsAnswerKey(Long questionId) {
        Optional<Question> first = questionById(questionId);
        return first.map((Question question) -> question.getType().getAnswerKey());
    }

    private Optional<Question> questionById(Long questionId) {
        return questions.stream().filter((Question question) -> question.getId().equals(questionId))
                .findFirst();
    }
}
