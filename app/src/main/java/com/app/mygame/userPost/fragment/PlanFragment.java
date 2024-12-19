package com.app.mygame.userPost.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.app.mygame.R;

public class PlanFragment extends Fragment {

    public PlanFragment() {
        super(R.layout.fragment_plan); // Reference to your Plan layout XML
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for the Plan fragment
        return inflater.inflate(R.layout.fragment_plan, container, false);
    }
}
