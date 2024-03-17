package org.cse232b;
import org.cse232b.antlr4.XQueryLexer;
import org.cse232b.antlr4.XQueryParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import java.io.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class XQueryRewriter {
    static String rewriteToJoinXquery(String originFilePath, InputStream inputStreamToParse) {
        String res = exeJoinWrite(inputStreamToParse);
        if( ExtendedJoinVisitor.NO_CHANGE_MARK.equals(res)) {
            File oriFile = new File(originFilePath);
            return readToString(oriFile);
        }
        return res;
    }

    private static String readToString(File file) {
        try {
            // Read all bytes from the file and convert to String
            return new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static String exeJoinWrite(InputStream inputStream) {
        try {
            CharStream cs = CharStreams.fromStream(inputStream);
            XQueryLexer lexer = new XQueryLexer(cs);
            CommonTokenStream tks = new CommonTokenStream(lexer);
            XQueryParser parser = new XQueryParser(tks);
            parser.removeErrorListeners();
            ExtendedJoinVisitor visitor = new ExtendedJoinVisitor();
            String res = visitor.visit(parser.xq());
            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
