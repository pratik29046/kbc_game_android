package com.app.mygame.usePre.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.app.mygame.databinding.FragmentMpinBinding;
import com.app.mygame.usePre.model.MpinViewModel;

public class MpinFragment extends Fragment {

    private FragmentMpinBinding binding;
    private MpinViewModel viewModel;

    public MpinFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment using ViewBinding
        binding = FragmentMpinBinding.inflate(inflater, container, false);

        // Get ViewModel instance
        viewModel = new ViewModelProvider(this).get(MpinViewModel.class);

        // Bind ViewModel to the layout
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        // Handle the submit button click
        binding.submitMpinButton.setOnClickListener(v -> submitMpin());

        return binding.getRoot();
    }

    private void submitMpin() {
        // Get entered MPIN
        String enteredMpin = binding.mpinEditText1.getText().toString().trim()
                + binding.mpinEditText2.getText().toString().trim()
                + binding.mpinEditText3.getText().toString().trim()
                + binding.mpinEditText4.getText().toString().trim();

        // Get re-entered MPIN
        String reenteredMpin = binding.mpinReenterEditText1.getText().toString().trim()
                + binding.mpinReenterEditText2.getText().toString().trim()
                + binding.mpinReenterEditText3.getText().toString().trim()
                + binding.mpinReenterEditText4.getText().toString().trim();

        // Check if both MPINs match
        if (enteredMpin.equals(reenteredMpin)) {
            Toast.makeText(getContext(), "MPIN is set successfully.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "MPINs do not match. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
