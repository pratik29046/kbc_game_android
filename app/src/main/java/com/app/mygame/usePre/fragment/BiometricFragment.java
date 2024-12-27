package com.app.mygame.usePre.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.app.mygame.R;
import com.app.mygame.databinding.FragmentBiometricBinding;
import com.app.mygame.network.ApiService;
import com.app.mygame.network.RetrofitClient;
import com.app.mygame.usePre.requestVo.LoginRequest;
import com.app.mygame.usePre.responseVo.LoginResponse;
import com.app.mygame.usePre.responseVo.ProfileResponse;
import com.app.mygame.userPost.DashboardActivity;
import com.app.mygame.utils.DeviceInfo;
import com.app.mygame.utils.StoreConfig;
import com.app.mygame.utils.TokenManager;

import org.json.JSONObject;

import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BiometricFragment extends Fragment {
    FragmentBiometricBinding binding;
    private String userId, mobileNumber;

    public BiometricFragment() {
        super(R.layout.fragment_biometric);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentBiometricBinding.bind(view);

        // Retrieve Profile Data from SharedPreferences
        ProfileResponse.Data profileData = StoreConfig.getObjectConfig(requireContext(), "UserProfilePrefs", "profile_data", ProfileResponse.Data.class);

        if (profileData != null) {
            binding.tvUserName.setText(profileData.userName + " " + profileData.userLastName);
            binding.tvUserEmail.setText(profileData.userEmail);
        }

        // Retrieve arguments passed from the previous fragment
        Bundle args = getArguments();
        if (args != null) {
            userId = args.getString("user_id");
            mobileNumber = args.getString("mobile_number");
        }

        checkBiometricSupport();

        // Handle back press to kill the app
        requireActivity().getOnBackPressedDispatcher().addCallback(
                getViewLifecycleOwner(),
                new androidx.activity.OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        requireActivity().finishAffinity(); // Kill the app
                    }
                }
        );
    }

    private void checkBiometricSupport() {
        BiometricManager biometricManager = BiometricManager.from(requireContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
                case BiometricManager.BIOMETRIC_SUCCESS:
                    showBiometricPrompt();
                    break;
                case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                    binding.btnLogin.setVisibility(View.VISIBLE); // Show the login button
                    binding.btnLogin.setOnClickListener(v -> proceedToLogin());
                    break;
                default:
                    Toast.makeText(requireContext(), "Authentication not supported", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private void showBiometricPrompt() {
        Executor executor = ContextCompat.getMainExecutor(requireContext());
        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(requireContext(), "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                proceedToLogin();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(requireContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Authenticate to proceed")
                .setNegativeButtonText("Cancel")
                .build();

        biometricPrompt.authenticate(promptInfo);
    }

    private void proceedToLogin() {
        ApiService apiService = RetrofitClient.getClient(requireContext()).create(ApiService.class);
        DeviceInfo.getDeviceInfo(requireContext(), deviceInfo -> {
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.userId = Long.parseLong(userId);
            loginRequest.userMobileNumber = mobileNumber;
            loginRequest.deviceId = deviceInfo.getDeviceId();
            loginRequest.userDevice = deviceInfo.getDeviceName();
            loginRequest.userNotificationId = deviceInfo.getFcmToken();
            loginRequest.deviceType = "Android";
            loginRequest.userIsActiveStatus = true;
            callLoginApi(apiService, loginRequest);
        });
    }

    private void callLoginApi(ApiService apiService, LoginRequest loginRequest) {
        Call<LoginResponse> loginCall = apiService.login(loginRequest);
        loginCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    if ("success".equalsIgnoreCase(loginResponse.status)) {
                        Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                        TokenManager.setAuthToken(loginResponse.data.token);
                        callProfileApi(apiService);
                    } else {
                        Toast.makeText(requireContext(), loginResponse.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String errorMessage = "Unknown error";
                    if (response.errorBody() != null) {
                        try {
                            String errorBody = response.errorBody().string();
                            JSONObject errorJson = new JSONObject(errorBody);
                            errorMessage = errorJson.optString("message", "Unknown error");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "Network error. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callProfileApi(ApiService apiService) {
        Call<ProfileResponse> profileCall = apiService.profile();
        profileCall.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProfileResponse profileResponse = response.body();
                    if ("success".equalsIgnoreCase(profileResponse.status)) {
                        ProfileResponse.Data data = profileResponse.data;
                        if (data != null) {
                            StoreConfig.storeObjectConfig(requireContext(), "UserProfilePrefs", "profile_data", data);
                            Toast.makeText(requireContext(), "Profile saved and Login Successful", Toast.LENGTH_SHORT).show();
                        }
                        Intent intent = new Intent(requireContext(), DashboardActivity.class);
                        startActivity(intent);
                        requireActivity().finish();
                    } else {
                        Toast.makeText(requireContext(), profileResponse.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String errorMessage = "Unknown error";
                    if (response.errorBody() != null) {
                        try {
                            String errorBody = response.errorBody().string();
                            JSONObject errorJson = new JSONObject(errorBody);
                            errorMessage = errorJson.optString("message", "Unknown error");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "Network error. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
