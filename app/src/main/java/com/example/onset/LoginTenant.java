package com.example.onset;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginTenant extends AppCompatActivity {
    private TextView text1;
    private MaterialButton btn;
    private EditText email, password;
    FirebaseAuth mAuth;
    private ProgressBar progress;


    // @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_tenant);

        text1 = findViewById(R.id.signup);

        btn=findViewById(R.id.login1);
        mAuth= FirebaseAuth.getInstance();
        progress=findViewById(R.id.progressbar);
        email=findViewById(R.id.Email_Address);
        password=findViewById(R.id.passwd);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is already logged in, redirect to the homepage
            goToHomePage();
        }


        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpTenant.class);
                startActivity(intent);
                finish();
            }
        });



        //Authentication
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email, Password;
                Email=String.valueOf(email.getText());
                Password=String.valueOf(password.getText());

                if(TextUtils.isEmpty(Email) || TextUtils.isEmpty(Password)){

                    Toast.makeText(LoginTenant.this, "All Fields Required !",Toast.LENGTH_SHORT).show();
                    return;
                }


                mAuth.signInWithEmailAndPassword(Email, Password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progress.setVisibility(View.GONE);
                                if (task.isSuccessful()) {

                                    Toast.makeText(LoginTenant.this, "Login Successful",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(getApplicationContext(), HomePage.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    // If sign in fails, display a message to the user.

                                    Toast.makeText(LoginTenant.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();


                                }
                            }
                        });


               /* if(!validateEmail() || !validatePassword()){

                }
                else{
                    progress.setVisibility(View.VISIBLE);
                    checkUser();

                }*/

            }
        });


    }

    private void goToHomePage() {
        Intent intent = new Intent(getApplicationContext(), HomePage.class);
        startActivity(intent);
        finish(); // Finish this activity so the user cannot go back to the login screen
    }

}
   /*public  boolean validateEmail(){
        String Email=email.getText().toString();
        String passwd=password.getText().toString();
        if(TextUtils.isEmpty(Email)){

            Toast.makeText(LoginTenant.this, "All Fields Required !",Toast.LENGTH_SHORT).show();
            return  false;

        }
        else{
            email.setError(null);
            return  true;
        }

    }

    public  boolean validatePassword(){

        String passwd=password.getText().toString();
        if(TextUtils.isEmpty(passwd)){

            Toast.makeText(LoginTenant.this, "All Fields Required !",Toast.LENGTH_SHORT).show();
            return  false;

        }
        else{
            password.setError(null);
            return  true;
        }

    }
    public void checkUser() {
        String userEmail = email.getText().toString().trim();
        String userPassword = password.getText().toString().trim();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("TenantUser");
        Query checkUser = databaseReference.orderByChild("email").equalTo(userEmail);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String passwdDB = userSnapshot.child("password").getValue(String.class);
                        if (passwdDB != null && passwdDB.equals(userPassword)) {
                            progress.setVisibility(View.GONE);
                            // Passwords match
                            email.setError(null);
                            Toast.makeText(LoginTenant.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), HomePage.class);
                            startActivity(intent);
                            finish();
                            return; // Exit method after successful login
                        }
                    }
                    // If the loop ends, it means no matching password was found
                    progress.setVisibility(View.GONE); // Hide progress bar
                    Toast.makeText(LoginTenant.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    password.requestFocus();
                }
                else {
                    // User not found
                    progress.setVisibility(View.GONE); // Hide progress bar
                    Toast.makeText(LoginTenant.this, "User Unknown", Toast.LENGTH_SHORT).show();
                    email.requestFocus();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event
                progress.setVisibility(View.GONE); // Hide progress bar in case of cancellation
            }
        });
    }*/

