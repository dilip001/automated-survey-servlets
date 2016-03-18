package com.twilio.automatedsurvey.servlets;

import com.twilio.automatedsurvey.survey.Question;
import com.twilio.automatedsurvey.survey.Survey;
import com.twilio.automatedsurvey.survey.SurveyRepository;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class QuestionServlet extends HttpServlet {
    private SurveyRepository surveyRepository;
    private ResponseWriter responseWriter;

    public QuestionServlet(SurveyRepository surveyRepository, ResponseWriter responseWriter) {
        this.surveyRepository = surveyRepository;
        this.responseWriter = responseWriter;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long surveyId = Long.parseLong(request.getParameter("survey"));
        Integer questionNumber = Integer.parseInt(request.getParameter("question"));

        Optional<Survey> survey = surveyRepository.find(surveyId);
        Question question = survey.get().getQuestionByNumber(questionNumber)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        TwiMLQuestion twiMLQuestion = Question.QuestionTypes.getTwiMLQuestion(question);
        String xml = twiMLQuestion.toEscapedXML();

        responseWriter.writeIn(response, xml);
    }

}
