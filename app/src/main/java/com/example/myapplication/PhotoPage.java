package com.example.myapplication;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import com.example.myapplication.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class PhotoPage extends Fragment {
    private static final int CAMERA_REQUEST_CODE = 3;
    private static final int GALLERY_REQUEST_CODE = 1;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;

    private static String predictionOutput;

    Button cameraButton, folderButton, identifiedSpeciesButton;
    TextView textPredictionOutput, textProbabilityOutput;
    ImageView imageView;
    FrameLayout formLayout;
    OutputPredictedImage outputPredictedImage;


    int imageSize = 256;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        predictionOutput = null;
        View view = inflater.inflate(R.layout.fragment_photo_page, container, false);
        // Initialize UI elements
        cameraButton = view.findViewById(R.id.camera_button);
        folderButton = view.findViewById(R.id.folder_button);
        identifiedSpeciesButton = view.findViewById(R.id.identified_species_button);
        textPredictionOutput = view.findViewById(R.id.textPredictionOutput);
        //textProbabilityOutput = view.findViewById(R.id.textProbabilityOutput);
        imageView = view.findViewById(R.id.main_image_display);
        Button seeTutoButton = view.findViewById(R.id.see_tuto);



        // Set up button click listeners
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                }
            }
        });

        folderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent folderIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(folderIntent, GALLERY_REQUEST_CODE);

            }
        });

        identifiedSpeciesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(predictionOutput == null){
                    Toast.makeText(getContext(), "Veuillez prendre une photo ou charger une image depuis votre gallerie.", Toast.LENGTH_LONG).show();
                }
                if(outputPredictedImage != null){
                    ResultsFragment resultsFragment = new ResultsFragment(outputPredictedImage.getPredictionResults());
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, resultsFragment) // Assuming you have a container in your activity layout
                            .addToBackStack(null)
                            .commit();
                }

            }
        });
        View.OnClickListener lSeeTutoButton = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                intent.putExtra("FROM_PHOTO_PAGE", true);
                startActivity(intent);
                getActivity().finish();
            }


        };

        seeTutoButton.setOnClickListener(lSeeTutoButton);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null || resultCode != getActivity().RESULT_OK) {
            Log.e("PhotoPage", "Error: Data is null or result not OK");
            return;
        }

        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                // Handle photo taken with camera
                Bitmap cameraImage = (Bitmap) data.getExtras().get("data");
                if (cameraImage != null) {
                    imageView.setImageBitmap(cameraImage);
                    processImage(cameraImage); // Pass the image for further processing
                    showOverlayForm();
                }
                break;

            case GALLERY_REQUEST_CODE:
                // Handle image picked from the gallery
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    try {
                        ContentResolver resolver = getContext().getContentResolver();
                        Bitmap galleryImage = MediaStore.Images.Media.getBitmap(resolver, selectedImageUri);
                        imageView.setImageBitmap(galleryImage);

                        processImage(galleryImage); // Pass the image for further processing
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("PhotoPage", "Error loading image from gallery: " + e.getMessage());
                    }
                }
                break;
        }
    }

    private void processImage(Bitmap bitmap) {
        // Resize and/or preprocess the image for your recognition program
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, imageSize, imageSize, true);

        // You can pass this `scaledBitmap` to your prediction program
        Log.d("PhotoPage", "Bitmap processed for prediction: " + scaledBitmap.toString());

        try {
            TestModeleTflite testModeleTflite = new TestModeleTflite(this.getContext());
            outputPredictedImage = testModeleTflite.recognizeSpeciesClass(bitmap);
            Bitmap annotatedImage = outputPredictedImage.getAnnotatedImage();
            double height = 1.5 * annotatedImage.getHeight();
            double width = 1.5 * annotatedImage.getWidth();
            scaledBitmap = Bitmap.createScaledBitmap(annotatedImage, (int) width, (int) height, true);
            imageView.setImageBitmap(scaledBitmap);
            //TextView textView = getView().findViewById(R.id.textView2);
            predictionOutput=outputPredictedImage.toString();
            textPredictionOutput.setText(outputPredictedImage.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSpeciesRecognizedException e) {
            Toast.makeText(getContext(), "L'application n'a pas reconnu d'espèce connue sur votre photo", Toast.LENGTH_SHORT).show();
            predictionOutput="nothing";
            textPredictionOutput.setText("Cette espèce n'est pas encore répertoriée.");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
            } else {
                Log.e("PhotoPage", "Camera permission denied");
                textPredictionOutput.setText("Camera permission denied. Please allow it to use this feature.");
            }
        }

    }

    public void showOverlayForm() {
        OverlayDialogFragmentForm overlayDialogForm = new OverlayDialogFragmentForm();
        overlayDialogForm.show(getParentFragmentManager(), "overlayDialogForm");
    }

    public static String getPredictionOutput() {
        return predictionOutput;
    }
}
