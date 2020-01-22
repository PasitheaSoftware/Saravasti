package com.software.pasithea.saravasti;

import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Semantic analysis class
 */
public class SemanticParser {
    private static final String TAG = "SemanticParser";

    public SemanticParser(){ }

    /**
     * Create the rules from the rules specified in the res.raw.rules_list.txt file
     * @param rawRule
     * @return a Rules instance
     */
    public static Rules createRule(String rawRule){
        String sem = "NaN";
        Rules mRules = null;
        String[] splittedRule = rawRule.split(";");
        String leftRule = splittedRule[0];
        if (splittedRule.length == 3){
            sem = splittedRule[2];
        }
        String[] splittedRightRule = splittedRule[1].split(" ");
        if (splittedRightRule.length == 1 && !splittedRightRule[0].startsWith("$")){
            String rightRule = splittedRightRule[0];
            mRules = new Rules(leftRule, rightRule, sem);
        } else if (splittedRightRule.length == 2
                && splittedRightRule[0].startsWith("$")
                && splittedRightRule[1].startsWith("$")){
            Pair<String, String> rulePair = Pair.create(splittedRightRule[0], splittedRightRule[1]);
            mRules = new Rules(leftRule, rulePair, sem);
        }
        return mRules;
    }

    /**
     * Create the dynamic rules for the applications installed on the device.
     * Set this rules list in the Constants.APPLICATION_LIST variable.
     */
    public static void createAppRules(){
        List<Rules> appRulesList = new ArrayList<>();
        for (Pair<String, String> app : Constants.APPLICATION_LIST){
            String[] tmp = app.first.split(" ");
            if (tmp.length == 1){
                Rules r = new Rules("$OBJ", app.first, app.second);
                appRulesList.add(r);
            }

        }
        Constants.setRulesList(appRulesList);
    }

    /**
     * We apply the CYK algorithm to transform our string array to an array of rules.
     * This is done in 2 steps:
     *
     * 1) apply the lexical rules
     *    The lexical rules are the rules that contain a terminal element as right-hand element.
     *
     * 2) apply the binary rules
     *    The binary rules are a combination of non terminal elements. This rule must be in a CNF
     *    (Chomsky Normal Form) where the right-hand element must contain only 2 elements.
     *
     * @param rawData
     */
    public static List<String> chartParsing(String[] rawData){
        List<String> resultList = new ArrayList<>();
        for (String data : rawData){
            String[] splitData = data.split(" ");
            int initialLength = splitData.length;
            for (int i = 0; i < splitData.length ; i++) {
                if (Pattern.matches("[0-9]+", splitData[i])) {
                    splitData[i] = Constants.DICTIONNARY
                            .get(Integer.parseInt(splitData[i]))
                            .split(";")[1];
                }
            }
            List<String> binaryData = new ArrayList<>();
            for (int i = 0; i < initialLength ; i++) {
                binaryData.add(applyLexicalRules(splitData[i]));
            }
            int length = binaryData.size();
            Log.d(TAG, "chartParsing: Start " + length);
            while (length > 1){
                binaryData = applyBinaryRules(binaryData, binaryData.size());
                Log.d(TAG, "chartParsing: Start " + length);
                if (binaryData.get(0).split(";")[0].equals("$S")){
                    length = 0;
                } else {
                    length = binaryData.size();
                }
            }
            String outString = "";
            if (binaryData.get(0).isEmpty() || !binaryData.get(0).split(";")[0].equals("$S")){
                outString = "None";
            } else {
                outString = binaryData.get(0);
            }
            Log.d(TAG, "Rule detected: " + outString);
            resultList.add(outString);
        }
        return resultList;
    }

    /**
     * Apply all the lexical rules on the input string
     * The rules class defines 3 variables:
     * - The Left hand Side (LHS), starting with $ that provide the category of the string
     * - The right Hand Side (RHS) which is the string itself.
     * - The STEM which is the action to perform if the LHS is a $VERB or the object on which the
     *   action must be done if the LHS is $OBJ
     *   Note that all RHS string not in the rules list are considered as $E (Empty) and the STEM is
     *   NaN
     *
     * @param inputString the string for the RHS
     * @return a string of $[RHS];STEM
     */
    private static String applyLexicalRules(String inputString){
        String ruleLhs = "";
        for (Rules rule : Constants.RULES_LIST){
            if (rule.isLexical() && rule.getRHS().equals(inputString)){
                //ruleLhs = rule.getLHS();
                ruleLhs = String.format("%s;%s",rule.getLHS(), rule.getSEM());
            }
        }
        if (ruleLhs.isEmpty()){
            ruleLhs = "$E;NaN";
        }
        return ruleLhs;
    }

    /**
     * The second part of the CYK algorithm. At this stage, the algorithm will create a pair by
     * combining the string (N) and the string (N+1). It will then execute the parsing of this pair.
     *
     * @param lexicalString the rule to parse
     * @param length the length of the complete string to parse
     * @return a list of string representing the new rules
     */
    private static List<String> applyBinaryRules(List<String> lexicalString, int length){
        List<String> outputArray = new ArrayList<>();
        for (int i = 0; i < length ; i++) {
            if (i+1 < length){
                if (lexicalString.get(i).isEmpty() || lexicalString.get(i + 1).isEmpty() && i+1 < length) {
                    outputArray.add("$E;NaN");
                } else if (i+1 < length){
                    String left = lexicalString.get(i);
                    String right = lexicalString.get(i+1);
                    outputArray.add(parseBinaryRule(left, right));
                }
            }
        }
        return outputArray;
    }

    /**
     * Apply the rules as defined in the rules text files provided. Because the rules must follow
     * the CNF, the parsing can be done on two rules only. Those rules can be either lexical rules,
     * empty rules or binary rules.
     *
     * @param left the left string to parse
     * @param right the right string to parse
     * @return the parsing result
     */
    private static String parseBinaryRule(String left, String right){
        Log.d(TAG, "parseBinaryRule: " + left);
        Log.d(TAG, "parseBinaryRule: " + right);
        String merged = "";
        String sem = "";
        for (Rules r : Constants.RULES_LIST){
            if (r.isBinary()){
                String[] splitrule = r.getRHS().split(" ");
                String[] splitLeft = left.split(";");
                String[] splitRight = right.split(";");
                if (splitrule[0].equals(splitLeft[0]) && splitrule[1].equals(splitRight[0])){

                    if (!splitLeft[1].equals("NaN")){
                        sem = sem + " " + splitLeft[1];
                    }
                    if (!splitRight[1].equals("NaN")){
                        sem = sem + " " + splitRight[1];
                    }
                    if (splitLeft[0].equals("$S")){
                        sem = splitLeft[1];
                    } else if (splitRight[0].equals("$S")){
                        sem = splitRight[1];
                    }
                    merged = String.format("%s;%s", r.getLHS(), sem.trim());
                    Log.i(TAG, String.format("parseBinaryRule: rule %s - result %s", r.getRHS(), merged));
                }
            }
        }
        if (merged.isEmpty()){
            merged = "$E;NaN";
        }
        Log.d(TAG, "merged = " + merged);
        return merged;
    }
}
