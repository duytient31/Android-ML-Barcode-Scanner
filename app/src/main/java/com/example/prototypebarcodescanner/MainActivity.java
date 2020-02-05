package com.example.prototypebarcodescanner;
import android.graphics.SurfaceTexture;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageAnalysisConfig;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;

import android.util.Size;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This is a sample application to showcase the usage of mlkits barcode scanner.
 * The scanner will scan barcode supported by mlkit aand display it in a text view.
 *
 * @author Lewis Garton
 * @version 1.0
 * @since 2020-02-05
 */
public class MainActivity extends AppCompatActivity {
    private Button button;
    private TextureView textureView;
    public TextView textView;
    private Boolean isCameraSet = false;
    private BarcodeAnalyzer barcodeAnalyzer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textureView = findViewById(R.id.textureView);
        SurfaceTexture surfaceTexture = new SurfaceTexture(1);
        textureView.setSurfaceTexture(surfaceTexture);
        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);

        setupCamerax();
        barcodeAnalyzer = new BarcodeAnalyzer(textView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Do something here!
            }
        });
    }

    /**
     * Bind the camera instance and preview use-case
     * This should be called once as camerax binds cameras and use-cases to a lifecycle Owner
     */
    protected void setupCamerax() {
        if (isCameraSet) return;
        isCameraSet = true;
        CameraX.unbindAll();

        //Create preview use-case using default builder
        Preview preview = new Preview(
                new PreviewConfig.Builder().build()
        );

        preview.setOnPreviewOutputUpdateListener(new Preview.OnPreviewOutputUpdateListener() {
            @Override
            public void onUpdated(@NonNull Preview.PreviewOutput output) {
                textureView.setSurfaceTexture(output.getSurfaceTexture());
            }
        });

        //Create ImageAnalyzer use-case
        //Read mode set to acquire last image to avoid frame stutter
        ImageAnalysisConfig imageAnalysisConfig = new ImageAnalysisConfig.Builder()
                .setImageReaderMode(ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE)
                .build();
        ImageAnalysis imageAnalyzer = new ImageAnalysis(imageAnalysisConfig);

        //Analyze runs in another thread, calls the barcode scanner
        ExecutorService executor = Executors.newFixedThreadPool(1);
        imageAnalyzer.setAnalyzer(executor, new ImageAnalysis.Analyzer() {
                    @Override
                    public void analyze(ImageProxy image, int rotationDegrees) {
                        barcodeAnalyzer.analyze(image, rotationDegrees);
                    }
                });
        CameraX.bindToLifecycle(this, preview, imageAnalyzer);

        /*
        //TODO: Implement for debugging
        previewBuilder.setTargetName();
        */
    }

}
