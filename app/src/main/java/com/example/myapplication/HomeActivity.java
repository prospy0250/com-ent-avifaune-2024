package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.myapplication.tutorials.TutorialActivity;

/**
 * HomeActivity represents the main screen of the application where users can access
 * various sections of the app, such as Learning, Stats, Settings, and Photo. The activity
 * also handles displaying tutorials for first-time users or specific navigation flows.
 */
public class HomeActivity extends AppCompatActivity {

    /**
     * Called when the activity is resumed. It checks if a tutorial should be shown based
     * on the user's previous interactions or the source of the current intent (e.g., from the photo page).
     * If the tutorial is needed, it is triggered.
     */
    @Override
    protected void onResume() {
        super.onResume();

        // If tutorial ran from photo page, show the tutorial
        boolean fromPhotoPage = getIntent().getBooleanExtra("FROM_PHOTO_PAGE", false);
        if (fromPhotoPage) runTutorial(true);

        String tutorialKey = "FIRST_TIME_TUTORIAL";
        boolean preferences = getIntent().getBooleanExtra("PREFERENCES", true);

        // If preferences is false, skip the tutorial on the first time
        if (!preferences) getPreferences(MODE_PRIVATE).edit().putBoolean(tutorialKey, false).apply();

        boolean firstTime = getPreferences(MODE_PRIVATE).getBoolean(tutorialKey, true);

        // Run the tutorial if it's the first time using the app
        if (firstTime) {
            runTutorial(false);
        }
    }

    /**
     * Starts the TutorialActivity to display a tutorial.
     * It checks if the tutorial is to be shown due to navigating from the photo page.
     *
     * @param fromPhotoPage A boolean that specifies whether the tutorial is shown due to navigation from the photo page.
     */
    private void runTutorial(boolean fromPhotoPage) {
        Intent intent = new Intent(HomeActivity.this, TutorialActivity.class);
        intent.putExtra("FROM_PHOTO_PAGE", fromPhotoPage);
        startActivity(intent);
        finish();
    }

    /**
     * Called when the activity is created. This method sets up the theme based on user preferences,
     * initializes the UI, and sets click listeners for each button to navigate to different pages.
     *
     * @param savedInstanceState A bundle containing the saved state of the activity,
     *                           or null if there was no previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the saved theme mode from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("theme_prefs", MODE_PRIVATE);
        int themeMode = preferences.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_NO);
        AppCompatDelegate.setDefaultNightMode(themeMode);

        setContentView(R.layout.activity_home);

        // On click listener for the Learning Button to open the "Apprendre" page by clicking
        // on the "Apprendre" button on the home page
        Button learningButton = (Button) findViewById(R.id.learning_button);
        View.OnClickListener listenerLearningButton = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToPage("LearningPage");
            }
        };
        learningButton.setOnClickListener(listenerLearningButton);

        // On click listener for the Stats Button to open the "Statistiques" page by clicking
        // on the "Statistiques" button on the home page
        Button statsButton = (Button) findViewById(R.id.stats_button);
        View.OnClickListener listenerStatsButton = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToPage("StatsLoginPage");
            }
        };
        statsButton.setOnClickListener(listenerStatsButton);

        // On click listener for the Settings Button to open the "Paramètres" page by clicking
        // on the "Paramètres" button on the home page
        Button settingsButton = (Button) findViewById(R.id.settings_button);
        View.OnClickListener listenerSettingsButton = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToPage("SettingsPage");
            }
        };
        settingsButton.setOnClickListener(listenerSettingsButton);

        // On click listener for the Photo Button to open the "Documenter" page by clicking
        // on the "Documenter" button on the home page
        Button photoButton = (Button) findViewById(R.id.photo_button);
        View.OnClickListener listenerPhotoButton = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToPage("PhotoPage");
            }
        };
        photoButton.setOnClickListener(listenerPhotoButton);
    }

    /**
     * Navigates the user to the specified page by opening the PublicActivity
     * and passing the target fragment as an extra.
     *
     * @param pageToGo A string representing the target page (fragment) to navigate to.
     */
    public void goToPage(String pageToGo) {
        Intent intent = new Intent(HomeActivity.this, PublicActivity.class);
        intent.putExtra("TARGET_FRAGMENT", pageToGo);
        startActivity(intent);
        finish();
    }
}
