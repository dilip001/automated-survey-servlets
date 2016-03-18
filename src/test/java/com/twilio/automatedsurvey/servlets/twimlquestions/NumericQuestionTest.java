package com.twilio.automatedsurvey.servlets.twimlquestions;

import com.twilio.automatedsurvey.servlets.twimlquestions.NumericQuestion;
import com.twilio.automatedsurvey.servlets.twimlquestions.XMlTestHelper;
import com.twilio.automatedsurvey.survey.Question;
import com.twilio.sdk.verbs.TwiMLException;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.hamcrest.Matchers.hasXPath;
import static org.junit.Assert.assertThat;

public class NumericQuestionTest {

    @Test
    public void shouldReturnResponseXMLRepresentation() throws IOException, SAXException,
            ParserConfigurationException, TwiMLException {

        NumericQuestion numericQuestion = new NumericQuestion(new Question("Is that a question?", Question.QuestionTypes.valueOf("numeric")));

        String xml = numericQuestion.toEscapedXML();

        Document document = XMlTestHelper.createDocumentFromXml(xml);

        Node responseNode = document.getElementsByTagName("Response").item(0);
        assertThat(responseNode, hasXPath("/Response/Say[text() = 'For the next question select a number with " +
                "the dial pad and then press the pound key']"));
        assertThat(responseNode, hasXPath("/Response/Say[text() = 'Is that a question?']"));
        assertThat(responseNode, hasXPath("/Response/Pause"));
        assertThat(responseNode, hasXPath("/Response/Gather"));
    }
}
