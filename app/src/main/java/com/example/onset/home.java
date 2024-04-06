package com.example.onset;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.onset.adapters.HomeAdapter;
import com.example.onset.adapters.ImageAdapter;
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

    public RecyclerView rc;
    public HomeAdapter adapter;
    //private List<item> itemList;
    ArrayList<item> itemList;

    private Context context;
    private ShapeableImageView imageView;
    private TextView textView,textView1;
    private CardView cardView;
    public ImageView image1;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private String userID, apartmentId;
    private StorageReference storageReference;
    ProgressDialog dialog;
    public LinearLayout linearLayout;
    private TextView searchText;

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
        linearLayout=view.findViewById(R.id.category);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        userID = auth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference();
        cardView=view.findViewById(R.id.bedding1);
        //dialog=new ProgressDialog(this);
        searchText=view.findViewById(R.id.search);
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
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        itemList = new ArrayList<>();
        adapter = new HomeAdapter(requireContext(), itemList);
        fetchApartments();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rc.setLayoutManager(linearLayoutManager);
        rc.setAdapter(adapter);

    }
    private void fetchImagesAndShowDialog() {
        List<String> imagesList = new ArrayList<>();
        firestore.collection("Apartments").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            String imageUrl = document.getString("ApartmentImage");
                            if (imageUrl != null && !imageUrl.isEmpty()) {
                                imagesList.add(imageUrl);
                            }
                        }
                        showImageDialog(imagesList);
                    }
                });
    }

    private void showImageDialog(List<String> imagesList) {
        List<String> namesList = new ArrayList<>();
        for (item apartment : itemList) {
            namesList.add(apartment.getName());
        }

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_image_grid, null);
        RecyclerView recyclerView = dialogView.findViewById(R.id.recyclerViewImages);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        ImageAdapter adapter = new ImageAdapter(imagesList, namesList);
        recyclerView.setAdapter(adapter);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void fetchApartments() {

        List<String> imagesList = new ArrayList<>();
        firestore.collection("Apartments")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            String imageUrl = document.getString("ApartmentImage");
                            String price = document.getString("price");
                            String owner=document.getString("ownerId");
                            String idApartment = document.getId();
                            String description = document.getString("description");
                            String name = document.getString("name");
                            String roomNumber = document.getString("roomNumber");
                            String rentType = document.getString("rentType");
                            String location = document.getString("location");
                            boolean parking = Boolean.TRUE.equals(document.getBoolean("parking"));
                            boolean wifi = Boolean.TRUE.equals(document.getBoolean("wifi"));
                            boolean water = Boolean.TRUE.equals(document.getBoolean("water"));
                            boolean security = Boolean.TRUE.equals(document.getBoolean("security"));
                            if (imageUrl != null && !imageUrl.isEmpty() && price != null && !price.isEmpty() && description != null && !description.isEmpty()&&idApartment != null && !idApartment.isEmpty()&&owner != null && !owner.isEmpty()) {
                                item newItem = new item(name, owner,location, price, rentType, imageUrl, description, roomNumber, idApartment, wifi, parking, water, security);
                               //  item newItem1=new item(owner);
                                itemList.add(newItem);
                               // itemList.add(newItem1);


                            }

                        }

                        adapter.notifyDataSetChanged();

                    }
                    dialog.dismiss();
                });
        linearLayout.setOnClickListener(view -> {
            fetchImagesAndShowDialog();
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
        if (imageUrl != null && !imageUrl.isEmpty() && context != null && !getActivity().isDestroyed()) {
            Glide.with(context)
                    .load(imageUrl)
                    .centerCrop()
                    .into(imageView);
        }
    }
    private void filter(String text) {
        ArrayList<item> filteredList = new ArrayList<>();
        for (item item : itemList) {
            if (item.getLocation().toLowerCase().contains(text.toLowerCase())
                    || item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }


}