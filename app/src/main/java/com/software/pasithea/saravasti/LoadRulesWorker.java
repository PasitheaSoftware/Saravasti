package com.software.pasithea.saravasti;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

class LoadRulesWorker extends Worker {
    private static final String TAG = "LoadRulesWorker";


    public LoadRulesWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        List<Rules> RulesList = new ArrayList<>();
        try {
            InputStream RulesInputStream = Constants.RESOURCES.openRawResource(R.raw.rules_list);
            BufferedReader RulesBufferedReader = new BufferedReader(new InputStreamReader(RulesInputStream));
            try {
                String ruleline = RulesBufferedReader.readLine();
                while (ruleline != null) {
                    RulesList.add(SemanticParser.createRule(ruleline));
                    ruleline = RulesBufferedReader.readLine();
                }
                RulesInputStream.close();
                RulesBufferedReader.close();
                Constants.RULES_LIST.addAll(RulesList);
                return Result.success();
            } catch (IOException e) {
                Log.e(TAG, "IOException during the rules creation", e);
                return Result.failure();
            }
        } catch (Exception e){
            Log.e(TAG, "An error occurred during the rules creation", e);
            return Result.failure();
        }
    }
}
