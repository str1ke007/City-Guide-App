// src/com.example.yourappname/MainActivity.java
package com.jkh.cityguideapp.CreateLocation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jkh.cityguideapp.Models.Location;
import com.jkh.cityguideapp.R;

public class CreateLocationActivity extends AppCompatActivity {

    private EditText editTextName, editTextDescription, editTextCategory;
    private Button buttonUpload, buttonSelectImage;
    private ImageView imageViewThumbnail;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_location);

        editTextName = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextCategory = findViewById(R.id.categoryEditText);
        buttonUpload = findViewById(R.id.buttonUpload);
        buttonSelectImage = findViewById(R.id.buttonSelectImage);
        imageViewThumbnail = findViewById(R.id.imageViewThumbnail);

        databaseReference = FirebaseDatabase.getInstance().getReference("locations");
        storageReference = FirebaseStorage.getInstance().getReference("your_storage_node");

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadData();
            }
        });

        buttonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageChooser();
            }
        });
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                // Load the selected image into the ImageView
                imageViewThumbnail.setImageURI(imageUri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadData() {
        // Retrieve values from EditText fields
        String name = editTextName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String category = editTextCategory.getText().toString().trim();

        // Check if the fields are not empty
        if (!name.isEmpty() && !description.isEmpty() && imageUri != null) {
            // Upload image to Firebase Storage
            StorageReference imageRef = storageReference.child("images/" + System.currentTimeMillis() + ".jpg");
            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Get the download URL of the uploaded image
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            // Create a data object to be uploaded to the database
                            Location data = new Location();
                            data.setName(name);
                            data.setDescription(description);
                            data.setCategory(category);
                            data.setThumbnail(uri.toString());

                            databaseReference.child(data.getId()).setValue(data);

                            finish();
                            // Display a success message
                            Toast.makeText(CreateLocationActivity.this, "Location added sucessfully", Toast.LENGTH_SHORT).show();
                        });
                    })
                    .addOnFailureListener(exception -> {
                        // Handle unsuccessful uploads
                        Toast.makeText(CreateLocationActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                    });
        } else {
            // Display an error message if any field is empty
            Toast.makeText(CreateLocationActivity.this, "Please fill out all fields and select an image", Toast.LENGTH_SHORT).show();
        }
    }
}