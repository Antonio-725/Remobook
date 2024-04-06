package com.example.onset;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class profile extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final int pickImage = 1;
    private ImageView imageView;
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private TextView username,email,phone;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    String userID;
    ShapeableImageView imageView1;
    private MaterialButton button;
    private StorageReference storageReference;
    private Context context;

    public profile() {
        // Required empty public constructor
    }

    public static profile newInstance(String param1, String param2) {
        profile fragment = new profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        context = getContext();
        imageView = view.findViewById(R.id.camera);
        username=view.findViewById(R.id.person_name);
        phone=view.findViewById(R.id.phonenumber1);
        email=view.findViewById(R.id.email);
        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        userID=auth.getCurrentUser().getUid();
        imageView1=view.findViewById(R.id.imageview);
        button=view.findViewById(R.id.editz);
        storageReference= FirebaseStorage.getInstance().getReference();
        DocumentReference reference=firestore.collection("Tenants").document(userID);
        reference.addSnapshotListener( new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    username.setText(value.getString("Name"));
                    email.setText(value.getString("Email"));
                    Long phoneLong = value.getLong("Phone");
                    if (phoneLong != null) {
                        phone.setText(String.valueOf(phoneLong));
                    }
                    loadProfileImage(value.getString("ProfileImage"));
                } else {
                    // Handle the case where the document does not exist
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a custom dialog layout
                LayoutInflater inflater = LayoutInflater.from(context);
                View dialogView = inflater.inflate(R.layout.dialog_user_details, null);

                // Initialize dialog components
                EditText dialogUsername = dialogView.findViewById(R.id.editTextName);
                EditText dialogPhone = dialogView.findViewById(R.id.editTextPhone);
                EditText dialogEmail = dialogView.findViewById(R.id.editTextEmail);

                // Set user details
                dialogUsername.setText(username.getText().toString());
                dialogPhone.setText(phone.getText().toString());
                dialogEmail.setText(email.getText().toString());

                // Build the dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(dialogView)
                        .setTitle("Edit User Details")
                        .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Handle edit button click
                                // You can implement the edit functionality here
                                String newName = dialogUsername.getText().toString();
                                String newPhone = dialogPhone.getText().toString();
                                String newEmail = dialogEmail.getText().toString();

                                // Update the UI with the new user details
                                username.setText(newName);
                                phone.setText(newPhone);
                                email.setText(newEmail);

                                // Update the user details in Firestore
                                firestore.collection("Tenants").document(userID)
                                        .update("Name", newName, "Email", newEmail, "Phone", Long.parseLong(newPhone))
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(context, "User details updated", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(context, "Failed to update user details", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Handle cancel button click
                                dialog.dismiss();
                            }
                        });

                // Show the dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        return view;
    }

    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*"); // Corrected the MIME type
        startActivityForResult(intent, pickImage);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pickImage && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            imageView1.setImageURI(imageUri);
            uploadImage(imageUri);
        }
    }

    public void uploadImage(Uri imageUri) {
        if (context != null) {
            StorageReference fileref = storageReference.child("Profile.jpg");
            fileref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl = uri.toString();
                            firestore.collection("Tenants").document(userID)
                                    .update("ProfileImage", imageUrl)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(context, "Profile Set", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Failed to update profile image URL", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "Failed to upload image", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public void loadProfileImage(String imageUrl) {
        if (context != null && imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(imageUrl)
                    .into(imageView1);
        }
    }

}
