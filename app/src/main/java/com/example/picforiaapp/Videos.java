package com.example.picforiaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

public class Videos extends AppCompatActivity {
    private DatabaseReference reference;
    private FirebaseStorage mStorage;
    private ArrayList<SerialName> videosList;
    private ValueEventListener valueEventListener;
    private Context mContext = Videos.this;
    private RecyclerView recyclerView;
    private RecyclerVideoAdapter recyclerAdapter;
    private ValueEventListener mDBListener;
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("Videos");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        recyclerView = findViewById(R.id.recyclerView1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mStorage=FirebaseStorage.getInstance();
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Videography");
        reference = databaseReference.child("Videos");
        videosList=new ArrayList<>();
        init();
    }

    private void init() {
        clearAll();
        mDBListener=reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                videosList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SerialName images = new SerialName();
                    images.setLink(snapshot.child("Link").getValue().toString());
                    images.setKey(snapshot.getKey());
                    images.setMetaData(snapshot.child("MetaData").getValue().toString());
                    videosList.add(images);
                }
                recyclerAdapter = new RecyclerVideoAdapter(videosList, mContext);
                recyclerView.setAdapter(recyclerAdapter);
               /* recyclerAdapter.setOnItemClickListener(Videos.this);*/
                recyclerAdapter.notifyDataSetChanged();
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
        if (videosList != null) {
            videosList.clear();
            if (recyclerAdapter != null) {
                recyclerAdapter.notifyDataSetChanged();
            }
        }
        videosList = new ArrayList<>();
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
                Intent intent = new Intent(Videos.this, UploadActivity.class);
                startActivity(intent);
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    /*@Override
    public void onItemClick(int position) {

        *//*String fileName=videosList.get(position).getMetaData();
        String link = videosList.get(position).getLink();
        String metaData = videosList.get(position).getMetaData();
        String key = videosList.get(position).getKey();
        Intent intent = new Intent(mContext, fullImage.class);
        intent.putExtra("pictureLink", link);
        intent.putExtra("fileName", fileName);
        intent.putExtra("metaData", metaData);
        intent.putExtra("key", key);
        intent.putExtra("Category","PortraitPhotography");
        mContext.startActivity(intent);*//**//*
        String link = videosList.get(position).getLink();
        Toast.makeText(mContext, link, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void DeleteClick(int position) {

        String fileName=videosList.get(position).getMetaData();
        SerialName serialPosition=videosList.get(position);
        final String key=serialPosition.getKey();

        StorageReference imageRef=mStorage.getReferenceFromUrl(serialPosition.getLink());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                reference.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Videos.this, "Deleted", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Videos.this,PortraitActivity.class));
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
        });*//*
    }*/
}