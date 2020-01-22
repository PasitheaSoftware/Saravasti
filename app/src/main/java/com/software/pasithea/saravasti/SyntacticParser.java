package com.software.pasithea.saravasti;

import android.util.ArrayMap;
import android.util.Log;

/**
 * Syntax parser class used to do the stemming step.
 */
public class SyntacticParser {
    private static final String TAG = "SyntacticParser";

    public SyntacticParser(){ }

    /**
     * Syntax parser to detect the semme of a word.
     * This parser is used at each step of the carry stemmer (CarryStemmer).
     * It relies on a dictionnary of ~ 49K words.
     * The method returns a ArrayMap with the index of the word in the dictionary as key and the
     * similarity score as value.
     *
     * @param s
     * @param Lemmes
     * @param initialWord
     * @return
     */
    public static ArrayMap<Integer, Float> parseDict(String s, ArrayMap<Integer, Float> Lemmes, String initialWord){
        Log.d(TAG, "parseDict: " + s);
        for (String fullinfo : Constants.DICTIONNARY) {
            String word = "";
            if (!fullinfo.isEmpty()) {
                word = fullinfo.split(";")[0];
                if (word.equals(s)){
                    int indexValue = Constants.DICTIONNARY.indexOf(fullinfo);
                    float similarityScore = MathLib.computeSimilarity(initialWord, word);
                    Lemmes.clear();
                    Lemmes.put(indexValue, similarityScore);
                    return Lemmes;
                } else if (word.startsWith(s)) {
                    int indexValue = Constants.DICTIONNARY.indexOf(fullinfo);
                    float similarityScore = MathLib.computeSimilarity(initialWord, word);
                    if (Lemmes.size() == 0 || !Lemmes.containsKey(indexValue) && similarityScore*100 > Constants.DICT_THRESHOLD){
                        Lemmes.put(indexValue, similarityScore);
                    } else if (Lemmes.containsKey(indexValue)) {
                        if (Lemmes.get(indexValue) < similarityScore) {
                            Lemmes.put(indexValue, similarityScore);
                        } else {
                            Lemmes.put(indexValue, similarityScore);
                        }
                    }
                }
            }
        }
        return Lemmes;
    }

    /**
     * Syntax parser to detect the semme of a word. The method is overloaded and this version is
     * used only before the first to detect if the word is known in the dictionary in order to
     * speed-up the process.
     * This parser is used at each step of the carry stemmer (CarryStemmer).
     * It relies on a dictionnary of ~ 49K words.
     * The method returns a ArrayMap with the index of the word in the dictionary as key and the
     * similarity score as value.
     *
     * @param s
     * @param Lemmes
     * @return
     */
    public static ArrayMap<Integer, Float> parseDict(String s, ArrayMap<Integer, Float> Lemmes){
        String initialWord = s;
        Log.d(TAG, "parseDict: " + s);
        for (String fullinfo : Constants.DICTIONNARY) {
            String word = "";
            if (!fullinfo.isEmpty()) {
                word = fullinfo.split(";")[0];
                if (word.equals(s)){
                    int indexValue = Constants.DICTIONNARY.indexOf(fullinfo);
                    float similarityScore = MathLib.computeSimilarity(initialWord, word);
                    Lemmes.clear();
                    Lemmes.put(indexValue, similarityScore);
                    return Lemmes;
                } else if (word.startsWith(s)) {
                    int indexValue = Constants.DICTIONNARY.indexOf(fullinfo);
                    float similarityScore = MathLib.computeSimilarity(initialWord, word);
                    if (Lemmes.size() == 0 || !Lemmes.containsKey(indexValue) && similarityScore*100 > Constants.DICT_THRESHOLD){
                        Lemmes.put(indexValue, similarityScore);
                    } else if (Lemmes.containsKey(indexValue)) {
                        if (Lemmes.get(indexValue) < similarityScore) {
                            Lemmes.put(indexValue, similarityScore);
                        } else {
                            Lemmes.put(indexValue, similarityScore);
                        }
                    }
                }
            }
        }
        return Lemmes;
    }
}
