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

            String countryCode = selectedCountryCode.split(" ")[0];
            Bundle bundle = new Bundle();
            bundle.putString("mobile_number", mobileNumber);
            bundle.putString("country_code", countryCode);

            EnterOtpFragment enterOtpFragment = new EnterOtpFragment();
            enterOtpFragment.setArguments(bundle);

            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, enterOtpFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        binding.registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), RegisterActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });
    }
}