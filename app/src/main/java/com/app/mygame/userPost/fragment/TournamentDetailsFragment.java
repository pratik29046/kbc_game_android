package com.app.mygame.userPost.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.app.mygame.databinding.FragmentTournamentDetailsBinding;
import com.app.mygame.userPost.model.TournamentCard;

import java.io.Serializable;

public class TournamentDetailsFragment extends Fragment {
    private static final String ARG_TOURNAMENT = "tournament";

    private TournamentCard tournament;
    private FragmentTournamentDetailsBinding binding;

    // Factory method to create a new instance of this fragment
    public static TournamentDetailsFragment newInstance(TournamentCard tournament) {
        TournamentDetailsFragment fragment = new TournamentDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TOURNAMENT, (Serializable) tournament);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tournament = (TournamentCard) getArguments().getSerializable(ARG_TOURNAMENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Initialize view binding
        binding = FragmentTournamentDetailsBinding.inflate(inflater, container, false);

        // Populate views with tournament data
        if (tournament != null) {
            binding.titleText.setText(tournament.getTitle());
            binding.descriptionText.setText(tournament.getDescription());
            binding.dateText.setText(tournament.getDate());
            binding.prizeText.setText(tournament.getPrize());

            Glide.with(requireContext())
                    .load(tournament.getImageUrl())
                    .centerCrop()
                    .into(binding.tournamentImage);
        }

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Avoid memory leaks
        binding = null;
    }
}
