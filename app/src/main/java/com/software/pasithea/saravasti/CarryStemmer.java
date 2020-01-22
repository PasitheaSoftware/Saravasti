package com.software.pasithea.saravasti;

import android.util.ArrayMap;
import android.util.Log;
import android.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * French stemmer class based on the Carry Stemmer Algorithm
 */

class CarryStemmer {
    private static final String TAG = "CarryStemmer";

    private static char[] vowel = {'a', 'e', 'i', 'o', 'u', 'y'};
    private static String initialWord;

    public CarryStemmer() {
    }

    /**
     * The stemmer runs in 2 phases.
     * <p>
     * First phase:
     * Check if the word is known as it is to speed up the parsing<br>
     *
     * Second phase:
     * Execute the 3 steps stemmer
     *
     * @param s the initial string
     * @return a list of pair which contains the index of the word in the dictionary and a float as similarity score
     */
    protected static List<Pair<Integer, Float>> executeCarryStemmer(String s) {
        String result = "";
        initialWord = s;
        ArrayMap<Integer, Float> resultMap = new ArrayMap<>();
        List<Pair<Integer, Float>> outputScore = new ArrayList<Pair<Integer, Float>>();
        Log.i(TAG, "Stemming: START");
        Log.i(TAG, "Initial word: " + s);

        resultMap = SyntacticParser.parseDict(s, resultMap);
        if (resultMap.size() == 1){
            // resultMap.size() = 1 means there is only one stem detected (i.e. 100%).
            // We than return this value w/o going further in the stemmer.
            if (!outputScore.isEmpty()){
                outputScore.clear();
            }
            outputScore = MathLib.getSoftmax(resultMap);
            return outputScore;
        }


        // Carry Step 1
        result = executeCarry(s, 1);
        Log.d(TAG, "executeCarryStemmer (Step 1): " + result);
        resultMap = SyntacticParser.parseDict(result, resultMap, s);

        // Carry Step 2
        result = executeCarry(result, 2);
        Log.d(TAG, "executeCarryStemmer (Step 2): " + result);
        resultMap = SyntacticParser.parseDict(result, resultMap, s);

        // Carry Step 3
        result = executeCarry(result, 3);
        List<Pair<Integer, Float>> tempOutput = new ArrayList<>();
        Log.d(TAG, "executeCarryStemmer (Step 3): " + result);
        resultMap = SyntacticParser.parseDict(result, resultMap, s);

        // Create the Softmax Similiraty scores
        if (!outputScore.isEmpty()){
            outputScore.clear();
        }
        if (resultMap.size() == 0 || resultMap.isEmpty()){
            Pair<Integer, Float> endPair = Pair.create(-1, (float)-1);
            outputScore.add(endPair);
        } else {
            outputScore = MathLib.getSoftmax(resultMap);
            Pair<Integer, Float> tempVar;
            Boolean swapped = true;
            while (swapped){
                swapped = false;
                for (int i = 0; i < outputScore.size() ; i++) {
                    if (i+1 != outputScore.size() && outputScore.get(i).second < outputScore.get(i+1).second){
                        tempVar = outputScore.get(i);
                        outputScore.set(i, outputScore.get(i+1));
                        outputScore.set(i+1, tempVar);
                        swapped = true;
                    }
                }
            }
        }
        if (outputScore.size() == 1) {
            tempOutput = outputScore;
        } else if (outputScore.size() >= Constants.MAX_RETURN_SCORES){
            tempOutput = outputScore.subList(0, Constants.MAX_RETURN_SCORES);
        } else if (outputScore.size() < Constants.MAX_RETURN_SCORES){
            tempOutput = outputScore.subList(0, outputScore.size()-1);
        }
        resultMap.clear();
        return tempOutput;
    }

    /**
     * Read the resources to use for the specific step
     * @param step the step the algorithm is running
     * @return a list of the resources to use for this step
     */
    private static ArrayList<String> getInfoList(int step) {
        ArrayList<String> suffix = new ArrayList<String>();
        InputStream mInputStream = null;
        BufferedReader mBufferedReader = null;
        switch (step) {
            case 1:
                mInputStream = Constants.RESOURCES.openRawResource(R.raw.carry_step1);
                mBufferedReader = new BufferedReader(new InputStreamReader(mInputStream));
                break;
            case 2:
                mInputStream = Constants.RESOURCES.openRawResource(R.raw.carry_step2);
                mBufferedReader = new BufferedReader(new InputStreamReader(mInputStream));
                break;
            case 3:
                mInputStream = Constants.RESOURCES.openRawResource(R.raw.carry_step3);
                mBufferedReader = new BufferedReader(new InputStreamReader(mInputStream));
                break;
        }

        try {
            String eachline = mBufferedReader.readLine();
            while (eachline != null) {
                suffix.add(eachline);
                eachline = mBufferedReader.readLine();
            }
            mInputStream.close();
            mBufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return suffix;
    }

    /**
     * Execute the algorithm at the step provided against the string provided
     * @param s the string to evaluate
     * @param step the step in which the algorithm is
     * @return a string with the suffix removed or replaced
     */
    private static String executeCarry(String s, int step) {
        int isDetected = 0;
        ArrayList<String> operationList = getInfoList(step);
        for (String info : operationList) {
            String substring = "";
            int valueof = new Integer(info.split(" ")[0]);
            String suffix = info.split(" ")[1];
            String operation = info.split(" ")[2];
            if (s.endsWith(suffix) && isDetected == 0 && s.length() != 0 && !s.equals(suffix)) {
                if (operation.equals("DEL")) {
                    substring = s.split(suffix)[0];
                } else {
                    substring = s.replace(suffix, operation);
                }
                int count = MathLib.countSequenceVc(substring);
                if (count > valueof){
                    s = substring;
                }
                isDetected += 1;
            }
        }
        return s;
    }
}