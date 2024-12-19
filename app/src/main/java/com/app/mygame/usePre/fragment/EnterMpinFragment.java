package com.app.mygame.usePre.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.app.mygame.R;
import com.app.mygame.databinding.FragmentEnterMpinBinding;

public class EnterMpinFragment extends Fragment {

    private FragmentEnterMpinBinding binding;
    private StringBuilder mpinBuilder = new StringBuilder();

    public EnterMpinFragment() {
        super(R.layout.fragment_enter_mpin);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = DataBindingUtil.bind(view);
        setKeyListeners();
    }

    private void setKeyListeners() {
        binding.key1.setOnClickListener(v -> appendDigit("1"));
        binding.key2.setOnClickListener(v -> appendDigit("2"));
        binding.key3.setOnClickListener(v -> appendDigit("3"));
        binding.key4.setOnClickListener(v -> appendDigit("4"));
        binding.key5.setOnClickListener(v -> appendDigit("5"));
        binding.key6.setOnClickListener(v -> appendDigit("6"));
        binding.key7.setOnClickListener(v -> appendDigit("7"));
        binding.key8.setOnClickListener(v -> appendDigit("8"));
        binding.key9.setOnClickListener(v -> appendDigit("9"));
        binding.key0.setOnClickListener(v -> appendDigit("0"));
        binding.keyDelete.setOnClickListener(v -> deleteLastDigit());
    }

    private void appendDigit(String digit) {
        if (mpinBuilder.length() < 4) {
            mpinBuilder.append(digit);
            updateMpinFields();
        }
        if (mpinBuilder.length() == 4) {
            callApiWithMpin(mpinBuilder.toString());
        }
    }

    private void deleteLastDigit() {
        if (mpinBuilder.length() > 0) {
            mpinBuilder.deleteCharAt(mpinBuilder.length() - 1);
            updateMpinFields();
        }
    }

    private void updateMpinFields() {
        String mpin = mpinBuilder.toString();
        binding.mpinEditText1.setText(!mpin.isEmpty() ? String.valueOf(mpin.charAt(0)) : "");
        binding.mpinEditText2.setText(mpin.length() > 1 ? String.valueOf(mpin.charAt(1)) : "");
        binding.mpinEditText3.setText(mpin.length() > 2 ? String.valueOf(mpin.charAt(2)) : "");
        binding.mpinEditText4.setText(mpin.length() > 3 ? String.valueOf(mpin.charAt(3)) : "");
    }

    private void callApiWithMpin(String mpin) {
        if (mpin.length() == 4) {
            Toast.makeText(requireContext(), "API Called with MPIN: " + mpin, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Please enter a valid 4-digit MPIN", Toast.LENGTH_SHORT).show();
        }
    }
}
