package com.example.onset;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfileLandlord extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private String userID;
    private ShapeableImageView imageView;
    private MaterialButton button;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_landlord);

        imageView = findViewById(R.id.imageview);
        button = findViewById(R.id.editzprofile);
        TextView phone = findViewById(R.id.phonenumber1);
        TextView email = findViewById(R.id.email);
        TextView apartment = findViewById(R.id.apartmentcount);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        userID = auth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference();

        firestore.collection("Apartments")
                .whereEqualTo("ownerId", userID) // Assuming landlordId is a field in your apartment document
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int count = queryDocumentSnapshots.size();
                    apartment.setText(String.valueOf(count));

                })
                .addOnFailureListener(e -> {

                });

        DocumentReference reference = firestore.collection("Landlord").document(userID);
        reference.addSnapshotListener((value, error) -> {
            if (value != null && value.exists()) {
                email.setText(value.getString("Email"));
                Long phoneLong = value.getLong("Phone");
                if (phoneLong != null) {
                    phone.setText(String.valueOf(phoneLong));
                }
                loadProfileImage(value.getString("ProfileImage"));
            } else {
                // Handle the case where the document does not exist
            }
        });

       // button.setOnClickListener(v -> openGallery());

        imageView.setOnClickListener(v -> openGallery());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            imageView.setImageURI(imageUri);
            uploadImage(imageUri);
        }
    }

    private void uploadImage(Uri imageUri) {
        StorageReference fileRef = storageReference.child("ProfileLandlord.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();
                firestore.collection("Landlord").document(userID)
                        .update("ProfileImage", imageUrl)
                        .addOnSuccessListener(aVoid -> Toast.makeText(ProfileLandlord.this, "Profile Image Set", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(ProfileLandlord.this, "Failed to update profile image URL", Toast.LENGTH_SHORT).show());
            });
        }).addOnFailureListener(e -> Toast.makeText(ProfileLandlord.this, "Failed to upload image", Toast.LENGTH_SHORT).show());
    }

    private void loadProfileImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .into(imageView);
        }
    }
}
