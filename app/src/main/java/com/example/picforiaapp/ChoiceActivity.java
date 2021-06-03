package com.example.picforiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class ChoiceActivity extends AppCompatActivity {
    private ImageView textViewPhotography;
    private ImageView textViewVideography;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        textViewPhotography=findViewById(R.id.textView_photography);
        textViewVideography=findViewById(R.id.textView_videography);
    }

    public void photographyClick(View view) {
        Intent intent = new Intent(this,ViewAndUpload.class);
        startActivity(intent);
    }

    public void videographyClick(View view) {
        Intent intent = new Intent(this,VideographyActivity.class);
        startActivity(intent);
    }
}
