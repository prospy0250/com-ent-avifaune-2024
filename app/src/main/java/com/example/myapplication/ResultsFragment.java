package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Map;

public class ResultsFragment extends Fragment {

    private Map<String, RecognizeSpecie> predictionResults;

    // Constructor to receive prediction results
    public ResultsFragment(Map<String, RecognizeSpecie> predictionResults) {
        this.predictionResults = predictionResults;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_prediction_results, container, false);

        // Reference the TableLayout
        TableLayout tableLayout = view.findViewById(R.id.results_table);

        // Add a header row to the table
        addHeaderRow(tableLayout);

        // Populate the table with prediction results
        populateTable(tableLayout);

        return view;
    }

    /**
     * Adds a header row to the table layout.
     */
    private void addHeaderRow(TableLayout tableLayout) {
        TableRow headerRow = new TableRow(getContext());
        headerRow.setBackgroundColor(getResources().getColor(R.color.logo2_background)); // Optional: Add header styling
        addTableCell(headerRow, "Espèce");
        addTableCell(headerRow, "Total individus");
        addTableCell(headerRow, "Précision(%)");
        tableLayout.addView(headerRow);
    }

    /**
     * Populates the table with rows based on the prediction results.
     */
    private void populateTable(TableLayout tableLayout) {
        if (predictionResults != null) {
            for (Map.Entry<String, RecognizeSpecie> entry : predictionResults.entrySet()) {
                TableRow dataRow = new TableRow(getContext());

                RecognizeSpecie specie = entry.getValue();

                addTableCell(dataRow, entry.getKey()); // Species name
                addTableCell(dataRow, String.valueOf(specie.getSpecieBoxes().size())); // Count
                addTableCell(dataRow, String.format("%.2f", specie.getAverageProbability())); // Average probability

                tableLayout.addView(dataRow);
            }
        } else {
            // If no data exists, add a message row
            TableRow emptyRow = new TableRow(getContext());
            addTableCell(emptyRow, "No data available");
            tableLayout.addView(emptyRow);
        }
    }

    /**
     * Adds a cell to a given table row.
     */
    private void addTableCell(TableRow row, String text) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setPadding(16, 16, 16, 16);

        textView.setTextColor(getResources().getColor(R.color.white)); // Optional: Add text styling
        row.addView(textView);
    }
}
