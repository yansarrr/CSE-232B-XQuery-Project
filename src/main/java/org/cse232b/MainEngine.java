package org.cse232b;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
public class MainEngine {
    public static void main(String[] args) {
        // Check if the correct number of arguments are passed
        if (args.length != 3) {
            System.out.printf("Incorrect number of arguments: expected 3 but received %d\n", args.length);
            System.out.println("Usage: java -jar CSE232B-Milestone2.jar <p/q> <input_xquery_file> <output_xml_file>");
            return;
        }

        try {
            // Process the arguments
            String choice = args[0].toLowerCase(); // Handle case sensitivity for 'p' or 'q'

            switch (choice) {
                case "p":
                    // Evaluate XPath
                    xPathEvaluate(args[1], args[2]);
                    break;
                case "q":
                    // Evaluate XQuery
                    xQueryEvaluate(args[1], args[2]);
                    break;
                default:
                    // Handle invalid choice
                    System.out.printf("Invalid choice: '%s'. Please choose 'p' for XPath or 'q' for XQuery.\n", choice);
                    break;
            }
        } catch (Exception e) {
            // Handle any exceptions during the evaluation
            System.err.printf("Evaluation terminated: %s\n", e.getMessage());
            e.printStackTrace();
        }
    }


    private static void xPathEvaluate(String xPathFilePath, String outputFilePath) {
        List<Node> rawEvaluateRes = null;
        try (InputStream xPathIStream = new FileInputStream(xPathFilePath)) {
            rawEvaluateRes = XPathEvaluator.evaluateXPathWithoutExceptionPrintErr(xPathIStream);
        } catch (IOException e) {
            System.err.println("Open XQuery file failed: " + e.getMessage());
            return;
        }
        if (rawEvaluateRes == null) {
            System.err.println("XQuery evaluation failed. No output file exported.");
            return;
        }
        System.out.println("XQuery evaluation finished, writing result file...");
        writeResultToFile(rawEvaluateRes, outputFilePath, true);
    }


    private static void xQueryEvaluate(String xQueryFilePath, String outputFilePath) {
        List<Node> rawEvaluateRes = null;
        try (InputStream xQueryIStream = new FileInputStream(xQueryFilePath)){
            rawEvaluateRes = XQueryEvaluator.evaluateXQueryWithoutExceptionPrintErr(xQueryIStream);

        }catch (IOException e) {
            System.err.println("open xQuery file failed: " + e.getMessage());
        }
        if (rawEvaluateRes == null) {
            System.err.println("XQuery evaluation failed. No result file generated.");
            return;
        }
        System.out.println("XQuery evaluation finished, writing result file...");
        writeResultToFile(rawEvaluateRes, outputFilePath, true);
    }

    private static void evalXQuery(String inputPath, String outputPath) throws Exception {
        InputStream xQueryInput = Files.newInputStream(Paths.get(inputPath));
        XQueryEvaluator evaluator = new XQueryEvaluator();
        List<Node> result = evaluator.eval(xQueryInput);
        XMLProcessor.writeFile(outputPath, result);
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
            System.err.println("Generate or transform XML failed: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Encountered a runtime exception during result generation/writing: " + e.getMessage());
        }
    }



}