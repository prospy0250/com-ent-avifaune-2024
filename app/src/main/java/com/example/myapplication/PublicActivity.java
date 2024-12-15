package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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
                Intent intent = new Intent(PublicActivity.this, PublicActivity.class);
                intent.putExtra("TARGET_FRAGMENT", "SettingsPage");
                loadFragment(new SettingsPage());
                bottomNavigationView.setSelectedItemId(R.id.navigation_parametres);
                break;
            case "PhotoPage":
                loadFragment(new PhotoPage());
                bottomNavigationView.setSelectedItemId(R.id.navigation_identifier);
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
            } else if (item.getItemId() == R.id.navigation_identifier) {
                selectedFragment = new PhotoPage();
            } else {
                selectedFragment=null;
                Intent intent = new Intent(PublicActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
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

