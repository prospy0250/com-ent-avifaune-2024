package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class PublicActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Check for which fragment to load
        String targetFragment=getIntent().getStringExtra("TARGET_FRAGMENT");
        assert targetFragment != null;
        switch (targetFragment) {
            case "LearningPage":
                loadFragment(new LearningPage());
                bottomNavigationView.setSelectedItemId(R.id.navigation_apprendre);
                break;
            case "StatsPage":
                loadFragment(new StatsPage());
                bottomNavigationView.setSelectedItemId(R.id.navigation_statistiques);
                break;
            case "SettingsPage":
                loadFragment(new SettingsPage());
                bottomNavigationView.setSelectedItemId(R.id.navigation_parametres);
                break;
            case "PhotoPage":
                loadFragment(new PhotoPage());
                bottomNavigationView.setSelectedItemId(R.id.navigation_documenter);
                break;
        }

        // Set up navigation item selection
        bottomNavigationView.setOnNavigationItemSelectedListener(item ->

        {
            Fragment selectedFragment;
            if (item.getItemId() == R.id.navigation_apprendre) {
                selectedFragment = new LearningPage();
            } else if (item.getItemId() == R.id.navigation_statistiques) {
                selectedFragment = new StatsPage();
            } else if (item.getItemId() == R.id.navigation_parametres) {
                selectedFragment = new SettingsPage();
            } else {
                selectedFragment = new PhotoPage();
            }
            loadFragment(selectedFragment);
            return true;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}

