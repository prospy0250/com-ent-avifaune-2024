package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Map;

/**
 * this class purpose is to store information of the output of the recognation species
 */
public class OutputPredictedImage {
    private Bitmap annotatedImage;
    private Map<String, Integer> predictionResults;

    public OutputPredictedImage(Bitmap annotatedImage, Map<String,Integer> predictionResults){
        this.annotatedImage = annotatedImage;
        this.predictionResults = predictionResults;
    }

    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, Integer> entry : predictionResults.entrySet()) {
            // Append the key (species) and value (probability) in the specified format
            result.append(entry.getKey())
                    .append(" : ")
                    .append(entry.getValue()) // Format the float to 2 decimal places
                    .append("\n"); // Add a newline for separation
        }

        // Return the concatenated string
        return result.toString(); // Remove the last newline
    }

    public Bitmap getAnnotatedImage() {
        return annotatedImage;
    }

    public Map<String, Integer> getPredictionResults() {
        return predictionResults;
    }

}
