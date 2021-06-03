package com.example.picforiaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class LandscapeActivity extends AppCompatActivity implements RecyclerAdapter.onItemClickListener {


    private DatabaseReference reference;
    /*private StorageReference mstorageReference;*/
    private FirebaseStorage mStorage;
    private ArrayList<SerialName> imagesList;
    private ValueEventListener valueEventListener;
    private Context mContext = LandscapeActivity.this;
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private ValueEventListener mDBListener;
    private Dialog dialog;
    private ImageView imageView;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landscape);
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("Landscape Photography");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        /*mStorage=FirebaseStorage.getInstance();*/
        mStorage=FirebaseStorage.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Photography").child("LandscapePhotography");
        /*mstorageReference = FirebaseStorage.getInstance().getReference();*/
        imagesList = new ArrayList<>();

        init();

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


    private void init() {
        clearAll();
        /*Query query = reference.child("PortraitPhotography");*/
        mDBListener=reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                imagesList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SerialName images = new SerialName();
                    images.setLink(snapshot.child("Link").getValue().toString());
                    images.setKey(snapshot.getKey());
                    images.setMetaData(snapshot.child("MetaData").getValue().toString());
                    imagesList.add(images);
                }
                recyclerAdapter = new RecyclerAdapter(mContext, imagesList);
                recyclerView.setAdapter(recyclerAdapter);
                recyclerAdapter.setOnItemClickListener(LandscapeActivity.this);
                recyclerAdapter.notifyDataSetChanged();
                /*recyclerAdapter.setItemClickedListener(PortraitActivity.this);*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        reference.removeEventListener(mDBListener);

    }

    private void clearAll() {
        if (imagesList != null) {
            imagesList.clear();

            if (recyclerAdapter != null) {
                recyclerAdapter.notifyDataSetChanged();
            }
        }
        imagesList = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            case (R.id.upload_menu):
                Intent intent = new Intent(LandscapeActivity.this, UploadActivity.class);
                startActivity(intent);
                return true;

            default:

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int position) {

        String fileName=imagesList.get(position).getMetaData();
        String link = imagesList.get(position).getLink();
        /*String fileName = imagesList.get(position).getFileName();*/
        String metaData = imagesList.get(position).getMetaData();
        String key = imagesList.get(position).getKey();
        Intent intent = new Intent(mContext, fullImage.class);
        intent.putExtra("pictureLink", link);
        intent.putExtra("fileName", fileName);
        intent.putExtra("metaData", metaData);
        intent.putExtra("key", key);
        intent.putExtra("Category","LandscapePhotography");
        mContext.startActivity(intent);
    }

    @Override
    public void DeleteClick(int position) {

        String fileName=imagesList.get(position).getMetaData();
        SerialName serialPosition=imagesList.get(position);
        final String key=serialPosition.getKey();
        Toast.makeText(mContext, "Pre Key: "+key, Toast.LENGTH_SHORT).show();
        StorageReference imageRef=mStorage.getReferenceFromUrl(serialPosition.getLink());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                reference.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(LandscapeActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                        /*Toast.makeText(PortraitActivity.this, "File Deleted"+fileName, Toast.LENGTH_SHORT).show();*/
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mContext, "Failed to delete", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mContext, "Something is Wrong", Toast.LENGTH_SHORT).show();
            }
        });

        /*Toast.makeText(mContext, "File Name: "+fileName, Toast.LENGTH_SHORT).show();*/

    }
}
