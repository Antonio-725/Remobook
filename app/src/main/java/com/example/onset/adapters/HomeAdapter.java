package com.example.onset.adapters;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;

import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onset.R;
import com.example.onset.model.item;
import com.example.onset.model.item1;

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

   /* public  HomeAdapter(Context context1, List<item1>itemlist2){
        this.context1=context1;
        this.itemlist2=itemlist2;

    } */

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

    }



    @Override
    public int getItemCount() {
        //return  itemlist2.size();

        return itemList.size();
    }
    public  class ViewHolder extends RecyclerView.ViewHolder{
        public TextView price,location,shortDescription;
        public ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            price=itemView.findViewById(R.id.priceFeatured);
            image=itemView.findViewById(R.id.bedding);
          //  location=itemView.findViewById(R.id.location);
           // shortDescription=itemView.findViewById(R.id.short_description);

        }
    }
}
