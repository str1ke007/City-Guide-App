package com.jkh.cityguideapp.User;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jkh.cityguideapp.CreateLocation.CreateLocationActivity;
import com.jkh.cityguideapp.HelperClasses.HomeAdapter.CategoriesAdapter;
import com.jkh.cityguideapp.HelperClasses.HomeAdapter.CategoriesHelperClass;
import com.jkh.cityguideapp.HelperClasses.HomeAdapter.FeaturedAdapter;
import com.jkh.cityguideapp.HelperClasses.HomeAdapter.MostViewedAdapter;
import com.jkh.cityguideapp.LocationsList.LocationsListActivity;
import com.jkh.cityguideapp.Models.Location;
import com.jkh.cityguideapp.R;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class UserDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Variables
    LinearLayout contentView;
    static final float END_SCALE = 0.5f;
    RecyclerView featuredRecycler, mostViewedRecycler, categoriesRecycler;
    private GradientDrawable gradient1, gradient2, gradient3, gradient4;
    ImageView menuIcon;

    // Drawer Menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    RecyclerView.Adapter adapter;

    FeaturedAdapter featuredAdapter;

    MostViewedAdapter mostViewedAdapter;

    public UserDashboard() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_dashboard);

        // Hooks
        featuredRecycler = findViewById(R.id.featured_recycler);
        mostViewedRecycler = findViewById(R.id.most_viewed_recycler);
//        categoriesRecycler = findViewById(R.id.categories_recycler1);
        menuIcon = findViewById(R.id.menu_icon);
        contentView = findViewById(R.id.content);
        ImageButton addLocationButton = findViewById(R.id.btnAdd);
        addLocationButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserDashboard.this, CreateLocationActivity.class);
                startActivity(intent);
            }
        });


        // Menu Hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        navigationDrawer();

        // Recycler View Function calls
        featuredRecycler();
        mostViewedRecycler();
//        categoriesRecycler();

        attachClickListeners();

        DatabaseReference locationRef = FirebaseDatabase.getInstance().getReference("locations");
        locationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mostViewedAdapter.notifyDataSetChanged();
                featuredAdapter.notifyDataSetChanged();

//                mostViewedAdapter.startListening();  // Add this line
//                featuredAdapter.startListening();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void attachClickListeners() {
        ImageButton restaurant = findViewById(R.id.category_restaurant);
        ImageButton education = findViewById(R.id.category_education);
        ImageButton shop = findViewById(R.id.category_shopping);
        ImageButton hospital = findViewById(R.id.category_hospital);

        restaurant.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(restaurant.getContext(), LocationsListActivity.class);
                        intent.putExtra("category", "restaurant");
                        startActivity(intent);
                    }
                }
        );

        education.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(education.getContext(), LocationsListActivity.class);
                        intent.putExtra("category", "education");
                        startActivity(intent);
                    }
                }
        );

        shop.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(shop.getContext(), LocationsListActivity.class);
                        intent.putExtra("category", "shopping");
                        startActivity(intent);
                    }
                }
        );

        hospital.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(hospital.getContext(), LocationsListActivity.class);
                        intent.putExtra("category", "hospital");
                        startActivity(intent);
                    }
                }
        );
    }

    // Navigation Drawer Functions
    private void navigationDrawer() {

        // Navigation Drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        animateNavigationDrawer();
    }

    private void animateNavigationDrawer() {

        drawerLayout.setScrimColor(getResources().getColor(R.color.colorPrimary));
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset

                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;

                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }

    // Recycler View Functions
    private void categoriesRecycler() {

        // All Gradients for this view are

        gradient1 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xff7adccf, 0xff7adccf});
        gradient2 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffd4cbe5, 0xffd4cbe5});
        gradient3 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xfff7c59f, 0xFFf7c59f});
        gradient4 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffb8d7f5, 0xffb8d7f5});


        ArrayList<CategoriesHelperClass> categoriesHelperClasses = new ArrayList<>();

        categoriesHelperClasses.add(new CategoriesHelperClass(gradient1, R.drawable.school_image, "education"));
        categoriesHelperClasses.add(new CategoriesHelperClass(gradient2, R.drawable.hospital_image, "hospital"));
        categoriesHelperClasses.add(new CategoriesHelperClass(gradient3, R.drawable.restaurant_image, "restaurant"));
        categoriesHelperClasses.add(new CategoriesHelperClass(gradient4, R.drawable.shopping_image, "shopping"));
        categoriesHelperClasses.add(new CategoriesHelperClass(gradient1, R.drawable.transport_image, "transport"));

        categoriesRecycler.setHasFixedSize(true);

        adapter = new CategoriesAdapter(categoriesHelperClasses);
        categoriesRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        categoriesRecycler.setAdapter(adapter);
    }

    private void mostViewedRecycler() {

        mostViewedRecycler.setHasFixedSize(true);
        mostViewedRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query featureLocationsQuery = reference.child("locations").orderByChild("views");
        FirebaseRecyclerOptions<Location> options = new FirebaseRecyclerOptions.Builder<Location>()
                .setQuery(featureLocationsQuery, Location.class)
                .build();

        mostViewedAdapter = new MostViewedAdapter(options);
        mostViewedRecycler.setAdapter(mostViewedAdapter);
    }

    private void featuredRecycler() {

        // All Gradients for this view are
//        GradientDrawable gradient1 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffeff400, 0xffaff600});

        featuredRecycler.setHasFixedSize(true);
        featuredRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query featureLocationsQuery = reference.child("locations").orderByChild("featured").equalTo(true);
        FirebaseRecyclerOptions<Location> options = new FirebaseRecyclerOptions.Builder<Location>()
                .setQuery(featureLocationsQuery, Location.class)
                .build();

        featuredAdapter = new FeaturedAdapter(options);
        featuredRecycler.setAdapter(featuredAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        featuredAdapter.startListening();
        mostViewedAdapter.startListening();
    }
}