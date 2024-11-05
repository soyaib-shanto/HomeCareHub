package com.example.mechanic_zone.ClientUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mechanic_zone.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ResultOfSearch extends AppCompatActivity {

    private TextView a,b,c;
    private String naame,ttitle,pphone,uid;
    private Button d;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_of_search);

        FirebaseApp.initializeApp(this);


        Intent intent = getIntent();
        if (intent != null) {
             uid = intent.getStringExtra("code");
            // Now, you have the data from Activity A in the "receivedData" variable.
            // You can use it as needed.
        }

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Mechanic").child(uid);

        a=findViewById(R.id.h1);
        b=findViewById(R.id.h2);
        c=findViewById(R.id.h3);
        d=findViewById(R.id.h4);


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method will be called whenever data at the DatabaseReference changes.
                // You can retrieve the data from dataSnapshot.
                if (dataSnapshot.exists()) {
                    // Data exists for the given UID
                    naame = dataSnapshot.child("name").getValue(String.class);
                    ttitle = dataSnapshot.child("title").getValue(String.class);
                    pphone = dataSnapshot.child("phone").getValue(String.class);

                    a.setText(naame);
                    b.setText(ttitle);
                    c.setText(pphone);

                    d.setOnClickListener(v -> {
                        if (pphone != null) {
                            // Check for permission at runtime on Android 6.0 and later
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (ContextCompat.checkSelfPermission(ResultOfSearch.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    // Request the permission
                                    ActivityCompat.requestPermissions(ResultOfSearch.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                                    return;
                                }
                            }

                            // Permission is granted, initiate the call
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + pphone));
                            startActivity(intent);
                        }

                    });
                  //  c.setText(pphone);
                    // Do something with the retrieved data
                    // For example, update UI elements with the user's information
                } else {
                    // Data does not exist for the given UID
                    Toast.makeText(getApplicationContext(), "Snapshot Not exists", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that may occur
                Toast.makeText(getApplicationContext(), "Database error", Toast.LENGTH_SHORT).show();

            }
        });

    }
}