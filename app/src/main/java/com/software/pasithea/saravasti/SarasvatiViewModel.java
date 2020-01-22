

package com.software.pasithea.saravasti;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkContinuation;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.List;

/**
 * Public ViewModel to use the semantic representation algorithm.
 */
public class SarasvatiViewModel extends AndroidViewModel {
    private static final String TAG = "SarasvatiViewModel";

    private LiveData<List<WorkInfo>> InitWorkInfo;
    private LiveData<List<WorkInfo>> NlpWorkInfo;
    private WorkManager mWorkManager;
    private WorkContinuation InitDataContinuation = null;
    private WorkContinuation mWorkContinuation = null;

    /**
     * Constructor for the viewmodel. Follow the standard contructor for an AnroidViewModel
     * @param application
     */
    public SarasvatiViewModel(@NonNull Application application) {
        super(application);
        mWorkManager = WorkManager.getInstance(application);
        NlpWorkInfo = mWorkManager.getWorkInfosForUniqueWorkLiveData(Constants.NLP_TAG);
    }

    /**
     * Get the status of the initialization workers.
     * @return
     */
    public LiveData<List<WorkInfo>> getInitWorkInfo() {
        return InitDataContinuation.getWorkInfosLiveData();
    }

    /**
     * Initialise the data in a Workers chain.</br>
     * First initialize the dictionnary and then the rules.
     */
    public void startInitData(){
        // Init the dictionnary initialization and add it to the WorkContinuation.
        // Will be executed first
        OneTimeWorkRequest initDictOneTimeRequest = new OneTimeWorkRequest.Builder(LoadDictWorker.class)
                .build();
        InitDataContinuation = mWorkManager.beginWith(initDictOneTimeRequest);

        // Create the work request for the rules initialization and add it to the WorkContinuation.
        // Will be executed upon the first request completion
        OneTimeWorkRequest initRulesOneTimeRequest = new OneTimeWorkRequest.Builder(LoadRulesWorker.class)
                .build();
        InitDataContinuation = InitDataContinuation.then(initRulesOneTimeRequest);

        // Start the WorkContinuation
        InitDataContinuation.enqueue();
    }

    /**
     * Wrapper to execute the action detected by the semantic parser
     * @param action
     * @param object
     */
    public void executeAction(String action, String object){
        Executor.Dispatcher(action, object);
    }

    /**
     * Create the work chain for the sentence analysis.
     * @param inputString
     */
    public void startNlpProcess(Data inputString){

        // Create the first worker and add it to the WorkContinuation
        OneTimeWorkRequest nlpWorkRequest = new OneTimeWorkRequest.Builder(NlpWorker.class)
                .setInputData(inputString)
                .build();
        mWorkContinuation = mWorkManager.beginWith(nlpWorkRequest);

        // Create the second worker and add it to the WorkContinuation
        OneTimeWorkRequest semanticWorkRequest = new OneTimeWorkRequest.Builder(SemanticWorker.class)
                .build();
        mWorkContinuation = mWorkContinuation.then(semanticWorkRequest);

        // Start the WorkContinuation
        mWorkContinuation.enqueue();
    }

    /**
     * get the status of the nlpWorkers.
     * @return
     */
    public LiveData<List<WorkInfo>> getNlpWorkInfo(){
        return NlpWorkInfo;
    }

    /**
     * Create the Data for the NLP workers.
     * @param rawInput
     * @return
     */
    public Data createInputString(String rawInput){
        String input = rawInput.trim();
        Log.i(TAG, "createInputString: " + input);
        Data.Builder mDataBuilder = new Data.Builder();
        mDataBuilder.putString(Constants.INPUT_STRING_ID, input);
        return mDataBuilder.build();
    }
}
