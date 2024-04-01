package com.example.onset;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import com.example.onset.home;


import com.example.onset.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomePage extends AppCompatActivity {

    ActivityMainBinding binding;
    Fragment fragment;
    private DrawerLayout dl;
    private ActionBarDrawerToggle abt;
    private NavigationView nav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_home_page);

      BottomNavigationView bottom=findViewById(R.id.navView);
      bottom.setOnNavigationItemSelectedListener(navListener);

      getSupportFragmentManager().beginTransaction().replace(R.id.container1,new home()).commit();

    }
    public BottomNavigationView.OnNavigationItemSelectedListener navListener=new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            fragment = null;
            int itemId = item.getItemId();
            if (itemId == R.id.home1) {
                fragment = new home();
            } else if (itemId == R.id.profile) {
                fragment = new profile();
            } else if (itemId == R.id.notify) {
                fragment = new notification();
            } else if (itemId == R.id.setting) {
                fragment = new settings();
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.container1,fragment).commit();
            return true;
        }
    };
    }
