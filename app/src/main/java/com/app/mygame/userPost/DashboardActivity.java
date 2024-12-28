package com.app.mygame.userPost;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.app.mygame.R;
import com.app.mygame.usePre.fragment.EnterMobileFragment;
import com.app.mygame.usePre.fragment.ProfileFragment;
import com.app.mygame.userPost.fragment.HomeFragment;
import com.app.mygame.userPost.fragment.MenuFragment;
import com.app.mygame.userPost.fragment.PlanFragment;
import com.app.mygame.userPost.fragment.GameFragment;
import com.app.mygame.utils.PopupUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.app.mygame.databinding.ActivityDashboardBinding; // Import ViewBinding

public class DashboardActivity extends AppCompatActivity {

    private ActivityDashboardBinding binding;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize ViewBinding
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize the DrawerLayout and NavigationView
        drawerLayout = binding.drawerLayout;
        navigationView = binding.navigationView;

        // Setup the ActionBarDrawerToggle
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.open_drawer, R.string.close_drawer
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Set the default fragment
        loadFragment(new HomeFragment());

        // Set up the BottomNavigationView
        binding.bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
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

                if (selectedFragment != null) {
                    loadFragment(selectedFragment);
                }
                return true;
            }
        });

        // Set up the Drawer Menu Icon click listener
        binding.drawerMenuIcon.setOnClickListener(v -> {
            drawerLayout.openDrawer(Gravity.LEFT);
        });

        // Handle drawer item selection
        navigationView.setNavigationItemSelectedListener(item -> {
            // Handle item clicks here
            switch (item.getItemId()) {
                case R.id.nav_profile:
                    ProfileFragment profileFragment = new  ProfileFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.mainContent, profileFragment)
                            .addToBackStack(null)
                            .commit();
                    break;
                case R.id.nav_settings:
                    // Handle settings click
                    break;
                case R.id.nav_logout:
                    PopupUtils.showNormalPopup(DashboardActivity.this, "Logout", "Are you sure you want to logout?", (dialog, which) -> {
                        finishAffinity();  // Perform logout action
                    });
                    break;
                default:
                    break;
            }
            drawerLayout.closeDrawers();  // Close the drawer after selection
            return true;
        });
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainContent, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        // Check if the drawer is open
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.mainContent);

            // If the current fragment is GameFragment, PlanFragment, etc., navigate to HomeFragment
            if (currentFragment instanceof GameFragment || currentFragment instanceof PlanFragment) {
                loadFragment(new HomeFragment()); // Load the HomeFragment instead of finishing the activity

                // Manually update the BottomNavigationView to select the Home tab
                binding.bottomNavigationView.setSelectedItemId(R.id.home); // Select the 'Home' item
            } else {
                super.onBackPressed(); // Default back press behavior
            }
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Avoid memory leaks by nullifying the binding
        binding = null;
    }
}
