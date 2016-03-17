package com.twilio.automatedsurvey.servlets;

import com.twilio.automatedsurvey.survey.Question;
import com.twilio.automatedsurvey.survey.Survey;
import com.twilio.automatedsurvey.survey.SurveyRepository;
import com.twilio.sdk.verbs.*;

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

    public void doGet(HttpServletRequest request, HttpServletResponse httpServletResponse) throws IOException {
        Long surveyId = Long.parseLong(request.getParameter("survey"));
        Integer questionNumber = Integer.parseInt(request.getParameter("question"));

        Optional<Survey> survey = surveyRepository.find(surveyId);
        Question question = survey.get().getQuestionByNumber(questionNumber)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        TwiMLResponse twiMLResponse;
        try {
            twiMLResponse = new TwiMLResponse();
            twiMLResponse.append(new Say("Record your answer after the beep and press the pound key when you are done."));
            twiMLResponse.append(new Say(question.getBody()));
            twiMLResponse.append(new Pause());
            Record record = new Record();
            record.setAction("/save_response?qid=" + question.getId());
            record.setMethod("POST");
            record.setFinishOnKey("#");
            twiMLResponse.append(record);

        } catch (TwiMLException e) {
            throw new RuntimeException(e);
        }
        responseWriter.writeIn(httpServletResponse, twiMLResponse);
    }
}
