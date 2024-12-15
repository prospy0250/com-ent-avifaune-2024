package com.example.myapplication;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.prefs.Preferences;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsPage extends Fragment {
    private Button nightModeButton;
    private Button downloadButton;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsPage.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsPage newInstance(String param1, String param2) {
        SettingsPage fragment = new SettingsPage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Load the saved theme mode
        SharedPreferences preferences = requireContext().getSharedPreferences("theme_prefs", requireContext().MODE_PRIVATE);
        int currentMode = preferences.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_NO);

        nightModeButton = view.findViewById(R.id.night_mode_button);
        nightModeButton.setOnClickListener(v -> {
            // Toggle between light and dark modes
            int newMode = (currentMode == AppCompatDelegate.MODE_NIGHT_NO)
                    ? AppCompatDelegate.MODE_NIGHT_YES
                    : AppCompatDelegate.MODE_NIGHT_NO;

            // Apply the theme globally
            AppCompatDelegate.setDefaultNightMode(newMode);

            // Save the preference
            saveThemeMode(newMode);
        });

        downloadButton = view.findViewById(R.id.download_tuto_button);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Copy the PDF file from assets to internal storage
                File pdfFile = copyPdfToInternalStorage();

                // Open the PDF if successfully copied
                if (pdfFile != null) {
                    openPdfFile(pdfFile);
                } else {
                    System.out.println("Failed to copy PDF file.");
                }
            }
        });
    }

    private void saveThemeMode(int mode) {
        SharedPreferences.Editor editor = requireContext()
                .getSharedPreferences("theme_prefs", requireContext().MODE_PRIVATE).edit();
        editor.putInt("theme_mode", mode);
        editor.apply();
    }

    private File copyPdfToInternalStorage() {
        File outputFile = new File(requireContext().getFilesDir(), "tuto.pdf");

        // Check if the file already exists to avoid unnecessary copying
        if (!outputFile.exists()) {
            try (InputStream inputStream = requireContext().getAssets().open("tuto.pdf");
                 FileOutputStream outputStream = new FileOutputStream(outputFile)) {

                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                System.out.println("PDF successfully copied to: " + outputFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        return outputFile;
    }

    private void openPdfFile(File file) {
        // Generate a URI using FileProvider
        Uri fileUri = FileProvider.getUriForFile(requireContext(), requireContext().getPackageName() + ".fileprovider", file);

        // Create an Intent to view the PDF
        Intent viewPdfIntent = new Intent(Intent.ACTION_VIEW);
        viewPdfIntent.setDataAndType(fileUri, "application/pdf");
        viewPdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        // Start the activity to open the PDF
        Intent chooser = Intent.createChooser(viewPdfIntent, "Open PDF with...");
        startActivity(chooser);
    }
}