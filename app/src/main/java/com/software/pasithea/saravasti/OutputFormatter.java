package com.software.pasithea.saravasti;

import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class OutputFormatter {
    private static final String TAG = "OutputFormatter";

    public OutputFormatter() {
    }

    public static List<String> formatOutput(List<String[]> outputMap) {
        List<String> subList = new ArrayList<>();
        List<String> returnList = new ArrayList<>();
        if (outputMap.size() == 1) {
            for (String st : outputMap.get(0)) {
                st = st.trim();
                if (st.length() != 0) {
                    subList.add(st);
                }
            }
            return returnList;
        } else if (subList.isEmpty() && outputMap.size() > 1) {
            subList = initList(outputMap.get(0), outputMap.get(1));
            outputMap.remove(0);
            outputMap.remove(0);
            for (String[] stringList : outputMap) {
                subList = createList(subList, stringList);
            }
        }
        returnList.addAll(subList);
        subList.clear();
        return returnList;
    }

    private static List<String> initList(String[] s1, String[] s2) {
        List<String> mList = new ArrayList<>();
        for (String sOf1 : s1) {
            sOf1 = sOf1.trim();
            if (sOf1.length() != 0) {
                for (String sOf2 : s2) {
                    sOf2 = sOf2.trim();
                    if (sOf2.length() != 0) {
                        String out = sOf1 + " " + sOf2;
                        mList.add(out);
                    }
                }
            }
        }
        return mList;
    }

    private static List<String> createList(List<String> sl, String[] s1) {
        List<String> mList = new ArrayList<>();
        for (String mString : sl) {
            for (String mString2 : s1) {
                mString2 = mString2.trim();
                if (mString2.length() != 0) {
                    String out = mString + " " + mString2;
                    mList.add(out);
                }
            }
        }
        return mList;
    }

    public static String formatOutputString(String[] outputString) {
        String o = "";
        String pkgname = "";
        try {
            for (String s : outputString) {
                String[] splitted = s.split(" ");
                for (String sw : splitted) {
                    if (Pattern.matches("[0-9]+", sw)) {
                        o = o + " " + Constants.DICTIONNARY.get(Integer.parseInt(sw)).split(";")[1]
                                + "[" + Constants.DICTIONNARY.get(Integer.parseInt(sw)).split(";")[2]
                                + "]";
                    } else {
                        for (Pair<String, String> app : Constants.APPLICATION_LIST) {
                            if (app.first.contains(sw)) {
                                pkgname = app.second;
                            }
                        }
                        if (pkgname.isEmpty()) {
                            o = o + " " + sw + " [UNK]";
                            Log.d(TAG, "No application found");
                        } else {
                            o = o + " " + pkgname + " [APP]";
                        }
                    }
                }
                o = o + "\n\n";
            }
        } catch (Exception e) {
            Log.e(TAG, "Error during the output string formatting", e);
        }
        return o;
    }
}

