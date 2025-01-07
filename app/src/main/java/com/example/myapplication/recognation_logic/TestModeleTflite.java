package com.example.myapplication.recognation_logic;

import static android.graphics.Paint.*;

import android.content.Context;
import android.graphics.Bitmap;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class for performing species recognition using a TensorFlow Lite model.
 * Handles model loading, inference, output processing, and annotation of input images.
 */
public class TestModeleTflite {
    private static final String MODEL_PATH = "model_float32.tflite";
    private static final String LABEL_PATH = "labels.txt";

    private static final float INPUT_MEAN = 0f;
    private static final float INPUT_STANDARD_DEVIATION = 255f;
    private static final DataType INPUT_IMAGE_TYPE = DataType.FLOAT32;
    private static final DataType OUTPUT_IMAGE_TYPE = DataType.FLOAT32;
    private static final float CONFIDENCE_THRESHOLD = 0.4F;
    private static final float IOU_THRESHOLD = 0.5F;

    private Interpreter interpreter;
    private int tensorWidth;
    private int tensorHeight;
    private int numChannel;
    private int numElements;
    private List<String> labels = new ArrayList<>();
    private ImageProcessor imageProcessor;

    /**
     * Initializes the TensorFlow Lite model interpreter and loads associated resources.
     *
     * @param context The application context for accessing assets.
     * @throws IOException If the model or label files cannot be loaded.
     */
    public TestModeleTflite(Context context) throws IOException {
        // Load the TensorFlow Lite model
        MappedByteBuffer model = FileUtil.loadMappedFile(context, MODEL_PATH);

        // Configure the interpreter with multithreading options
        Interpreter.Options options = new Interpreter.Options();
        options.setNumThreads(4);
        interpreter = new Interpreter(model, options);

        // Extract tensor shapes from the model
        int[] inputShape = interpreter.getInputTensor(0).shape();
        int[] outputShape = interpreter.getOutputTensor(0).shape();

        tensorWidth = inputShape[1];
        tensorHeight = inputShape[2];
        numChannel = outputShape[1];
        numElements = outputShape[2];

        // Load the label list
        try (InputStream inputStream = context.getAssets().open(LABEL_PATH);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                labels.add(line);
            }
        }

        // Configure preprocessing steps for the input image
        imageProcessor = new ImageProcessor.Builder()
                .add(new NormalizeOp(INPUT_MEAN, INPUT_STANDARD_DEVIATION))
                .add(new CastOp(INPUT_IMAGE_TYPE))
                .build();
    }

    /**
     * Prepares an input image and performs inference using the TensorFlow Lite model.
     *
     * @param bitmap The input image to analyze.
     * @return A TensorBuffer containing the model's raw output.
     */
    public TensorBuffer predict(Bitmap bitmap) {
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, tensorWidth, tensorHeight, false);

        TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
        tensorImage.load(resizedBitmap);
        TensorImage processedImage = imageProcessor.process(tensorImage);

        TensorBuffer outputBuffer = TensorBuffer.createFixedSize(
                new int[]{1, numChannel, numElements},
                OUTPUT_IMAGE_TYPE
        );

        interpreter.run(processedImage.getBuffer(), outputBuffer.getBuffer());
        return outputBuffer;
    }

    /**
     * Processes the raw model output to generate bounding boxes for detected objects.
     *
     * @param outputBuffer The TensorBuffer containing raw model output.
     * @return A list of bounding boxes for detected objects.
     */
    public List<BoundingBox> processOutput(TensorBuffer outputBuffer) {
        float[] outputArray = outputBuffer.getFloatArray();
        return bestBox(outputArray);
    }

    /**
     * Annotates an input image with bounding boxes for detected objects.
     *
     * @param inputImage The original image to annotate.
     * @param boxes The bounding boxes of detected objects.
     * @return A new image with bounding boxes drawn.
     */
    public Bitmap annotateImage(Bitmap inputImage, List<BoundingBox> boxes) {
        return drawBoundingBoxes(inputImage, boxes);
    }

    /**
     * Groups detections by class and constructs a structured output image and metadata.
     *
     * @param inputImage The input image for recognition.
     * @return An OutputPredictedImage containing the annotated image and prediction results.
     * @throws NoSpeciesRecognizedException If no objects are detected.
     */
    public OutputPredictedImage recognizeSpeciesClass(Bitmap inputImage) throws NoSpeciesRecognizedException {
        Map<String, RecognizeSpecie> predictionResults = new HashMap<>();

        TensorBuffer outputBuffer = predict(inputImage);
        List<BoundingBox> boxes = processOutput(outputBuffer);
        if (boxes != null && !boxes.isEmpty()) {
            for (BoundingBox box : boxes) {
                if (predictionResults.containsKey(box.clsName)) {
                    predictionResults.get(box.clsName).getSpecieBoxes().add(box);
                } else {
                    predictionResults.put(box.clsName, new RecognizeSpecie(box.clsName, new ArrayList<>()));
                    predictionResults.get(box.clsName).getSpecieBoxes().add(box);
                }
            }
            Bitmap annotatedImage = annotateImage(inputImage, boxes);
            return new OutputPredictedImage(annotatedImage, predictionResults);
        } else {
            throw new NoSpeciesRecognizedException();
        }

    }

    /**
     * Releases the resources held by the TensorFlow Lite interpreter.
     */
    public void close() {
        if (interpreter != null) {
            interpreter.close();
            interpreter = null;
        }
    }

    /**
     * Filters bounding boxes based on confidence and non-maximum suppression (NMS).
     */
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

        return applyNMS(boundingBoxes);
    }

    /**
     * Applies non-maximum suppression (NMS) to eliminate overlapping boxes.
     */
    private List<BoundingBox> applyNMS(List<BoundingBox> boxes) {
        List<BoundingBox> sortedBoxes = new ArrayList<>(boxes);
        sortedBoxes.sort((b1, b2) -> Float.compare(b2.cnf, b1.cnf));
        List<BoundingBox> selectedBoxes = new ArrayList<>();

        while (!sortedBoxes.isEmpty()) {
            BoundingBox first = sortedBoxes.remove(0);
            selectedBoxes.add(first);
            sortedBoxes.removeIf(nextBox -> calculateIoU(first, nextBox) >= IOU_THRESHOLD);
        }

        return selectedBoxes;
    }

    /**
     * Calculates the Intersection over Union (IoU) between two bounding boxes.
     */
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

    /**
     * Draws bounding boxes on an image.
     */
    private Bitmap drawBoundingBoxes(Bitmap bitmap, List<BoundingBox> boxes) {
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);

        // Scale stroke width dynamically based on image resolution
        float strokeWidth = Math.max(mutableBitmap.getWidth(), mutableBitmap.getHeight()) / 470.0f;

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(strokeWidth); // Dynamically set stroke width

        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setStyle(Style.FILL);

        for (BoundingBox box : boxes) {
            RectF rect = new RectF(
                    box.x1 * mutableBitmap.getWidth(),
                    box.y1 * mutableBitmap.getHeight(),
                    box.x2 * mutableBitmap.getWidth(),
                    box.y2 * mutableBitmap.getHeight()
            );

            // Draw the bounding box
            canvas.drawRect(rect, paint);

            // Dynamically adjust text size to fit within the box
            float boxWidth = rect.width();
            float boxHeight = rect.height();
            float maxTextSize = boxHeight * 0.2f; // Limit text size to a fraction of the box height
            textPaint.setTextSize(maxTextSize);

            // Measure text width and adjust if necessary
            float textWidth = textPaint.measureText(box.clsName);
            if (textWidth > boxWidth) {
                textPaint.setTextSize(maxTextSize * (boxWidth / textWidth));
            }

            // Draw the text at the bottom of the box
            canvas.drawText(box.clsName, rect.left, rect.bottom - 5, textPaint); // Adjust -5 for padding
        }

        return mutableBitmap;
    }



    /**
     * Main method for debugging or testing purposes.
     */
    public static void main(String[] args) {
        String currentDir = System.getProperty("user.dir");
        System.out.println("Current working directory: " + currentDir);
    }
}
