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
import com.app.mygame.usePre.activity.RegisterActivity;

public class EnterMobileFragment extends Fragment {

    private FragmentEnterMobileBinding binding;

    public EnterMobileFragment() {
        super(R.layout.fragment_enter_mobile);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentEnterMobileBinding.bind(view);

        // Initialize the Spinner for country code
        Spinner countryCodeSpinner = binding.countryCodeSpinner;

        // Create an ArrayAdapter for the country codes from the resources
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.country_codes, android.R.layout.simple_spinner_item);

        // Set the drop-down view for the Spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter to the Spinner
        countryCodeSpinner.setAdapter(adapter);

        // Set the default selection to India (first item in the spinner)
        countryCodeSpinner.setSelection(0);

        countryCodeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle country code selection if needed
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle case when no country code is selected
            }
        });

        // Set up the next button listener
        binding.nextButton.setOnClickListener(v -> {
            String mobileNumber = binding.mobileEditText.getText().toString().trim();
            String selectedCountryCode = (String) countryCodeSpinner.getSelectedItem();

            // Extract the country code from the selected item (e.g., "+91" from "+91 (India)")
            String countryCode = selectedCountryCode.split(" ")[0];

            // Check if the mobile number is valid
            if (mobileNumber.length() == 10) {
                // Pass both the country code and mobile number to the fragment via a Bundle
                Bundle bundle = new Bundle();
                bundle.putString("mobile_number", mobileNumber);  // Pass the mobile number
                bundle.putString("country_code", countryCode);  // Pass the country code

                // Replace the fragment using FragmentTransaction
                EnterOtpFragment enterOtpFragment = new EnterOtpFragment();
                enterOtpFragment.setArguments(bundle);

                // Begin fragment transaction to replace the current fragment with EnterOtpFragment
                FragmentTransaction transaction = requireFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, enterOtpFragment);  // Replace with EnterOtpFragment
                transaction.addToBackStack(null);  // Add the transaction to back stack if needed
                transaction.commit();
            } else {
                Toast.makeText(requireContext(), "Please enter a valid 10-digit mobile number", Toast.LENGTH_SHORT).show();
            }
        });

        binding.registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), RegisterActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });
    }
}
