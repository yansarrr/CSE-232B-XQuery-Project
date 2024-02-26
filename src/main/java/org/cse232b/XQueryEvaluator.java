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
        CharStream cs = CharStreams.fromStream(xQueryIStream);
        XQueryLexer lexer = new XQueryLexer(cs);
        CommonTokenStream tks = new CommonTokenStream(lexer);
        XQueryParser parser = new XQueryParser(tks);
        parser.removeErrorListeners();
        DocumentBuilder bd = XMLProcessor.buildFactory.newDocumentBuilder();
        ExtendedXQueryVisitor visitor = new ExtendedXQueryVisitor(bd.newDocument());
        List<Node> res = visitor.visit(parser.xq());
        if (res == null) {
            throw new RuntimeException("visitor failed to get result.");
        }
        return res;
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
