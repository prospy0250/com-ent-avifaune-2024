package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OverlayDialogFragmentForm extends DialogFragment {
    private String zoneSelected;
    private boolean isZoneSelected = false;

    private String finalPredictionOutput;

    private String becasseauCounter = "0";
    private String bernacheCounter = "0";
    private String goelandCounter = "0";
    private String mouetteCounter = "0";
    private String pluvierCounter = "0";
    private String cormoranCounter = "0";
    private String foulqueCounter = "0";
    private String tadorneCounter = "0";
    private String chevalierCounter = "0";

    private String surroundingsDescription;

    private String unknownSpeciesDescription="";

    // Linking the Google Forms
    private final Retrofit retrofit = new Retrofit.Builder().baseUrl("https://docs.google.com/").addConverterFactory(GsonConverterFactory.create()).build();

    private final GoogleFormApi api = retrofit.create(GoogleFormApi.class);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this dialog fragment
        View view = inflater.inflate(R.layout.fragment_form, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent); // Transparent background
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button zoneInfoButton = view.findViewById(R.id.form_zoneInfo);
        zoneInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OverlayDialogFragmentZonage overlayDialogZonage = new OverlayDialogFragmentZonage();
                overlayDialogZonage.show(getParentFragmentManager(), "overlayDialogZonage");
            }
        });


        // Select the dropdown menu
        Spinner zoneSelector = view.findViewById(R.id.zone_selector);

        // Create the list of options
        String[] items = new String[]{"Veuillez sélectionner une zone", "Zone 1", "Zone 2", "Zone 3", "Zone 4",
                "Je ne suis pas dans la rade de Lorient"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(),
                android.R.layout.simple_spinner_item, items);

        // Set the list of options in the dropdown menu
        zoneSelector.setAdapter(adapter);

        // If an item is selected we retain its value
        zoneSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                zoneSelected = parent.getItemAtPosition(position).toString();
                if (!zoneSelected.equals(items[0])) isZoneSelected = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                isZoneSelected = false;
            }
        });

        // Display the result in the form
        finalPredictionOutput = PhotoPage.getPredictionOutput();
        if (finalPredictionOutput != "nothing") {
            TextView predictionDisplay = view.findViewById(R.id.prediction_output);
            String predictionText = "Nous avons détecté sur votre photo :\n" + finalPredictionOutput;
            predictionDisplay.setText(predictionText);
            unknownSpeciesDescription="Pas d'espèces fournies.";
        } else {
            EditText unknownSpeciesInput = view.findViewById(R.id.unknown_species_input);
            unknownSpeciesInput.setVisibility(EditText.VISIBLE);

            TextView unknownSpecies = view.findViewById(R.id.unknown_species);
            unknownSpecies.setVisibility(TextView.VISIBLE);

            unknownSpeciesInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    unknownSpeciesDescription="Pas d'espèces fournies.";
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    unknownSpeciesDescription = unknownSpeciesInput.getText().toString();
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (unknownSpeciesDescription.isBlank()) unknownSpeciesDescription="Pas d'espèces fournies.";
                }
            });
        }


        String[] numberBySpecies = finalPredictionOutput.split("\n");

        // See how many birds of each species were identified
        if (finalPredictionOutput.contains("Bécasseau Sanderling")) {
            for (String species : numberBySpecies) {
                if (species.contains("Bécasseau Sanderling")) {
                    becasseauCounter = species.split(" : ")[1];
                }
            }
        }

        if (finalPredictionOutput.contains("Bernache Cravant")) {
            for (String species : numberBySpecies) {
                if (species.contains("Bernache Cravant")) {
                    bernacheCounter = species.split(" : ")[1];
                }
            }
        }

        if (finalPredictionOutput.contains("Goéland Argenté")) {
            for (String species : numberBySpecies) {
                if (species.contains("Goéland Argenté")) {
                    goelandCounter = species.split(" : ")[1];
                }
            }
        }

        if (finalPredictionOutput.contains("Mouette Rieuse")) {
            for (String species : numberBySpecies) {
                if (species.contains("Mouette Rieuse")) {
                    mouetteCounter = species.split(" : ")[1];
                }
            }
        }

        if (finalPredictionOutput.contains("Pluvier Argenté")) {
            for (String species : numberBySpecies) {
                if (species.contains("Pluvier Argenté")) {
                    pluvierCounter = species.split(" : ")[1];
                }
            }
        }

        if (finalPredictionOutput.contains("Grand Cormoran")) {
            for (String species : numberBySpecies) {
                if (species.contains("Grand Cormoran")) {
                    cormoranCounter = species.split(" : ")[1];
                }
            }
        }

        if (finalPredictionOutput.contains("Foulque Macroule")) {
            for (String species : numberBySpecies) {
                if (species.contains("Foulque Macroule")) {
                    foulqueCounter = species.split(" : ")[1];
                }
            }
        }

        if (finalPredictionOutput.contains("Tadorne de Belon")) {
            for (String species : numberBySpecies) {
                if (species.contains("Tadorne de Belon")) {
                    tadorneCounter = species.split(" : ")[1];
                }
            }
        }

        if (finalPredictionOutput.contains("Chevalier Gambette")) {
            for (String species : numberBySpecies) {
                if (species.contains("Chevalier Gambette")) {
                    chevalierCounter = species.split(" : ")[1];
                }
            }
        }

        EditText surroundings = view.findViewById(R.id.location_input);
        surroundings.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                surroundingsDescription = "Pas de description fournie";
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                surroundingsDescription = surroundings.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                surroundingsDescription = surroundings.getText().toString();
            }
        });



        Button submitButton=(Button) this.getView().findViewById(R.id.submit_form_button);

        View.OnClickListener listenerSubmitButton=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<Void> call = api.sendFormData(becasseauCounter, bernacheCounter, goelandCounter, mouetteCounter, pluvierCounter, cormoranCounter,
                        foulqueCounter, tadorneCounter, chevalierCounter, unknownSpeciesDescription, zoneSelected, surroundingsDescription);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        try {
                            if (response.isSuccessful()) {
                                Toast.makeText(getContext(), "Merci pour votre participation !", Toast.LENGTH_SHORT).show();
                                getDialog().dismiss();
                            } else {
                                if (!isZoneSelected) Toast.makeText(getContext(), "Veuillez sélectionner une zone", Toast.LENGTH_SHORT).show();
                                Toast.makeText(getContext(), "Erreur : " + response.code(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        try {
                            Toast.makeText(getContext(), "Il vous faut être connecté à Internet pour continuer", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };

        submitButton.setOnClickListener(listenerSubmitButton);
    }
}
