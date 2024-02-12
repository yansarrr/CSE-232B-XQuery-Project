package org.cse232b;

import org.cse232b.antlr4.XPathParser;
import org.w3c.dom.Node;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class XPathFilterProcessor {


    public static List<Node> visitStringFilter(List<Node> paramNodes, XPathParser.StringFilterContext ctx, Function<XPathParser.RpContext, List<Node>> visit) {
        String stringConstant = ctx.stringConstant().ID().getText();
        return paramNodes.stream()
                .flatMap(node -> visit.apply(ctx.rp()).stream())
                .filter(node -> node.getTextContent().equals(stringConstant))
                .collect(Collectors.toList());
    }


    public static List<Node> visitEqFilter(List<Node> paramNodes, XPathParser.EqFilterContext ctx, Function<XPathParser.RpContext, List<Node>> visit) {
        return filterCollectVisitHelper(paramNodes,
                node -> {
                    List<Node> oneNodeList = new LinkedList<>();
                    oneNodeList.add(node);
                    List<Node> res1 = visit.apply(ctx.rp(0));
                    List<Node> res2 = visit.apply(ctx.rp(1));
                    for (Node x : res1) {
                        for (Node y : res2) {
                            if (x.isEqualNode(y)) {
                                return true;
                            }
                        }
                    }
                    return false;
                }
        );
    }

    public static List<Node> visitNotFilter(List<Node> paramNodes, XPathParser.NotFilterContext ctx, Function<XPathParser.FContext, List<Node>> visit) {
        List<Node> filteredF = visit.apply(ctx.f());
        HashSet<Node> s = new HashSet<>(filteredF);
        return filterCollectVisitHelper(paramNodes, node -> !s.contains(node));
    }

    public static List<Node> visitAndFilter(List<Node> paramNodes, XPathParser.AndFilterContext ctx, Function<XPathParser.FContext, List<Node>> visit) {
        List<Node> filteredWithF1 = visit.apply(ctx.f(0));
        return visit.apply(ctx.f(1));
    }

    public static List<Node> visitBracketFilter(List<Node> paramNodes, XPathParser.BracketFilterContext ctx, Function<XPathParser.FContext, List<Node>> visit) {
        return visit.apply(ctx.f());
    }

    public static List<Node> visitIsFilter(List<Node> paramNodes, XPathParser.IsFilterContext ctx, Function<XPathParser.RpContext, List<Node>> visit) {
        return filterCollectVisitHelper(paramNodes,
                node -> {
                    List<Node> oneNodeList = new LinkedList<>();
                    oneNodeList.add(node);
                    List<Node> res1 = visit.apply(ctx.rp(0));
                    List<Node> res2 = visit.apply(ctx.rp(1));
                    for (Node x : res1) {
                        for (Node y : res2) {
                            if (x.isSameNode(y)) {
                                return true;
                            }
                        }
                    }
                    return false;
                }
        );
    }

    public static List<Node> visitRpFilter(List<Node> paramNodes, XPathParser.RpFilterContext ctx, Function<XPathParser.RpContext, List<Node>> visit) {
        return filterCollectVisitHelper(paramNodes,
                node -> {
                    List<Node> oneNodeList = new LinkedList<>();
                    oneNodeList.add(node);
                    List<Node> res = visit.apply(ctx.rp());
                    return res.size() > 0;
                }
        );
    }

    public static List<Node> visitOrFilter(List<Node> paramNodes, XPathParser.OrFilterContext ctx, Function<XPathParser.FContext, List<Node>> visit) {
        List<Node> rf1 = visit.apply(ctx.f(0));
        List<Node> rf2 = visit.apply(ctx.f(1));
        HashSet<Node> s1 = new HashSet<>(rf1);
        HashSet<Node> s2 = new HashSet<>(rf2);
        return filterCollectVisitHelper(paramNodes,
                node -> s1.contains(node) || s2.contains(node));
    }

    private static List<Node> filterCollectVisitHelper(List<Node> origin, Predicate<Node> rule) {
        return origin.stream()
                .filter(rule)
                .collect(Collectors.toCollection(LinkedList::new));
    }

}
