package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class StatsPage extends Fragment {
    private static final String VALID_USERNAME = "user";
    private static final String VALID_PASSWORD = "password";

    private EditText usernameField;
    private EditText passwordField;
    private Button loginButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats_page, container, false);

        usernameField = view.findViewById(R.id.usernameField);
        passwordField = view.findViewById(R.id.passwordField);
        loginButton = view.findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> authenticateUser());

        return view;
    }

    private void authenticateUser() {
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Please enter both username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (username.equals(VALID_USERNAME) && password.equals(VALID_PASSWORD)) {
            // Navigate to the stats content (replace fragment or show stats content here)
            Toast.makeText(getContext(), "Login successful!", Toast.LENGTH_SHORT).show();
            showStatsContent();
        } else {
            Toast.makeText(getContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }

    private void showStatsContent() {
        // Replace the login layout with the stats content once authenticated
        // For example, you could replace this fragment or just update the view
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new Stats())
                .commit();
    }
}