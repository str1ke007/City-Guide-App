package com.jkh.cityguideapp.LocationsList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jkh.cityguideapp.Models.Location;
import com.jkh.cityguideapp.R;

public class LocationsListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;

    private FirebaseRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locations_list);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseReference = FirebaseDatabase.getInstance().getReference().child("locations");

        Intent intent = getIntent();
        String category = intent.getStringExtra("category");
        Query queryRef = databaseReference.orderByChild("category").equalTo(category);
        FirebaseRecyclerOptions<Location> options =
                new FirebaseRecyclerOptions.Builder<Location>()
                        .setQuery(queryRef, Location.class)
                        .build();

        this.adapter =
                new FirebaseRecyclerAdapter<Location, LocationViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull LocationViewHolder holder, int position, @NonNull Location model) {
                        // Bind the data to the ViewHolder
                        holder.setThumbnail(model.getThumbnail());
                        holder.setPlaceName(model.getName());
                        holder.setDescription(model.getDescription());
                    }

                    @NonNull
                    @Override
                    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location, parent, false);
                        return new LocationViewHolder(view);
                    }
                };

        recyclerView.setAdapter(this.adapter);
        queryRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Define the ViewHolder for the Location items
    public static class LocationViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageViewThumbnail;
        private final TextView textViewPlaceName;
        private final TextView textViewDescription;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewThumbnail = itemView.findViewById(R.id.imageViewThumbnail);
            textViewPlaceName = itemView.findViewById(R.id.textViewPlaceName);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
        }

        public void setThumbnail(String url) {
            Glide.with(imageViewThumbnail.getContext()).load(url).into(imageViewThumbnail);
        }

        public void setPlaceName(String placeName) {
            textViewPlaceName.setText(placeName);
        }

        public void setDescription(String description) {
            textViewDescription.setText(description);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        this.adapter.startListening();
    }
}
