package com.twilio.automatedsurvey.servlets.twimlquestions;

import com.twilio.automatedsurvey.survey.Question;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.hamcrest.Matchers.hasXPath;
import static org.junit.Assert.assertThat;

public class VoiceQuestionTest {

    @Test
    public void shouldReturnVoiceResponseTiMLRepresentation() throws ParserConfigurationException, IOException, SAXException {
        VoiceQuestion voiceQuestion = new VoiceQuestion(1L, new Question("Is that a question?", Question.QuestionTypes.valueOf("voice")));

        String xml = voiceQuestion.toEscapedXML();

        Document xmlDocument = XMlTestHelper.createDocumentFromXml(xml);

        Node response = xmlDocument.getElementsByTagName("Response").item(0);
        assertThat(response, hasXPath("/Response/Say[text() = 'Record your answer after the " +
                "beep and press the pound key when you are done.']"));
        assertThat(response, hasXPath("/Response/Say[text() = 'Is that a question?']"));
        assertThat(response, hasXPath("/Response/Pause"));
        assertThat(response, hasXPath("/Response/Record"));
    }

}
