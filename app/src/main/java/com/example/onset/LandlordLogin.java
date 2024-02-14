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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LandlordLogin extends AppCompatActivity {
    private TextView text;
    private MaterialButton btn;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    private EditText Email,Passwd;
    public  void onStart(){
        super.onStart();
        FirebaseUser currentUser=mAuth.getCurrentUser();
        if(currentUser!=null){
            Intent intent=new Intent(LandlordLogin.this, DashboardLandlord.class);
            startActivity(intent);
            finish();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landlord_login);

        text = findViewById(R.id.signup1);
        mAuth = FirebaseAuth.getInstance();
        btn=findViewById(R.id.login2);
        Email=findViewById(R.id.emailLogin);
        Passwd=findViewById(R.id.passLogin);

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandlordLogin.this, SignupLandlord.class);
                startActivity(intent);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Emails, Password;
                Emails=String.valueOf(Email.getText());
                Password=String.valueOf(Passwd.getText());

                if(TextUtils.isEmpty(Emails) || TextUtils.isEmpty(Password)){

                    Toast.makeText(LandlordLogin.this, "All Fields Required !",Toast.LENGTH_SHORT).show();
                    return;
                }


                mAuth.signInWithEmailAndPassword(Emails, Password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        String userID = user.getUid(); // Get the userID

                                        /* Check if the role matches
                                        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                                        String storedRole = sharedPref.getString("role", "");
                                        if (!storedRole.equals("") && !storedRole.equals("landlord")) {
                                            // Show an error message or prevent login

                                            return;
                                        }*/

                                        Toast.makeText(LandlordLogin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), DashboardLandlord.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(LandlordLogin.this, "User is null", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(LandlordLogin.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                   /* if(!validateEmail() || !validatePassword()){

                    }
                    else{
                      //  progress.setVisibility(View.VISIBLE);
                        checkUser();

                    } */

                }

        });

    }

}
    /*public  boolean validateEmail(){
        String Email1=Email.getText().toString();
        String passwd=Passwd.getText().toString();
        if(TextUtils.isEmpty(Email1)){

            Toast.makeText(LandlordLogin.this, "All Fields Required !",Toast.LENGTH_SHORT).show();
            return  false;

        }
        else{
            // Email1.setError(null);
            return  true;
        }

    }
    public  boolean validatePassword(){

        String passwd=Passwd.getText().toString();
        if(TextUtils.isEmpty(passwd)){

            Toast.makeText(LandlordLogin.this, "All Fields Required !",Toast.LENGTH_SHORT).show();
            return  false;

        }
        else{
            //  password.setError(null);
            return  true;
        }

    }
    public void checkUser() {
        String userEmail = Email.getText().toString().trim();
        String userPassword = Passwd.getText().toString().trim();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("LandlordUser");
        Query checkUser = databaseReference.orderByChild("email").equalTo(userEmail);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String passwdDB = userSnapshot.child("password").getValue(String.class);
                        if (passwdDB != null && passwdDB.equals(userPassword)) {
                            //  progress.setVisibility(View.GONE);
                            // Passwords match
                            //   email.setError(null);
                            Toast.makeText(LandlordLogin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), DashboardLandlord.class);
                            startActivity(intent);
                            finish();
                            return; // Exit method after successful login
                        }
                    }
                    // If the loop ends, it means no matching password was found
                    //    progress.setVisibility(View.GONE); // Hide progress bar
                    Toast.makeText(LandlordLogin.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    // password.requestFocus();
                }
                else {
                    // User not found
                    // progress.setVisibility(View.GONE); // Hide progress bar
                    Toast.makeText(LandlordLogin.this, "User Unknown", Toast.LENGTH_SHORT).show();
                    //email.requestFocus();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event
                //   progress.setVisibility(View.GONE); // Hide progress bar in case of cancellation
            }
        });
*/
