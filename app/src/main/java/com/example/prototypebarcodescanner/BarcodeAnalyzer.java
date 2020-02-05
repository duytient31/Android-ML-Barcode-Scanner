package com.example.prototypebarcodescanner;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.media.Image;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;

import java.util.List;

public class BarcodeAnalyzer implements ImageAnalysis.Analyzer {
    private FirebaseVisionBarcodeDetector detector;
    private TextView textView;

    /**
     * Constructor, requires ref to Textview, note this is a hack and needs to be fixed
     * Initializes FirebaseVisionBarcodeDetector instance
     * @param textView: text view to write barcodes to UI
     */
    public BarcodeAnalyzer(TextView textView) {
        //TODO: Alter implementation to avoid passing mainActivity reference
        this.textView = textView;
        FirebaseVisionBarcodeDetectorOptions options =
                new FirebaseVisionBarcodeDetectorOptions.Builder()
                        .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_ALL_FORMATS)
                        .build();
        detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options);
    }

    /**
     * Convert ImageAnalysis.Analyzer's calculation of rotation to ML kit constant
     * @param degrees
     * @return int: ML kit ROTATION constant
     */
    private int degreesToFirebaseRotation(int degrees) {
        switch (degrees) {
            case 0:
                return FirebaseVisionImageMetadata.ROTATION_0;
            case 90:
                return FirebaseVisionImageMetadata.ROTATION_90;
            case 180:
                return FirebaseVisionImageMetadata.ROTATION_180;
            case 270:
                return FirebaseVisionImageMetadata.ROTATION_270;
            default:
                throw new IllegalArgumentException(
                        "Rotation must be 0, 90, 180, or 270.");
        }
    }

    @Override
    public void analyze(ImageProxy imageProxy, int degrees) {
        if (imageProxy == null || imageProxy.getImage() == null) {
            return;
        }
        Image mediaImage = imageProxy.getImage();
        int rotation = degreesToFirebaseRotation(degrees);
        FirebaseVisionImage image =
                FirebaseVisionImage.fromMediaImage(mediaImage, rotation);

        Task<List<FirebaseVisionBarcode>> result = detector.detectInImage(image)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionBarcode> barcodes) {
                        String out = "";
                        for(int i = 0; i < barcodes.size(); i++) {
                            out += barcodes.get(i).getDisplayValue();
                            out += "/n";
                        }
                        textView.setText(out);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //TODO: Log to error
                        textView.setText("FAIL: " + e.getMessage());
                    }
                });
    }

}
