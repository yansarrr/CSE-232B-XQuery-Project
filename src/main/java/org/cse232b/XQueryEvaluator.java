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
        // Create input stream and parsing components
        CharStream charStream = CharStreams.fromStream(xQueryIStream);
        XQueryLexer lexer = new XQueryLexer(charStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        XQueryParser parser = new XQueryParser(tokenStream);
        parser.removeErrorListeners(); // Suppress default error handling

        // Prepare XML processing
        DocumentBuilder documentBuilder = XMLProcessor.buildFactory.newDocumentBuilder();
        ExtendedXQueryVisitor visitor = new ExtendedXQueryVisitor(documentBuilder.newDocument());

        // Perform XQuery evaluation
        List<Node> result = visitor.visit(parser.xq());

        // Handle potential failure in visitor
        if (result == null) {
            throw new RuntimeException("XQuery evaluation failed."); // More informative message
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
