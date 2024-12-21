package com.app.mygame.userPost.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import com.app.mygame.R;
import com.app.mygame.userPost.DashboardActivity;

public class MenuFragment extends Fragment {

    public MenuFragment() {
        super(R.layout.fragment_menu); // Reference to your Menu layout XML
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for the Menu fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Add the back press callback to navigate to HomeFragment
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Call the loadFragment method from DashboardActivity
                if (getActivity() instanceof DashboardActivity) {
                    ((DashboardActivity) getActivity()).loadFragment(new HomeFragment()); // Navigate to HomeFragment
                }
            }
        });
    }

}
