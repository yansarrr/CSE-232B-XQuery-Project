package org.cse232b;

import org.cse232b.antlr4.XQueryParser;
import org.cse232b.antlr4.XQueryLexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class XQueryEvaluator {


    public static List<Node> evaluateXQuery(InputStream xQueryIStream) throws IOException, ParserConfigurationException {
        System.out.println("Starting XQuery evaluation...");

        // Create input stream and parsing components
        CharStream charStream = CharStreams.fromStream(xQueryIStream);
        System.out.println("CharStream created.");

        XQueryLexer lexer = new XQueryLexer(charStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        System.out.println("Lexer and TokenStream initialized.");

        XQueryParser parser = new XQueryParser(tokenStream);
        parser.removeErrorListeners();
        System.out.println("Parser initialized and default error listeners removed.");

        // Prepare XML processing
        DocumentBuilder documentBuilder = XMLProcessor.buildFactory.newDocumentBuilder();
        System.out.println("DocumentBuilder initialized.");

        ExtendedXQueryVisitor visitor = new ExtendedXQueryVisitor(documentBuilder.newDocument());
        System.out.println("Visitor initialized with a new document.");

        // Perform XQuery evaluation
        List<Node> result = null;
        try {
            result = visitor.visit(parser.xq());
            System.out.println("XQuery evaluation performed.");
        } catch (Exception e) {
            System.err.println("Exception caught during XQuery evaluation: " + e.toString());
            e.printStackTrace(); // Print stack trace for debugging
        }

        return result;
    }


    public static List<Node> evaluateXQueryWithoutExceptionPrintErr(InputStream xQueryStream) {
        try {
            return evaluateXQuery(xQueryStream);
        } catch (Exception e) {
            System.err.println("XQuery evaluation ended due to an error: " + e.getMessage());
            return null;
        }
    }
}
