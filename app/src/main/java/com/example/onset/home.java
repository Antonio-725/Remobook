package com.example.onset;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.onset.adapters.HomeAdapter;
import com.example.onset.model.item;

import java.util.ArrayList;
import java.util.List;

import com.example.onset.model.item1;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import  com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class home extends Fragment {

    private RecyclerView rc;
    private HomeAdapter adapter;
    private List<item>itemList;
    private Context context1;


    private  DatabaseReference refer;
    private List<item1>itemlist2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for  this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rc=view.findViewById(R.id.recyclerview2);
        context1 = getContext();

/*
        refer= FirebaseDatabase.getInstance().getReference();
        itemlist2=new ArrayList<>();
        clearAll();

        GetDataFromFirebase();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        rc.setAdapter(adapter);
*/

        itemList=new ArrayList<>();
        itemList.add(new item ("Street # 1, Chuka","Ksh 25,000","3-Bedroom"));
        itemList.add(new item ("Street # 4, Embu","Ksh 18,000","4-Bedroom"));
        itemList.add(new item ("Street # 6, Meru","Ksh 10,000","1-Bedroom"));
        adapter=new HomeAdapter(getContext(),itemList);



      LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        rc.setLayoutManager(linearLayoutManager);
        rc.setAdapter(adapter);

    }

    private void GetDataFromFirebase() {
        Query query=refer.child("Apartment");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                clearAll();
                for (DataSnapshot snapshot1: snapshot.getChildren()){
                     item1 item=new item1();
                     item.setImageUrl(snapshot1.child("imageUrl").getValue().toString());
                     item.setPrice((snapshot1.child("Price").getValue().toString()));
                     itemlist2.add(item);

                }

               // adapter=new HomeAdapter(context1,itemlist2);
                adapter.notifyDataSetChanged();
             //   LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
             //   linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

              //  rc.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });


    }
    private  void clearAll(){
        if (itemlist2 !=null){
            itemlist2.clear();
            if (adapter !=null){
                adapter.notifyDataSetChanged();
            }
        }
            itemlist2=new ArrayList<>();
    }

}
