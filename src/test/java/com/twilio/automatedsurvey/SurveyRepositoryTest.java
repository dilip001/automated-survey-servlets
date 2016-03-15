package com.twilio.automatedsurvey;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class SurveyRepositoryTest {

    private SurveyRepository surveyRepository;
    private EntityManager entityManager;

    @Before
    public void setup() {
        Injector injector = Guice.createInjector(new JpaPersistModule("testJpaUnit"));
        initPersistService(injector);
        surveyRepository = injector.getInstance(SurveyRepository.class);
        entityManager = injector.getInstance(EntityManager.class);
        entityManager.getTransaction().begin();
        entityManager.createQuery("delete from Survey").executeUpdate();
        entityManager.getTransaction().commit();
    }

    private void initPersistService(Injector injector) {
        PersistService instance = injector.getInstance(PersistService.class);
        instance.start();
    }

    @Test
    public void shouldProvideAnIdToAnAddedSurvey() {
        String newSurveyTitle = "new survey";
        Survey addedSurvey = surveyRepository.add(new Survey(newSurveyTitle));

        assertThat(addedSurvey.getId(), is(notNullValue()));
        assertThat(addedSurvey.getTitle(), is(newSurveyTitle));
    }

    @Test
    public void shouldInformWhenItWasNotPossibleTooReturnAnSurvey() {
        Optional<Survey> last = surveyRepository.findLast();

        assertThat(last.isPresent(), is(false));
    }

    @Test
    public void shouldReturnLastSurvey() {
        final Survey firstSurvey = new Survey("first survey");
        final Survey lastSurvey = new Survey("last survey");
        givenThatSurveysExists(firstSurvey, lastSurvey);

        Optional<Survey> retrievedLastSurvey = surveyRepository.findLast();

        assertThat(retrievedLastSurvey.isPresent(), is(true));
        Survey testedSurvey = retrievedLastSurvey.get();
        assertThat(testedSurvey.getId(), is(lastSurvey.getId()));
        assertThat(testedSurvey.getTitle(), is(lastSurvey.getTitle()));
    }

    private void givenThatSurveysExists(Survey... surveys) {
        entityManager.getTransaction().begin();

        for (Survey survey : surveys) {
            surveyRepository.add(survey);
        }
        entityManager.getTransaction().commit();
    }
}
