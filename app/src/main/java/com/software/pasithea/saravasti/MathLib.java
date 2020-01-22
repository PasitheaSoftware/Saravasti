package com.software.pasithea.saravasti;

import android.util.ArrayMap;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.exp;

/**
 * Mathematical class for the statistical methods
 */
class MathLib {
    private static final String TAG = "MathLib";

    private static List<Pair<Integer, Float>> outputArray = new ArrayList<Pair<Integer, Float>>();
    private static char[] vowel = {'a'
            ,'e'
            ,'i'
            ,'o'
            ,'u'
            ,'y'
            ,'é'
            ,'è'
            ,'ê'
            ,'ù'
            ,'ë'
            ,'ü'
            ,'û'
            ,'î'
            ,'ï'
            ,'à'
            ,'â'};

    public MathLib(){ }

    /**
     * Compute the SoftMax value for each element of the array.
     * The softmax value is given by the formula:<br>
     *
     * Xj = e(Xj) / Sum(e(X1), e(X2), ...., e(Xk))
     *
     * <br>This is done in 2 steps:
     *
     * <br>(1) Compute the sum of all the similarity scores reported by the parsing.
     * <br>(2) Compute the SofMax value for each element of the array, using the sum computed in (1)
     *
     * @param inputArray
     * @return
     */
    protected static List<Pair<Integer, Float>> getSoftmax(ArrayMap<Integer, Float> inputArray) {
        if (!outputArray.isEmpty() || inputArray.size() != 0){
            outputArray.clear();
        }

        /**TODO
         *  Sort the input in descending order
         *  filter the input by the 3 largest similiraty scores
         */

        // SUM
        double sumOfSimilarities = 0;
        for (int i = 0; i < inputArray.size() ; i++) {
            int k = inputArray.keyAt(i);
            sumOfSimilarities += exp(inputArray.get(k));
        }

        // SOFTMAX
        for (int i = 0; i < inputArray.size() ; i++) {
            int k = inputArray.keyAt(i);
            double valueOfX = inputArray.get(k);
            float resultN = (float)exp(valueOfX) / (float)sumOfSimilarities;
            Pair<Integer, Float> mPair = Pair.create(k, resultN);
            outputArray.add(mPair);
        }
        return outputArray;
    }

    /**
     * Compute the similarity score for a detected string against the initial word.
     * This is done by:
     *
     * (1) Normalizaton step:
     * Compute the size difference between the 2 strings and complete the missing parts of the smallest one by
     * 0 (zero) which will not be used during the computation process
     *
     * (2) Computation step:
     * Each chars of the 2 strings at the same index are compared. If this is the same letter,
     * an int is incremented. At the end of the operation this int reflects the number of similar chars.
     * A percent spécific to this string is than computed and returned. This percent give the score
     * of similiraty between the 2 string, the higher it is, the more chars are common to the strings.
     * If this score equals 1.0 (i.e. 100%) this means the strings are identical.
     *
     * @param s (Initial word)
     * @param w (String evaluated)
     * @return float (similarity score)
     */
    protected static float computeSimilarity(String s, String w) {
        char[] initialWord = null;
        char[] dictionnaryWord = null;
        int startIndex = -1;
        float commonChar = 0;

        // Normalisation step
        if (s.length() < w.length()) {
            startIndex = w.length() - s.length();
            for (int i = 1; i <= startIndex; i++) {
                s = s + "0";
            }
            dictionnaryWord = w.toCharArray();
            initialWord = s.toCharArray();
        } else if (s.length() > w.length()) {
            startIndex = s.length() - w.length();
            for (int i = 1; i <= startIndex; i++) {
                w = w + "0";
            }
            dictionnaryWord = w.toCharArray();
            initialWord = s.toCharArray();
        } else if (s.length() == w.length()) {
            dictionnaryWord = w.toCharArray();
            initialWord = s.toCharArray();
        }

        // Create the common char integer
        for (int i = 0; i < initialWord.length; i++) {
            if (initialWord[i] == dictionnaryWord[i]) {
                commonChar += 1;
            }
        }
        return commonChar / (float) initialWord.length; // The score is created here
    }

    /**
     * Count the number of Vowel/Consonnant (i.e. VC) sequences in a string. This step is used by
     * the stemmer in order to confirm either it must do the removal/replacement action or not.
     * @param s
     * @return
     */
    protected static int countSequenceVc(String s) {
        int m = 0;
        for (int i = 0; i < s.length(); i++) {
            for (char v : vowel) {
                if (s.charAt(i) == v
                        && i + 1 < s.length()
                        && s.charAt(i + 1) != 'a'
                        && s.charAt(i + 1) != 'e'
                        && s.charAt(i + 1) != 'i'
                        && s.charAt(i + 1) != 'o'
                        && s.charAt(i + 1) != 'u'
                        && s.charAt(i + 1) != 'y'
                        && s.charAt(i + 1) != 'é'
                        && s.charAt(i + 1) != 'è'
                        && s.charAt(i + 1) != 'ê'
                        && s.charAt(i + 1) != 'ù'
                        && s.charAt(i + 1) != 'ë'
                        && s.charAt(i + 1) != 'ü'
                        && s.charAt(i + 1) != 'û'
                        && s.charAt(i + 1) != 'î'
                        && s.charAt(i + 1) != 'ï'
                        && s.charAt(i + 1) != 'à'
                        && s.charAt(i + 1) != 'â') {
                    m += 1;
                }
            }
        }
        return m;
    }
}
