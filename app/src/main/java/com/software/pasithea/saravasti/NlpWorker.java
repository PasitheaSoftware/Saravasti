package com.software.pasithea.saravasti;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.ArrayList;
import java.util.List;

public class NlpWorker extends Worker {
    private static final String TAG = "NlpWorker";

    public NlpWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        List<String[]> outputList = new ArrayList<>();
        String result = "";
        String input = getInputData().getString(Constants.INPUT_STRING_ID);
        try {
            ArrayList<String> tokens = WordPreProcessor.startPreProcessing(input);
            for (int i = 0; i < tokens.size(); i++) {
                String outputValue = "";
                List<Pair<Integer, Float>> resulttemp = CarryStemmer.executeCarryStemmer(tokens.get(i));
                for (Pair<Integer, Float> mPair : resulttemp) {
                    if (mPair.first != -1) {
                        Log.d(TAG, Constants.DICTIONNARY.get(mPair.first).split(";")[1]
                                + "["
                                        + Constants.DICTIONNARY.get(mPair.first).split(";")[2]
                                        + "]: "
                                        + mPair.second);
                                outputValue = outputValue + " " + mPair.first.toString();

                            } else {
                                Log.d(TAG, tokens.get(i) + ": Not recognized");
                                outputValue = outputValue + " " + tokens.get(i);
                            }
                        }
                        outputList.add(outputValue.split(" "));
                    }
            List<String> formattedOutput = OutputFormatter.formatOutput(outputList);
            String[] outputArray = new String[formattedOutput.size()];
            outputArray = formattedOutput.toArray(outputArray);
            Data output = new Data.Builder()
                    .putStringArray(Constants.OUTPUT_STRING_ID, outputArray)
                    .build();
            return Result.success(output);
        } catch (Exception e){
                Log.e(TAG, "An error occurred during the Carry Stemmer application", e);
                return Result.failure();
        }
    }
}