package com.example.onset;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class DashboardLandlord extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    ActionBar actionBar;
    ActionBarDrawerToggle actionBarDrawerToggle;
   private CardView cardView1,cardView2,cardView3,cardView4;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_landlord);
        cardView1=findViewById(R.id.profilelandlord);
        cardView2=findViewById(R.id.regNew);
        cardView3=findViewById(R.id.viewBook);
        cardView4=findViewById(R.id.manageApartment);

       //View profile
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DashboardLandlord.this, ProfileLandlord.class);
                startActivity(intent);


            }
        });

        //Apartment Registration

        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DashboardLandlord.this, NewApartment.class);
                startActivity(intent);


            }
        });

        //View bookings
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DashboardLandlord.this, ViewBookings.class);
                startActivity(intent);


            }
        });

        //Manage apartment
        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DashboardLandlord.this, ManageApartment.class);
                startActivity(intent);


            }
        });


    }


}
