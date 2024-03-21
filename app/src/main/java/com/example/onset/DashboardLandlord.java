package com.example.onset;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DashboardLandlord extends AppCompatActivity {
    private CardView cardView1, cardView2, cardView3, cardView4;
    private ImageView imageView;
    private FirebaseAuth auth;
    private String userID;
    private FirebaseFirestore firestore;
    private ShapeableImageView shapeableImageView;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_landlord);

        cardView1 = findViewById(R.id.profilelandlord);
        cardView2 = findViewById(R.id.regNew);
        cardView3 = findViewById(R.id.viewBook);
        cardView4 = findViewById(R.id.manageApartment);
        imageView = findViewById(R.id.logoutIcon);
        shapeableImageView=findViewById(R.id.imageview2);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        userID = auth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference();

        DocumentReference reference = firestore.collection("Landlord").document(userID);
        reference.addSnapshotListener((value, error) -> {
            if (value != null && value.exists()) {
                TextView textView = findViewById(R.id.greeting);
                textView.setText("Hello " + value.getString("Name"));
                loadProfileImage(value.getString("ProfileImage"));
            }
        });

        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(this, LandlordLogin.class));
            finish();
        }

        imageView.setOnClickListener(v -> logout());

        cardView1.setOnClickListener(v -> startActivity(new Intent(DashboardLandlord.this, ProfileLandlord.class)));

        cardView2.setOnClickListener(v -> startActivity(new Intent(DashboardLandlord.this, NewApartment.class)));

        cardView3.setOnClickListener(v -> startActivity(new Intent(DashboardLandlord.this, ViewBookings.class)));

        cardView4.setOnClickListener(v -> startActivity(new Intent(DashboardLandlord.this, ManageApartment.class)));
    }

    private void logout() {
        auth.signOut();
        Toast.makeText(this, "You have Logged Out", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void loadProfileImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty() && this != null) {
            Glide.with(this)
                    .load(imageUrl)
                    .into(shapeableImageView);
        }
    }
}
