package org.cse232b;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.util.List;

public class MainEngine {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.printf("Incorrect number of arguments: expected 3 but received %d\n", args.length);
            System.out.println("Usage: java -jar CSE232B-Milestone3.jar <-option> <input_xquery_file> <output_xml_file>");
            return;
        }
        if ("-p".equals(args[0])) {
            xPathEvaluate(args[1], args[2]);
        } else if ("-q".equals(args[0])) {
            xQueryEvaluate(args[1], args[2]);
        } else if ("-j".equals(args[0])) {
            reWriteXQueryToJoin(args[1]);
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

    private static void reWriteXQueryToJoin(String xQueryFilePath) {
        String reWriteRes = "";
        try (InputStream xQueryIStream = new FileInputStream(xQueryFilePath)){
            reWriteRes = XQueryRewriter.rewriteToJoinXquery(xQueryFilePath, xQueryIStream);
        } catch (IOException e) {
            System.err.println("open xQuery file failed: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("rewrite xquery failed : " + e.getMessage());
        }

        File file = new File("rewrite-" + xQueryFilePath);

        // Ensure parent directories exist
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        // Create file if it doesn't exist
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        // Write to file
        try (FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
             BufferedWriter bufferWriter = new BufferedWriter(fileWriter)) {
            bufferWriter.write(reWriteRes);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

//    private static void reWriteXQueryToJoin(String xQueryFilePath) {
//        String reWriteRes = "";
//        try (InputStream xQueryIStream = new FileInputStream(xQueryFilePath)){
//            reWriteRes = XQueryRewriter.rewriteToJoinXquery(xQueryFilePath, xQueryIStream);
//        }catch (IOException e) {
//            System.err.println("open xQuery file failed: " + e.getMessage());
//        } catch (Exception e) {
//            System.err.println("rewrite xquery failed : " + e.getMessage());
//        }
//        File file =new File("rewrite-" + xQueryFilePath);
//
//        //if file doesn't exist, then create it
//        if(!file.exists()){
//            try {
//                file.createNewFile();
//            } catch (IOException ex) {
//                throw new RuntimeException(ex);
//            }
//        }
//
//        //true = append file
//        try (            FileWriter fileWriter = new FileWriter(file.getName());
//                         BufferedWriter bufferWriter = new BufferedWriter(fileWriter)){
//            bufferWriter.write(reWriteRes);
//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }
//    }


}