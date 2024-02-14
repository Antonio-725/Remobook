package com.example.onset;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class NewApartment extends AppCompatActivity {
    private CardView selectPhoto;
    private EditText apartmentName,description,price,location,roomnumber;
    private CheckBox box1,box2,box3,box4;
    private RadioButton monthly,yearly;
    MaterialButton button;
    private String ownerID;
    private Uri selectedImageUri;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_apartment);
        Spinner spinner=findViewById(R.id.spinnerApartment);
        selectPhoto=findViewById(R.id.gallery);
        apartmentName=findViewById(R.id.edit);
        description=findViewById(R.id.editDescribe);
        price=findViewById(R.id.price2);
        location=findViewById(R.id.locationtext);
        roomnumber=findViewById(R.id.rooms);
        box1=findViewById(R.id.security);
        box2=findViewById(R.id.water);
        box3=findViewById(R.id.parking);
        box4=findViewById(R.id.wifi);
        monthly=findViewById(R.id.radio1);
        yearly=findViewById(R.id.radio2);
        button=findViewById(R.id.submit);

        retrieveOwnerID("owner@example.com");
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String item=adapterView.getItemAtPosition(position).toString();
                Toast.makeText(NewApartment.this, "Selected item" +item,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayList<String>arraylist=new ArrayList<>();
        arraylist.add("1 Bedroom");
        arraylist.add("2 Bedroom");
        arraylist.add("3 Bedroom");
        arraylist.add("Bedsitter");
        arraylist.add("Single Room");
        arraylist.add("Flat");

        ArrayAdapter<String>adapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,arraylist);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
                if (validateFields()) {
                    uploadApartmentDetails();
                }
            }
        });

    }
    private void retrieveOwnerID(String ownerId) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("LandlordUser");

        // Query the database to find the user node with the specified email
        Query query = databaseReference.orderByChild("email").equalTo(ownerId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Retrieve the user ID from the snapshot
                        ownerID = snapshot.getKey();
                    }
                } else {
                    // User with the specified email does not exist
                    Toast.makeText(NewApartment.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that may occur during the query
                Toast.makeText(NewApartment.this, "Database Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private  void pickImage(){
        Intent intent=new Intent();
        intent.setType("Image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

    }
    ActivityResultLauncher<Intent>launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    // Handle the result here
                    Intent data = result.getData();
                    selectedImageUri = result.getData().getData();
                    // Process the selected image
                }
            });
    private String generateRandomID() {
        // Generate a random ID of 6 characters
        // For simplicity, assuming it's an alphanumeric ID
        // Modify this method according to your requirements
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomID = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            randomID.append(characters.charAt(random.nextInt(characters.length())));
        }
        return randomID.toString();
    }
    private void uploadApartmentDetails() {
        // Generate a random apartment ID
        if (ownerID == null) {
            Toast.makeText(this, "Owner ID is null", Toast.LENGTH_SHORT).show();
            return;
        }

        String apartmentID = generateRandomID();

        // Create an Apartment object with all details
        Apartment apartment = new Apartment(
                apartmentID,
                ownerID, // Use owner's ID here
                apartmentName.getText().toString(),
                description.getText().toString(),
                price.getText().toString(),
                location.getText().toString(),
                roomnumber.getText().toString(),
                box1.isChecked(),
                box2.isChecked(),
                box3.isChecked(),
                box4.isChecked(),
                monthly.isChecked() ? "Monthly" : "Yearly"
        );

        // Upload apartment details to Firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Apartments");
        databaseReference.child(ownerID).child("apartments").child(apartmentID).setValue(apartment);
    }

    private boolean validateFields() {
        if (apartmentName.getText().toString().isEmpty() ||
                description.getText().toString().isEmpty() ||
                price.getText().toString().isEmpty() ||
                location.getText().toString().isEmpty() ||
                roomnumber.getText().toString().isEmpty() ||
                (!monthly.isChecked() && !yearly.isChecked())) {
            Toast.makeText(this, "Please fill all fields and select rent type", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }




}




