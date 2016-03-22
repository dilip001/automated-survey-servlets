package com.twilio.automatedsurvey.survey;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
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

    private Stream<Question> getSortedQuestions() {
        Comparator<Question> questionIdComparator = (elem1, elem2) -> elem1.getId().compareTo(elem2.getId());
        return questions.stream().sorted(questionIdComparator);
    }

    public Question answer(HttpServletRequest request) {
        Long questionId = Long.parseLong(request.getParameter("question"));
        Optional<Question> question = questionById(questionId);

        return question.map((Question q) -> {
            String answerKey = q.getType().getAnswerKey();
            q.setAnswer(request.getParameter(answerKey));
            return q;
        }).orElseThrow(() -> new RuntimeException(String.format("Question %s from Survey %s not found", id, questionId)));
    }

    public Optional<Question> questionById(Long questionId) {
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
            return Optional.of(sortedQuestions.get(nextQuestionIndex));
        }
    }

    public Optional<Question> getFirstQuestion() {
        return getSortedQuestions().findFirst();
    }


}
