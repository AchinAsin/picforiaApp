package com.example.picforiaapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.List;

public class RecyclerVideoAdapter extends RecyclerView.Adapter<RecyclerVideoAdapter.MyViewHolder> {

    List<SerialName> list;
    Context context;

    public RecyclerVideoAdapter(List<SerialName> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        /*RequestOptions myOptions = new RequestOptions()
                .override(120, 120);
        Glide.with(context)
                .asBitmap()
                .apply(myOptions)
                .load(list.get(position).getLink())
                .into(holder.imageView);*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        PlayerView playerView;
        ProgressDialog progDailog;
        private SimpleExoPlayer player;
        private PlayerControlView playerControlView;
        private boolean playWhenReady = true;
        private int currentWindow = 0;
        private long playbackPosition = 0;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            /*imageView=itemView.findViewById(R.id.thumbnail);*/
            playerView = itemView.findViewById(R.id.video_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getBindingAdapterPosition();
                    String uri = list.get(pos).getLink();
                    /*Toast.makeText(context, uri, Toast.LENGTH_SHORT).show();*/
                    Toast.makeText(context, "Your Video is Being Played", Toast.LENGTH_SHORT).show();
                    initializePlayer(uri);
                }
            });
        }

        private void initializePlayer(String uri) {
            player = new SimpleExoPlayer.Builder(context).build();
            playerView.setPlayer(player);
            MediaItem mediaItem = MediaItem.fromUri(uri);
            player.setMediaItem(mediaItem);
            player.setPlayWhenReady(playWhenReady);
            player.seekTo(currentWindow, playbackPosition);
            player.prepare();

        }
    }

}
