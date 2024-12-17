package com.example.myapplication;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class StatsPage extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats_page, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Set up the web view to display a few statistics
        WebView sheetsView = view.findViewById(R.id.sheets_view);

        sheetsView.getSettings().setJavaScriptEnabled(true);
        sheetsView.setInitialScale(250);
        sheetsView.loadUrl("https://docs.google.com/spreadsheets/d/e/2PACX-1vTic7i0iLCZzMpbmMKfsEzW4F6aYTBPoARxt_kkTpvWpWoLoUstdfN1F5kp0sKMCZu2IYhB7XIlN_kr/pubhtml?gid=1133598439&single=true");

        Button exportButton = view.findViewById(R.id.export_button);

        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse("https://docs.google.com/spreadsheets/d/e/2PACX-1vTic7i0iLCZzMpbmMKfsEzW4F6aYTBPoARxt_kkTpvWpWoLoUstdfN1F5kp0sKMCZu2IYhB7XIlN_kr/pub?gid=1600309431&single=true&output=csv");
                String fileName="formReponses.csv";
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setTitle(fileName);
                request.setDescription("Downloading " + fileName);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                downloadManager.enqueue(request);
            }
        });
    }
}