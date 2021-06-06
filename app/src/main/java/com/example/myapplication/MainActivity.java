package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int inputSize = 224;
    private String modelPath = "converted_model.tflite";
    private String labelPath = "label.txt";
    private Classifier classifier;
    TextView textView;
    Button button;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        //imageView = findViewById(R.id.imageView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, 100);
            }
        });


        try {
            initClassifier();
            initViews();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void initClassifier() throws IOException {
        classifier = new Classifier(getAssets(), modelPath, labelPath, inputSize);
    }

    private void initViews() {
        findViewById(R.id.imageView).setOnClickListener(this);
    }


    public void onClick(View v) {
        Bitmap bitmap = ((BitmapDrawable)((ImageView)v).getDrawable()).getBitmap();
        List<Classifier.Recognition> result = classifier.recognizeImage(bitmap);
        Toast.makeText(this, result.get(0).toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        Uri imageUri;
        ImageView imageView = findViewById(R.id.imageView);

        if (resultCode == RESULT_OK && reqCode == 100){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }



}