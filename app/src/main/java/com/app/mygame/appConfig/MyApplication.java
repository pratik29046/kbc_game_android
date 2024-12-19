package com.app.mygame.appConfig;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;

public class MyApplication extends Application {

    private static final String TAG = "MyApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        // Global application initialization
        initializeApp();

    }

    /**
     * Initialize global configurations or utilities for the app.
     */
    private void initializeApp() {
        Log.d(TAG, "Application started successfully.");
        boolean isInternetAvailable = NetworkUtil.isInternetAvailable(this);
        if (!isInternetAvailable) {
            Toast.makeText(this, "No internet connection available!", Toast.LENGTH_LONG).show();
        }
    }
}
