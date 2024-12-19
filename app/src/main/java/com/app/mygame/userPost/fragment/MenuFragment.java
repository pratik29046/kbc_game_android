package com.app.mygame.userPost.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.app.mygame.R;

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
}
