package com.example.onset.adapters;

//import com.example.onset.home;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import com.google.android.gms.tasks.OnFailureListener;


import com.bumptech.glide.Glide;
import com.example.onset.BookNow;
import com.example.onset.home;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onset.R;
import com.example.onset.model.item;
import com.example.onset.model.item1;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private Context context;
    private HomeAdapter adapter;

    private  static  final  String Tag="RecyclerView";


    //private List<item>itemList;
    private  ArrayList<item>itemList;
    private Context context1;
    private List<item1>itemlist2;
    public  HomeAdapter(Context context, ArrayList<item>itemList){
        this.context=context;
        this.itemList=itemList;
    }



    @NonNull
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.featured,parent,false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        item currentItem = itemList.get(position);

        // Set the price
        holder.price.setText(currentItem.getPrice());
        Glide.with(context)
                .load(itemList.get(position).getImageUrl())
                .centerCrop()
                .into(holder.image);
        holder.likesCount.setText(String.valueOf(currentItem.getLikesCount()));

        holder.like.setOnClickListener(v -> {
            int newPosition = holder.getAdapterPosition();
            if (newPosition != RecyclerView.NO_POSITION) {
                item clickedItem = itemList.get(newPosition);
                boolean newLikeStatus = !clickedItem.isLiked();
                clickedItem.setLiked(newLikeStatus);
                if (newLikeStatus) {
                    clickedItem.setLikesCount(clickedItem.getLikesCount() + 1);
                } else {
                    clickedItem.setLikesCount(clickedItem.getLikesCount() - 1);
                }
                notifyItemChanged(newPosition);
                updateLikeInFirestore(clickedItem.getApartmentID(), newLikeStatus);
            }
        });
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = holder.getAdapterPosition();
                item clickedItem = itemList.get(clickedPosition);
                Intent intent = new Intent(context, BookNow.class);
                intent.putExtra("ownerId",clickedItem.getOwnerID());
                intent.putExtra("apartmentId", clickedItem.getApartmentID());
                intent.putExtra("name", clickedItem.getName());
                intent.putExtra("price", clickedItem.getPrice());
                intent.putExtra("description", clickedItem.getDescription());
                intent.putExtra("roomNumber", clickedItem.getTotalRooms());
                intent.putExtra("location", clickedItem.getLocation());
                intent.putExtra("imageUrl", clickedItem.getImageUrl());
                intent.putExtra("parking", clickedItem.isParking());
                intent.putExtra("wifi", clickedItem.isWifi());
                intent.putExtra("security", clickedItem.isSecurity());
                intent.putExtra("water", clickedItem.isWater());
                intent.putExtra("rentType", clickedItem.getRentType());
                intent.putExtra("likes", clickedItem.getLikesCount());

                context.startActivity(intent);
                Log.d("BookNow", "Apartment id: " +clickedItem.getApartmentID());
                Log.d("BookNow", "Apartment owner: " + clickedItem.getOwnerID());
                Log.d("BookNow", "description: " + clickedItem.getShortDescription());
            }
        });

    }



    @Override
    public int getItemCount() {
        //return  itemlist2.size();

        return itemList.size();
    }
    public  class ViewHolder extends RecyclerView.ViewHolder{
        public TextView price,location,shortDescription,likesCount;
        public ImageView image,like;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            price=itemView.findViewById(R.id.priceFeatured);
            image=itemView.findViewById(R.id.bedding);
            like=itemView.findViewById(R.id.likes);
            likesCount = itemView.findViewById(R.id.likesCount);
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        item clickedItem = itemList.get(position);
                        boolean newLikeStatus = !clickedItem.isLiked();
                        clickedItem.setLiked(newLikeStatus);
                        notifyItemChanged(position);
                        updateLikeInFirestore(clickedItem.getApartmentID(), newLikeStatus);
                    }
                }
            });
        }


    }
    public void filterList(ArrayList<item> filteredList) {
        itemList = filteredList;
        notifyDataSetChanged();
    }
    private void updateLikeInFirestore(String apartmentId, boolean newLikeStatus) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference apartmentRef = firestore.collection("Apartments").document(apartmentId);

        apartmentRef.update("likes", newLikeStatus)
                .addOnSuccessListener(aVoid -> Log.d("HomeAdapter", "Like status updated successfully"))
                .addOnFailureListener(e -> Log.e("HomeAdapter", "Error updating like status", e));
    }
}
