package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * PublicActivity represents an activity that hosts different fragments and provides navigation
 * through a BottomNavigationView. It loads fragments based on the provided intent extra
 * or the user's navigation choice.
 */
public class PublicActivity extends AppCompatActivity {

    /**
     * Called when the activity is created. This method initializes the UI, sets up the
     * BottomNavigationView, and loads the correct fragment based on the "TARGET_FRAGMENT"
     * provided in the intent or the user's navigation selection.
     *
     * @param savedInstanceState A bundle containing the saved state of the activity,
     *                           or null if there was no previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public);

        // Initialize the BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Get the target fragment name from the intent
        String targetFragment = getIntent().getStringExtra("TARGET_FRAGMENT");

        // Assert the fragment name is not null, then load the appropriate fragment
        assert targetFragment != null;
        switch (targetFragment) {
            case "LearningPage":
                loadFragment(new LearningPage()); // Load the LearningPage fragment
                bottomNavigationView.setSelectedItemId(R.id.navigation_apprendre); // Select the appropriate bottom nav item
                break;
            case "StatsPage":
                loadFragment(new StatsPage()); // Load the StatsPage fragment
                bottomNavigationView.setSelectedItemId(R.id.navigation_statistiques);
                break;
            case "SettingsPage":
                // Launch a new instance of PublicActivity to load the SettingsPage fragment
                Intent intent = new Intent(PublicActivity.this, PublicActivity.class);
                intent.putExtra("TARGET_FRAGMENT", "SettingsPage");
                loadFragment(new SettingsPage());
                bottomNavigationView.setSelectedItemId(R.id.navigation_parametres);
                break;
            case "PhotoPage":
                loadFragment(new PhotoPage()); // Load the PhotoPage fragment
                bottomNavigationView.setSelectedItemId(R.id.navigation_identifier);
                break;
        }

        // Set up the BottomNavigationView item selection listener
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment;
            // Check the selected navigation item and load the corresponding fragment
            if (item.getItemId() == R.id.navigation_apprendre) {
                selectedFragment = new LearningPage();
            } else if (item.getItemId() == R.id.navigation_statistiques) {
                selectedFragment = new StatsPage();
            } else if (item.getItemId() == R.id.navigation_parametres) {
                selectedFragment = new SettingsPage();
            } else if (item.getItemId() == R.id.navigation_identifier) {
                selectedFragment = new PhotoPage();
            } else {
                // If none of the known items are selected, navigate back to HomeActivity
                selectedFragment = null;
                Intent intent = new Intent(PublicActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
            loadFragment(selectedFragment); // Load the selected fragment
            return true;
        });
    }

    /**
     * Loads the specified fragment into the fragment container.
     *
     * @param fragment The fragment to load.
     */
    private void loadFragment(Fragment fragment) {
        // Begin a fragment transaction to replace the current fragment with the new one
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment) // Replace the current fragment
                .commit(); // Commit the transaction
    }
}
