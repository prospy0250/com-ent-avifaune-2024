package com.example.myapplication;

import java.io.Serializable;
import java.util.List;

public class RecognizeSpecie implements Serializable {
    private String nameRecognizedSpecie;
    private List<BoundingBox> specieBoxes;

    RecognizeSpecie(String nameRecognizedSpecie, List<BoundingBox> specieBoxes){
        this.nameRecognizedSpecie = nameRecognizedSpecie;
        this.specieBoxes = specieBoxes;
    }

    public String getNameRecognizedSpecie() {
        return nameRecognizedSpecie;
    }

    public List<BoundingBox> getSpecieBoxes() {
        return specieBoxes;
    }

    public String toString(){
        int totalNumber = specieBoxes.size();
        float sumProbability = 0;
        for(BoundingBox boundingBox:specieBoxes){
            sumProbability += boundingBox.cnf;
        }
        String meanProbability = String.format("%.2f", 100 * (sumProbability / totalNumber));
        return nameRecognizedSpecie + " : " + totalNumber + "; precision " + meanProbability +"%";
    }

    public float getAverageProbability() {
        int totalNumber = specieBoxes.size();
        float sumProbability = 0;
        for(BoundingBox boundingBox:specieBoxes){
            sumProbability += boundingBox.cnf;
        }
        return 100 * (sumProbability / totalNumber);
    }
}
