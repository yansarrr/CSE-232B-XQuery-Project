package org.cse232b;

import org.cse232b.antlr4.XPathLexer;
import org.cse232b.antlr4.XPathParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.w3c.dom.Node;

import java.io.InputStream;
import java.util.List;

public class XPathEvaluator {

    private static XPathParser initializeParser(InputStream xPathStream) throws Exception {
        CharStream cs = CharStreams.fromStream(xPathStream);
        XPathLexer lexer = new XPathLexer(cs);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return new XPathParser(tokens);
    }

    public static List<Node> evaluateXPath(InputStream xPathStream) throws Exception {
        XPathParser parser = initializeParser(xPathStream);
        parser.removeErrorListeners();
        ExtendedXPathVisitor visitor = new ExtendedXPathVisitor();
        List<Node> result = visitor.visit(parser.ap());
        if (result == null) {
            throw new Exception("visitor did not successfully retrieve the result");
        }
        return result;
    }


    // XQuery AP parse
    public static List<Node> evaluateXPathAPWithRtException(InputStream inputStream) {
        List<Node> res = null;
        try {
            res = evaluateXPath(inputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    //  XQuery RP parse
    public static List<Node> evaluateXPathRPByPNodesWithRtException(InputStream inputStream, List<Node> pNodes) {
        try {
            // Setup parsing components
            CharStream charStream = CharStreams.fromStream(inputStream);
            XPathLexer lexer = new XPathLexer(charStream);
            CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            XPathParser parser = new XPathParser(tokenStream);
            parser.removeErrorListeners(); // Suppress default errors

            // Configure and execute XPath evaluation
            ExtendedXPathVisitor visitor = new ExtendedXPathVisitor();
            visitor.setParamNodes(pNodes);
            return visitor.visit(parser.rp());
        } catch (Exception e) {
            throw new RuntimeException("XPath evaluation failed.", e); // Specific message
        }
    }




    public static List<Node> evaluateXPathWithoutExceptionPrintErr(InputStream xPathStream) {
        try {
            return evaluateXPath(xPathStream);
        } catch (Exception e) {
            System.err.println("XPath evaluation ended due to an error: " + e.getMessage());
            return null;
        }
    }
}
