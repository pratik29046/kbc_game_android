    package com.app.mygame.userPost.fragment;

    import android.os.Bundle;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;

    import androidx.activity.OnBackPressedCallback;
    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.fragment.app.Fragment;
    import androidx.recyclerview.widget.LinearLayoutManager;

    import com.app.mygame.R;
    import com.app.mygame.databinding.FragmentPlanBinding;
    import com.app.mygame.userPost.DashboardActivity;
    import com.app.mygame.userPost.adapter.PlanAdapter;
    import com.app.mygame.userPost.model.Plan;

    import java.util.ArrayList;
    import java.util.List;

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

            // Set up RecyclerView
            binding.planRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            // Dummy data for testing
            List<Plan> plans = new ArrayList<>();
            plans.add(new Plan(1, "211220242000", "It is a platinum plan", "www.platinum.com", true,
                    "Platinum Plan", 2000, 2500, 30.0, "2024-12-21T12:14:40.470742", null));
            plans.add(new Plan(2, "211220242001", "It is a gold plan", "www.gold.com", true,
                    "Gold Plan", 1500, 1800, 20.0, "2024-12-21T12:15:40.470742", null));
            plans.add(new Plan(1, "211220242000", "It is a platinum plan", "www.platinum.com", true,
                    "Platinum Plan", 2000, 2500, 30.0, "2024-12-21T12:14:40.470742", null));
            plans.add(new Plan(2, "211220242001", "It is a gold plan", "www.gold.com", true,
                    "Gold Plan", 1500, 1800, 20.0, "2024-12-21T12:15:40.470742", null));
            plans.add(new Plan(1, "211220242000", "It is a platinum plan", "www.platinum.com", true,
                    "Platinum Plan", 2000, 2500, 30.0, "2024-12-21T12:14:40.470742", null));
            plans.add(new Plan(2, "211220242001", "It is a gold plan", "www.gold.com", true,
                    "Gold Plan", 1500, 1800, 20.0, "2024-12-21T12:15:40.470742", null));
            plans.add(new Plan(1, "211220242000", "It is a platinum plan", "www.platinum.com", true,
                    "Platinum Plan", 2000, 2500, 30.0, "2024-12-21T12:14:40.470742", null));
            plans.add(new Plan(2, "211220242001", "It is a gold plan", "www.gold.com", true,
                    "Gold Plan", 1500, 1800, 20.0, "2024-12-21T12:15:40.470742", null));

            PlanAdapter adapter = new PlanAdapter(plans, getContext());
            binding.planRecyclerView.setAdapter(adapter);

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
    }
