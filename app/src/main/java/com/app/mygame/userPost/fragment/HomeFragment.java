package com.app.mygame.userPost.fragment;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.app.mygame.R;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        super(R.layout.fragment_home); // Ensure you have a fragment_home.xml layout
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}
