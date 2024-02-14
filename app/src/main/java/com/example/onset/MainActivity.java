package com.example.onset;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.Firebase;
//import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private Button btn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         btn=findViewById(R.id.button);
         btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnClick();

            }
        });

    }
private  void btnClick(){
  Intent intent=new Intent(MainActivity.this , MainActivity2.class);
                startActivity(intent);
}

}