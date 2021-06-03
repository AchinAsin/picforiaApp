package com.example.picforiaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class PhotographyActivity extends AppCompatActivity {
    public CardView portraitButton;
    public CardView landscapeButton;
    public CardView nightButton;
    public CardView streetButton;
    public CardView sunsetButton;
    public CardView wildLifeButton;
    private SwipeRefreshLayout swipeContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*getActionBar().setTitle("Photography Category");
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(true);*/
/*
        Toolbar toolbar = (android.widget.Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
*/
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("Photography");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        setContentView(R.layout.activity_photography);
        portraitButton = findViewById(R.id.cardView1);
        landscapeButton = findViewById(R.id.cardView2);
        nightButton = findViewById(R.id.cardView3);
        streetButton = findViewById(R.id.cardView4);
        sunsetButton = findViewById(R.id.cardView5);
        wildLifeButton = findViewById(R.id.cardView6);
    }



    public void portraitClick(View view) {
        startActivity(new Intent(PhotographyActivity.this,PortraitActivity.class));
    }

    public void landscapeClick(View view) {
        startActivity(new Intent(PhotographyActivity.this,LandscapeActivity.class));
    }

    public void nightClick(View view) {
        startActivity(new Intent(PhotographyActivity.this,NightActivity.class));
    }

    public void streetClick(View view) {
        startActivity(new Intent(PhotographyActivity.this,StreetPhotography.class));
    }

    public void sunsetClick(View view) {
        startActivity(new Intent(PhotographyActivity.this,SunsetPhotography.class));
    }

    public void wildLifeClick(View view) {
        startActivity(new Intent(PhotographyActivity.this,WildlifePhotography.class));
    }
}
