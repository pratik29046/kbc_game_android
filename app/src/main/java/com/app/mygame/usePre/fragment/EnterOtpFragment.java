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
import com.app.mygame.userPost.DashboardActivity;

public class EnterOtpFragment extends Fragment {

    private FragmentEnterOtpBinding binding;
    private StringBuilder otpBuilder = new StringBuilder();

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
            String mobileNumber = args.getString("mobile_number");
            String countryCode = args.getString("country_code");
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
            callApiWithOtp(otpBuilder.toString());
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

    private void callApiWithOtp(String otp) {
        // Call your API to verify the OTP
        Toast.makeText(requireContext(), "API Called with OTP: " + otp, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getContext(), DashboardActivity.class);
        startActivity(intent);
    }
}
