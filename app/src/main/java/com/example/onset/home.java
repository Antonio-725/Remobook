package com.example.onset;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.onset.adapters.HomeAdapter;
import com.example.onset.model.item;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class home extends Fragment {

    private RecyclerView rc;
    private HomeAdapter adapter;
    //private List<item> itemList;
    ArrayList<item> itemList;

    private Context context;
    private ShapeableImageView imageView;
    private TextView textView,textView1;
    private CardView cardView;
    private ImageView image1;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private String userID, apartmentId;
    private StorageReference storageReference;
    ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();
        rc = view.findViewById(R.id.recyclerview2);
        imageView = view.findViewById(R.id.imageview);
        textView = view.findViewById(R.id.greeting);
        image1 = view.findViewById(R.id.bedding);
        textView1=view.findViewById(R.id.priceFeatured);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        userID = auth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference();
        cardView=view.findViewById(R.id.bedding1);
        //dialog=new ProgressDialog(this);
        dialog = new ProgressDialog(requireActivity());

        dialog.setCancelable(false);
        dialog.setMessage("Fetching Images...");
        dialog.show();

        DocumentReference reference = firestore.collection("Tenants").document(userID);

        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    textView.setText("Hello " + value.getString("Name"));
                    loadProfileImage(value.getString("ProfileImage"));
                }
            }
        });


        itemList = new ArrayList<item>();
        adapter = new HomeAdapter(requireContext(), itemList);
        fetchApartments();

       /* itemList.add(new item("Street # 1, Chuka", "Ksh 25,000", "3-Bedroom"));
        itemList.add(new item("Street # 4, Embu", "Ksh 18,000", "4-Bedroom"));
        itemList.add(new item("Street # 6, Meru", "Ksh 10,000", "1-Bedroom"));
        adapter = new HomeAdapter(getContext(), itemList);*/

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        rc.setLayoutManager(linearLayoutManager);
        rc.setAdapter(adapter);

       /* cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), BookNow.class);


                startActivity(intent);
            }
        });*/



    }
 //   List<String> imagesList = new ArrayList<>();
 private void fetchApartments() {
     List<String> imagesList = new ArrayList<>();
     firestore.collection("Apartments")
             .get()
             .addOnCompleteListener(task -> {
                 if (task.isSuccessful()) {
                     for (DocumentSnapshot document : task.getResult()) {
                         String imageUrl = document.getString("ApartmentImage");
                         String price = document.getString("price");
                         if (imageUrl != null && !imageUrl.isEmpty() && price != null && !price.isEmpty()) {
                             imagesList.add(imageUrl);
                             imagesList.add(price); // Add the price as well
                         }
                     }

                     // Create items for each image URL
                    /* for (String imageUrl : imagesList) {
                         item newItem = new item(imagesList, imageUrl); // Use imagesList instead of Collections.singletonList
                         itemList.add(newItem);
                     }*/
                     for (int i = 0; i < imagesList.size(); i += 2) {
                         String imageUrl = imagesList.get(i);
                         String price = imagesList.get(i + 1);
                         item newItem = new item(null, imageUrl, price); // Pass null for other parameters
                         itemList.add(newItem);
                     }

                     adapter.notifyDataSetChanged();
                     // Load images after adding all to the itemList
                     for (String imageUrl : imagesList) {
                         loadProfileImage1(imageUrl, adapter);

                     }
                 } else {
                     Log.e("Fetch Apartments", "Error getting documents: ", task.getException());
                 }
                 dialog.dismiss();
             });
 }

    public void loadProfileImage1(String imageUrl, HomeAdapter adapter) {
        if (imageUrl != null && !imageUrl.isEmpty() && context != null) {
            StorageReference imageRef = storageReference.child("apartment_images/" + imageUrl + ".jpg");
            int position = itemList.size() - 1; // Assuming the new item is added at the end of the list
            HomeAdapter.ViewHolder holder = (HomeAdapter.ViewHolder) rc.findViewHolderForAdapterPosition(position);
            if (holder != null) {
                Glide.with(context)
                        .load(imageRef)
                        .centerCrop()
                        .into(holder.image);
            }
        }
    }

    private void clearAll() {
        if (itemList != null) {
            itemList.clear();
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
        itemList = new ArrayList<>();
    }

    public void loadProfileImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty() && context != null) {
            Glide.with(context)
                    .load(imageUrl)
                    .centerCrop()
                    .into(imageView);

        }
    }
    public void onItemPosition(int position){
        Intent intent =new Intent(getContext(), BookNow.class);
        startActivity(intent);
    }

}