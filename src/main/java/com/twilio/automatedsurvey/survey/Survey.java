package com.twilio.automatedsurvey.survey;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;
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

    private Stream<Question> getSortedQuestions() {
        Comparator<Question> questionIdComparator = (elem1, elem2) -> elem1.getId().compareTo(elem2.getId());
        return questions.stream().sorted(questionIdComparator);
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

    public Optional<Question> getNextQuestion(Question previousQuestion) {
        List<Question> sortedQuestions = getSortedQuestions().collect(Collectors.toList());
        int previousQuestionIndex = sortedQuestions.indexOf(previousQuestion);

        int nextQuestionIndex = previousQuestionIndex+1;

        if (nextQuestionIndex >= sortedQuestions.size()) {
            return Optional.empty();
        } else {
            return Optional.of(sortedQuestions.get(previousQuestionIndex+1));

        }
    }

    public Optional<Question> getFirstQuestion() {
        return getSortedQuestions().findFirst();
    }
}
