package com.twilio.automatedsurvey.survey;

import com.google.inject.persist.Transactional;

import javax.persistence.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
public class Survey {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="survey_id")
    private Set<Question> questions;

    {
        questions = new HashSet<>();
    }

    private Survey() { /* needed by the ORM */ }

    public Survey(String title) {
        this(null, title);
    }

    public Survey(Long id, String title) {
        this.id = id;
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

    public Optional<Question> getFirstQuestion() {
        return getSortedQuestions().findFirst();
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    private Stream<Question> getSortedQuestions() {
        Comparator<Question> questionIdComparator = (elem1, elem2) -> elem1.getId().compareTo(elem2.getId());
        return questions.stream().sorted(questionIdComparator);
    }

    @Transactional
    public Question answerCall(Map<String, String[]> parameters) {
        return answerUsing(parameters, (Question question) -> {
            String answerKey = parameters.containsKey("TranscriptionText") ?
                    "TranscriptionText" : question.getType().getAnswerKey();

            question.setAnswer(parameters.get(answerKey)[0]);
            return question;
        });
    }

    public Question answerSMS(Map<String, String[]> parameters) {
        return answerUsing(parameters, (Question q) -> {
            q.setAnswer(parameters.get("Body")[0]);
            return q;
        });
    }

    private Question answerUsing(Map<String, String[]> parameters, Function<Question, Question> extractAndApplyAnswer) {
        String questionId = parameters.get("question")[0];

        Optional<Question> question = questionById(Long.parseLong(questionId));

        return question.map(extractAndApplyAnswer)
                .orElseThrow(() -> new RuntimeException(String.format("Question %s from Survey %s not found", id, questionId)));
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

}
