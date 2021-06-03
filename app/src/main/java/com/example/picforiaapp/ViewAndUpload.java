package com.example.picforiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ViewAndUpload extends AppCompatActivity {

    Button button_View, button_Upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_and_upload);
        button_View=findViewById(R.id.btn_ViewPictures);
        button_Upload=findViewById(R.id.btn_UploadPictures);

        button_View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewAndUpload.this,PhotographyActivity.class));
            }
        });

        button_Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewAndUpload.this,UploadActivity.class));
            }
        });


    }
}