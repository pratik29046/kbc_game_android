package com.app.mygame;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.app.mygame.appConfig.NetworkUtil;
import com.app.mygame.utils.DeviceInfo;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity"; // Define TAG for logging

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Applying insets to ensure content is not hidden behind system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Fetch device info asynchronously
        DeviceInfo.getDeviceInfo(this, new DeviceInfo.DeviceInfoCallback() {
            @Override
            public void onDeviceInfoFetched(DeviceInfo deviceInfo) {
                // Log device info once fetched
                if (deviceInfo != null) {
                    Log.d(TAG, "Device Model: " + deviceInfo.getDeviceModel());
                    Log.d(TAG, "Android Version: " + deviceInfo.getAndroidVersion());
                    Log.d(TAG, "Device ID: " + deviceInfo.getDeviceId());
                    Log.d(TAG, "App Version: " + deviceInfo.getAppVersion());
                    Log.d(TAG, "Notifications Enabled: " + deviceInfo.areNotificationsEnabled());
                    Log.d(TAG, "FCM Token: " + deviceInfo.getFcmToken());
                } else {
                    Log.e(TAG, "Failed to fetch device info.");
                }
            }
        });
    }
}
