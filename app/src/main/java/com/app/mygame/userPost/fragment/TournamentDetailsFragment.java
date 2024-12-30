package com.app.mygame.userPost.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.app.mygame.network.ApiService;
import com.app.mygame.network.RetrofitClient;
import com.app.mygame.userPost.responseVo.AllTournamentsResponse;
import com.app.mygame.userPost.responseVo.TournamentsRegisterResponse;
import com.app.mygame.utils.DateUtility;
import com.app.mygame.utils.PopupUtils;
import com.bumptech.glide.Glide;
import com.app.mygame.databinding.FragmentTournamentDetailsBinding;

import org.json.JSONObject;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TournamentDetailsFragment extends Fragment {
    private static final String ARG_TOURNAMENT = "tournament";

    private AllTournamentsResponse.Data tournament;
    private FragmentTournamentDetailsBinding binding;

    boolean registerStatus=false;
    // Factory method to create a new instance of this fragment
    public static TournamentDetailsFragment newInstance(AllTournamentsResponse.Data tournament) {
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
            tournament = (AllTournamentsResponse.Data) getArguments().getSerializable(ARG_TOURNAMENT);
        }
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Initialize view binding
        binding = FragmentTournamentDetailsBinding.inflate(inflater, container, false);

        // Populate views with tournament data
        if (tournament != null) {
            binding.titleText.setText("Title: " + tournament.title);
            binding.descriptionText.setText("Description: " +tournament.description);
            String formattedDate = DateUtility.formatDate(tournament.tournamentStartDateTime);
            binding.dateText.setText("Date: "+formattedDate);
            binding.prizeText.setText(String.valueOf("Price: "+tournament.winningPrice));

            // Display maxParticipants, totalRegistered, and tableType
            binding.maxParticipantsText.setText("Max Participants: " + tournament.maxParticipants);
            binding.totalRegisteredText.setText("Total Registered: " + tournament.totalRegistered);
            binding.tableTypeText.setText("Table Type: " + tournament.tableType);

            Glide.with(requireContext())
                    .load(tournament.imageUrl)
                    .centerCrop()
                    .into(binding.tournamentImage);

            // Handle registration button based on 'registered' flag
            if (tournament.registered) {
                binding.nextButton.setText("DeRegistered");
            } else {
                binding.nextButton.setEnabled(true);
            }

            binding.nextButton.setOnClickListener(v -> onRegisterClicked());
        }
        return binding.getRoot();
    }

    private void onRegisterClicked() {
        ApiService apiService = RetrofitClient.getClient(getContext()).create(ApiService.class);
        if (tournament != null && !tournament.registered && !registerStatus) {
            PopupUtils.showNormalPopup(getContext(), "Register", "Are you sure you want to register to tournament?", (dialog, which) -> {
                callToTournamentsRegister(apiService, tournament.tournamentId);
            });
        }else{
            PopupUtils.showNormalPopup(getContext(), "De-Register", "Are you sure you want to De-register to tournament?", (dialog, which) -> {
                callToTournamentsDeRegister(apiService, tournament.tournamentId);
            });
        }
    }
    private void callToTournamentsRegister(ApiService apiService,long id){
        Call<TournamentsRegisterResponse>  tournamentsRegisterResponseCall = apiService.tournamentsRegister(id);
        tournamentsRegisterResponseCall.enqueue(new Callback<TournamentsRegisterResponse>() {
            @Override
            public void onResponse(Call<TournamentsRegisterResponse> call, Response<TournamentsRegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TournamentsRegisterResponse tournamentsRegisterResponse = response.body();
                    if ("success".equalsIgnoreCase(tournamentsRegisterResponse.status)) {
                        PopupUtils.showSuccessPopup(getContext(),"Success", tournamentsRegisterResponse.message,"Done",(dialog, which) -> {
                            dialog.dismiss();
                        });
                        binding.nextButton.setText("You are already registered");
                        registerStatus=true;
                    } else {
                        Toast.makeText(requireContext(), tournamentsRegisterResponse.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String errorMessage = "Unknown error";
                    if (response.errorBody() != null) {
                        try {
                            String errorBody = response.errorBody().string();
                            JSONObject errorJson = new JSONObject(errorBody);
                            Log.e("Error", "Error body: " + errorBody);
                            errorMessage = errorJson.optString("message", "Unknown error");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TournamentsRegisterResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "Network error. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void callToTournamentsDeRegister(ApiService apiService,long id){
        Call<TournamentsRegisterResponse>  tournamentsRegisterResponseCall = apiService.tournamentsDeRegister(id);
        tournamentsRegisterResponseCall.enqueue(new Callback<TournamentsRegisterResponse>() {
            @Override
            public void onResponse(Call<TournamentsRegisterResponse> call, Response<TournamentsRegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TournamentsRegisterResponse tournamentsRegisterResponse = response.body();
                    if ("success".equalsIgnoreCase(tournamentsRegisterResponse.status)) {
                        PopupUtils.showSuccessPopup(getContext(),"Success", tournamentsRegisterResponse.message,"Done",(dialog, which) -> {
                            dialog.dismiss();
                        });
                        binding.nextButton.setText("Register");
                        registerStatus=false;
                    } else {
                        Toast.makeText(requireContext(), tournamentsRegisterResponse.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String errorMessage = "Unknown error";
                    if (response.errorBody() != null) {
                        try {
                            String errorBody = response.errorBody().string();
                            JSONObject errorJson = new JSONObject(errorBody);
                            Log.e("Error", "Error body: " + errorBody);
                            errorMessage = errorJson.optString("message", "Unknown error");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TournamentsRegisterResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "Network error. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
