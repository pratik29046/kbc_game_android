package com.app.mygame;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.app.mygame.databinding.ActivitySplashBinding;
import com.app.mygame.usePre.activity.RegisterActivity;
import com.app.mygame.usePre.fragment.BiometricFragment;
import com.app.mygame.utils.StoreConfig;

import java.util.HashMap;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE_PHONE = 1;
    private static final int PERMISSION_REQUEST_CODE_NOTIFICATION = 2;
    private ActivitySplashBinding binding; // ViewBinding reference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize ViewBinding
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION_REQUEST_CODE_PHONE);
            } else {
                checkNotificationPermission();
            }
        } else {
            proceedToNextScreen();
        }
    }

    private void checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQUEST_CODE_NOTIFICATION);
            } else {
                proceedToNextScreen();
            }
        } else {
            proceedToNextScreen();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE_PHONE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkNotificationPermission();
            } else {
                Toast.makeText(this, "Permission denied. The app cannot access device information.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PERMISSION_REQUEST_CODE_NOTIFICATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                proceedToNextScreen();
            } else {
                Toast.makeText(this, "Notification permission denied.", Toast.LENGTH_SHORT).show();
                proceedToNextScreen();
            }
        }
    }

    private void proceedToNextScreen() {
        HashMap<String, String> configMap = StoreConfig.getAllConfigs(this, "callApiWithOtp");
        if (configMap.containsKey("user_id") && configMap.containsKey("mobile_number") &&
                configMap.get("user_id") != null && !configMap.get("user_id").isEmpty() &&
                configMap.get("mobile_number") != null && !configMap.get("mobile_number").isEmpty()) {
            // Hide logo, show fragment container
            binding.logo.setVisibility(View.GONE);
            binding.fragmentContainer.setVisibility(View.VISIBLE);

            Bundle bundle = new Bundle();
            bundle.putString("user_id", configMap.get("user_id"));
            bundle.putString("mobile_number", configMap.get("mobile_number"));
            BiometricFragment biometricFragment = new BiometricFragment();
            biometricFragment.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, biometricFragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            Intent intent = new Intent(SplashActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
