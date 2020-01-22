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

/**
 * Worker class to initialize the dictionnary.
 * It will open the dictionnary text file and create a list of string.
 * It will than store the list in the Constants.DICTIONNARY variable.
 * Because the file is quite big (49K element) one decided to execute it in a background task.
 */
class LoadDictWorker extends Worker {
    private static final String TAG = "LoadDictWorker";

    public LoadDictWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        ArrayList<String> dictList = new ArrayList<>();
        try {
            InputStream WordInputStream = Constants.RESOURCES.openRawResource(R.raw.full_dict);
            BufferedReader WordBufferedReader = new BufferedReader(new InputStreamReader(WordInputStream));
            try {
                String wordline = WordBufferedReader.readLine();
                while (wordline != null) {
                    dictList.add(wordline);
                    wordline = WordBufferedReader.readLine();
                }
                WordInputStream.close();
                WordBufferedReader.close();
                Constants.setDictionnay(dictList);

                return Result.success();
            } catch (IOException e) {
                Log.e(TAG, "IOException during the dictionnary loading", e);
                return Result.failure();
            }
        } catch (Exception e){
            Log.e(TAG, "An error occurred during the dictionnary loading", e);
            return Result.failure();
        }
    }
}
