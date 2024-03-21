package com.example.onset;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ManageApartment extends AppCompatActivity {
    private ListView listView;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private String ownerID;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> arrayAdapter;
    private TextView textViewDetails,Priceapartment,description,type,location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_apartment);

        listView = findViewById(R.id.listViewApartments);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        ownerID = auth.getCurrentUser().getUid();
        arrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(ManageApartment.this, R.layout.listviewapartment, R.id.textViewApartment, arrayList);
        listView.setAdapter(arrayAdapter);

        fetchApartments();
    }

    private void fetchApartments() {
        firestore.collection("Apartments")
                .whereEqualTo("ownerId", ownerID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            String apartmentName = document.getString("name");
                            arrayList.add(apartmentName);
                        }
                        arrayAdapter.notifyDataSetChanged();
                        // Update the ListView
                    } else {
                        Toast.makeText(ManageApartment.this, "Failed to fetch apartments", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showApartmentDetailsDialog(DocumentSnapshot document) {
        // Get apartment details
        String apartmentDetails = document.getString("name");
        String rent=document.getString("price");
        String describe=document.getString("description");
        String type1=document.getString("roomNumber");
        String locate=document.getString("location");
        String imageUrl = document.getString("ApartmentImage");


        // Inflate the custom layout for the dialog
        View dialogView = getLayoutInflater().inflate(R.layout.popupbox, null);

        // Initialize views in the dialog
        ImageView imageView = dialogView.findViewById(R.id.imageViewEdit);
        textViewDetails = dialogView.findViewById(R.id.textViewDetails);
        Priceapartment=dialogView.findViewById(R.id.PriceApartment);
        description=dialogView.findViewById(R.id.Description);
        type=dialogView.findViewById(R.id.Type);
        location=dialogView.findViewById(R.id.Location);


        Button buttonEdit = dialogView.findViewById(R.id.buttonEdit);
        Button buttonDelete = dialogView.findViewById(R.id.buttonDelete);

        // Set apartment details and image
        textViewDetails.setText(apartmentDetails);
        Priceapartment.setText(rent);
        description.setText(describe);
        type.setText(type1);
        location.setText(locate);
        Glide.with(this).load(imageUrl).into(imageView);

        // Create the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        // Set click listeners for edit and delete buttons
        buttonEdit.setOnClickListener(v -> {
            // Handle edit button click
            // For example, navigate to an edit activity
            Intent intent = new Intent(v.getContext(), Editprofile.class);
            intent.putExtra("apartmentId", document.getId());
            intent.putExtra("name", textViewDetails.getText());
            intent.putExtra("price", Priceapartment.getText());
            intent.putExtra("description", description.getText());
            intent.putExtra("roomNumber", type.getText());
            intent.putExtra("location", location.getText());
            intent.putExtra("imageUrl", imageUrl);


            // Pass the apartment ID to the edit activity
            startActivity(intent);
            dialog.dismiss();
        });

        buttonDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(ManageApartment.this)
                    .setTitle("Confirm Delete")
                    .setMessage("Are you sure you want to delete this apartment?")
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        deleteApartment(document.getId()); // Call method to delete apartment
                        dialog.dismiss();
                    })
                    .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss())
                    .show();
            // Handle delete button click
            // For example, show a confirmation dialog and delete the apartment
            dialog.dismiss();
        });

        // Show the dialog
        dialog.show();
    }
    private void deleteApartment(String apartmentId) {
        // Code to delete the apartment from the database
        firestore.collection("Apartments")
                .document(apartmentId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ManageApartment.this, "Apartment deleted successfully", Toast.LENGTH_SHORT).show();
                    // Refresh the list of apartments
                    arrayList.clear();
                    fetchApartments();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ManageApartment.this, "Failed to delete apartment", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Set item click listener for the ListView
        listView.setOnItemClickListener((parent, view, position, id) -> {
            // Get the selected apartment name
            String apartmentName = arrayList.get(position);

            // Fetch the apartment document
            firestore.collection("Apartments")
                    .whereEqualTo("name", apartmentName)
                    .whereEqualTo("ownerId", ownerID)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            // Get the document snapshot
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                            // Show the apartment details dialog
                            showApartmentDetailsDialog(document);
                        } else {
                            Toast.makeText(ManageApartment.this, "Failed to fetch apartment details", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
