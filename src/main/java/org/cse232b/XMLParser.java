package org.cse232b;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class XMLParser {
    public static final String DEFAULT_XML_FILE_NAME = "j_caesar.xml";
    public static final String DEFAULT_DTD_FILE_NAME = "play.dtd";

    public static void main(String[] args) {
        try {
            // Create a DocumentBuilderFactory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // Enabling validation
            factory.setValidating(true);
            // Setting the DTD
            factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaSource", DEFAULT_DTD_FILE_NAME);

            // Create a DocumentBuilder
            DocumentBuilder builder = factory.newDocumentBuilder();
            // Parse the XML file and return a DOM Document object
            Document document = builder.parse(DEFAULT_XML_FILE_NAME);

            // Normalize the XML structure
            document.getDocumentElement().normalize();

            
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}
