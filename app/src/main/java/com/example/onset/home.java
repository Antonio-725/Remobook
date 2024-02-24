package com.example.onset;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.onset.adapters.HomeAdapter;
import com.example.onset.model.item;
import com.example.onset.model.item1;
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
    private List<item> itemList;
    private Context context;
    private ShapeableImageView imageView;
    private TextView textView;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private String userID;
    private StorageReference storageReference;

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
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        userID = auth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference();
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

        itemList = new ArrayList<>();
        itemList.add(new item("Street # 1, Chuka", "Ksh 25,000", "3-Bedroom"));
        itemList.add(new item("Street # 4, Embu", "Ksh 18,000", "4-Bedroom"));
        itemList.add(new item("Street # 6, Meru", "Ksh 10,000", "1-Bedroom"));
        adapter = new HomeAdapter(getContext(), itemList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        rc.setLayoutManager(linearLayoutManager);
        rc.setAdapter(adapter);

       // GetDataFromFirebase();
    }

   /* private void GetDataFromFirebase() {
        DatabaseReference refer = FirebaseDatabase.getInstance().getReference();
        List<item1> itemlist2 = new ArrayList<>();
        Query query = refer.child("Apartment");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                clearAll();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    item1 item = new item1();
                    item.setImageUrl(snapshot1.child("imageUrl").getValue().toString());
                    item.setPrice(snapshot1.child("Price").getValue().toString());
                    itemlist2.add(item);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }*/

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
                    .into(imageView);
        }
    }
}
