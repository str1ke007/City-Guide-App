package com.jkh.cityguideapp.HelperClasses.HomeAdapter;


import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.jkh.cityguideapp.Models.Location;
import com.jkh.cityguideapp.R;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class MostViewedAdapter extends FirebaseRecyclerAdapter<Location, MostViewedAdapter.MostViewedViewHolder> {
    public MostViewedAdapter(@NonNull FirebaseRecyclerOptions<Location> options) {
        super(options);
    }

    @NonNull
    @Override
    public MostViewedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.most_viewed_card_design, parent, false);
        MostViewedViewHolder mostViewedViewHolder = new MostViewedViewHolder(view);
        return mostViewedViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MostViewedViewHolder holder, int position, Location location) {
        Glide.with(holder.imageView.getContext()).load(location.getThumbnail()).into(holder.imageView);
        holder.textView.setText(location.getName());
        holder.descriptionView.setText(location.getDescription());
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    public static class MostViewedViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        TextView descriptionView;

        public MostViewedViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.mv_image);
            textView = itemView.findViewById(R.id.mv_title);
            descriptionView = itemView.findViewById(R.id.mv_description);
        }
    }
}