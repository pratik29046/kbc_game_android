package com.app.mygame.usePre.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.app.mygame.R;
import com.app.mygame.network.ApiService;
import com.app.mygame.network.RetrofitClient;
import com.app.mygame.usePre.fragment.EnterMobileFragment;
import com.app.mygame.usePre.requestVo.User;
import com.app.mygame.usePre.responseVo.RegisterResponse;
import com.app.mygame.utils.StoreConfig;
import com.app.mygame.databinding.ActivityRegisterBinding;
import com.app.mygame.usePre.model.RegisterViewModel;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private RegisterViewModel viewModel;
    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        // Set up DataBinding with ViewModel and LifecycleOwner
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
    }

    // Method to handle the register button click and save data to SharedPreferences
    public void onRegisterClicked(View view) {
        // Collecting data in a HashMap to store in SharedPreferences
        var keyValueMap = new HashMap<String, String>();

        // Using Optional to handle potential null values from viewModel
        Optional.ofNullable(viewModel.getUserName().getValue()).ifPresent(value -> keyValueMap.put("userName", value));
        Optional.ofNullable(viewModel.getUserLastName().getValue()).ifPresent(value -> keyValueMap.put("userLastName", value));
        Optional.ofNullable(viewModel.getUserAddress().getValue()).ifPresent(value -> keyValueMap.put("userAddress", value));
        Optional.ofNullable(viewModel.getUserAdhar().getValue()).ifPresent(value -> keyValueMap.put("userAdhar", value));
        Optional.ofNullable(viewModel.getUserAge().getValue()).ifPresent(value -> keyValueMap.put("userAge", String.valueOf(value))); // Converting int to String
        Optional.ofNullable(viewModel.getUserPhone().getValue()).ifPresent(value -> keyValueMap.put("userPhone", value));
        Optional.ofNullable(viewModel.getUserEmail().getValue()).ifPresent(value -> keyValueMap.put("userEmail", value));
        Optional.ofNullable(viewModel.getUserPan().getValue()).ifPresent(value -> keyValueMap.put("userPan", value));
        Optional.ofNullable(viewModel.getIfscCode().getValue()).ifPresent(value -> keyValueMap.put("ifscCode", value));
        Optional.ofNullable(viewModel.getBankName().getValue()).ifPresent(value -> keyValueMap.put("bankName", value));
        Optional.ofNullable(viewModel.getBankAccountNumber().getValue()).ifPresent(value -> keyValueMap.put("bankAccountNumber", value));
        Optional.ofNullable(viewModel.getUpi().getValue()).ifPresent(value -> keyValueMap.put("upi", value));
        Optional.ofNullable(viewModel.getGender().getValue()).ifPresent(value -> keyValueMap.put("gender", value));
        StoreConfig.storeMultipleConfigs(this, "userPrefs", keyValueMap);

        User user = new User();
        user.setUserName(viewModel.getUserName().getValue());
        user.setUserLastName(viewModel.getUserLastName().getValue());
        user.setUserAddress(viewModel.getUserAddress().getValue());
        user.setUserAdhar(viewModel.getUserAdhar().getValue());
        user.setUserAge(viewModel.getUserAge().getValue() != null && !viewModel.getUserAge().getValue().isEmpty() ? Integer.parseInt(viewModel.getUserAge().getValue()) : 0);
        user.setUserPhone(viewModel.getUserPhone().getValue());
        user.setUserEmail(viewModel.getUserEmail().getValue());
        user.setUserPan(viewModel.getUserPan().getValue());
        user.setIfscCode(viewModel.getIfscCode().getValue());
        user.setBankName(viewModel.getBankName().getValue());
        user.setBankAccountNumber(viewModel.getBankAccountNumber().getValue());
        user.setUpi(viewModel.getUpi().getValue());
        user.setUserGender(viewModel.getGender().getValue());

        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);
        apiService.registerUser(user).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.body() != null) {
                    RegisterResponse registerResponse = response.body();
                    if ("SUCCESS".equalsIgnoreCase(registerResponse.status)) {
                        Toast.makeText(RegisterActivity.this, registerResponse.message, Toast.LENGTH_SHORT).show();
                        binding.scrollView.setVisibility(View.GONE); // Hides the ScrollView that contains the registration form
                        binding.fragmentContainer.setVisibility(View.VISIBLE);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new EnterMobileFragment())
                                .addToBackStack(null)
                                .commit();
                    } else {
                        Toast.makeText(RegisterActivity.this, registerResponse.message, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: "+t.getMessage() );
                Toast.makeText(RegisterActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
