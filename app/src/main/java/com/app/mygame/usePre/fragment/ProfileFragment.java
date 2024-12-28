package com.app.mygame.usePre.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.mygame.databinding.FragmentProfileBinding;
import com.app.mygame.usePre.responseVo.ProfileResponse;
import com.app.mygame.utils.StoreConfig;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private boolean isEditing = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Load Profile Data from SharedPreferences
        ProfileResponse.Data profileData = StoreConfig.getObjectConfig(requireContext(), "UserProfilePrefs", "profile_data", ProfileResponse.Data.class);

        if (profileData != null) {
            populateProfileData(profileData);
        } else {
            Toast.makeText(requireContext(), "Profile data not available", Toast.LENGTH_SHORT).show();
        }

        // Edit Button Action
        binding.btnEdit.setOnClickListener(v -> toggleEditMode());

        // Submit Button Action
        binding.btnSubmit.setOnClickListener(v -> submitProfileChanges());
    }

    private void populateProfileData(ProfileResponse.Data data) {
        binding.tvUserName.setText(data.userName);
        binding.tvUserLastName.setText(data.userLastName);
        binding.tvUserAddress.setText(data.userAddress);
        binding.tvUserPhone.setText(data.userPhone);
        binding.tvUserEmail.setText(data.userEmail);
        binding.tvUserGender.setText(data.userGender);
        binding.tvUserAccountNo.setText(data.userAccountNo);
        binding.tvBankName.setText(data.bankName);
        binding.tvIfscCode.setText(data.ifscCode);
    }

    private void toggleEditMode() {
        isEditing = !isEditing;

        binding.btnSubmit.setVisibility(isEditing ? View.VISIBLE : View.GONE);
        binding.btnEdit.setText(isEditing ? "Cancel" : "Edit");

        // Enable/Disable Editable Fields
        binding.etUserName.setEnabled(isEditing);
        binding.etUserLastName.setEnabled(isEditing);
        binding.etUserAddress.setEnabled(isEditing);
        binding.etUserGender.setEnabled(isEditing);
        binding.etBankName.setEnabled(isEditing);
        binding.etIfscCode.setEnabled(isEditing);

        // Swap TextViews with EditTexts in Edit Mode
        if (isEditing) {
            binding.etUserName.setVisibility(View.VISIBLE);
            binding.tvUserName.setVisibility(View.GONE);
            binding.etUserName.setText(binding.tvUserName.getText());

            binding.etUserLastName.setVisibility(View.VISIBLE);
            binding.tvUserLastName.setVisibility(View.GONE);
            binding.etUserLastName.setText(binding.tvUserLastName.getText());

            binding.etUserAddress.setVisibility(View.VISIBLE);
            binding.tvUserAddress.setVisibility(View.GONE);
            binding.etUserAddress.setText(binding.tvUserAddress.getText());

            binding.etUserGender.setVisibility(View.VISIBLE);
            binding.tvUserGender.setVisibility(View.GONE);
            binding.etUserGender.setText(binding.tvUserGender.getText());

            binding.etBankName.setVisibility(View.VISIBLE);
            binding.tvBankName.setVisibility(View.GONE);
            binding.etBankName.setText(binding.tvBankName.getText());

            binding.etIfscCode.setVisibility(View.VISIBLE);
            binding.tvIfscCode.setVisibility(View.GONE);
            binding.etIfscCode.setText(binding.tvIfscCode.getText());
        } else {
            // Use constructor to update the profile with edited data
            ProfileResponse.Data updatedData = new ProfileResponse.Data(
                    0, // userId
                    binding.etUserName.getText().toString(),
                    binding.etUserLastName.getText().toString(),
                    binding.etUserAddress.getText().toString(),
                    null, // userAdhar
                    0, // userAge
                    binding.etUserPhone.getText().toString(),
                    binding.etUserGender.getText().toString(),
                    binding.etUserAccountNo.getText().toString(),
                    false, // active
                    binding.etUserEmail.getText().toString(),
                    null, // userPan
                    binding.etIfscCode.getText().toString(),
                    binding.etBankName.getText().toString(),
                    null, // bankAccountNumber
                    "",
                    0
            );

            // Populate the TextViews with updated data
            populateProfileData(updatedData);

            binding.tvUserName.setVisibility(View.VISIBLE);
            binding.etUserName.setVisibility(View.GONE);

            binding.tvUserLastName.setVisibility(View.VISIBLE);
            binding.etUserLastName.setVisibility(View.GONE);

            binding.tvUserAddress.setVisibility(View.VISIBLE);
            binding.etUserAddress.setVisibility(View.GONE);

            binding.tvUserGender.setVisibility(View.VISIBLE);
            binding.etUserGender.setVisibility(View.GONE);

            binding.tvBankName.setVisibility(View.VISIBLE);
            binding.etBankName.setVisibility(View.GONE);

            binding.tvIfscCode.setVisibility(View.VISIBLE);
            binding.etIfscCode.setVisibility(View.GONE);
        }
    }




    private void submitProfileChanges() {
        if (TextUtils.isEmpty(binding.etUserName.getText()) || TextUtils.isEmpty(binding.etUserAddress.getText())) {
            Toast.makeText(requireContext(), "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        ProfileResponse.Data updatedData = new ProfileResponse.Data(
                0,  // userId, you can update this accordingly
                binding.etUserName.getText().toString(),
                binding.etUserLastName.getText().toString(),
                binding.etUserAddress.getText().toString(),
                null, // userAdhar (set if needed)
                0, // userAge (set if needed)
                binding.etUserPhone.getText().toString(),  // Ensure this is populated
                binding.etUserGender.getText().toString(),
                binding.etUserAccountNo.getText().toString(),  // Ensure this is populated if needed
                false,  // active (set accordingly)
                binding.etUserEmail.getText().toString(),  // Ensure this is populated
                null,  // userPan (set if needed)
                binding.etIfscCode.getText().toString(),
                binding.etBankName.getText().toString(),
                null, // bankAccountNumber (set if needed)
                "",
                0
        );

        StoreConfig.storeObjectConfig(requireContext(), "UserProfilePrefs", "profile_data", updatedData);
        Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();

        toggleEditMode();
    }
}
