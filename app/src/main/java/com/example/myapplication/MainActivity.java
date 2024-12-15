package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the saved theme mode from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("theme_prefs", MODE_PRIVATE);
        int themeMode = preferences.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_NO);
        AppCompatDelegate.setDefaultNightMode(themeMode);

        setContentView(R.layout.activity_main);


        int delayMillis = 3000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                }
        }, delayMillis);



    }

    public void testPredict(View v){
        TextView textView = findViewById(R.id.testInference);
        TestModeleTflite testModeleTflite = null;
        try {
            testModeleTflite = new TestModeleTflite(this);
            String currentDir = System.getProperty("user.dir");
            String imagePath = "mouette_rieuse.jpg";
            Bitmap inputImage = TestModeleTflite.loadImage(imagePath, this);

            if (inputImage == null) {
                System.out.println("Failed to load the image. Check the file path.");
                return;
            }

            // Perform prediction
            TensorBuffer outputBuffer = testModeleTflite.predict(inputImage);

            // Process the output to get bounding boxes
            List<BoundingBox> boxes = testModeleTflite.processOutput(outputBuffer);

            if (boxes == null || boxes.isEmpty()) {
                textView.setText("No objects detected in the image.");
            } else {
                // Output results to the console
                for (BoundingBox box : boxes) {
                    System.out.println("Class: " + box.clsName);
                    System.out.println("Confidence: " + box.cnf);
                    System.out.println("----------------------------");
                    textView.setText("Class: " + box.clsName + " Confidence : " + box.cnf);
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

       //textView.setText("bonjour");
    }


}
