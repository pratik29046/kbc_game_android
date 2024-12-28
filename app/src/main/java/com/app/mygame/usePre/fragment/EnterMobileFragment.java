package com.app.mygame.usePre.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.app.mygame.R;
import com.app.mygame.databinding.FragmentEnterMobileBinding;
import com.app.mygame.network.ApiService;
import com.app.mygame.network.RetrofitClient;
import com.app.mygame.usePre.activity.RegisterActivity;
import com.app.mygame.usePre.requestVo.SendOtpRequest;
import com.app.mygame.usePre.requestVo.User;
import com.app.mygame.utils.ProgressPopup;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterMobileFragment extends Fragment {

    private FragmentEnterMobileBinding binding;

    public EnterMobileFragment() {
        super(R.layout.fragment_enter_mobile);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentEnterMobileBinding.bind(view);
        Spinner countryCodeSpinner = binding.countryCodeSpinner;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.country_codes, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countryCodeSpinner.setAdapter(adapter);
        int defaultPosition = adapter.getPosition("+91 (India)");
        countryCodeSpinner.setSelection(defaultPosition);

        countryCodeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle selection
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle no selection
            }
        });

        binding.nextButton.setOnClickListener(v -> {
            String mobileNumber = binding.mobileEditText.getText().toString().trim();
            String selectedCountryCode = (String) countryCodeSpinner.getSelectedItem();

            if (mobileNumber.length() != 10 || !mobileNumber.matches("\\d+")) {
                Toast.makeText(requireContext(), "Please enter a valid 10-digit mobile number", Toast.LENGTH_SHORT).show();
                return;
            }
            SendOtpRequest sendOtpRequest = new SendOtpRequest(mobileNumber,"");
            sendOtp(sendOtpRequest);
        });

        binding.registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), RegisterActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });
    }

    private void sendOtp(SendOtpRequest sendOtpRequest) {
        ApiService apiService = RetrofitClient.getClient(requireContext()).create(ApiService.class);
        // Assuming the endpoint for sending OTP is '/login/sendotp'
        Call<User> call = apiService.sendOtp(sendOtpRequest);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Assuming the API response is successful
                    Toast.makeText(requireContext(), "OTP sent successfully", Toast.LENGTH_SHORT).show();
                    // You can now transition to the OTP fragment
                    EnterOtpFragment enterOtpFragment = new EnterOtpFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("mobile_number", sendOtpRequest.getMobileNo());
                    enterOtpFragment.setArguments(bundle);

                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, enterOtpFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }else{
                    // Handle failure
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
            public void onFailure(Call<User> call, Throwable t) {
                // Handle network error
                Toast.makeText(requireContext(), "Network error. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}