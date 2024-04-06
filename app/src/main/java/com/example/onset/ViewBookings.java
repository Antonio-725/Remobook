package com.example.onset;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.onset.adapters.BookingAdapter;
import com.example.onset.model.UserBooking;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;


import java.util.ArrayList;
import java.util.List;

public class ViewBookings extends AppCompatActivity {
    private FirebaseFirestore firestore;
    private RecyclerView recyclerView;
    private BookingAdapter adapter;
    private List<UserBooking> bookingList;
    private Button buttonAccept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bookings);
        firestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.bookingRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookingList = new ArrayList<>();
        adapter = new BookingAdapter(bookingList);
        recyclerView.setAdapter(adapter);


        // Load bookings for the owner's apartment
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            firestore.collection("Bookings")
                    .whereEqualTo("ownerId", user.getUid())
                    .addSnapshotListener((snapshot, e) -> {
                        if (e != null) {
                            Log.e("ViewBookings", "Error fetching bookings", e);
                            return;
                        }

                        if (snapshot != null) {
                            bookingList.clear();
                            for (DocumentSnapshot document : snapshot.getDocuments()) {
                                UserBooking booking = document.toObject(UserBooking.class);
                                if (booking != null) {
                                    // Check if the customerName field is null and set it to an empty string if it is
                                    String customerName = booking.getusername();
                                    if (customerName == null) {
                                        customerName = "";
                                    }
                                    booking.setusername(customerName);

                                    bookingList.add(booking);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                    });
        }
    }
    private int getUniqueNotificationId() {
        return (int) System.currentTimeMillis();
    }

    private void sendMessage(String title, String body) {
        try {
            int notificationId = getUniqueNotificationId();
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channelId = "BookingConfirmation";
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                if (notificationManager.getNotificationChannel(channelId) == null) {
                    NotificationChannel channel = new NotificationChannel(channelId, "Booking Confirmation", NotificationManager.IMPORTANCE_HIGH);
                    notificationManager.createNotificationChannel(channel);
                }
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "BookingConfirmation")
                    .setSmallIcon(R.drawable.notification)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            notificationManagerCompat.notify(notificationId, builder.build());
        } catch (Exception e) {
            // Log the error
            Log.e("SendMessage", "Error sending notification: " + e.getMessage(), e);

            // Display a toast message to the user
            Toast.makeText(this, "Error sending notification: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}