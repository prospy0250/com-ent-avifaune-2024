package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class LearningPage extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_learning_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Listener pour le becasseau
        Button buttonBecasseau = (Button) this.getView().findViewById(R.id.button_becasseau);
        View.OnClickListener listenerButtonBecasseau=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOverlayBecasseau();
            }
        };

        buttonBecasseau.setOnClickListener(listenerButtonBecasseau);

        // Listener pour la bernache
        Button buttonBernache = (Button) this.getView().findViewById(R.id.button_bernache);

        View.OnClickListener listenerButtonBernache=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOverlayBernache();
            }
        };

        buttonBernache.setOnClickListener(listenerButtonBernache);

        // Listener pour le goeland
        Button buttonGoeland = (Button) this.getView().findViewById(R.id.button_goeland);

        View.OnClickListener listenerButtonGoeland=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOverlayGoeland();
            }
        };

        buttonGoeland.setOnClickListener(listenerButtonGoeland);
    }

    public void showOverlayBecasseau() {
        OverlayDialogFragmentBecasseau overlayDialogBecasseau = new OverlayDialogFragmentBecasseau();
        overlayDialogBecasseau.show(getParentFragmentManager(), "overlayDialogBecasseau");
    }

    public void showOverlayBernache() {
        OverlayDialogFragmentBernache overlayDialogBernache = new OverlayDialogFragmentBernache();
        overlayDialogBernache.show(getParentFragmentManager(), "overlayDialogBernache");
    }

    public void showOverlayGoeland() {
        OverlayDialogFragmentGoeland overlayDialogGoeland = new OverlayDialogFragmentGoeland();
        overlayDialogGoeland.show(getParentFragmentManager(), "overlayDialogGoeland");
    }
}