package com.example.picforiaapp;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class fullImage extends AppCompatActivity {

    private static final int REQUEST_CODE = 100;
    String pictureUrl;
    String name;
    String Category;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        pictureUrl = getIntent().getStringExtra("pictureLink");
        name = getIntent().getStringExtra("metaData");
        setImage(pictureUrl);
        Category=getIntent().getStringExtra("Category");

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(android.R.color.transparent);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh(){
                finish();
                startActivity(getIntent());
            }
        });

    }


    private void setImage(String pictureUrl) {

        ImageView FullImage = findViewById(R.id.fullView);
        Glide.with(this)
                .asBitmap()
                .load(pictureUrl)
                .into(FullImage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_download, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case (R.id.download_menu_Icon):
                Toast.makeText(getApplicationContext(), "Downloading...", Toast.LENGTH_LONG).show();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
                    } else {
                        String imagePathName = getIntent().getStringExtra("metaData");
                        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                        StorageReference storageReference = firebaseStorage.getReference().child(Category+"/");
                        storageReference.child(imagePathName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                downloadImage(uri);
                                Toast.makeText(fullImage.this, uri.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(fullImage.this, "Failed to create Download URL", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                return true;
            case (R.id.delete_menu_item):
                Toast.makeText(this, "Deleting", Toast.LENGTH_SHORT).show();
                String imagePathName = getIntent().getStringExtra("metaData");
                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                StorageReference storageReference = firebaseStorage.getReference().child(Category+"/");
                storageReference.child(imagePathName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DeleteImage(uri);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(fullImage.this, "Failed to create Download URL", Toast.LENGTH_SHORT).show();
                    }
                });
                return true;

            default:

        }
        return super.onOptionsItemSelected(item);
    }

    public void downloadImage(Uri uri) {

        String nameFile = getIntent().getStringExtra("fileName");
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(nameFile);
        request.setDescription("Downloading " + nameFile);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.allowScanningByMediaScanner();
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name);
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);
    }

    public void DeleteImage(Uri uri) {

        final String key=getIntent().getStringExtra("key");
        FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
        StorageReference storageReference=firebaseStorage.getReferenceFromUrl(String.valueOf(uri));
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(fullImage.this, "Deleted", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(fullImage.this, "Failed to delete", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
