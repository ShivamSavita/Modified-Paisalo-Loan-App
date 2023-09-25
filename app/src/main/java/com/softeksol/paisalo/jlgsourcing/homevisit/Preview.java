package com.softeksol.paisalo.jlgsourcing.homevisit;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.net.Uri;

import com.softeksol.paisalo.jlgsourcing.R;

import java.io.File;

public class Preview extends AppCompatActivity {

    private ImageView imageView;
    Button buttonOkay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        imageView = findViewById(R.id.imagePreview); // Initialize your ImageView
        buttonOkay = findViewById(R.id.buttonOkay); // Initialize your ImageView
        // Retrieve the image URI from the Intent
        String imageFile = getIntent().getStringExtra("imageFilePath");
        Log.d("TAG", "onCreate: "+imageFile);
        // Convert the URI string back to a URI
        Bitmap myBitmap = BitmapFactory.decodeFile(imageFile);
        // Set the image URI to the ImageView to display it
        imageView.setImageBitmap(myBitmap);
        buttonOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
    }
}
