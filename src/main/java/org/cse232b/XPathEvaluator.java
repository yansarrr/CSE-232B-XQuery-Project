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
    // once succeed, return value is valid.
    public static List<Node> evaluateXPath(InputStream xPathStream) throws Exception {
        CharStream cs = CharStreams.fromStream(xPathStream);
        XPathLexer lexer = new XPathLexer(cs);
        CommonTokenStream tks = new CommonTokenStream(lexer);
        XPathParser parser = new XPathParser(tks);
        parser.removeErrorListeners();
        ExtendedEngineXPathVisitor visitor = new ExtendedEngineXPathVisitor();
        List<Node> res = visitor.visit(parser.ap());
        if (res == null) {
            throw new Exception("visitor failed to get result.");
        }
        return res;
    }

    //TODO: use this to parse AP in xquery. Get string of rp text and transform it to InputStream.
    public static List<Node> evaluateXPathAPWithRtException(InputStream inputStream) {
        List<Node> res = null; // actually the same as evaluating xpath itself
        try {
            res = evaluateXPath(inputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    // TODO: use this to pare RP in xquery. If the connector is '//', use
    public static List<Node> evaluateXPathRPByPNodesWithRtException(InputStream inputStream, List<Node> pNodes) {
        try {
            CharStream cs = CharStreams.fromStream(inputStream);
            XPathLexer lexer = new XPathLexer(cs);
            CommonTokenStream tks = new CommonTokenStream(lexer);
            XPathParser parser = new XPathParser(tks);
            parser.removeErrorListeners();
            ExtendedEngineXPathVisitor visitor = new ExtendedEngineXPathVisitor();
            visitor.setPNodes(pNodes);
            return visitor.visit(parser.rp());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    // return null once failed.
    public static List<Node> evaluateXPathWithoutExceptionPrintErr(InputStream xPathStream) {
        List<Node> res = null;
        try {
            res = evaluateXPath(xPathStream);
        } catch (Exception e) {
            System.err.println("XPath evaluation terminated with error: " + e.getMessage());
            //e.printStackTrace();
        }
        return res;
    }

}