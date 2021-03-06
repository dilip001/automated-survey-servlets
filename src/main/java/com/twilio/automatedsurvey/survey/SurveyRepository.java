package com.twilio.automatedsurvey.survey;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SurveyRepository {

    private EntityManager entityManager;

    @Inject
    public SurveyRepository(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public Optional<Survey> findLast() {
        Query query = entityManager.createQuery("select s from Survey s order by s.id desc");
        List<Survey> queryResult = query.getResultList();

        return queryResult.isEmpty() ? Optional.empty() : Optional.of(queryResult.get(0));
    }

    public Survey add(Survey survey) {
        entityManager.persist(survey);
        return survey;
    }

    public Optional<Survey> find(Long id) {
        Survey survey = entityManager.find(Survey.class, id);
        return Optional.ofNullable(survey);
    }

    public void update(Survey survey) {
        entityManager.getTransaction().begin();
        entityManager.getTransaction().commit();
    }

    public List<Survey> all() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Survey> query = criteriaBuilder.createQuery(Survey.class);
        Root<Survey> root = query.from(Survey.class);
        CriteriaQuery<Survey> select = query.select(root);
        return entityManager.createQuery(select).getResultList();
    }
}
