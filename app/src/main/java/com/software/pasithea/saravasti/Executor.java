package com.software.pasithea.saravasti;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.List;

/**
 * Executor helper class
 */

class Executor {
    private static final String TAG = "Executor";

    private static Intent LaunchIntent;


    public Executor() {
    }

    private static Boolean isAppRunning(String pkgname) {
        final ActivityManager mActivityManager = (ActivityManager) Constants.APPLICATION.getSystemService(Constants.CONTEXT.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> TaskInfos = mActivityManager.getRunningAppProcesses();
        if (TaskInfos != null) {
            for (int i = 0; i < TaskInfos.size(); i++) {
                Log.d(TAG, "isAppRunning: " + TaskInfos.get(i).processName);
                if (TaskInfos.get(i).processName.equals(pkgname)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected static void Dispatcher(String action, String object) {
        Log.d(TAG, "Dispatcher: " + action);
        if (action.equals("open")) {
            startApp(object);
        }

    }

    private static void startApp(String pkgname) {
        // check if the app is already running and if not start it
        if (!Executor.isAppRunning(pkgname)) {
            final PackageManager mPackagerManager = Constants.APPLICATION.getPackageManager();
            LaunchIntent = mPackagerManager.getLaunchIntentForPackage(pkgname);
            if (LaunchIntent != null) {
                Constants.APPLICATION.startActivity(LaunchIntent);
                Log.i(TAG, String.format("startApp: %s started", pkgname));
            } else {
                Log.i(TAG, String.format("startApp: Launch Intent not found for %s", pkgname));
            }
        }
    }
}
