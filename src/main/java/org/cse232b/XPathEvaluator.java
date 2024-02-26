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


    public List<Node> eval(InputStream inputStream) throws Exception {
        CharStream cs = CharStreams.fromStream(inputStream);
        XPathLexer lexer = new XPathLexer(cs);
        CommonTokenStream tks = new CommonTokenStream(lexer);
        XPathParser parser = new XPathParser(tks);
        parser.removeErrorListeners();
        ExtendedXPathVisitor visitor = new ExtendedXPathVisitor();
        List<Node> res = visitor.visit(parser.ap());
        if (res == null) {
            throw new Exception("Empty result.");
        }
        return res;
    }

    public List<Node> evalRP(InputStream inputStream, List<Node> ctxNodes) {
        try {
            CharStream cs = CharStreams.fromStream(inputStream);
            XPathLexer lexer = new XPathLexer(cs);
            CommonTokenStream tks = new CommonTokenStream(lexer);
            XPathParser parser = new XPathParser(tks);
            parser.removeErrorListeners();
            ExtendedXPathVisitor visitor = new ExtendedXPathVisitor();
            visitor.setParamNodes(ctxNodes);
            return visitor.visit(parser.rp());
        } catch (Exception e) {
            throw new RuntimeException(e);
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
