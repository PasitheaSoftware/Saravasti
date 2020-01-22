package com.software.pasithea.saravasti;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.List;

public class SemanticWorker extends Worker {
    private static final String TAG = "SemanticWorker";


    public SemanticWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            String[] outputString = getInputData().getStringArray(Constants.OUTPUT_STRING_ID);
            List<String> parsedList = SemanticParser.chartParsing(outputString);
            for (String s : parsedList){
                Log.d(TAG, "String detected: " + s);
            }
            String output = "";
            for (int i = 0; i < parsedList.size() ; i++) {
                if (parsedList.get(i).equals("None")){
                    output = output + "Action: None\n";
                } else {
                    String splitRuleResult = parsedList.get(i).split(";")[1];
                    String action = splitRuleResult.split(" ")[0];
                    String object = splitRuleResult.split(" ")[1];
                    Executor.Dispatcher(action, object);
                    output = output + String.format("Action: %s\n",splitRuleResult);
                }
            }
            return Result.success();
        } catch (Exception e) {
            Log.e(TAG, "Error during the semantic parsing", e);
            return Result.failure();
        }
    }
}
