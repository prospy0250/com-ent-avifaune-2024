package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onResume() {
        super.onResume();
        String tutorialKey = "FIRST_TIME_TUTORIAL";
        boolean preferences = getIntent().getBooleanExtra("PREFERENCES",true);
        if (!preferences) getPreferences(MODE_PRIVATE).edit().putBoolean(tutorialKey, false).apply();
        boolean firstTime = getPreferences(MODE_PRIVATE).getBoolean(tutorialKey, true);
        if (firstTime) {
            runTutorial();
        }
    }

    private void runTutorial() {
        Intent intent = new Intent(HomeActivity.this, TutorialActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /*
        On click listener for the Learning Button to open the "Apprendre" page by clicking
        on the "Apprendre" button on the home page
         */
        Button learningButton=(Button)findViewById(R.id.learning_button);
        View.OnClickListener listenerLearningButton=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToPage("LearningPage");
            }
        };
        learningButton.setOnClickListener(listenerLearningButton);

        /*
        On click listener for the Stats Button to open the "Statistiques" page by clicking
        on the "Statistiques" button on the home page
         */
        Button statsButton=(Button)findViewById(R.id.stats_button);
        View.OnClickListener listenerStatsButton=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToPage("StatsPage");
            }
        };
        statsButton.setOnClickListener(listenerStatsButton);

        /*
        On click listener for the Settings Button to open the "Paramètres" page by clicking
        on the "Paramètres" button on the home page
         */
        Button settingsButton=(Button)findViewById(R.id.settings_button);
        View.OnClickListener listenerSettingsButton=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToPage("SettingsPage");
            }
        };
        settingsButton.setOnClickListener(listenerSettingsButton);

        /*
        On click listener for the Photo Button to open the "Documenter" page by clicking
        on the "Documenter" button on the home page
         */
        Button photoButton=(Button)findViewById(R.id.photo_button);
        View.OnClickListener listenerPhotoButton=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToPage("PhotoPage");
            }
        };
        photoButton.setOnClickListener(listenerPhotoButton);
    }

    /**
     * A method to go to the learning page
     * @param pageToGo a string to signify to which page we want to go
     */
    public void goToPage(String pageToGo) {
        Intent intent = new Intent(HomeActivity.this, PublicActivity.class);
        intent.putExtra("TARGET_FRAGMENT", pageToGo);
        startActivity(intent);
        finish();
    }
}
