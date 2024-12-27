    package com.app.mygame.userPost.fragment;

    import android.os.Bundle;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Toast;

    import androidx.activity.OnBackPressedCallback;
    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.fragment.app.Fragment;
    import androidx.fragment.app.FragmentTransaction;
    import androidx.recyclerview.widget.LinearLayoutManager;

    import com.app.mygame.R;
    import com.app.mygame.databinding.FragmentPlanBinding;
    import com.app.mygame.network.ApiService;
    import com.app.mygame.network.RetrofitClient;
    import com.app.mygame.userPost.DashboardActivity;
    import com.app.mygame.userPost.adapter.PlanAdapter;
    import com.app.mygame.userPost.adapter.TournamentAdapter;
    import com.app.mygame.userPost.model.Plan;
    import com.app.mygame.userPost.responseVo.PlansResponse;
    import com.app.mygame.userPost.responseVo.PlansResponse;

    import org.json.JSONObject;

    import java.io.Serializable;
    import java.util.ArrayList;
    import java.util.List;

    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.Response;

    public class PlanFragment extends Fragment {

        private FragmentPlanBinding binding;

        public PlanFragment() {
            super(R.layout.fragment_plan);
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Initialize ViewBinding
            binding = FragmentPlanBinding.inflate(inflater, container, false);
            return binding.getRoot();
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            ApiService apiService = RetrofitClient.getClient(getContext()).create(ApiService.class);
            callToPlans(apiService);
            // Handle back press
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
            binding = null; // Avoid memory leaks
        }

        private void callToPlans(ApiService apiService){
            Call<PlansResponse> loginCall = apiService.plans();
            loginCall.enqueue(new Callback<PlansResponse>() {
                @Override
                public void onResponse(Call<PlansResponse> call, Response<PlansResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        PlansResponse plansResponse = response.body();
                        if ("success".equalsIgnoreCase(plansResponse.status)) {
                            binding.planRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            PlanAdapter adapter = new PlanAdapter(plansResponse.data, getContext());
                            binding.planRecyclerView.setAdapter(adapter);
                        } else {
                            Toast.makeText(requireContext(), plansResponse.message, Toast.LENGTH_SHORT).show();
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
                public void onFailure(Call<PlansResponse> call, Throwable t) {
                    Toast.makeText(requireContext(), "Network error. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
