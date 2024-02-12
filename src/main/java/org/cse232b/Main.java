package org.cse232b;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.printf("Incorrect number of arguments: expected 2 but received %d\n", args.length);
            System.out.println("Usage: java -jar CSE232B-Milestone1.jar <input_xpath_file> <output_xml_file>");
            return;
        }
        xPathEvaluate(args[0], args[1]);
    }

    private static void xPathEvaluate(String xPathFilePath, String outputFilePath) {
        List<Node> rawEvaluateRes = null;
        try (InputStream xPathIStream = new FileInputStream(xPathFilePath)) {
            rawEvaluateRes = XPathEvaluator.evaluateXPathWithoutExceptionPrintErr(xPathIStream);
        } catch (IOException e) {
            System.err.println("Open XPath file failed: " + e.getMessage());
            return;
        }
        if (rawEvaluateRes == null) {
            System.err.println("XPath evaluation failed. No output file exported.");
            return;
        }
        System.out.println("XPath evaluation finished, writing result file...");
        writeResultToFile(rawEvaluateRes, outputFilePath, true);
    }

    private static void writeResultToFile(List<Node> rawRes, String outputFilePath, boolean addResEle) {
        try {
            Document resultDocument = XMLProcessor.generateResultDocument(rawRes, addResEle);

            try (OutputStream resultXMLOStream = new FileOutputStream(outputFilePath)) {
                XMLProcessor.writeDocumentToStream(resultDocument, resultXMLOStream);
                System.out.println("Result File exported successfully!");
            }
        } catch (IOException e) {
            System.err.println("Open result file failed: " + e.getMessage());
        } catch (ParserConfigurationException | TransformerException e) {
            System.err.println("Generating XML or transforming failed: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Runtime exception while generating/writing result: " + e.getMessage());
        }
    }



}