package org.cse232b;

import org.cse232b.antlr4.XQueryBaseVisitor;
import org.cse232b.antlr4.XQueryParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.*;
import java.util.stream.Collectors;

import static org.cse232b.XPathEvaluator.evaluateXPathRPByPNodesWithRtException;


public class ExtendedXQueryVisitor extends XQueryBaseVisitor<List<Node>>{

    private final Document tmpDoc;
    private Map<String, List<Node>> contextMap = new HashMap<>();
    private List<Map<String, List<Node>>> forClausePerStates = new ArrayList<>();
    void setContextMap(Map<String,List<Node>> c) {
        this.contextMap = new HashMap<>(c);
    }
    private final List<Node> DEFAULT_COND_TRUE_LIST = new ArrayList<>(0);

    public LinkedList<Node> getStrictDescendents(Node node) {
        LinkedList<Node> result = new LinkedList<>();
        addDescendents(node, result);
        return result;
    }

    private void addDescendents(Node node, LinkedList<Node> result) {
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            result.add(child);
            addDescendents(child, result);
        }
    }

    public LinkedList<Node> getSelfAndDescendents(List<Node> nodes) {
        // Temporary set for efficient duplicate tracking
        HashSet<Node> nodeSet = new HashSet<>(nodes);

        // Collect ALL strict descendants, including potential duplicates
        LinkedList<Node> allDescendants = nodes.stream()
                .flatMap(node -> getStrictDescendents(node).stream())
                .collect(Collectors.toCollection(LinkedList::new));

        // Add each descendant, ensuring uniqueness via the set
        allDescendants.forEach(node -> nodeSet.add(node));

        return new LinkedList<>(nodeSet);
    }


    private Node makeElem(String tag, List<Node> children) {
        Node r = tmpDoc.createElement(tag);
        for (Node n : children) {
            if (n != null) {
                Node newNode = this.tmpDoc.importNode(n,true);
                r.appendChild(newNode);
            }
        }
        return r;
    }

    public ExtendedXQueryVisitor(Document tmpDoc) {
        this.tmpDoc = tmpDoc;
    }


    @Override
    public List<Node> visitFLWR(XQueryParser.FLWRContext ctx) {
        List<Map<String, List<Node>>> originalStates = this.forClausePerStates;

        visit(ctx.forClause()); // Populate varsInForClause (side-effect)
        List<Map<String, List<Node>>> updatedStates = this.forClausePerStates;

        // Update states with let clause (if present)
        if (ctx.letClause() != null) {
            for (int i = 0; i < updatedStates.size(); i++) {
                Map<String, List<Node>> stateToUpdate = updatedStates.get(i);
                setContextMap(stateToUpdate);
                visit(ctx.letClause());
                updatedStates.set(i, this.contextMap);
            }
        }

        List<Node> result = new LinkedList<>();
        for (Map<String, List<Node>> state : updatedStates) {
            setContextMap(state);

            // Handle where clause evaluation
            boolean whereClauseSatisfied = true;
            if (ctx.whereClause() != null) {
                List<Node> whereResult = visit(ctx.whereClause());
                whereClauseSatisfied = whereResult != null;
            }

            if (whereClauseSatisfied) {
                setContextMap(state);
                result.addAll(visit(ctx.returnClause()));
            }
        }

        this.forClausePerStates = originalStates;
        return result;
    }


    @Override
    public List<Node> visitXqSingleSlash(XQueryParser.XqSingleSlashContext ctx) {
        String relativePath = ctx.rp().getText();
        InputStream relativePathStream = new ByteArrayInputStream(relativePath.getBytes());

        List<Node> contextNodes = visit(ctx.xq()); // Evaluate the preceding XQuery expression
        return evaluateXPathRPByPNodesWithRtException(relativePathStream, contextNodes);
    }


    @Override
    public List<Node> visitXqDoubleSlash(XQueryParser.XqDoubleSlashContext ctx) {
        String relativePath = ctx.rp().getText();
        InputStream i = new ByteArrayInputStream(relativePath.getBytes());

        List<Node> xqRes = getSelfAndDescendents(visit(ctx.xq())); // Evaluate the preceding XQuery expression
        return evaluateXPathRPByPNodesWithRtException(i, xqRes);
    }


    @Override
    public List<Node> visitXqTag(XQueryParser.XqTagContext ctx) {
        String tag = ctx.startTag().tagName().ID().getText();
        LinkedList<Node> res = new LinkedList<>();

        setContextMap(contextMap);
        List<Node> nodeList = visit(ctx.xq());

        Node node = makeElem(tag, nodeList);
        res.add(node);

        return res;
    }

    @Override
    public List<Node> visitXqBracket(XQueryParser.XqBracketContext ctx) {
        setContextMap(contextMap);
        String ap = ctx.getText();
        InputStream is = new ByteArrayInputStream(ap.getBytes());
        return XPathEvaluator.evaluateXPathAPWithRtException(is);
    }

    @Override
    public List<Node> visitXqLet(XQueryParser.XqLetContext ctx) {
        // prepare vars
        visit(ctx.letClause());
        Map<String,List<Node>> currentContext = this.contextMap;
        setContextMap(currentContext);
        List<Node> res = visit(ctx.xq());
        return res;

    }

    @Override
    public List<Node> visitXqConcat(XQueryParser.XqConcatContext ctx) {
        Map<String, List<Node>> currentCtxMap = contextMap;
        setContextMap(currentCtxMap);
        List<Node> res1 = visit(ctx.xq(0));
        setContextMap(currentCtxMap);
        List<Node> res2 = visit(ctx.xq(1));

        res1.addAll(res2);
        return res1;
    }

    @Override
    public List<Node> visitVariable(XQueryParser.VariableContext ctx) {
        setContextMap(contextMap);
        return this.contextMap.getOrDefault(ctx.var().ID().getText(), new LinkedList<Node>());
    }

    @Override
    public List<Node> visitString(XQueryParser.StringContext ctx) {
        String quotedString = ctx.StringConstant().getText();
        String content = quotedString.substring(1, quotedString.length() - 1); // Extract text content

        LinkedList<Node> result = new LinkedList<>();
        result.add(this.tmpDoc.createTextNode(content));

        return result;
    }

    @Override
    public List<Node> visitXqBrace(XQueryParser.XqBraceContext ctx) {
        setContextMap(contextMap);
        List<Node> result = visit(ctx.xq());

        return result;
    }


    public void dfsForVarState(XQueryParser.ForClauseContext ctx,
                               int curIndex,
                               Map<String, List<Node>> curMap,
                               List<Map<String,List<Node>>> perStates) {
        List<Node> res;

        if(ctx.var().size() == curIndex) {
            perStates.add(curMap);
            return;
        }
        String var = ctx.var(curIndex).ID().getText();
        XQueryParser.XqContext xq = ctx.xq(curIndex);
        setContextMap(curMap);
        res = visit(xq);
        for (Node node : res) {
            Map<String, List<Node>> nextMap = new HashMap<>(curMap);
            LinkedList<Node> curNodeList = new LinkedList<>();
            curNodeList.add(node);
            nextMap.put(var, curNodeList);   // results in a deeper context map extended on the basis of the current context map

            dfsForVarState(ctx, curIndex + 1, nextMap, perStates);
        }

    }

    @Override
    public List<Node> visitForClause(XQueryParser.ForClauseContext ctx) {
        // for should generate all permutation state maps and then set perStates in this class for FLWR
        Map<String,List<Node>> currentContext = this.contextMap;
        List<Map<String, List<Node>>> targetPerStates = new ArrayList<>();
        // set for FLWR
        dfsForVarState(ctx, 0,currentContext,targetPerStates);
        this.forClausePerStates = targetPerStates;
        return null;
    }

    @Override
    public List<Node> visitLetClause(XQueryParser.LetClauseContext ctx) {
        Map<String, List<Node>> currentContext = this.contextMap;
        // Process variable bindings
        List<XQueryParser.VarContext> variables = ctx.var();
        List<XQueryParser.XqContext> expressions = ctx.xq();

        for (int i = 0; i < variables.size(); i++) { // assuming variables and expressions have equal lengths
            String variableName = variables.get(i).ID().getText();
            List<Node> expressionResult = visit(expressions.get(i));
            currentContext.put(variableName, expressionResult);
        }
        setContextMap(currentContext); // Restore context
        return null;
    }

    @Override
    public List<Node> visitWhereClause(XQueryParser.WhereClauseContext ctx) {
        // with given context, return the boolean result of cond
        return visit(ctx.cond());
    }

    @Override
    public List<Node> visitReturnClause(XQueryParser.ReturnClauseContext ctx) {
        // with given context, evaluate xq. should use the same context with where clause.
        return visit(ctx.xq());
    }

    @Override
    public List<Node> visitBraceCond(XQueryParser.BraceCondContext ctx) {
        return visit(ctx.cond());
    }

    @Override
    public List<Node> visitOrCond(XQueryParser.OrCondContext ctx) {
        Map<String,List<Node>> currentContext = this.contextMap;
        setContextMap(currentContext);
        boolean bL = visit(ctx.cond(0)) != null;
        setContextMap(currentContext);
        boolean bR = visit(ctx.cond(1)) != null;
        return bL || bR ? DEFAULT_COND_TRUE_LIST : null;
    }


    public List<Node> visitSomeVarXq(XQueryParser.SatisfyCondContext ctx, int curIndex, Map<String, List<Node>> curMap) {
        if (ctx.var().size() == curIndex) {
            setContextMap(curMap);
            return visit(ctx.cond());
        }

        String varName = ctx.var(curIndex).ID().getText();
        XQueryParser.XqContext xqContext = ctx.xq(curIndex);

        setContextMap(curMap);
        List<Node> xqResult = visit(xqContext);

        for (Node node : xqResult) {
            Map<String, List<Node>> extendedContext = new HashMap<>(curMap);
            extendedContext.put(varName, Collections.singletonList(node));

            List<Node> recursiveResult = visitSomeVarXq(ctx, curIndex + 1, extendedContext);
            if (recursiveResult != null) {
                return recursiveResult;
            }
        }

        return null;
    }

    @Override
    public List<Node> visitSatisfyCond(XQueryParser.SatisfyCondContext ctx) {
        return visitSomeVarXq(ctx, 0, contextMap);   // call in a recursive way
    }



    @Override
    public List<Node> visitEmptyCond(XQueryParser.EmptyCondContext ctx) {

        List<Node> res;
        setContextMap(contextMap);
        res = visit(ctx.xq());
        if (res.size() != 0) return null;  // false
        return DEFAULT_COND_TRUE_LIST;   // true
    }

    @Override
    public List<Node> visitAndCond(XQueryParser.AndCondContext ctx) {
        Map<String,List<Node>> currentContext = this.contextMap;
        setContextMap(currentContext);
        boolean bL = visit(ctx.cond(0)) != null; // not null -true /null -false
        setContextMap(currentContext);
        boolean bR = visit(ctx.cond(1)) != null; // not null -true
        return bL && bR ? DEFAULT_COND_TRUE_LIST: null;
    }

    @Override
    public List<Node> visitIsCond(XQueryParser.IsCondContext ctx) {

        Map<String, List<Node>> currentCtxMap = contextMap;

        setContextMap(currentCtxMap);
        List<Node> l = visit(ctx.xq(0));
        setContextMap(currentCtxMap);
        List<Node> r = visit(ctx.xq(1));

        for (Node ln: l) {
            for (Node rn: r) {
                if (ln.isSameNode(rn)) {
                    return DEFAULT_COND_TRUE_LIST;  // true
                }
            }
        }
        return null; // false
    }

    @Override
    public List<Node> visitEqCond(XQueryParser.EqCondContext ctx) {
        Map<String, List<Node>> currentContext = contextMap; // Store original context

        List<Node> leftResult = visit(ctx.xq(0));
        List<Node> rightResult = visit(ctx.xq(1));

        for (Node leftNode : leftResult) {
            for (Node rightNode : rightResult) {
                if (leftNode.isEqualNode(rightNode)) {
                    return DEFAULT_COND_TRUE_LIST;  // Early return if match found
                }
            }
        }
        setContextMap(currentContext); // Restore original context
        return null; // No matches found
    }


    @Override
    public List<Node> visitNotCond(XQueryParser.NotCondContext ctx) {
        boolean flag = visit(ctx.cond()) != null;

        return flag ? null : DEFAULT_COND_TRUE_LIST;
    }


    private List<Node> obtainColumns(Node tuple) {
        List<Node> columns= new LinkedList<>();
        int childrenSize = tuple.getChildNodes().getLength();
        for (int i = 0; i < childrenSize; ++i)
            columns.add(tuple.getChildNodes().item(i));
        return columns;
    }




    // Attention: this method will never be called.
    @Override
    public List<Node> visitStartTag(XQueryParser.StartTagContext ctx) {
        return super.visitStartTag(ctx);
    }

    // Attention: this method will never be called.
    @Override
    public List<Node> visitEndTag(XQueryParser.EndTagContext ctx) {
        return super.visitEndTag(ctx);
    }


    // Attention: this method will never be called.
    @Override
    public List<Node> visitVar(XQueryParser.VarContext ctx) {
        return super.visitVar(ctx);
    }




    // TODO: Do NOT change methods below!

    @Override
    public List<Node> visitSingleAP(XQueryParser.SingleAPContext ctx) {
        return super.visitSingleAP(ctx);
    }

    @Override
    public List<Node> visitDoubleAP(XQueryParser.DoubleAPContext ctx) {
        return super.visitDoubleAP(ctx);
    }

    @Override
    public List<Node> visitDoc(XQueryParser.DocContext ctx) {
        return super.visitDoc(ctx);
    }

    @Override
    public List<Node> visitAttrRP(XQueryParser.AttrRPContext ctx) {
        return super.visitAttrRP(ctx);
    }

    @Override
    public List<Node> visitDoubleSlashRP(XQueryParser.DoubleSlashRPContext ctx) {
        return super.visitDoubleSlashRP(ctx);
    }

    @Override
    public List<Node> visitTextRP(XQueryParser.TextRPContext ctx) {
        return super.visitTextRP(ctx);
    }

    @Override
    public List<Node> visitParentRP(XQueryParser.ParentRPContext ctx) {
        return super.visitParentRP(ctx);
    }

    @Override
    public List<Node> visitSelfRP(XQueryParser.SelfRPContext ctx) {
        return super.visitSelfRP(ctx);
    }

    @Override
    public List<Node> visitFilterRP(XQueryParser.FilterRPContext ctx) {
        return super.visitFilterRP(ctx);
    }

    @Override
    public List<Node> visitCommaRP(XQueryParser.CommaRPContext ctx) {
        return super.visitCommaRP(ctx);
    }

    @Override
    public List<Node> visitChildrenRP(XQueryParser.ChildrenRPContext ctx) {
        return super.visitChildrenRP(ctx);
    }

    @Override
    public List<Node> visitTagRP(XQueryParser.TagRPContext ctx) {
        return super.visitTagRP(ctx);
    }

    @Override
    public List<Node> visitBracketRP(XQueryParser.BracketRPContext ctx) {
        return super.visitBracketRP(ctx);
    }

    @Override
    public List<Node> visitSingleSlashRP(XQueryParser.SingleSlashRPContext ctx) {
        return super.visitSingleSlashRP(ctx);
    }

    @Override
    public List<Node> visitEqFilter(XQueryParser.EqFilterContext ctx) {
        return super.visitEqFilter(ctx);
    }

    @Override
    public List<Node> visitNotFilter(XQueryParser.NotFilterContext ctx) {
        return super.visitNotFilter(ctx);
    }

    @Override
    public List<Node> visitAndFilter(XQueryParser.AndFilterContext ctx) {
        return super.visitAndFilter(ctx);
    }

    @Override
    public List<Node> visitBracketFilter(XQueryParser.BracketFilterContext ctx) {
        return super.visitBracketFilter(ctx);
    }

    @Override
    public List<Node> visitIsFilter(XQueryParser.IsFilterContext ctx) {
        return super.visitIsFilter(ctx);
    }

    @Override
    public List<Node> visitRpFilter(XQueryParser.RpFilterContext ctx) {
        return super.visitRpFilter(ctx);
    }

    @Override
    public List<Node> visitOrFilter(XQueryParser.OrFilterContext ctx) {
        return super.visitOrFilter(ctx);
    }

    @Override
    public List<Node> visitTagName(XQueryParser.TagNameContext ctx) {
        return super.visitTagName(ctx);
    }

    @Override
    public List<Node> visitAttrName(XQueryParser.AttrNameContext ctx) {
        return super.visitAttrName(ctx);
    }

    @Override
    public List<Node> visitFileName(XQueryParser.FileNameContext ctx) {
        return super.visitFileName(ctx);
    }

}
