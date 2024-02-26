package org.cse232b;

import org.cse232b.antlr4.XQueryBaseVisitor;
import org.cse232b.antlr4.XQueryParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

public class ExtendedXQueryVisitor extends XQueryBaseVisitor<List<Node>> {
    Map<String, List<Node>> varMap = new HashMap<>();

    List<Map<String, List<Node>>> forStates;

    Document doc;

    XPathEvaluator evaluator = new XPathEvaluator();

    public ExtendedXQueryVisitor(Document doc) {
        this.doc = doc;
    }

    private Node makeText(String text) {
        return doc.createTextNode(text);
    }

    @Override
    public List<Node> visitVarXQ(XQueryParser.VarXQContext ctx) {
        String varName = ctx.var().attName().getText();
        return varMap.getOrDefault(varName, new LinkedList<>());
    }

    @Override
    public List<Node> visitStrXQ(XQueryParser.StrXQContext ctx) {
        String s = ctx.StringConstant().getText();
        s = s.substring(1, s.length() - 1); // remove braces

        List<Node> res = new LinkedList<>();
        res.add(makeText(s));
        return res;
    }

    @Override
    public List<Node> visitApXQ(XQueryParser.ApXQContext ctx) {
        String ap = ctx.ap().getText();
        InputStream is = new ByteArrayInputStream(ap.getBytes());
        List<Node> res = null;
        try {
            res = evaluator.eval(is);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    @Override
    public List<Node> visitBraceXQ(XQueryParser.BraceXQContext ctx) {
        return visit(ctx.xq());
    }

    @Override
    public List<Node> visitCommaXQ(XQueryParser.CommaXQContext ctx) {
        Map<String, List<Node>> temMap = new HashMap<>(varMap);
        List<Node> res1 = visit(ctx.xq(0));
        varMap = temMap;
        List<Node> res2 = visit(ctx.xq(1));
        res1.addAll(res2);
        return res1;
    }

    @Override
    public List<Node> visitSingleSlashXQ(XQueryParser.SingleSlashXQContext ctx) {
        String rpText = ctx.rp().getText();
        InputStream is = new ByteArrayInputStream(rpText.getBytes());
//        return evaluator.evalRP(is, visit(ctx.xq()));
        List<Node> temp = visit(ctx.xq());
        List<Node> res = evaluator.evalRP(is, temp);
        return res;
    }

    @Override
    public List<Node> visitDoubleSlashXQ(XQueryParser.DoubleSlashXQContext ctx) {
        String rpText = ctx.rp().getText();
        InputStream is = new ByteArrayInputStream(rpText.getBytes());
        List<Node> xqRes = getDescendantOrSelf(visit(ctx.xq()));
        return evaluator.evalRP(is, xqRes);
    }

    public List<Node> getDescendantOrSelf(List<Node> nodes){
        ExtendedXPathVisitor xpathVisitor = new ExtendedXPathVisitor();
        List<Node> res = new LinkedList<>();
        for (Node node : nodes){
            res.addAll(xpathVisitor.getDescendantOrSelf(node));
        }
        return new LinkedList<>(
                new LinkedHashSet<>(res));
    }

    @Override
    public List<Node> visitTagXQ(XQueryParser.TagXQContext ctx) {
        String tag = ctx.tagName(0).getText();
        List<Node> nodes = visit(ctx.xq());
        List<Node> res = new LinkedList<>();
        res.add(makeElem(tag, nodes));
        return res;
    }

    @Override
    public List<Node> visitFlwrXQ(XQueryParser.FlwrXQContext ctx) {
        List<Map<String, List<Node>>> oldForStates = forStates;
        visit(ctx.forClause());
        if (ctx.letClause() != null) {
            for (int i = 0; i < forStates.size(); i++) {
                varMap = forStates.get(i);
                visit(ctx.letClause());
                forStates.set(i, varMap);
            }
        }
        List<Node> res = new LinkedList<>();
        for (Map<String, List<Node>> varState : forStates) {
            varMap = varState;
            List<Node> whereRes = null;
            if (ctx.whereClause() != null) {
                whereRes = visit(ctx.whereClause());
            } else {
                whereRes = new LinkedList<>();
            }
            if (whereRes != null) {
                varMap = varState;
                res.addAll(visit(ctx.returnClause()));
            }
        }
        forStates = oldForStates;
        return res;
    }

    @Override
    public List<Node> visitLetXQ(XQueryParser.LetXQContext ctx) {
        visit(ctx.letClause());
        return visit(ctx.xq());
    }

    @Override
    public List<Node> visitForClause(XQueryParser.ForClauseContext ctx) {
        List<Map<String, List<Node>>> states = new LinkedList<>();
        recVisitForClause(ctx, 0, varMap, states);
        forStates = states;
        return null;
    }

    private void recVisitForClause(
            XQueryParser.ForClauseContext ctx,
            int index,
            Map<String, List<Node>> currVarMap,
            List<Map<String, List<Node>>> states
    ) {
        if (ctx.var().size() == index) {
            states.add(currVarMap);
            return;
        }
        String varName = ctx.var(index).attName().getText();
        varMap = currVarMap;
        List<Node> nodes = visit(ctx.xq(index));
        for (Node node : nodes) {
            Map<String, List<Node>> newMap = new HashMap<>(currVarMap);
            List<Node> newList = new LinkedList<>();
            newList.add(node);
            newMap.put(varName, newList);
            recVisitForClause(ctx, index + 1, newMap, states);
        }
    }

    @Override
    public List<Node> visitLetClause(XQueryParser.LetClauseContext ctx) {
        for (int i = 0; i < ctx.var().size(); i++) {
            List<Node> res = visit(ctx.xq(i));
            varMap.put(ctx.var(i).getText(), res);
        }
        return null;
    }

    @Override
    public List<Node> visitWhereClause(XQueryParser.WhereClauseContext ctx) {
        return visit(ctx.cond());
    }

    @Override
    public List<Node> visitReturnClause(XQueryParser.ReturnClauseContext ctx) {
        return visit(ctx.xq());
    }

    @Override
    public List<Node> visitEqCond(XQueryParser.EqCondContext ctx) {
        Map<String, List<Node>> temp = varMap;
        List<Node> left = visit(ctx.xq(0));
        varMap = temp;
        List<Node> right = visit(ctx.xq(1));
        for (Node n1 : left) {
            for (Node n2 : right) {
                if (n1.isEqualNode(n2)) {
                    return new LinkedList<>();
                }
            }
        }
        return null;
    }

    @Override
    public List<Node> visitIsCond(XQueryParser.IsCondContext ctx) {
        Map<String, List<Node>> temp = varMap;
        List<Node> left = visit(ctx.xq(0));
        varMap = temp;
        List<Node> right = visit(ctx.xq(1));
        for (Node n1 : left) {
            for (Node n2 : right) {
                if (n1.isSameNode(n2)) {
                    return new LinkedList<>();
                }
            }
        }
        return null;
    }

    @Override
    public List<Node> visitEmptyCond(XQueryParser.EmptyCondContext ctx) {
        List<Node> res = visit(ctx.xq());
        if (res.isEmpty()) {
            return new LinkedList<>();
        } else {
            return null;
        }
    }

    @Override
    public List<Node> visitSatisfyCond(XQueryParser.SatisfyCondContext ctx) {
        return recVisitSatisfyCond(ctx, 0, varMap);
    }

    private List<Node> recVisitSatisfyCond(
            XQueryParser.SatisfyCondContext ctx,
            int index,
            Map<String, List<Node>> currVarMap
    ) {
        if (ctx.var().size() == index) {
            varMap = currVarMap;
            return visit(ctx.cond());
        }
        String varName = ctx.var(index).attName().getText();
        varMap = currVarMap;
        List<Node> nodes = visit(ctx.xq(index));
        for (Node node : nodes) {
            Map<String, List<Node>> newMap = new HashMap<>(currVarMap);
            List<Node> newList = new LinkedList<>();
            newList.add(node);
            newMap.put(varName, newList);
            List<Node> res = recVisitSatisfyCond(ctx, index + 1, newMap);
            if (res != null) {
                return res;
            }
        }
        return null;
    }

    @Override
    public List<Node> visitBraceCond(XQueryParser.BraceCondContext ctx) {
        return visit(ctx.cond());
    }

    @Override
    public List<Node> visitAndCond(XQueryParser.AndCondContext ctx) {
        Map<String, List<Node>> temp = varMap;
        List<Node> left = visit(ctx.cond(0));
        varMap = temp;
        List<Node> right = visit(ctx.cond(1));
        if (left != null && right != null) {
            return new LinkedList<>();
        } else {
            return null;
        }
    }

    @Override
    public List<Node> visitOrCond(XQueryParser.OrCondContext ctx) {
        Map<String, List<Node>> temp = varMap;
        List<Node> left = visit(ctx.cond(0));
        varMap = temp;
        List<Node> right = visit(ctx.cond(1));
        if (left != null || right != null) {
            return new LinkedList<>();
        } else {
            return null;
        }
    }

    @Override
    public List<Node> visitNotCond(XQueryParser.NotCondContext ctx) {
        List<Node> res = visit(ctx.cond());
        if (res == null) {
            return new LinkedList<>();
        } else {
            return null;
        }
    }


    private Node makeElem(String tag, List<Node> nodes) {
        Node res = doc.createElement(tag);
        for (Node node : nodes) {
            if (node != null) {
                res.appendChild(doc.importNode(node, true));
            }
        }
        return res;
    }
}
