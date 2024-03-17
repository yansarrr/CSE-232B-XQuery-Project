package org.cse232b;
import org.cse232b.antlr4.XQueryBaseVisitor;
import org.cse232b.antlr4.XQueryParser;

import java.util.*;

public class ExtendedJoinVisitor extends XQueryBaseVisitor<String> {
    public static final String NO_CHANGE_MARK = "original";

    private void addReturnClause(HashMap<Integer, String> finalForgp, HashMap<Integer, LinkedList<String>> gpVar){
        for (Map.Entry<Integer, LinkedList<String>> set : gpVar.entrySet()) {
            int curgp = set.getKey();
            String strBlock = finalForgp.get(curgp);
            strBlock += "return <tuple>{ ";
            for (String curVar : set.getValue()){
                strBlock += String.format("<%s>{%s}</%s>, ", curVar.substring(1), curVar, curVar.substring(1));
            }
            strBlock = strBlock.substring(0, strBlock.length() - 2);
            strBlock += " }</tuple>,\n";
            finalForgp.put(curgp, strBlock);
        }
    }

    private String getFinalReturnClause(String finalQuery, XQueryParser.FLWRContext ctx){

        boolean isVariable = false;
        String strReturn = ctx.returnClause().getText();

        for (int i = 0; i < strReturn.length(); i++) {

            char curCh = strReturn.charAt(i);
            if (!Character.isDigit(curCh) && !Character.isLetter(curCh) && isVariable) {
                strReturn = strReturn.substring(0, i) + "/*)" + strReturn.substring(i);
                isVariable = false;
            }
            if (curCh == '$') {
                isVariable = true;
            }
        }
        // bracket linear match
        strReturn = strReturn.replace("$", "($tuple/");
        finalQuery += strReturn;

        return finalQuery;
    }

    private void joinUnrelatedGroups(HashMap<HashSet<Integer>, String> finalResult){
        List<HashSet<Integer>> keyList = new ArrayList<>();
        keyList.addAll(finalResult.keySet());
        for (int i = keyList.size() - 1; i > 0; i--) {
            HashSet<Integer> key1 = keyList.remove(i) ;
            HashSet<Integer> key2 = keyList.remove(i - 1);
            String gpString1 = finalResult.remove(key1);
            String gpString2 = finalResult.remove(key2);

            String strFinal = "join (\n" + gpString1 + "\n" + gpString2 + "\n" + "[], []),\n";

            key1.addAll(key2);
            keyList.add(key1);
            finalResult.put(key1, strFinal);
        }
    }

    private void getFinalForGp(HashMap<Integer, String> finalForgp, HashMap<Integer, LinkedList<String>> gpVar,
                               XQueryParser.FLWRContext ctx, HashMap<String, Integer> indexVariable){
        for (Map.Entry<Integer, LinkedList<String>> set : gpVar.entrySet()) {
            int curgp = set.getKey();

            String strBlock = "for " + ctx.forClause().var(curgp).getText() + " in " + ctx.forClause().xq(curgp).getText() + ",\n";
            for (String curVar : set.getValue()) {
                int curIndex =  indexVariable.get(curVar);
                if (curgp != curIndex) {
                    strBlock += curVar + " in " + ctx.forClause().xq(curIndex).getText() + ",\n";
                }
            }

            strBlock = strBlock.substring(0, strBlock.length() - 2);
            strBlock += "\n";
            finalForgp.put(curgp, strBlock);
        }
    }

    private void joinResults(HashMap<HashSet<Integer>, String> finalResult, HashMap<String, LinkedList<String[]>> gpsCond, HashMap<Integer, String> finalForgp) {
        addInitialResults(finalResult, finalForgp);
        processGpsConditions(finalResult, gpsCond);
    }

    private void addInitialResults(HashMap<HashSet<Integer>, String> finalResult, HashMap<Integer, String> finalForgp) {
        finalForgp.forEach((gp, value) -> {
            HashSet<Integer> newList = new HashSet<>();
            newList.add(gp);
            finalResult.put(newList, value);
        });
    }

    private void processGpsConditions(HashMap<HashSet<Integer>, String> finalResult, HashMap<String, LinkedList<String[]>> gpsCond) {
        gpsCond.forEach((key, value) -> {
            HashSet<Integer> newList = new HashSet<>();
            String[] gps = key.split(",");
            int gp1 = Integer.valueOf(gps[0]);
            int gp2 = Integer.valueOf(gps[1]);

            String gpString1 = removeFromFinalResults(finalResult, gp1, newList);
            String gpString2 = removeFromFinalResults(finalResult, gp2, newList);

            String conditionsStr = buildConditionsString(value);
            String strFinal = buildFinalString(gpString1, gpString2, conditionsStr);

            finalResult.put(newList, strFinal);
        });
    }

    private String removeFromFinalResults(HashMap<HashSet<Integer>, String> finalResult, int gp, HashSet<Integer> newList) {
        for (HashSet<Integer> lKey : new HashSet<>(finalResult.keySet())) { // Avoid ConcurrentModificationException
            if (lKey.contains(gp)) {
                String result = finalResult.remove(lKey);
                newList.addAll(lKey);
                return result;
            }
        }
        return "";
    }

    private String buildConditionsString(LinkedList<String[]> conditions) {
        String l1 = "[", l2 = "[";
        for (String[] eqCond : conditions) {
            l1 += eqCond[0].substring(1) + ",";
            l2 += eqCond[1].substring(1) + ",";
        }
        l1 = l1.substring(0, l1.length() - 1) + "], ";
        l2 = l2.substring(0, l2.length() - 1) + "]";
        return l1 + l2;
    }

    private String buildFinalString(String gpString1, String gpString2, String conditionsStr) {
        return "join (\n\n" + gpString1 + "\n" + gpString2 + "\n" + conditionsStr + "),\n";
    }


    @Override
    public String visitFLWR(XQueryParser.FLWRContext ctx) {

        HashMap<String, Integer> gpVariable = new HashMap<>();
        HashMap<String, Integer> indexVariable = new HashMap<>();
        HashMap<Integer, LinkedList<String>> gpVar = new HashMap<>();
        HashMap<Integer, LinkedList<String[]>> gpConst = new HashMap<>();
        HashMap<String, LinkedList<String[]>> gpsCond = new HashMap<>();


        for (int i = 0; i < ctx.forClause().var().size(); i++) {
            String var = ctx.forClause().var(i).getText();
            String path = ctx.forClause().xq(i).getText();

            String parentVar = path.substring(0, 1).equals("$") ? path.split("/")[0] : null;
            int gpVariableValue = parentVar != null ? gpVariable.get(parentVar) : i;

            gpVariable.put(var, gpVariableValue);
            indexVariable.put(var, i);

            String key = ctx.forClause().var(i).getText();
            Integer val = gpVariable.get(key);

            if (!gpVar.containsKey(val)) {
                LinkedList<String> newList = new LinkedList<>();
                gpVar.put(val, newList);
            }
            gpVar.get(val).add(key);
        }

        if (gpVar.size() == 1) {
            return NO_CHANGE_MARK;
        }


        String[] equalCond;

        for (int i = 0; i < ctx.whereClause().getText().substring(5).split("and").length; i++) {

            String curtCondition = ctx.whereClause().getText().substring(5).split("and")[i];

            if (curtCondition.contains("=")) {
                equalCond = curtCondition.split("=");
            } else if (curtCondition.contains("eq$")) {
                equalCond = curtCondition.split("eq\\$");
                equalCond[1] = "$" + equalCond[1];
            } else {
                equalCond = curtCondition.split("eq\"");
                equalCond[1] = "\"" + equalCond[1];
            }

            if (!curtCondition.contains("\"")) {

                int gp1 = gpVariable.get(equalCond[0]);
                int gp2 = gpVariable.get(equalCond[1]);

                if (gp1 != gp2) {
                    if (gp1 > gp2) {
                        int temp = gp1;
                        gp1 = gp2;
                        gp2 = temp;

                        String tempString = equalCond[0];
                        equalCond[0] = equalCond[1];
                        equalCond[1] = tempString;
                    }

                    String key = gp1 + "," + gp2 ;
                    if (!gpsCond.containsKey(key)){
                        LinkedList<String[]> val = new LinkedList<>();
                        gpsCond.put(key, val);
                    }
                    gpsCond.get(key).add(equalCond);
                }
                else {
                    if (!gpConst.containsKey(gp1)) {
                        LinkedList<String[]> newList = new LinkedList<>();
                        gpConst.put(gp1, newList);
                    }
                    gpConst.get(gp1).add(equalCond);
                }
            }

            else {
                boolean isFirConst = equalCond[0].substring(0, 1).equals("\"");
                boolean isSecConst = equalCond[1].substring(0, 1).equals("\"");

                if (!(isFirConst && isSecConst)) {
                    int gp;

                    if (isFirConst) {
                        gp = gpVariable.get(equalCond[1]);
                    }
                    else {
                        gp = gpVariable.get(equalCond[0]);
                    }

                    if (!gpConst.containsKey(gp)){
                        LinkedList<String[]> newList = new LinkedList<>();
                        gpConst.put(gp, newList);
                    }
                    gpConst.get(gp).add(equalCond);
                }
                else {
                    for (Integer key : gpVar.keySet()) {
                        if (!gpConst.containsKey(key)){
                            LinkedList<String[]> newList = new LinkedList<>();
                            gpConst.put(key, newList);
                        }
                        gpConst.get(key).add(equalCond);
                    }
                }
            }
        }

        // get final for group
        HashMap<Integer, String> finalForgp = new HashMap<>();
        getFinalForGp(finalForgp, gpVar, ctx, indexVariable);


        for (Map.Entry<Integer, LinkedList<String[]>> set : gpConst.entrySet()) {

            int curgp = set.getKey();
            String strBlock = finalForgp.get(curgp);
            strBlock += "where ";

            for (String[] eqCond : set.getValue()) {
                strBlock += eqCond[0] + " eq " + eqCond[1] + " and ";
            }
            strBlock = strBlock.substring(0, strBlock.length() - 5);
            strBlock += "\n";

            finalForgp.put(curgp, strBlock);
        }


        // add return clause
        addReturnClause(finalForgp, gpVar);

        // join the results
        HashMap<HashSet<Integer>, String> finalResult = new HashMap<>();
        joinResults(finalResult, gpsCond, finalForgp);

        // join all unrelated gps
        joinUnrelatedGroups(finalResult);

        // get the final query
        String finalQuery = "for $tuple in ";
        String strFinal = finalResult.values().toString();
        strFinal = strFinal.substring(1, strFinal.length() - 3);
        finalQuery += strFinal + "\n";

        // get final return clause
        return getFinalReturnClause(finalQuery, ctx);
    }
}
