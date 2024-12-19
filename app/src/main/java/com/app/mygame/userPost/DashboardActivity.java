package com.app.mygame.userPost;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.app.mygame.R;
import com.app.mygame.userPost.fragment.HomeFragment;
import com.app.mygame.userPost.fragment.MenuFragment;
import com.app.mygame.userPost.fragment.PlanFragment;
import com.app.mygame.userPost.fragment.GameFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Set default fragment as HomeFragment
        loadFragment(new HomeFragment());

        // Set up the BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Handle Bottom Navigation item selection using OnItemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                // Determine the selected fragment based on item clicked
                switch (item.getItemId()) {
                    case R.id.home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.menu:
                        selectedFragment = new MenuFragment();
                        break;
                    case R.id.plan:
                        selectedFragment = new PlanFragment();
                        break;
                    case R.id.game:
                        selectedFragment = new GameFragment();
                        break;
                    default:
                        break;
                }

                // If a valid fragment is selected, load it
                if (selectedFragment != null) {
                    loadFragment(selectedFragment);
                }
                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainContent, fragment);  // R.id.mainContent is the container for fragments
        transaction.addToBackStack(null); // Allow back navigation
        transaction.commit();
    }
}
