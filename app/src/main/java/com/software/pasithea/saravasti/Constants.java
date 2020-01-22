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

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * The class that holds the Application information used through all the components.
 */

class Constants {

    protected static Application APPLICATION = null;
    protected static Context CONTEXT = null;
    protected static Resources RESOURCES = null;

    // Workers constant variables
    protected static final String INPUT_STRING_ID = "com.example.semanticrepresentationtesting.INPUT_STRING";
    protected static final String OUTPUT_STRING_ID = "com.example.semanticrepresentationtesting.OUTPUT_STRING";
    protected static final String OUTPUT_STRING_LIST_ID = "com.example.semanticrepresentationtesting.OUTPUT_STRING_LIST_LIST";
    protected static final String NLP_TAG = "com.example.semanticrepresentationtesting.NLP_WORKER_TAG";
    protected static final String DISPLAY_TAG = "com.example.semanticrepresentationtesting.DISPLAY_WORKER_TAG";

    // NLP constant variables
    protected static float DICT_THRESHOLD = (float)0.55;
    protected static int MAX_RETURN_SCORES = 3;
    protected static ArrayList<String> DICTIONNARY;
    protected static List<Pair<String, String>> APPLICATION_LIST;

    // CYK constant variables
    protected static List<Rules> RULES_LIST;

    public Constants() { }

    protected static void setAPPLICATION(Application application) {
        APPLICATION = application;
    }

    protected static void setRESOURCES(Resources RESOURCES) { Constants.RESOURCES = RESOURCES; }

    protected static void setCONTEXT(Context context) {
        Constants.CONTEXT = context;
    }

    protected static void setDictionnay(ArrayList<String> dictionnary) {
        DICTIONNARY = dictionnary;
    }

    protected static void setApplicationList(List<Pair<String, String>> applicationList) {
        APPLICATION_LIST = applicationList;
    }

    protected static void setRulesList(List<Rules> rulesList) {
        RULES_LIST = rulesList;
    }
}
