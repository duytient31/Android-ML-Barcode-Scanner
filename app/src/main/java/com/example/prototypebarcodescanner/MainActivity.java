package com.example.prototypebarcodescanner;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.TextureView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

/**
 * This is a sample application to showcase the usage of mlkits barcode scanner.
 * The scanner will scan barcode supported by mlkit aand display it in a text view.
 *
 * @author Lewis Garton
 * @version 1.0
 * @since 2020-02-05
 *
 */
public class MainActivity extends AppCompatActivity {
    private Button button;
    private TextureView textureView;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        textureView = findViewById(R.id.textureView);
        button = findViewById(R.id.button);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Do something here!
            }
        });


    }
}
