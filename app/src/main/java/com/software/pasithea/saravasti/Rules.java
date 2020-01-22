package com.software.pasithea.saravasti;

import android.util.Pair;

/**
 * class used as a rule holder
 */

public class Rules {
    private static final String TAG = "Rules";

    private String RHS;
    private String LHS;
    private String SEM;

    public Rules(String LHS, String RHS, String SEM) {
        this.RHS = RHS;
        this.LHS = LHS;
        this.SEM = SEM;
    }

    public Rules(String LHS, Pair<String, String> RHS, String SEM){
        this.LHS = LHS;
        this.RHS = RHS.first + " " + RHS.second;
        this.SEM = SEM;
    }

    public String getRHS() {
        return RHS;
    }

    public String getLHS() {
        return LHS;
    }

    public String getSEM() { return SEM; }

    public String getRule(){
        return String.format("(%s ( %s )) [%s]", LHS, RHS, SEM);
    }

    public Boolean isLexical(){
        String[] Rhs = RHS.split(" ");
        return Rhs.length == 1 && !RHS.startsWith("$");
    }

    public Boolean isBinary(){
        String[] Rhs = RHS.split(" ");
        return  Rhs.length == 2 && Rhs[0].startsWith("$") && Rhs[1].startsWith("$");
    }
}