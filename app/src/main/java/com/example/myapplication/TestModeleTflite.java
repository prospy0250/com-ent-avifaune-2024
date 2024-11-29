package com.example.myapplication;

import static android.graphics.Paint.*;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.common.ops.CastOp;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestModeleTflite {
    private static final String MODEL_PATH = "model_float32.tflite";
    private static final String LABEL_PATH = "labels.txt";

    private static final float INPUT_MEAN = 0f;
    private static final float INPUT_STANDARD_DEVIATION = 255f;
    private static final DataType INPUT_IMAGE_TYPE = DataType.FLOAT32;
    private static final DataType OUTPUT_IMAGE_TYPE = DataType.FLOAT32;
    private static final float CONFIDENCE_THRESHOLD = 0.3F;
    private static final float IOU_THRESHOLD = 0.5F;

    private Interpreter interpreter;
    private int tensorWidth;
    private int tensorHeight;
    private int numChannel;
    private int numElements;
    private List<String> labels = new ArrayList<>();
    private ImageProcessor imageProcessor;

    public TestModeleTflite(Context context) throws IOException {
        // Load the model
        MappedByteBuffer model = FileUtil.loadMappedFile(context, MODEL_PATH);

        // Set interpreter options
        Interpreter.Options options = new Interpreter.Options();
        options.setNumThreads(4);
        interpreter = new Interpreter(model, options);

        // Get tensor shapes
        int[] inputShape = interpreter.getInputTensor(0).shape();
        int[] outputShape = interpreter.getOutputTensor(0).shape();

        tensorWidth = inputShape[1];
        tensorHeight = inputShape[2];
        numChannel = outputShape[1];
        numElements = outputShape[2];

        // Load labels
        try (InputStream inputStream = context.getAssets().open(LABEL_PATH);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                labels.add(line);
            }
        }

        // Initialize image processor
        imageProcessor = new ImageProcessor.Builder()
                .add(new NormalizeOp(INPUT_MEAN, INPUT_STANDARD_DEVIATION))
                .add(new CastOp(INPUT_IMAGE_TYPE))
                .build();
    }

    public TensorBuffer predict(Bitmap bitmap) {
        // Resize bitmap to model's input size
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, tensorWidth, tensorHeight, false);

        // Preprocess input image
        TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
        tensorImage.load(resizedBitmap);
        TensorImage processedImage = imageProcessor.process(tensorImage);

        // Prepare output buffer
        TensorBuffer outputBuffer = TensorBuffer.createFixedSize(
                new int[]{1, numChannel, numElements},
                OUTPUT_IMAGE_TYPE
        );

        // Run inference
        interpreter.run(processedImage.getBuffer(), outputBuffer.getBuffer());

        return outputBuffer;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void close() {
        if (interpreter != null) {
            interpreter.close();
            interpreter = null;
        }
    }

    public List<BoundingBox> processOutput(TensorBuffer outputBuffer) {
        float[] outputArray = outputBuffer.getFloatArray();
        return bestBox(outputArray);
    }

    public Bitmap annotateImage(Bitmap inputImage, List<BoundingBox> boxes) {
        return drawBoundingBoxes(inputImage, boxes);
    }

    private List<BoundingBox> bestBox(float[] array) {
        List<BoundingBox> boundingBoxes = new ArrayList<>();

        for (int c = 0; c < numElements; c++) {
            float maxConf = -1.0f;
            int maxIdx = -1;
            int j = 4;
            int arrayIdx = c + numElements * j;

            while (j < numChannel) {
                if (array[arrayIdx] > maxConf) {
                    maxConf = array[arrayIdx];
                    maxIdx = j - 4;
                }
                j++;
                arrayIdx += numElements;
            }

            if (maxConf > CONFIDENCE_THRESHOLD) {
                String clsName = labels.get(maxIdx);
                float cx = array[c];
                float cy = array[c + numElements];
                float w = array[c + numElements * 2];
                float h = array[c + numElements * 3];
                float x1 = cx - (w / 2F);
                float y1 = cy - (h / 2F);
                float x2 = cx + (w / 2F);
                float y2 = cy + (h / 2F);

                if (x1 < 0F || x1 > 1F || y1 < 0F || y1 > 1F || x2 < 0F || x2 > 1F || y2 < 0F || y2 > 1F)
                    continue;

                boundingBoxes.add(new BoundingBox(x1, y1, x2, y2, cx, cy, w, h, maxConf, maxIdx, clsName));
            }
        }

        if (boundingBoxes.isEmpty()) return null;

        return applyNMS(boundingBoxes);
    }

    private List<BoundingBox> applyNMS(List<BoundingBox> boxes) {
        List<BoundingBox> sortedBoxes = new ArrayList<>(boxes);
        sortedBoxes.sort((b1, b2) -> Float.compare(b2.cnf, b1.cnf)); // Sort by confidence descending
        List<BoundingBox> selectedBoxes = new ArrayList<>();

        while (!sortedBoxes.isEmpty()) {
            BoundingBox first = sortedBoxes.remove(0);
            selectedBoxes.add(first);

            sortedBoxes.removeIf(nextBox -> calculateIoU(first, nextBox) >= IOU_THRESHOLD);
        }

        return selectedBoxes;
    }

    private float calculateIoU(BoundingBox box1, BoundingBox box2) {
        float x1 = Math.max(box1.x1, box2.x1);
        float y1 = Math.max(box1.y1, box2.y1);
        float x2 = Math.min(box1.x2, box2.x2);
        float y2 = Math.min(box1.y2, box2.y2);

        float intersectionArea = Math.max(0F, x2 - x1) * Math.max(0F, y2 - y1);
        float box1Area = box1.w * box1.h;
        float box2Area = box2.w * box2.h;

        return intersectionArea / (box1Area + box2Area - intersectionArea);
    }



    private Bitmap drawBoundingBoxes(Bitmap bitmap, List<BoundingBox> boxes) {
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(8f);

        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(40f);
        //textPaint.setTypeface(Paint.DEFAULT_BOLD);

        for (BoundingBox box : boxes) {
            RectF rect = new RectF(
                    box.x1 * mutableBitmap.getWidth(),
                    box.y1 * mutableBitmap.getHeight(),
                    box.x2 * mutableBitmap.getWidth(),
                    box.y2 * mutableBitmap.getHeight()
            );
            canvas.drawRect(rect, paint);
            canvas.drawText(box.clsName, rect.left, rect.bottom, textPaint);
            canvas.drawText(new StringBuilder().append("prob : ").append(String.format("%.2f", box.cnf)).toString(), rect.left, rect.top, textPaint);
        }

        return mutableBitmap;
    }

    /**
     * Loads an image from the specified file path and converts it to a Bitmap.
     *
     * @param fileName Path to the image file.
     * @return Bitmap representation of the image or null if an error occurs.
     */
    public static Bitmap loadImage(String fileName, Context context) {
        try (InputStream inputStream = context.getAssets().open(fileName)) {
            return BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Bitmap recognizeSpecies(Bitmap inputImage){
        TensorBuffer outputBuffer = predict(inputImage);
        List<BoundingBox> boxes = processOutput(outputBuffer);
        return annotateImage(inputImage, boxes);
    }

    public OutputPredictedImage recognizeSpeciesClass(Bitmap inputImage){
        //ArrayList<String> speciesNames = new ArrayList<>();
        Map<String, Integer> predictionResults = new HashMap<>();
        TensorBuffer outputBuffer = predict(inputImage);
        List<BoundingBox> boxes = processOutput(outputBuffer);
        for(BoundingBox box:boxes){
            if(predictionResults.containsKey(box.clsName)){
                int previousNumber = predictionResults.get(box.clsName);
                predictionResults.put(box.clsName, previousNumber + 1);
            }else{
                predictionResults.put(box.clsName, 1);
            }

        }
        Bitmap annotatedImage =  annotateImage(inputImage, boxes);
        return new OutputPredictedImage(annotatedImage, predictionResults);
    }


    public static void main(String [] args){
    /*
        TestModeleTflite testModeleTflite = new TestModeleTflite(this);
        String imagePath = "../../res/drawable/bernache_cravant.jpg";
        Bitmap inputImage = TestModeleTflite.loadImage(imagePath);

        if (inputImage == null) {
            System.out.println("Failed to load the image. Check the file path.");
            return;
        }

        // Perform prediction
        TensorBuffer outputBuffer = testModeleTflite.predict(inputImage);

        // Process the output to get bounding boxes
        List<BoundingBox> boxes = testModeleTflite.processOutput(outputBuffer);

        if (boxes == null || boxes.isEmpty()) {
            System.out.println("No objects detected in the image.");
        } else {
            // Output results to the console
            for (BoundingBox box : boxes) {
                System.out.println("Class: " + box.clsName);
                System.out.println("Confidence: " + box.cnf);
                System.out.println("----------------------------");
            }
        }*/
        String currentDir = System.getProperty("user.dir");
        System.out.println("Current working directory: " + currentDir);
    }



}

