package org.cse232b;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

public class XMLProcessor {
    public static final String DEFAULT_XML_FILE_NAME = "j_caesar.xml";
    public static final String DEFAULT_DTD_FILE_NAME = "play.dtd";
    private static final DocumentBuilderFactory buildFactory = DocumentBuilderFactory.newInstance();
    private static final TransformerFactory transformerFactory = TransformerFactory.newInstance();


    private static DocumentBuilder getDocumentBuilder(InputStream dtdStream) throws ParserConfigurationException {
        DocumentBuilder builder = buildFactory.newDocumentBuilder();
        builder.setEntityResolver((publicId, systemId) -> systemId.contains(".dtd") ? new InputSource(dtdStream) : null);
        return builder;
    }

    private static List<Node> loadXMLToDomNodes(String xmlFileName, String dtdFileName) throws ParserConfigurationException, IOException, SAXException {
        try (InputStream xmlDataFileStream = XMLProcessor.class.getClassLoader().getResourceAsStream(xmlFileName);
             InputStream dtdStream = XMLProcessor.class.getClassLoader().getResourceAsStream(dtdFileName)) {
            DocumentBuilder builder = getDocumentBuilder(dtdStream);
            Document document = builder.parse(xmlDataFileStream);
            document.getDocumentElement().normalize();
            return List.of(document);
        }
    }

    public static List<Node> loadNodes(String xmlFileName) throws Exception {
        if (!DEFAULT_XML_FILE_NAME.equals(xmlFileName)) {
            throw new Exception("XML data file is not in resources");
        }
        return loadXMLToDomNodes(DEFAULT_XML_FILE_NAME, DEFAULT_DTD_FILE_NAME);
    }

    public static Document generateResultDocument(List<Node> rawResult, boolean addResultElement) throws ParserConfigurationException {
        DocumentBuilder builder = buildFactory.newDocumentBuilder();
        Document outputDoc = builder.newDocument();
        Element resultEle = null;
        if (addResultElement) {
            resultEle = outputDoc.createElement("RESULT");
            outputDoc.appendChild(resultEle);
        }

        for (Node node : rawResult) {
            Node importedNode = outputDoc.importNode(node, true);
            if (resultEle != null) {
                resultEle.appendChild(importedNode);
            } else {
                outputDoc.appendChild(importedNode);
            }
        }

        return outputDoc;
    }

    public static void writeDocumentToStream(Document document, OutputStream outputStream) throws TransformerException {
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.transform(new DOMSource(document), new StreamResult(outputStream));
    }

    public static void processAndOutputXML(String xmlFileName, OutputStream outputStream, boolean addResultElement)
            throws Exception {
        List<Node> nodes = loadNodes(xmlFileName);
        Document document = generateResultDocument(nodes, addResultElement);
        writeDocumentToStream(document, outputStream);
    }
}
