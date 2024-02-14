package com.example.onset;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SignupLandlord extends AppCompatActivity {
    private MaterialButton btn;
    private EditText name, email, phone, password;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    String userID;
    @Override
    public  void onStart(){
        super.onStart();
        FirebaseUser currentUser=mAuth.getCurrentUser();
        if(currentUser!=null){
            Intent intent=new Intent(SignupLandlord.this, DashboardLandlord.class);
            startActivity(intent);
            finish();

        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_landlord);

        btn = findViewById(R.id.button2);
        mAuth = FirebaseAuth.getInstance();
        name = findViewById(R.id.nameLandlord);
        email = findViewById(R.id.emailLandlord);
        phone = findViewById(R.id.phoneLandlord);
        password = findViewById(R.id.passwordLandlord);
        firestore= FirebaseFirestore.getInstance();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Initialize Firebase
                String contactText = phone.getText().toString();
                String name1=String.valueOf(name.getText());
                String Email, Password;
                Email=String.valueOf(email.getText());
                Password=String.valueOf(password.getText());


                int Contact;
                try {
                    Contact = Integer.parseInt(contactText);
                } catch (NumberFormatException e) {
                    Toast.makeText(SignupLandlord.this, "Invalid Contact Number !", Toast.LENGTH_SHORT).show();
                   // progress.setVisibility(View.GONE);
                    return;
                }



                if (TextUtils.isEmpty(Email) || TextUtils.isEmpty(Password)) {

                    Toast.makeText(SignupLandlord.this, "All Fields Required !", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(Email, Password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        userID = user.getUid(); // Get the userID
                                        Toast.makeText(SignupLandlord.this, "SignIn Successful !", Toast.LENGTH_SHORT).show();

                                        // Check if the role matches
                                        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                                        String storedRole = sharedPref.getString("role", "");
                                        if (!storedRole.equals("") && !storedRole.equals("landlord")) {
                                            // Show an error message or prevent login
                                            return;
                                        }

                                        Intent intent = new Intent(SignupLandlord.this, DashboardLandlord.class);
                                        startActivity(intent);
                                        finish();
                                        DocumentReference reference = firestore.collection("Landlord").document(userID);
                                        Map<String, Object> landlord = new HashMap<>();
                                        landlord.put("Name", name1); // Use name1 instead of name
                                        landlord.put("Email", Email);
                                        landlord.put("Phone", Contact);

                                        reference.set(landlord)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        // Handle success
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        // Handle failure
                                                        Toast.makeText(SignupLandlord.this, "Authentication failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(SignupLandlord.this, "User is null", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(SignupLandlord.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }
        });
    }

}



    // Method to generate a random alphanumeric string of given length


/*
database = FirebaseDatabase.getInstance();

        databaseReference = database.getReference("LandlordUser");

        // Get user input
        String Name = name.getText().toString();
        String Emails = email.getText().toString();
        String Passwd = password.getText().toString();
        String contactText = phone.getText().toString();

        // Validate input
        if (TextUtils.isEmpty(Name) || TextUtils.isEmpty(Emails) || TextUtils.isEmpty(Passwd) || TextUtils.isEmpty(contactText)) {
        Toast.makeText(SignupLandlord.this, "All Fields Required !", Toast.LENGTH_SHORT).show();
        return;
        }

        // Convert contact number to integer
        int Contact;
        try {
        Contact = Integer.parseInt(contactText);
        } catch (NumberFormatException e) {
        Toast.makeText(SignupLandlord.this, "Invalid Contact Number !", Toast.LENGTH_SHORT).show();
        return;
        }

        // Generate a unique ID
        String uniqueID = generateUniqueID(10);

        // Create a ControllerLogin object with user details
        ControllerLogin controllerLogin = new ControllerLogin(Name, Emails, Contact, Passwd, uniqueID);

        // Store user details in Firebase Realtime Database
        databaseReference.child(uniqueID).setValue(controllerLogin);

        // Show success message and navigate to dashboard
        Toast.makeText(SignupLandlord.this, "Sign Up Successful !", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SignupLandlord.this, DashboardLandlord.class);
        startActivity(intent);
        finish();

         private String generateUniqueID(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder uniqueID = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < length; i++) {
            uniqueID.append(characters.charAt(rnd.nextInt(characters.length())));
        }
        return uniqueID.toString();
    }

        */
