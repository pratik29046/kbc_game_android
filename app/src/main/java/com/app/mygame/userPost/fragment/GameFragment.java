package com.app.mygame.userPost.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.app.mygame.R;

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
}
