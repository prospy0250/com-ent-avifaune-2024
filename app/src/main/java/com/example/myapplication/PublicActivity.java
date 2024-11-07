package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PublicActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // YOU NEED TO DEFINE THE NEW HOME PAGE AND MAKE IT SHOW BY DEFAULT


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
            } else if (item.getItemId() == R.id.navigation_documenter) {
                selectedFragment = new PhotoPage();
            } else {
                selectedFragment = new HomePage();
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

