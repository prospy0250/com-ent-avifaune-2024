package com.example.myapplication.recognation_logic;

import android.graphics.Bitmap;

import java.util.Map;

/**
 * this class purpose is to store information of the output of the recognation species
 */
public class OutputPredictedImage {
    private Bitmap annotatedImage;
    private Map<String, RecognizeSpecie> predictionResults;

    public OutputPredictedImage(Bitmap annotatedImage, Map<String,RecognizeSpecie> predictionResults){
        this.annotatedImage = annotatedImage;
        this.predictionResults = predictionResults;
    }


    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, RecognizeSpecie> entry : predictionResults.entrySet()) {
            // Append the key (species) and value (probability) in the specified format
            result.append(entry.getValue().toString()) // Format the float to 2 decimal places
                    .append("\n"); // Add a newline for separation
        }

        // Return the concatenated string
        return result.toString(); // Remove the last newline
    }

    public Bitmap getAnnotatedImage() {
        return annotatedImage;
    }

    public Map<String, RecognizeSpecie> getPredictionResults() {
        return predictionResults;
    }

}
