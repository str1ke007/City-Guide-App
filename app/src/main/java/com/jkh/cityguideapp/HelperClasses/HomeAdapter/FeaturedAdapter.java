package com.jkh.cityguideapp.HelperClasses.HomeAdapter;

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

import java.util.List;

public class FeaturedAdapter extends FirebaseRecyclerAdapter<Location, FeaturedAdapter.FeaturedViewHolder> {
    @NonNull
    @Override
    public FeaturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.featured_card_design,parent,false);
        return new FeaturedViewHolder(view);
    }

    public FeaturedAdapter(@NonNull FirebaseRecyclerOptions<Location> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FeaturedViewHolder holder, int position, @NonNull Location model) {
        Glide.with(holder.image.getContext()).load(model.getThumbnail()).into(holder.image);
        holder.title.setText(model.getName());
        holder.desc.setText(model.getDescription());
    }

    public static class FeaturedViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title, desc;

        public FeaturedViewHolder(@NonNull View itemView) {
            super(itemView);

            // Hooks
            image = itemView.findViewById(R.id.featured_image);
            title = itemView.findViewById(R.id.featured_title);
            desc = itemView.findViewById(R.id.featured_desc);
        }

        public ImageView getImage() {
            return image;
        }

        public TextView getTitle() {
            return title;
        }

        public TextView getDesc() {
            return desc;
        }
    }
}
