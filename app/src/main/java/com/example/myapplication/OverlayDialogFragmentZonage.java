package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class OverlayDialogFragmentZonage extends DialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this dialog fragment
        return inflater.inflate(R.layout.fragment_overlay_dialog_zonage, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent); // Transparent background
        }
        Button goBackButton=(Button) this.getView().findViewById(R.id.return_zonage);

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
