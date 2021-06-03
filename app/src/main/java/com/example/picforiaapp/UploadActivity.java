package com.example.picforiaapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class UploadActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    ArrayList<Uri> ImageList = new ArrayList<>();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ArrayList<String> photographyCategory;
    ArrayAdapter<String> photographyStyle;
    ArrayList<String> strings;
    private Button chooseimage, uploadimage, showImages;
    private ImageView imageView;
    private int uploadcount = 0;
    private ProgressBar mProgressBar;
    private Uri ImageUri;
    private AutoCompleteTextView autoCompleteTextView;
    private TextInputLayout textInputLayout;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        mAuth.signInAnonymously();
        chooseimage = findViewById(R.id.button_chooseImage);
        uploadimage = findViewById(R.id.button_upload);
        mProgressBar = findViewById(R.id.progressBar2);
        showImages = findViewById(R.id.button_showImages);
        imageView = findViewById(R.id.imageView);
        textInputLayout = findViewById(R.id.dropdown_menu);
        autoCompleteTextView = findViewById(R.id.dropdown_menu_textView);
        photographyCategory = new ArrayList<>();
        strings = new ArrayList<>();
        photographyCategory.add("PortraitPhotography");
        photographyCategory.add("LandscapePhotography");
        photographyCategory.add("NightPhotography");
        photographyCategory.add("SunsetPhotography");
        photographyCategory.add("StreetPhotography");
        photographyCategory.add("WildlifePhotography");

        photographyStyle = new ArrayAdapter<>(getApplicationContext(), R.layout.drop_down_menu, photographyCategory);
        autoCompleteTextView.setAdapter(photographyStyle);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = parent.getItemAtPosition(position);
                String name = o.toString();
                Toast.makeText(UploadActivity.this, "Category Name: " + name, Toast.LENGTH_SHORT).show();
                strings.clear();
                strings.add(name);
            }
        });

        showImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadActivity.this, PhotographyActivity.class);
                startActivity(intent);

            }
        });

        uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadFile();
            }
        });
    }

    private void UploadFile() {
        String databaseValue = strings.get(0);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Photography");
        StorageReference ImageFolder = storageReference.child(databaseValue);
        for (uploadcount = 0; uploadcount < ImageList.size(); uploadcount++) {
            mProgressBar.setVisibility(View.VISIBLE);
            Uri IndividualImage = ImageList.get(uploadcount);
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
                            Toast.makeText(UploadActivity.this, metaData, Toast.LENGTH_SHORT).show();

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
        String databaseValue = strings.get(0);
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Photography");
        DatabaseReference databaseReference = databaseReference1.child(databaseValue);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Link", url);
        hashMap.put("FileName", fileName);
        hashMap.put("MetaData", metaData);
        databaseReference.push().setValue(hashMap);
        Glide.with(this).load(url).into(imageView);
        ImageList.clear();
    }

    public void ChooseImage(View view) {
        Toast.makeText(this, "please select more than 1 Images", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            {
                if (resultCode == RESULT_OK) {
                    if (data.getClipData() != null) {
                        int countClipData = data.getClipData().getItemCount();
                        int currentImageSelect = 0;
                        while (currentImageSelect < countClipData) {
                            ImageUri = data.getClipData().getItemAt(currentImageSelect).getUri();
                            ImageList.add(ImageUri);
                            currentImageSelect = currentImageSelect + 1;
                        }
                    } else {
                        Toast.makeText(this, "Error Choosing Multiple Images", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}
