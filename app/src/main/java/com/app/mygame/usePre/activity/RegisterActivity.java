package com.app.mygame.usePre.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.app.mygame.R;
import com.app.mygame.usePre.fragment.EnterMobileFragment;
import com.app.mygame.usePre.fragment.MpinFragment;
import com.app.mygame.utils.StoreConfig;
import com.app.mygame.databinding.ActivityRegisterBinding;
import com.app.mygame.usePre.model.RegisterViewModel;

import java.util.HashMap;
import java.util.Optional;

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

        // Save the collected data to SharedPreferences
        StoreConfig.storeMultipleConfigs(this, "userPrefs", keyValueMap);

        // Show success message
        Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
        binding.scrollView.setVisibility(View.GONE); // Hides the ScrollView that contains the registration form
        binding.fragmentContainer.setVisibility(View.VISIBLE);
        // Replace the current fragment container with the EnterMobileFragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new EnterMobileFragment()) // Replace with your desired fragment
                .addToBackStack(null) // Add to back stack so the user can navigate back
                .commit();
    }
}
