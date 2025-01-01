package com.app.mygame.userPost.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import com.app.mygame.R;
import com.app.mygame.databinding.FragmentGameBinding;
import com.app.mygame.network.ApiService;
import com.app.mygame.network.RetrofitClient;
import com.app.mygame.userPost.DashboardActivity;
import com.app.mygame.userPost.requestVo.StartRoundRequest;
import com.app.mygame.userPost.requestVo.SubmitAnswerRequest;
import com.app.mygame.userPost.requestVo.TableWinnerRequest;
import com.app.mygame.userPost.responseVo.AllTournamentsResponse;
import com.app.mygame.userPost.responseVo.StartRoundResponse;
import com.app.mygame.userPost.responseVo.TableWinnerResponse;
import com.app.mygame.utils.PopupUtils;
import com.app.mygame.utils.QuestionEncryptor;
import com.app.mygame.utils.StoreConfig;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameFragment extends Fragment {

    private FragmentGameBinding binding;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 10000; // 30 seconds
    private String selectedOption; // Store the selected option (A, B, C, D)
    private String optionText ="";
    private String timeValue;
    private long questionIdValue;
    private long currentTableNumber;
    private int gameLevel,myGameLevel=1;
    private String correctAnswer;
    AllTournamentsResponse.Data firstTournament;

    public GameFragment() {
        super(R.layout.fragment_game);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGameBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        AllTournamentsResponse tournamentsResponse = StoreConfig.getObjectConfig(
                requireContext(), "TournamentsResponse", "TournamentsResponseKey", AllTournamentsResponse.class);

        if (tournamentsResponse != null && !tournamentsResponse.data.isEmpty()) {
            firstTournament = tournamentsResponse.data.get(0);
            setupTournamentDetails(firstTournament);

            binding.startGameButton.setOnClickListener(v -> {
                binding.gameDetailsCard.setVisibility(View.GONE);
                StartRoundRequest startRoundRequest = createStartRoundRequest(firstTournament);
                callToStartRoundApi(startRoundRequest);
            });
        } else {
            Toast.makeText(requireContext(), "No game data available", Toast.LENGTH_SHORT).show();
        }

        binding.submitButton.setOnClickListener(v -> handleAnswerSubmission());
        setupRadioButtons();
        return view;
    }

    private void setupTournamentDetails(AllTournamentsResponse.Data tournament) {
        Log.d("GameFragment", "Setting up tournament details: " + tournament.title);
        binding.gameTitle.setText(tournament.title);
        String formattedDate = new SimpleDateFormat("MMMM dd, yyyy").format(tournament.tournamentStartDateTime);
        binding.gameDate.setText(formattedDate);
        String formattedTime = new SimpleDateFormat("hh:mm a").format(tournament.tournamentStartDateTime);
        binding.gameTime.setText(formattedTime);
        binding.maxParticipants.setText("Max Participants: " + tournament.maxParticipants);
        binding.totalRegistered.setText("Total Registered: " + tournament.totalRegistered);
        binding.tableType.setText("Table Type: " + tournament.tableType);
        binding.winningPrice.setText("Winning Price: " + tournament.winningPrice);
        Glide.with(this)
                .load(tournament.imageUrl)
                .placeholder(R.drawable.baseline_account_circle_24)
                .into(binding.gameImage);
    }

    private StartRoundRequest createStartRoundRequest(AllTournamentsResponse.Data tournament) {
        gameLevel = tournament.gameLevel;
        Log.d("GameFragment", "Creating start round request for game level: " + gameLevel+ " myGameLevel "+myGameLevel);
        if (gameLevel >= myGameLevel) {
            int currentQuestionIdValue = 0;
            switch ((int) myGameLevel) {
                case 1:
                    currentQuestionIdValue = tournament.questionId1;
                    break;
                case 2:
                    currentQuestionIdValue = tournament.questionId2;
                    break;
                case 3:
                    currentQuestionIdValue = (int) tournament.questionId3;
                    break;
                case 4:
                    currentQuestionIdValue = (int) tournament.questionId4;
                    break;
                case 5:
                    currentQuestionIdValue = (int) tournament.questionId5;
                    break;
                case 6:
                    currentQuestionIdValue = (int) tournament.questionId6;
                    break;
                case 7:
                    currentQuestionIdValue = (int) tournament.questionId7;
                    break;
                case 8:
                    currentQuestionIdValue = (int) tournament.questionId8;
                    break;
                case 9:
                    currentQuestionIdValue = (int) tournament.questionId9;
                    break;
                case 10:
                    currentQuestionIdValue = (int) tournament.questionId10;
                    break;
                default:
                    Log.d("GameFragment", "No question ID found for game level: " + gameLevel);
                    Toast.makeText(getContext(), "No Question id found", Toast.LENGTH_SHORT).show();
                    break;
            }

            Log.d("GameFragment", "Current question ID value: " + currentQuestionIdValue);
            StartRoundRequest startRoundRequest = new StartRoundRequest();
            startRoundRequest.setUserName("test");
            startRoundRequest.setGameLevel(myGameLevel);
            startRoundRequest.setActive(true);
            startRoundRequest.setQuestionId(currentQuestionIdValue);
            startRoundRequest.setTournamentId(tournament.tournamentId);
            startRoundRequest.setTableType(tournament.tableType);
            return startRoundRequest;
        } else {
            Log.d("GameFragment", "Game over, level " + gameLevel + " is less than required level.");
            Toast.makeText(getContext(), "Game Over", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    private void setupRadioButtons() {
        Log.d("GameFragment", "Setting up radio buttons");

        binding.optionA.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Log.d("GameFragment", "Option A is selected");
                optionText = buttonView.getText().toString();
                selectedOption = "A";
                updateRadioButtons(buttonView, binding.optionB, binding.optionC, binding.optionD);
            }
        });

        binding.optionB.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Log.d("GameFragment", "Option B is selected");
                optionText = buttonView.getText().toString();
                selectedOption = "B";
                updateRadioButtons(buttonView, binding.optionA, binding.optionC, binding.optionD);
            }
        });

        binding.optionC.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Log.d("GameFragment", "Option C is selected");
                optionText = buttonView.getText().toString();
                selectedOption = "C";
                updateRadioButtons(buttonView, binding.optionA, binding.optionB, binding.optionD);
            }
        });

        binding.optionD.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Log.d("GameFragment", "Option D is selected");
                optionText = buttonView.getText().toString();
                selectedOption = "D";
                updateRadioButtons(buttonView, binding.optionA, binding.optionB, binding.optionC);
            }
        });
    }

    private void resetRadioButtons() {
        Log.d("GameFragment", "Resetting radio buttons for the new round");
        // Uncheck all the radio buttons for the new round
        binding.optionA.setChecked(false);
        binding.optionB.setChecked(false);
        binding.optionC.setChecked(false);
        binding.optionD.setChecked(false);

        // Optionally, reset the button styles as well if needed
        binding.optionA.setTextColor(getResources().getColor(R.color.default_text_color));
        binding.optionB.setTextColor(getResources().getColor(R.color.default_text_color));
        binding.optionC.setTextColor(getResources().getColor(R.color.default_text_color));
        binding.optionD.setTextColor(getResources().getColor(R.color.default_text_color));

        // Reset the background color of the radio buttons
        binding.optionA.setBackgroundResource(R.drawable.radio_selector);
        binding.optionB.setBackgroundResource(R.drawable.radio_selector);
        binding.optionC.setBackgroundResource(R.drawable.radio_selector);
        binding.optionD.setBackgroundResource(R.drawable.radio_selector);
    }

    private void updateRadioButtons(CompoundButton selectedButton, CompoundButton... otherButtons) {
        Log.d("GameFragment", "Updating selected option: " + selectedOption);

        selectedButton.setBackgroundResource(R.drawable.radio_selector);
        selectedButton.setTextColor(getResources().getColor(R.color.selected_text_color));

        for (CompoundButton button : otherButtons) {
            button.setBackgroundResource(R.drawable.radio_selector);
            button.setTextColor(getResources().getColor(R.color.default_text_color));
        }
    }

    private void displayQuestionAndStartTimer(StartRoundResponse response) {
        Log.d("GameFragment", "Displaying question and starting timer");
        questionIdValue = response.data.questionId;
        currentTableNumber =response.data.tableNo;
        QuestionEncryptor questionEncryptor = new QuestionEncryptor();
        String questionText = questionEncryptor.decrypt(response.data.question);
        binding.questionText.setText(questionText);
        binding.optionA.setText(questionEncryptor.decrypt(response.data.optionA));
        binding.optionB.setText(questionEncryptor.decrypt(response.data.optionB));
        binding.optionC.setText(questionEncryptor.decrypt(response.data.optionC));
        binding.optionD.setText(questionEncryptor.decrypt(response.data.optionD));
        correctAnswer = questionEncryptor.decrypt(response.data.correctAnswer);
        binding.questionShowRelativeLayout.setVisibility(View.VISIBLE);
        startCountdownTimer();
    }

    private void handleAnswerSubmission() {
        Log.d("GameFragment", "Handling answer submission");
        if (!selectedOption.isEmpty()) {
            timeValue = (String) binding.timerTextView.getText();
            Log.d("GameFragment", "Selected Option: " + selectedOption +
                    " Option Text: " + optionText +
                    " Time Value: " + timeValue +
                    " Question ID: " + questionIdValue);
            PopupUtils.showSuccessPopup(getContext(), "PLEASE WAIT", "You selected option has been locked, please wait 30 seconds, and time is " + timeValue + " seconds", "Done", (dialog, which) -> {
                dialog.dismiss();
            });
        } else {
            Toast.makeText(getContext(), "Please select an option before submitting.", Toast.LENGTH_SHORT).show();
        }
    }

    private void callToStartRoundApi(StartRoundRequest startRoundRequest) {
        Log.d("GameFragment", "Calling API to start round");
        ApiService apiService = RetrofitClient.getClient(getContext()).create(ApiService.class);
        Call<StartRoundResponse> startRoundCall = apiService.startRound(startRoundRequest);
        startRoundCall.enqueue(new Callback<StartRoundResponse>() {
            @Override
            public void onResponse(Call<StartRoundResponse> call, Response<StartRoundResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("GameFragment", "API response: " + response.body().status);
                    StartRoundResponse startRoundResponse = response.body();
                    if ("success".equalsIgnoreCase(startRoundResponse.status)) {
                        displayQuestionAndStartTimer(startRoundResponse);
                    } else {
                        Toast.makeText(getActivity(), startRoundResponse.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    handleErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<StartRoundResponse> call, Throwable t) {
                Log.d("GameFragment", "API call failed: " + t.getMessage());
                Toast.makeText(getContext(), "Network error. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callToSubmitRound(SubmitAnswerRequest submitAnswerRequest) {
        Log.d("GameFragment", "Calling API to start round");
        ApiService apiService = RetrofitClient.getClient(getContext()).create(ApiService.class);
        Call<StartRoundResponse> submitAnswerRound = apiService.submitAnswerRound(submitAnswerRequest);
        submitAnswerRound.enqueue(new Callback<StartRoundResponse>() {
            @Override
            public void onResponse(Call<StartRoundResponse> call, Response<StartRoundResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("GameFragment", "API response: " + response.body().status);
                    StartRoundResponse startRoundResponse = response.body();
                    if ("success".equalsIgnoreCase(startRoundResponse.status)) {
                        Toast.makeText(getContext(), startRoundResponse.message, Toast.LENGTH_SHORT).show();
                        TableWinnerRequest tableWinnerRequest = new TableWinnerRequest();
                        tableWinnerRequest.setTableNo((int) currentTableNumber);
                        tableWinnerRequest.setQuestionId((int) questionIdValue);
                        callToRoundTableWinner(tableWinnerRequest);
                    } else {
                        Toast.makeText(getActivity(), startRoundResponse.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    handleErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<StartRoundResponse> call, Throwable t) {
                Log.d("GameFragment", "API call failed: " + t.getMessage());
                Toast.makeText(getContext(), "Network error. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void callToRoundTableWinner(TableWinnerRequest tableWinnerRequest) {
        Log.d("GameFragment", "Calling API to start round");
        ApiService apiService = RetrofitClient.getClient(getContext()).create(ApiService.class);
        Call<TableWinnerResponse> checkTableRoundWinner = apiService.checkTableRoundWinner(tableWinnerRequest);
        checkTableRoundWinner.enqueue(new Callback<TableWinnerResponse>() {
            @Override
            public void onResponse(Call<TableWinnerResponse> call, Response<TableWinnerResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("GameFragment", "API response: " + response.body().status);
                    TableWinnerResponse tableWinnerResponse = response.body();
                    if ("success".equalsIgnoreCase(tableWinnerResponse.status)) {
                        Toast.makeText(getContext(), tableWinnerResponse.message, Toast.LENGTH_SHORT).show();
                        resetTimer();
                        resetRadioButtons();
                        myGameLevel = myGameLevel + 1;
                        StartRoundRequest startRoundRequest = createStartRoundRequest(firstTournament);
                        callToStartRoundApi(startRoundRequest);
                    } else {
                        Toast.makeText(getActivity(), tableWinnerResponse.message, Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<TableWinnerResponse> call, Throwable t) {
                Log.d("GameFragment", "API call failed: " + t.getMessage());
                Toast.makeText(getContext(), "Network error. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void startCountdownTimer() {
        Log.d("GameFragment", "Starting countdown timer");
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                int seconds = (int) (millisUntilFinished / 1000);
                if (binding != null) {
                    binding.timerTextView.setText(String.valueOf(seconds));
                }
            }

            @Override
            public void onFinish() {
                binding.timerTextView.setText("0");
                SubmitAnswerRequest submitAnswerRequest = new SubmitAnswerRequest();
                submitAnswerRequest.setSelectedAnswer(optionText);
                submitAnswerRequest.setAnswerTime(Integer.parseInt(timeValue));
                submitAnswerRequest.setQuestionId(questionIdValue);
                if (correctAnswer.equals(optionText)) {
                    callToSubmitRound(submitAnswerRequest);
                } else {
                    Log.d("GameFragment", "Answer is incorrect. Correct answer: " + correctAnswer);
                    PopupUtils.showSuccessPopup(getContext(), "SUBMIT", "Your answer is wrong, the correct answer is " + correctAnswer, "Done", (dialog, which) -> {
                        dialog.dismiss();
                    });
                }
            }
        }.start();
    }

    private void resetTimer() {
        Log.d("GameFragment", "Resetting timer for the new round");
        // Reset the timer to the initial time (e.g., 30 seconds)
        timeLeftInMillis = 10000; // 30 seconds, you can adjust this value if needed
        // Ensure the timer is reset before starting
        if (countDownTimer != null) {
            countDownTimer.cancel(); // Stop the previous timer if any
        }
    }

    private void handleErrorResponse(Response<StartRoundResponse> response) {
        String errorMessage = "Unknown error";
        if (response.errorBody() != null) {
            try {
                String errorBody = response.errorBody().string();
                JSONObject errorJson = new JSONObject(errorBody);
                errorMessage = errorJson.optString("message", "Unknown error");
            } catch (Exception e) {
                Log.e("GameFragment", "Error while parsing error response", e);
            }
        }
        Log.e("GameFragment", "API Error: " + errorMessage);
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("GameFragment", "Fragment resumed");

        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getActivity() instanceof DashboardActivity) {
                    ((DashboardActivity) getActivity()).loadFragment(new HomeFragment());
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("GameFragment", "Fragment destroyed");
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        binding = null;
    }
}
