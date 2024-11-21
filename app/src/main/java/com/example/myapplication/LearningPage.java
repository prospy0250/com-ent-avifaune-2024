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

        // Listener pour la mouette
        Button buttonMouette = (Button) this.getView().findViewById(R.id.button_mouette);

        View.OnClickListener listenerButtonMouette=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOverlayMouette();
            }
        };

        buttonMouette.setOnClickListener(listenerButtonMouette);

        // Listener pour le pluvier
        Button buttonPluvier = (Button) this.getView().findViewById(R.id.button_pluvier);

        View.OnClickListener listenerButtonPluvier=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOverlayPluvier();
            }
        };

        buttonPluvier.setOnClickListener(listenerButtonPluvier);

        // Listener pour le cormoran
        Button buttonCormoran = (Button) this.getView().findViewById(R.id.button_cormoran);

        View.OnClickListener listenerButtonCormoran=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOverlayCormoran();
            }
        };

        buttonCormoran.setOnClickListener(listenerButtonCormoran);

        // Listener pour la foulque macroule
        Button buttonFoulque = (Button) this.getView().findViewById(R.id.button_foulque);

        View.OnClickListener listenerButtonFoulque=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOverlayFoulque();
            }
        };

        buttonFoulque.setOnClickListener(listenerButtonFoulque);

        // Listener pour le tadorne de Belon
        Button buttonTadorne = (Button) this.getView().findViewById(R.id.button_tadorne);

        View.OnClickListener listenerButtonTadorne=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOverlayTadorne();
            }
        };

        buttonTadorne.setOnClickListener(listenerButtonTadorne);

        // Listener pour le chevalier gambette
        Button buttonChevalier = (Button) this.getView().findViewById(R.id.button_chevalier);

        View.OnClickListener listenerButtonChevalier=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOverlayChevalier();
            }
        };

        buttonChevalier.setOnClickListener(listenerButtonChevalier);
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

    public void showOverlayMouette() {
        OverlayDialogFragmentMouette overlayDialogMouette = new OverlayDialogFragmentMouette();
        overlayDialogMouette.show(getParentFragmentManager(), "overlayDialogMouette");
    }

    public void showOverlayPluvier() {
        OverlayDialogFragmentPluvier overlayDialogPluvier = new OverlayDialogFragmentPluvier();
        overlayDialogPluvier.show(getParentFragmentManager(), "overlayDialogPluvier");
    }

    public void showOverlayCormoran() {
        OverlayDialogFragmentCormoran overlayDialogCormoran = new OverlayDialogFragmentCormoran();
        overlayDialogCormoran.show(getParentFragmentManager(), "overlayDialogCormoran");
    }

    public void showOverlayFoulque() {
        OverlayDialogFragmentFoulque overlayDialogFoulque = new OverlayDialogFragmentFoulque();
        overlayDialogFoulque.show(getParentFragmentManager(), "overlayDialogFoulque");
    }

    public void showOverlayTadorne() {
        OverlayDialogFragmentTadorne overlayDialogTadorne = new OverlayDialogFragmentTadorne();
        overlayDialogTadorne.show(getParentFragmentManager(), "overlayDialogTadorne");
    }

    public void showOverlayChevalier() {
        OverlayDialogFragmentChevalier overlayDialogChevalier = new OverlayDialogFragmentChevalier();
        overlayDialogChevalier.show(getParentFragmentManager(), "overlayDialogChevalier");
    }
}