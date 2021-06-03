package com.example.picforiaapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class UploadVideos extends AppCompatActivity {

    private static final int PICK_VIDEO = 1;
    ArrayList<Uri> videoList = new ArrayList<>();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ArrayList<String> strings;

    private Button chooseVideos,uploadVideos,showVideos;
    private TextView textView;


    private int uploadCount = 0;
    private ProgressBar mProgressBar;
    private Uri videoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_videos);
        mAuth.signInAnonymously();
        mProgressBar = findViewById(R.id.progressBar2);
        textView=findViewById(R.id.textViewSelection);
        chooseVideos=findViewById(R.id.button_chooseVideos);
        uploadVideos=findViewById(R.id.button_upload);
        showVideos=findViewById(R.id.button_showVideos);


        chooseVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UploadVideos.this, "please Select Video", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("video/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, PICK_VIDEO);
            }
        });



        uploadVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadFile();
            }
        });

        showVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadVideos.this, VideographyActivity.class);
                startActivity(intent);

            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_VIDEO) {
            {
                if (resultCode == RESULT_OK) {
                    if (data.getClipData() != null) {
                        int countClipData = data.getClipData().getItemCount();
                        int currentVideoSelect = 0;
                        while (currentVideoSelect < countClipData) {
                            videoUri = data.getClipData().getItemAt(currentVideoSelect).getUri();
                            videoList.add(videoUri);
                            currentVideoSelect = currentVideoSelect + 1;
                        }
                    } else {
                        Toast.makeText(this, "Error Choosing Videos", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    /*public void ChooseVideo(View view) {
        Toast.makeText(this, "please Select Video", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, PICK_VIDEO);
    }*/

    private void UploadFile() {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Videography");
        StorageReference ImageFolder = storageReference.child("Videos");
        for (uploadCount = 0; uploadCount < videoList.size(); uploadCount++) {
            mProgressBar.setVisibility(View.VISIBLE);
            Uri IndividualImage = videoList.get(uploadCount);
            String mimeType = getContentResolver().getType(IndividualImage);
            Cursor returnCursor =
                    getContentResolver().query(IndividualImage, null, null, null, null);

            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            returnCursor.moveToFirst();
            String fileName = returnCursor.getString(nameIndex);
            final StorageReference ImageName = ImageFolder.child(fileName);

            ImageName.putFile(IndividualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                    ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            mProgressBar.setProgress(0);
                            String url = String.valueOf(uri);
                            String fileName = ImageName.toString();
                            String metaData = taskSnapshot.getMetadata().getName();
                            Toast.makeText(UploadVideos.this, metaData, Toast.LENGTH_SHORT).show();

                            StoreLink(url, fileName, metaData);
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                            mProgressBar.setVisibility(View.GONE);
                        }
                    });
                }
            });
        }
    }

    private void StoreLink(String url, String fileName, String metaData) {
        /*String databaseValue = strings.get(0);*/
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Videography");
        DatabaseReference databaseReference = databaseReference1.child("Videos");
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Link", url);
        hashMap.put("FileName", fileName);
        hashMap.put("MetaData", metaData);
        databaseReference.push().setValue(hashMap);
        String s="Video Selected";
        textView.setText(s);
        videoList.clear();
    }

}