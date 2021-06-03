package com.example.picforiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class VideographyActivity extends AppCompatActivity {

    Button button_View, button_Upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videography);
        button_View=findViewById(R.id.btn_ViewVideos);
        button_Upload=findViewById(R.id.btn_UploadVideos);


        button_View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VideographyActivity.this,Videos.class));
            }
        });
        button_Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VideographyActivity.this,UploadVideos.class));
            }
        });
    }
}