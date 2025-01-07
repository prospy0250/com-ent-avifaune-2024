package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myapplication.overlays.OverlayDialogFragmentBecasseau;
import com.example.myapplication.overlays.OverlayDialogFragmentBernache;
import com.example.myapplication.overlays.OverlayDialogFragmentChevalier;
import com.example.myapplication.overlays.OverlayDialogFragmentCormoran;
import com.example.myapplication.overlays.OverlayDialogFragmentFoulque;
import com.example.myapplication.overlays.OverlayDialogFragmentGoeland;
import com.example.myapplication.overlays.OverlayDialogFragmentMouette;
import com.example.myapplication.overlays.OverlayDialogFragmentPluvier;
import com.example.myapplication.overlays.OverlayDialogFragmentTadorne;

/**
 * This fragment represents a learning page where the user can interact with different buttons.
 * Each button triggers a dialog showing detailed information about a particular bird species.
 */
public class LearningPage extends Fragment {

    /**
     * Inflates the layout for this fragment.
     *
     * @param inflater The LayoutInflater object that can be used to inflate views in the fragment.
     * @param container The parent container that the fragment's UI should be attached to.
     * @param savedInstanceState A bundle containing the state of the fragment.
     * @return The root view of the fragment's layout.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_learning_page, container, false);
    }

    /**
     * Sets up the listeners for each button on the learning page. Each listener triggers the display
     * of a corresponding overlay dialog fragment for a bird species.
     *
     * @param view The root view of the fragment.
     * @param savedInstanceState A bundle containing the state of the fragment.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // Set up the listener for the Becasseau button
        Button buttonBecasseau = (Button) this.getView().findViewById(R.id.button_becasseau);
        View.OnClickListener listenerButtonBecasseau = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOverlayBecasseau();
            }
        };
        buttonBecasseau.setOnClickListener(listenerButtonBecasseau);

        // Set up the listener for the Bernache button
        Button buttonBernache = (Button) this.getView().findViewById(R.id.button_bernache);
        View.OnClickListener listenerButtonBernache = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOverlayBernache();
            }
        };
        buttonBernache.setOnClickListener(listenerButtonBernache);

        // Set up the listener for the Goeland button
        Button buttonGoeland = (Button) this.getView().findViewById(R.id.button_goeland);
        View.OnClickListener listenerButtonGoeland = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOverlayGoeland();
            }
        };
        buttonGoeland.setOnClickListener(listenerButtonGoeland);

        // Set up the listener for the Mouette button
        Button buttonMouette = (Button) this.getView().findViewById(R.id.button_mouette);
        View.OnClickListener listenerButtonMouette = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOverlayMouette();
            }
        };
        buttonMouette.setOnClickListener(listenerButtonMouette);

        // Set up the listener for the Pluvier button
        Button buttonPluvier = (Button) this.getView().findViewById(R.id.button_pluvier);
        View.OnClickListener listenerButtonPluvier = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOverlayPluvier();
            }
        };
        buttonPluvier.setOnClickListener(listenerButtonPluvier);

        // Set up the listener for the Cormoran button
        Button buttonCormoran = (Button) this.getView().findViewById(R.id.button_cormoran);
        View.OnClickListener listenerButtonCormoran = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOverlayCormoran();
            }
        };
        buttonCormoran.setOnClickListener(listenerButtonCormoran);

        // Set up the listener for the Foulque button
        Button buttonFoulque = (Button) this.getView().findViewById(R.id.button_foulque);
        View.OnClickListener listenerButtonFoulque = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOverlayFoulque();
            }
        };
        buttonFoulque.setOnClickListener(listenerButtonFoulque);

        // Set up the listener for the Tadorne button
        Button buttonTadorne = (Button) this.getView().findViewById(R.id.button_tadorne);
        View.OnClickListener listenerButtonTadorne = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOverlayTadorne();
            }
        };
        buttonTadorne.setOnClickListener(listenerButtonTadorne);

        // Set up the listener for the Chevalier button
        Button buttonChevalier = (Button) this.getView().findViewById(R.id.button_chevalier);
        View.OnClickListener listenerButtonChevalier = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOverlayChevalier();
            }
        };
        buttonChevalier.setOnClickListener(listenerButtonChevalier);
    }

    /**
     * Shows an overlay dialog for the Becasseau bird species.
     */
    public void showOverlayBecasseau() {
        OverlayDialogFragmentBecasseau overlayDialogBecasseau = new OverlayDialogFragmentBecasseau();
        overlayDialogBecasseau.show(getParentFragmentManager(), "overlayDialogBecasseau");
    }

    /**
     * Shows an overlay dialog for the Bernache bird species.
     */
    public void showOverlayBernache() {
        OverlayDialogFragmentBernache overlayDialogBernache = new OverlayDialogFragmentBernache();
        overlayDialogBernache.show(getParentFragmentManager(), "overlayDialogBernache");
    }

    /**
     * Shows an overlay dialog for the Goeland bird species.
     */
    public void showOverlayGoeland() {
        OverlayDialogFragmentGoeland overlayDialogGoeland = new OverlayDialogFragmentGoeland();
        overlayDialogGoeland.show(getParentFragmentManager(), "overlayDialogGoeland");
    }

    /**
     * Shows an overlay dialog for the Mouette bird species.
     */
    public void showOverlayMouette() {
        OverlayDialogFragmentMouette overlayDialogMouette = new OverlayDialogFragmentMouette();
        overlayDialogMouette.show(getParentFragmentManager(), "overlayDialogMouette");
    }

    /**
     * Shows an overlay dialog for the Pluvier bird species.
     */
    public void showOverlayPluvier() {
        OverlayDialogFragmentPluvier overlayDialogPluvier = new OverlayDialogFragmentPluvier();
        overlayDialogPluvier.show(getParentFragmentManager(), "overlayDialogPluvier");
    }

    /**
     * Shows an overlay dialog for the Cormoran bird species.
     */
    public void showOverlayCormoran() {
        OverlayDialogFragmentCormoran overlayDialogCormoran = new OverlayDialogFragmentCormoran();
        overlayDialogCormoran.show(getParentFragmentManager(), "overlayDialogCormoran");
    }

    /**
     * Shows an overlay dialog for the Foulque bird species.
     */
    public void showOverlayFoulque() {
        OverlayDialogFragmentFoulque overlayDialogFoulque = new OverlayDialogFragmentFoulque();
        overlayDialogFoulque.show(getParentFragmentManager(), "overlayDialogFoulque");
    }

    /**
     * Shows an overlay dialog for the Tadorne bird species.
     */
    public void showOverlayTadorne() {
        OverlayDialogFragmentTadorne overlayDialogTadorne = new OverlayDialogFragmentTadorne();
        overlayDialogTadorne.show(getParentFragmentManager(), "overlayDialogTadorne");
    }

    /**
     * Shows an overlay dialog for the Chevalier bird species.
     */
    public void showOverlayChevalier() {
        OverlayDialogFragmentChevalier overlayDialogChevalier = new OverlayDialogFragmentChevalier();
        overlayDialogChevalier.show(getParentFragmentManager(), "overlayDialogChevalier");
    }
}
