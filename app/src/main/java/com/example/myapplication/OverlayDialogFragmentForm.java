package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

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
}
