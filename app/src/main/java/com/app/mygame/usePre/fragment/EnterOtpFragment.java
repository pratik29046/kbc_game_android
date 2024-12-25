package com.app.mygame.usePre.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.mygame.R;
import com.app.mygame.databinding.FragmentEnterOtpBinding;
import com.app.mygame.network.ApiService;
import com.app.mygame.network.RetrofitClient;
import com.app.mygame.usePre.activity.RegisterActivity;
import com.app.mygame.usePre.requestVo.LoginRequest;
import com.app.mygame.usePre.requestVo.SendOtpRequest;
import com.app.mygame.usePre.requestVo.User;
import com.app.mygame.usePre.responseVo.LoginResponse;
import com.app.mygame.usePre.responseVo.OtpSuccessResponse;
import com.app.mygame.userPost.DashboardActivity;
import com.app.mygame.utils.DeviceInfo;
import com.app.mygame.utils.StoreConfig;
import com.app.mygame.utils.TokenManager;

import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterOtpFragment extends Fragment {

    private FragmentEnterOtpBinding binding;
    private StringBuilder otpBuilder = new StringBuilder();

    String mobileNumber, countryCode;

    public EnterOtpFragment() {
        super(R.layout.fragment_enter_otp);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up data binding
        binding = FragmentEnterOtpBinding.bind(view);

        // Get the mobile number and country code passed from the previous fragment
        Bundle args = getArguments();
        if (args != null) {
            mobileNumber = args.getString("mobile_number");
            countryCode = args.getString("country_code");
            Toast.makeText(requireContext(), "OTP sent to: " + mobileNumber + " (" + countryCode + ")", Toast.LENGTH_SHORT).show();
        }

        // Set up button click listeners
        setKeyListeners();
    }

    private void setKeyListeners() {
        binding.key1.setOnClickListener(v -> appendOtpDigit("1"));
        binding.key2.setOnClickListener(v -> appendOtpDigit("2"));
        binding.key3.setOnClickListener(v -> appendOtpDigit("3"));
        binding.key4.setOnClickListener(v -> appendOtpDigit("4"));
        binding.key5.setOnClickListener(v -> appendOtpDigit("5"));
        binding.key6.setOnClickListener(v -> appendOtpDigit("6"));
        binding.key7.setOnClickListener(v -> appendOtpDigit("7"));
        binding.key8.setOnClickListener(v -> appendOtpDigit("8"));
        binding.key9.setOnClickListener(v -> appendOtpDigit("9"));
        binding.key0.setOnClickListener(v -> appendOtpDigit("0"));
        binding.keyDelete.setOnClickListener(v -> deleteOtpLastDigit());
    }

    private void appendOtpDigit(String digit) {
        if (otpBuilder.length() < 4) {
            otpBuilder.append(digit);
            updateOtpFields();
        }
        // When OTP is complete, validate and proceed
        if (otpBuilder.length() == 4) {
            SendOtpRequest sendOtpRequest = new SendOtpRequest(mobileNumber, otpBuilder.toString());
            callApiWithOtp(sendOtpRequest);
        }
    }

    private void deleteOtpLastDigit() {
        if (otpBuilder.length() > 0) {
            otpBuilder.deleteCharAt(otpBuilder.length() - 1);
            updateOtpFields();
        }
    }

    private void updateOtpFields() {
        String otp = otpBuilder.toString();
        binding.otpEditText1.setText(otp.length() > 0 ? String.valueOf(otp.charAt(0)) : "");
        binding.otpEditText2.setText(otp.length() > 1 ? String.valueOf(otp.charAt(1)) : "");
        binding.otpEditText3.setText(otp.length() > 2 ? String.valueOf(otp.charAt(2)) : "");
        binding.otpEditText4.setText(otp.length() > 3 ? String.valueOf(otp.charAt(3)) : "");
    }

    private void callApiWithOtp(SendOtpRequest sendOtpRequest) {
        // Create a User object with mobile number and OTP
        ApiService apiService = RetrofitClient.getClient(requireContext()).create(ApiService.class);

// Call the API to verify the OTP
        Call<OtpSuccessResponse> call = apiService.verifyOtp(sendOtpRequest);

        call.enqueue(new Callback<OtpSuccessResponse>() {
            @Override
            public void onResponse(Call<OtpSuccessResponse> call, Response<OtpSuccessResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    OtpSuccessResponse otpSuccessResponse = response.body();
                    if ("success".equalsIgnoreCase(otpSuccessResponse.status)) {
                        // OTP verification is successful
                        Toast.makeText(requireContext(), "OTP Verified", Toast.LENGTH_SHORT).show();

                        Bundle bundle = new Bundle();
                        bundle.putString("user_id", String.valueOf(otpSuccessResponse.data.userId));
                        bundle.putString("mobile_number", otpSuccessResponse.data.mobileNo);

                        HashMap<String, String> configMap = new HashMap<>();
                        configMap.put("user_id", String.valueOf(otpSuccessResponse.data.userId));
                        configMap.put("mobile_number", otpSuccessResponse.data.mobileNo);
                        StoreConfig.storeMultipleConfigs(requireContext(), "callApiWithOtp", configMap);

                        BiometricFragment biometricFragment = new BiometricFragment();
                        biometricFragment.setArguments(bundle);
                        requireActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, biometricFragment) // Replace with your container ID
                                .addToBackStack(null)
                                .commit();
                    } else {
                        // Handle server-side validation failure
                        Toast.makeText(requireContext(), otpSuccessResponse.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String errorMessage = "Unknown error"; // Default message
                    if (response.errorBody() != null) {
                        try {
                            String errorBody = response.errorBody().string();
                            JSONObject errorJson = new JSONObject(errorBody);
                            errorMessage = errorJson.optString("message", "Unknown error"); // Extract "message" field
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<OtpSuccessResponse> call, Throwable t) {
                // Handle network or other errors
                Toast.makeText(requireContext(), "Network error. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
