package com.example.prototypebarcodescanner;
import android.graphics.SurfaceTexture;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraX;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;

import android.util.Size;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
    private TextView textView;
    private Boolean isCameraSet = false;

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

        //Create preview use-case
        Size textureViewResolution = new Size(textureView.getWidth(), textureView.getHeight());
        PreviewConfig previewConfig = new PreviewConfig.Builder()
                .build();
        Preview preview = new Preview(previewConfig);

        preview.setOnPreviewOutputUpdateListener(new Preview.OnPreviewOutputUpdateListener() {
            @Override
            public void onUpdated(@NonNull Preview.PreviewOutput output) {
                textureView.setSurfaceTexture(output.getSurfaceTexture());
            }
        });

        CameraX.bindToLifecycle(this, preview);

        /*
        //TODO: Implement for debugging
        previewBuilder.setTargetName();
        */
    }
}
