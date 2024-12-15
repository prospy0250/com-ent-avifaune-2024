package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class OverlayDialogFragmentForm extends DialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this dialog fragment
        View view = inflater.inflate(R.layout.fragment_form, container, false);
        return inflater.inflate(R.layout.fragment_form, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent); // Transparent background
        }
        Button goBackButton=(Button) this.getView().findViewById(R.id.submit_form_button);

        View.OnClickListener listenerGoBackButton=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        };

        goBackButton.setOnClickListener(listenerGoBackButton);
    }

    private void goBack() {
        this.getDialog().dismiss();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Select the dropdown menu
        Spinner zoneSelector = view.findViewById(R.id.zone_selector);

        // Create the list of options
        String[] items = new String[]{"Zone 1\n","Zone 2\n","Zone 3\n","Zone 4\n",
                "Je ne suis pas dans la rade de Lorient"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(),
                android.R.layout.simple_spinner_item, items);

        // Set the list of options in the dropdown menu
        zoneSelector.setAdapter(adapter);

        // Set the time picker settings
        TimePicker timePicker = view.findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);

        // Display the result in the form
        TextView predictionDisplay = view.findViewById(R.id.prediction_output);
        String predictionText = "Nous avons détecté sur votre photo :\n" + PhotoPage.getPredictionOutput();
        predictionDisplay.setText(predictionText);
    }
}
