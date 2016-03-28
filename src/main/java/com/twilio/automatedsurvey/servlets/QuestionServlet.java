package com.twilio.automatedsurvey.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twilio.automatedsurvey.servlets.twimlquestions.AbstractTwiMLQuestionFactory;
import com.twilio.automatedsurvey.servlets.twimlquestions.TwiMLQuestion;
import com.twilio.automatedsurvey.survey.Question;
import com.twilio.automatedsurvey.survey.Survey;
import com.twilio.automatedsurvey.survey.SurveyRepository;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

@Singleton
public class QuestionServlet extends HttpServlet {
    private SurveyRepository surveyRepository;
    private ResponseWriter responseWriter;

    @Inject
    public QuestionServlet(SurveyRepository surveyRepository, ResponseWriter responseWriter) {
        this.surveyRepository = surveyRepository;
        this.responseWriter = responseWriter;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long surveyId = Long.parseLong(request.getParameter("survey"));
        String questionId = request.getParameter("question");


        Optional<Survey> survey = surveyRepository.find(surveyId);
        Optional<Question> question;

        if (questionId == null) {
            question = survey.flatMap((Survey s) -> s.getFirstQuestion());
        } else {
            Long parsedQuestionId = Long.parseLong(questionId);
            question = survey.flatMap((Survey s) -> s.questionById(parsedQuestionId));
        }

        AbstractTwiMLQuestionFactory factory = AbstractTwiMLQuestionFactory.getInstance(request);

        TwiMLQuestion twiMLQuestion = question.map((Question q) -> factory.build(surveyId, q))
                .orElseThrow(()  -> new RuntimeException(String.format("Survey/question %s/%s not found",
                        surveyId, questionId)));

        HttpSession session = request.getSession(true);
        session.setAttribute("lastSurvey", surveyId);
        session.setAttribute("lastQuestion", question.get().getId());

        String xml = twiMLQuestion.toEscapedXML();

        responseWriter.writeIn(response, xml);
    }

}
