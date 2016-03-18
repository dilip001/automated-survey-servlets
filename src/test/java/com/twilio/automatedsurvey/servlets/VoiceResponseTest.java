package com.twilio.automatedsurvey.servlets;

import com.twilio.automatedsurvey.survey.Question;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasXPath;
import static org.junit.Assert.assertThat;

public class VoiceResponseTest {

    @Test
    public void shouldReturnVoiceResponseTiMLRepresentation() throws ParserConfigurationException, IOException, SAXException {
        VoiceResponse voiceResponse = new VoiceResponse(new Question("Is that a question?", "voice"));

        String xml = voiceResponse.toEscapedXML();

        Document xmlDocument = XMlTestHelper.createDocumentFromXml(xml);

        Node response = xmlDocument.getElementsByTagName("Response").item(0);
        assertThat(response, hasXPath("/Response/Say[text() = 'Record your answer after the " +
                "beep and press the pound key when you are done.']"));
        assertThat(response, hasXPath("/Response/Say[text() = 'Is that a question?']"));
        assertThat(response, hasXPath("/Response/Pause"));
        assertThat(response, hasXPath("/Response/Record"));
    }

}
