package com.example.onset;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

import org.checkerframework.checker.units.qual.C;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SignUpTenant extends AppCompatActivity {
    private MaterialButton btn;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    private ProgressBar progress;
    EditText fullname, contact, email, password ,passwordConfirm;
    DatabaseReference databaseReference;
    FirebaseFirestore firestore;
    String userID;


    @Override
    public  void onStart(){
        super.onStart();
        FirebaseUser currentUser=mAuth.getCurrentUser();
        if(currentUser!=null){
            Intent intent=new Intent(SignUpTenant.this, HomePage.class);
            startActivity(intent);
            finish();

        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_tenant);

        btn = findViewById(R.id.button1);
        fullname = findViewById(R.id.fullname);
        mAuth = FirebaseAuth.getInstance();
        progress = findViewById(R.id.progressbar1);
        contact = findViewById(R.id.contact);
        email = findViewById(R.id.emailReg);
        password = findViewById(R.id.password1);
        passwordConfirm=findViewById(R.id.password2);

        firestore= FirebaseFirestore.getInstance();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setVisibility(View.VISIBLE);
               // database = FirebaseDatabase.getInstance();
               // databaseReference = database.getReference("TenantUser");


                //String Emails = email.getText().toString();
                //String Passwd = password.getText().toString();
                String name = fullname.getText().toString().trim();
                String contactText = contact.getText().toString().trim();
                String Email, Password,Password2;
                Email=String.valueOf(email.getText());
                Password=String.valueOf(password.getText());
                Password2=String.valueOf(passwordConfirm.getText());


                int Contact;
                try {
                    Contact = Integer.parseInt(contactText);
                } catch (NumberFormatException e) {
                    Toast.makeText(SignUpTenant.this, "Invalid Contact Number !", Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.GONE);
                    return;
                }



                if (TextUtils.isEmpty(Email) || TextUtils.isEmpty(Password)) {

                    //Toast.makeText(SignUpTenant.this, "All Fields Required !", Toast.LENGTH_SHORT).show();
                    email.setError("This Field Required");
                    return;
                }
                else if(!Password.equals(Password2)){
                    Toast.makeText(SignUpTenant.this, "Passwords not matched !", Toast.LENGTH_SHORT).show();

                }

                mAuth.createUserWithEmailAndPassword(Email, Password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progress.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        userID = user.getUid(); // Get the userID
                                        Toast.makeText(SignUpTenant.this, "SignIn Successful !", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignUpTenant.this, HomePage.class);
                                        startActivity(intent);
                                        finish();

                                        DocumentReference reference = firestore.collection("Tenants").document(userID);
                                        Map<String, Object> tenant = new HashMap<>();
                                        tenant.put("Name", name);
                                        tenant.put("Email", Email);
                                        tenant.put("Phone", Contact);

                                        reference.set(tenant)
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
                                                        Toast.makeText(SignUpTenant.this, "Authentication failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(SignUpTenant.this, "User is null", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(SignUpTenant.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }
        });
    }
}

    // Method to generate a random alphanumeric string of given length
  /*  private String generateUniqueID(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder uniqueID = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < length; i++) {
            uniqueID.append(characters.charAt(rnd.nextInt(characters.length())));
        }
        return uniqueID.toString();
    }
}
/*    String uniqueID = generateUniqueID(5);

    ControllerLogin controllerLogin = new ControllerLogin(name, Emails, Contact, Passwd, uniqueID);
                databaseReference.child(uniqueID).setValue(controllerLogin);

                Toast.makeText(SignUpTenant.this, "Sign Up Successful !", Toast.LENGTH_SHORT).show();
    Intent intent = new Intent(SignUpTenant.this, HomePage.class);
    startActivity(intent);
    finish();
} */
//  String Email, Password;
//  int Contact;
// Fullname=String.valueOf(fullname.getText());
//  Contact=Integer.parseInt(String.valueOf(contact.getText()));
//   Email=String.valueOf(email.getText());
// Password=String.valueOf(password.getText());
                /*
                if(TextUtils.isEmpty(Email) || TextUtils.isEmpty(Password)){

                    Toast.makeText(SignUpTenant.this, "All Fields Required !",Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(Email,Password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progress.setVisibility(View.GONE);
                                if (task.isSuccessful()) {

                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(SignUpTenant.this, "SignIn Successful !",Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(SignUpTenant.this, HomePage.class);
                                    startActivity(intent);
                                    finish();


                                } else {
                                    // If sign in fails, display a message to the user.
                                    // Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                  //  Toast.makeText(SignUpTenant.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();



                                }
                            }
                        });

                 */
