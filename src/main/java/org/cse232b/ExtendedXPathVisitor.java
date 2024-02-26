package org.cse232b;

import org.cse232b.antlr4.XPathBaseVisitor;
import org.cse232b.antlr4.XPathParser;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.LinkedList;
import java.util.*;
import java.util.stream.IntStream;

public class ExtendedXPathVisitor extends XPathBaseVisitor<List<Node>> {

    private List<Node> paramNodes = new LinkedList<>();

    void setParamNodes(List<Node> origin){
        paramNodes = new LinkedList<>(origin);
    }


    public LinkedList<Node> getDescendents(Node node) {

        LinkedList<Node> res = new LinkedList<>(); // result
        NodeList childrenNodes = node.getChildNodes();

        for (int i = 0; i < childrenNodes.getLength(); i++) {
            Node curtChild = childrenNodes.item(i);
            res.add(curtChild); // add a child
            res.addAll(getDescendents(curtChild));
        }
        return res;
    }

    public List<Node> getDescendantOrSelf(Node node){
        List<Node> res = new LinkedList<>();
        res.add(node); // add self
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ATTRIBUTE_NODE) continue; // skip attribute nodes
            res.addAll(getDescendantOrSelf(child));
        }
        return res;
    }


    public List<Node> visitDoubleSlash(XPathParser.RpContext ctx) {
        Set<Node> uniqueNodes = new HashSet<>();

        for (Node node : paramNodes) {
            uniqueNodes.add(node); // Add the node itself
            uniqueNodes.addAll(getDescendents(node)); // Add all descendants
        }
        paramNodes.clear();
        paramNodes.addAll(uniqueNodes);

        setParamNodes(paramNodes);
        return visit(ctx);
    }


    @Override
    public List<Node> visitDoc(XPathParser.DocContext ctx) {
        try {
            return XMLProcessor.loadNodes(ctx.fileName().FILENAME().getText());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Node> visitSingleAP(XPathParser.SingleAPContext ctx) {
        List<Node> resDoc = visit(ctx.doc());
        setParamNodes(resDoc);
        return visit(ctx.rp());
    }

    @Override
    public List<Node> visitDoubleAP(XPathParser.DoubleAPContext ctx) {
        List<Node> resDoc = visit(ctx.doc()); // Only the document root node is in the list.
        setParamNodes(resDoc);
        return visitDoubleSlash(ctx.rp());
    }

    @Override
    public List<Node> visitTagRP(XPathParser.TagRPContext ctx) {
        String targetTagName = ctx.tagName().ID().getText();
        return paramNodes.stream()
                .flatMap(node -> IntStream.range(0, node.getChildNodes().getLength())
                        .mapToObj(node.getChildNodes()::item)
                        .filter(child -> child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals(targetTagName)))
                .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    public List<Node> visitChildrenRP(XPathParser.ChildrenRPContext ctx) {
        return paramNodes.stream()
                .flatMap(node -> IntStream.range(0, node.getChildNodes().getLength())
                        .mapToObj(node.getChildNodes()::item))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public List<Node> visitSelfRP(XPathParser.SelfRPContext ctx) {
        return paramNodes;
    }

    @Override
    public List<Node> visitParentRP(XPathParser.ParentRPContext ctx) {
        LinkedList<Node> res = new LinkedList<>();

        for (Node node : paramNodes) {
            Node parentNode = node.getParentNode();
            if (parentNode != null && !res.contains(parentNode)) {
                res.add(parentNode);
            }
        }
        return res;
    }

    @Override
    public List<Node> visitTextRP(XPathParser.TextRPContext ctx) {
        return paramNodes.stream()
                .flatMap(node -> IntStream.range(0, node.getChildNodes().getLength())
                        .mapToObj(node.getChildNodes()::item)
                        .filter(child -> child.getNodeType() == Node.TEXT_NODE))
                .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    public List<Node> visitAttRP(XPathParser.AttRPContext ctx) {
        List<Node> res = paramNodes.stream()
                .filter(node -> node.getNodeType() == Node.ELEMENT_NODE)
                .flatMap(node -> IntStream.range(0, node.getAttributes().getLength())
                        .mapToObj(idx -> node.getAttributes().item(idx)))
                .collect(Collectors.toCollection(LinkedList::new));

        setParamNodes(res);
        return visit(ctx.attName());
    }


    @Override
    public List<Node> visitBracketRP(XPathParser.BracketRPContext ctx) {

        List<Node> currentCtxPNodes = paramNodes;
        setParamNodes(currentCtxPNodes);
        return visit(ctx.rp());
    }

    @Override
    public List<Node> visitSingleSlashRP(XPathParser.SingleSlashRPContext ctx) {

        List<Node> currentCtxPNodes = paramNodes;
        setParamNodes(currentCtxPNodes);
        List<Node> res1 = visit(ctx.rp(0));
        setParamNodes(res1);
        List<Node> res2 = visit(ctx.rp(1));

        // remove duplicates
        LinkedHashSet<Node> lhs = new LinkedHashSet<>(res2);
        return new LinkedList<>(lhs);
    }

    @Override
    public List<Node> visitDoubleSlashRP(XPathParser.DoubleSlashRPContext ctx) {

        List<Node> currentCtxPNodes = paramNodes;
        setParamNodes(currentCtxPNodes);
        List<Node> res1 = visit(ctx.rp(0));
        setParamNodes(res1);
        List<Node> res2 = visitDoubleSlash(ctx.rp(1));

        // remove duplicates
        LinkedHashSet<Node> lhs = new LinkedHashSet<>(res2);
        return new LinkedList<>(lhs);
    }

    @Override
    public List<Node> visitFilterRP(XPathParser.FilterRPContext ctx) {

        List<Node> currentCtxPNodes = paramNodes;
        setParamNodes(currentCtxPNodes);
        List<Node> res = visit(ctx.rp());
        setParamNodes(res);
        return visit(ctx.f());
    }

    @Override
    public List<Node> visitCommaRP(XPathParser.CommaRPContext ctx) {
        List<Node> originalParamNodes = new ArrayList<>(paramNodes);
        List<Node> res1 = visit(ctx.rp(0));
        paramNodes = originalParamNodes; // Reset paramNodes to original state
        List<Node> res2 = visit(ctx.rp(1));
        res1.addAll(res2); // Combine results
        return res1;
    }


    @Override
    public List<Node> visitTagName(XPathParser.TagNameContext ctx) {
        return super.visitTagName(ctx);
    }

    @Override
    public List<Node> visitAttName(XPathParser.AttNameContext ctx) {
        return super.visitAttName(ctx);
    }


    @Override
    public List<Node> visitFileName(XPathParser.FileNameContext ctx) {
        return super.visitFileName(ctx);
    }

    @Override
    public List<Node> visitStringConstant(XPathParser.StringConstantContext ctx) {
        return super.visitStringConstant(ctx);
    }


    @Override
    public List<Node> visitStringFilter(XPathParser.StringFilterContext ctx) {
        String stringConstant = ctx.stringConstant().ID().getText();
        return filterCollectVisitHelper(paramNodes, node -> {
            setParamNodes(Collections.singletonList(node));
            return visit(ctx.rp()).stream()
                    .anyMatch(x -> x.getTextContent().equals(stringConstant));
        });
    }


    @Override
    public List<Node> visitEqFilter(XPathParser.EqFilterContext ctx) {
        return filterCollectVisitHelper(paramNodes, node -> {
            setParamNodes(Collections.singletonList(node));
            List<Node> res1 = visit(ctx.rp(0));
            List<Node> res2 = visit(ctx.rp(1));
            // Check if there is any text match between any nodes in res1 and res2
            return res1.stream().anyMatch(n1 -> res2.stream().anyMatch(n2 ->
                    n1.isEqualNode(n2)));
        });
    }


    @Override
    public List<Node> visitNotFilter(XPathParser.NotFilterContext ctx) {
        List<Node> origin = paramNodes;
        setParamNodes(origin);
        List<Node> filteredF = visit(ctx.f());
        HashSet<Node> s = new HashSet<>(filteredF);
        return filterCollectVisitHelper(origin, node -> !s.contains(node));
    }

    @Override
    public List<Node> visitAndFilter(XPathParser.AndFilterContext ctx) {
        List<Node> origin = paramNodes;
        setParamNodes(origin);
        List<Node> filteredWithF1 = visit(ctx.f(0));
        setParamNodes(filteredWithF1);
        return visit(ctx.f(1));
    }

    @Override
    public List<Node> visitBracketFilter(XPathParser.BracketFilterContext ctx) {
        return visit(ctx.f());
    }

    @Override
    public List<Node> visitIsFilter(XPathParser.IsFilterContext ctx) {
        return filterCollectVisitHelper(paramNodes, node -> {
            setParamNodes(Collections.singletonList(node));
            List<Node> res1 = visit(ctx.rp(0));
            List<Node> res2 = visit(ctx.rp(1));
            return res1.stream().anyMatch(x -> res2.stream().anyMatch(x::isSameNode));
        });
    }


    private List<Node> filterCollectVisitHelper(List<Node> origin, Predicate<Node> rule) {
        return origin.stream()
                .filter(rule)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public List<Node> visitRpFilter(XPathParser.RpFilterContext ctx) {
        return filterCollectVisitHelper(paramNodes, node -> {
            setParamNodes(Collections.singletonList(node));
            return !visit(ctx.rp()).isEmpty();
        });
    }

    @Override
    public List<Node> visitOrFilter(XPathParser.OrFilterContext ctx) {
        List<Node> origin = paramNodes;
        setParamNodes(origin);
        List<Node> rf1 = visit(ctx.f(0));
        setParamNodes(origin);
        List<Node> rf2 = visit(ctx.f(1));
        HashSet<Node> s1 = new HashSet<>(rf1);
        HashSet<Node> s2 = new HashSet<>(rf2);
        return filterCollectVisitHelper(origin,
                node -> s1.contains(node) || s2.contains(node));
    }


}
