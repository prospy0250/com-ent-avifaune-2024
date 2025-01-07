package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

/**
 * The MainActivity class represents the main entry point of the application.
 * It is responsible for loading the theme preference and transitioning to the HomeActivity
 * after a brief delay.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Called when the activity is first created. This method is used to initialize
     * the app's settings, including theme preferences, and to set up a delayed transition
     * to the HomeActivity.
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

        // Apply the saved theme mode to the app
        AppCompatDelegate.setDefaultNightMode(themeMode);

        // Set the content view to the layout for this activity
        setContentView(R.layout.activity_main);

        // Delay time before transitioning to HomeActivity (in milliseconds)
        int delayMillis = 3000;

        // Create a new handler to delay the transition to HomeActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Create an intent to launch HomeActivity
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);

                // Start HomeActivity
                startActivity(intent);

                // Finish MainActivity so that it won't remain in the back stack
                finish();
            }
        }, delayMillis); // Delay the transition by the specified time
    }
}
