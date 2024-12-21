package com.app.mygame.userPost.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import com.app.mygame.R;
import com.app.mygame.userPost.DashboardActivity;

public class GameFragment extends Fragment {

    public GameFragment() {
        super(R.layout.fragment_game); // Reference to your Game layout XML
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for the Game fragment
        return inflater.inflate(R.layout.fragment_game, container, false);
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