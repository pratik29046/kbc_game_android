package com.app.mygame.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessaging;

public class DeviceInfo {

    private final String deviceModel;
    private final String androidVersion;
    private final String deviceId;
    private final String appVersion;
    private final boolean notificationsEnabled;
    private final String fcmToken;  // FCM Token
    private final String deviceName;


    // Constructor
    public DeviceInfo(String deviceModel, String androidVersion, String deviceId, String appVersion, boolean notificationsEnabled, String fcmToken, String deviceName) {
        this.deviceModel = deviceModel;
        this.androidVersion = androidVersion;
        this.deviceId = deviceId;
        this.appVersion = appVersion;
        this.notificationsEnabled = notificationsEnabled;
        this.fcmToken = fcmToken;
        this.deviceName = deviceName;
    }

    // Getters
    public String getDeviceModel() {
        return deviceModel;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public boolean areNotificationsEnabled() {
        return notificationsEnabled;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public String getDeviceName() {
        return deviceName;
    }

    // Method to get device info
    public static void getDeviceInfo(Context context, DeviceInfoCallback callback) {
        String deviceModel = Build.MODEL;
        String androidVersion = Build.VERSION.RELEASE;
        String deviceId = getDeviceId(context);
        String appVersion = getAppVersion(context);
        boolean notificationsEnabled = getNotificationStatus(context);
        String deviceName = getDeviceNameInfo();

        // Fetch FCM token asynchronously
       /* FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    String fcmToken = "";
                    if (task.isSuccessful()) {
                        fcmToken = task.getResult();
                    }
                    DeviceInfo deviceInfo = new DeviceInfo(deviceModel, androidVersion, deviceId, appVersion, notificationsEnabled, fcmToken);
                    callback.onDeviceInfoFetched(deviceInfo);
                });*/
        DeviceInfo deviceInfo = new DeviceInfo(deviceModel, androidVersion, deviceId, appVersion, notificationsEnabled, "", deviceName);
        callback.onDeviceInfoFetched(deviceInfo);
    }

    // Device ID
    private static String getDeviceId(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10+ - Use ANDROID_ID as a unique identifier
            return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } else {
            // For older versions, use IMEI (but ensure proper permissions)
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    return telephonyManager.getImei(); // For devices below Android 10
                }
                return telephonyManager.getDeviceId(); // For devices before Android 8 (API 26)
            } else {
                return null; // Handle the case when permission is not granted
            }
        }
    }

    // App Version
    private static String getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "Unknown";
        }
    }

    // Notification Status
    private static boolean getNotificationStatus(Context context) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        return notificationManager.areNotificationsEnabled();
    }

    // Callback Interface for fetching device info asynchronously
    public interface DeviceInfoCallback {
        void onDeviceInfoFetched(DeviceInfo deviceInfo);
    }


    public static String getDeviceNameInfo() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
}
