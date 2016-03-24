package com.twilio.automatedsurvey.servlets;

import com.google.inject.Singleton;
import com.twilio.automatedsurvey.survey.Survey;
import com.twilio.automatedsurvey.survey.SurveyRepository;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Singleton
public class IndexServlet extends HttpServlet{

    private SurveyRepository repository;

    @Inject
    public IndexServlet(SurveyRepository repository) {
        this.repository = repository;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Survey> surveys = repository.all();
        request.setAttribute("surveys", surveys);
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}
