package com.app.mygame.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class StoreConfig {

    private static final String TAG = "AppConfig";

    /**
     * Store multiple key-value pairs in SharedPreferences with dynamic name.
     *
     * @param context Application context
     * @param prefName The dynamic SharedPreferences name
     * @param keyValueMap A HashMap containing keys and values to store
     */
    public static void storeMultipleConfigs(Context context, String prefName, HashMap<String, String> keyValueMap) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        for (Map.Entry<String, String> entry : keyValueMap.entrySet()) {
            editor.putString(entry.getKey(), entry.getValue());
        }

        editor.apply();
        Log.d(TAG, "Stored multiple configurations in " + prefName + ": " + keyValueMap.toString());
    }

    /**
     * Retrieve all key-value pairs from SharedPreferences with dynamic name.
     *
     * @param context Application context
     * @param prefName The dynamic SharedPreferences name
     * @return A HashMap containing all stored key-value pairs
     */
    public static HashMap<String, String> getAllConfigs(Context context, String prefName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();
        HashMap<String, String> resultMap = new HashMap<>();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            resultMap.put(entry.getKey(), entry.getValue().toString());
        }

        Log.d(TAG, "Retrieved all configurations from " + prefName + ": " + resultMap.toString());
        return resultMap;
    }

    /**
     * Store a single key-value pair in SharedPreferences with dynamic name.
     *
     * @param context Application context
     * @param prefName The dynamic SharedPreferences name
     * @param key The key to store
     * @param value The value to store
     */
    public static void storeSingleConfig(Context context, String prefName, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
        Log.d(TAG, "Stored single configuration in " + prefName + ": {" + key + ": " + value + "}");
    }

    /**
     * Retrieve a single value from SharedPreferences by key with dynamic name.
     *
     * @param context Application context
     * @param prefName The dynamic SharedPreferences name
     * @param key The key to retrieve
     * @param defaultValue Default value if the key doesn't exist
     * @return The value associated with the key, or the default value if not found
     */
    public static String getSingleConfig(Context context, String prefName, String key, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        String value = sharedPreferences.getString(key, defaultValue);
        Log.d(TAG, "Retrieved single configuration from " + prefName + ": {" + key + ": " + value + "}");
        return value;
    }

    // Store an object by converting it to a JSON string
    public static void storeObjectConfig(Context context, String prefName, String key, Object object) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Convert the object to a JSON string using Gson
        Gson gson = new Gson();
        String json = gson.toJson(object);

        editor.putString(key, json);
        editor.apply();
        Log.d(TAG, "Stored object configuration in " + prefName + ": {" + key + ": " + json + "}");
    }

    // Retrieve an object by converting it from a JSON string
    public static <T> T getObjectConfig(Context context, String prefName, String key, Class<T> classOfT) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(key, null);

        if (json != null) {
            // Convert the JSON string back to an object using Gson
            Gson gson = new Gson();
            return gson.fromJson(json, classOfT);
        }

        Log.d(TAG, "No object configuration found in " + prefName + " for key: " + key);
        return null;
    }
}

