/* ====================================================================
 *
 *  Copyright (C) 2019 François Laforgia - All Rights Reserved
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL FRANCOIS LAFORGIA BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * ====================================================================
 */

package com.software.pasithea.saravasti;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * This class is used to perform the string processing in French language.
 */

public class WordPreProcessor {
    private static final String TAG = "FrenchAnalyzer";

    public WordPreProcessor(){
    }

    public static ArrayList<String> startPreProcessing(String s){
        ArrayList<String> tokenizedString = new ArrayList<String>();
        ArrayList<String> specialCharRemoved = new ArrayList<String>();
        ArrayList<String> removedStopwords = new ArrayList<String>();

        Log.i(TAG, "String detected: ");

        String s2 = s.toLowerCase();
        Log.i(TAG, "Put everything in lower case: " + s2);

        tokenizedString = tokenizeString(s2);
        Log.i(TAG, "Tokenized string: " + tokenizedString);

        specialCharRemoved = removeSpecialChars(tokenizedString);
        Log.i(TAG, "Removed special chars; " + specialCharRemoved);

        removedStopwords = removeStopwords(specialCharRemoved);
        Log.i(TAG, "Removed stopwords; " + removedStopwords);

        return removedStopwords;
    }

    public static ArrayList<String> tokenizeString(String s){
        StringTokenizer mStringTokenizer = new StringTokenizer(s);
        ArrayList<String> returnList = new ArrayList<String>();
        while (mStringTokenizer.hasMoreTokens()){
            returnList.add(mStringTokenizer.nextToken());
        }
        return returnList;
    }

    public static ArrayList<String> removeSpecialChars(ArrayList<String> tokenizedString){
        ArrayList<String> specialCharTokenizedString = new ArrayList<String>();
        for (String token : tokenizedString){
            StringTokenizer subStringTokenizer = new StringTokenizer(token, "-//'//.//?//!");
            while (subStringTokenizer.hasMoreTokens()){
                specialCharTokenizedString.add(subStringTokenizer.nextToken());
            }
        }
        return specialCharTokenizedString;
    }

    public static ArrayList<String> removeStopwords(ArrayList<String> tokenizedString){
        ArrayList<String> Stopwords = new ArrayList<String>();
        InputStream mInputStream = Constants.RESOURCES.openRawResource(R.raw.stopwords);
        BufferedReader mBufferedReader = new BufferedReader(new InputStreamReader(mInputStream));
        try {
            String eachline = mBufferedReader.readLine();
            while (eachline != null){
                Stopwords.add(eachline);
                eachline = mBufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<String> removedStopwords = new ArrayList<String>();
        for (String token : tokenizedString){
            if (!Stopwords.contains(token.toLowerCase())){
                removedStopwords.add(token);
            }
        }
        try {
            mBufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return removedStopwords;
    }
}