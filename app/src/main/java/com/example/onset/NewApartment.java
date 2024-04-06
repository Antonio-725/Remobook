package com.example.onset;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Random;

public class NewApartment extends AppCompatActivity {
    private EditText apartmentName, description, price, location, roomnumber;
    private CheckBox box1, box2, box3, box4;
    private RadioButton monthly, yearly;
    private FirebaseAuth auth;
    private StorageReference storageReference;
    private FirebaseFirestore firestore;
    private MaterialButton button;
    private String ownerID;
    private ImageView imageView;
    private Context context;
    private Uri selectedImageUri;
    private static final int PICK_IMAGE = 1;
    private Spinner spinner;
   // private Uri selectedImageUri;
    private Uri selectedDocumentUri;
    private MaterialButton uploadDoc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_apartment);

        context = this;
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        ownerID = auth.getCurrentUser().getUid();
        uploadDoc=findViewById(R.id.uploadDocumentButton);

        apartmentName = findViewById(R.id.edit);
        description = findViewById(R.id.editDescribe);
        price = findViewById(R.id.price2);
        location = findViewById(R.id.locationtext);
        roomnumber = findViewById(R.id.rooms);
        box1 = findViewById(R.id.security);
        box2 = findViewById(R.id.water);
        box3 = findViewById(R.id.parking);
        box4 = findViewById(R.id.wifi);
        monthly = findViewById(R.id.radio1);
        yearly = findViewById(R.id.radio2);
        button = findViewById(R.id.submit);
        imageView = findViewById(R.id.imageApartment);

         spinner = findViewById(R.id.spinnerApartment);
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
        auth= FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        ownerID=auth.getCurrentUser().getUid();
        storageReference= FirebaseStorage.getInstance().getReference();
        DocumentReference reference=firestore.collection("Landlord").document(ownerID);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()) {
                    uploadApartmentDetails();
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        uploadDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDocument();

            }
        });
    }

    private void uploadApartmentDetails() {
        String name = apartmentName.getText().toString();
        String desc = description.getText().toString();
        String p = price.getText().toString();
        String loc = location.getText().toString();
        String rooms = roomnumber.getText().toString();
        boolean security = box1.isChecked();
        boolean water = box2.isChecked();
        boolean parking = box3.isChecked();
        boolean wifi = box4.isChecked();
        String rentType = monthly.isChecked() ? "Monthly" : "Yearly";
        String apartmentType = spinner.getSelectedItem().toString();

        Apartment apartment = new Apartment(name,ownerID, desc, p, loc, rooms, security, water, parking, wifi, rentType,apartmentType);

        // Add the apartment to Firestore
        firestore.collection("Apartments")
                .add(apartment)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        uploadImage(documentReference.getId()); // Pass the document ID to upload the image
                        Toast.makeText(NewApartment.this, "Apartment details uploaded successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NewApartment.this, "Failed to upload apartment details", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private static final int PICK_DOCUMENT = 2;

    public void openDocument() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, PICK_DOCUMENT);
    }

    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data!= null) {
            selectedImageUri = data.getData();
            imageView.setImageURI(selectedImageUri);
        } else if (requestCode == PICK_DOCUMENT && resultCode == RESULT_OK && data!= null) {
            selectedDocumentUri = data.getData();
            String mimeType = getContentResolver().getType(selectedDocumentUri);
            if (mimeType.equals("application/pdf") || mimeType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
                // Handle the selected document URI here
                // For example, you can display the document name or path
                String documentName = getFileName(selectedDocumentUri);
                Toast.makeText(this, "Document selected: " + documentName, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please select a PDF or DOCX file", Toast.LENGTH_SHORT).show();
                selectedDocumentUri = null;
            }
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor!= null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (index != -1) {
                        result = cursor.getString(index);
                    }
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut!= -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
    public void uploadDocument(String apartmentId) {
        if (selectedDocumentUri != null) {
            // Upload the document
            StorageReference documentRef = storageReference.child("apartment_documents/" + apartmentId + ".pdf");
            documentRef.putFile(selectedDocumentUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            documentRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String documentUrl = uri.toString();
                                    // Update the apartment document with the document URL
                                    firestore.collection("Apartments").document(apartmentId)
                                            .update("ApartmentDocument", documentUrl)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(context, "Apartment document uploaded successfully", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(context, "Failed to update apartment document URL", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Failed to upload document", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void uploadImage(String apartmentId) {
        if (selectedImageUri != null) {
            StorageReference imageRef = storageReference.child("apartment_images/" + apartmentId + ".jpg");
            imageRef.putFile(selectedImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    // Update the apartment document with the image URL
                                    firestore.collection("Apartments").document(apartmentId)
                                            .update("ApartmentImage", imageUrl)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    uploadDocument(apartmentId);
                                                    Toast.makeText(context, "Apartment image uploaded successfully", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(context, "Failed to update apartment image URL", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Failed to upload image", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
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




























