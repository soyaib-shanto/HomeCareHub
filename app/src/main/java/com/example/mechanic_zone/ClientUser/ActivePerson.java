package com.example.mechanic_zone.ClientUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.mechanic_zone.R;
import com.example.mechanic_zone.Model.ClientUsersModel.nameAndisActive;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActivePerson extends AppCompatActivity {
     DatabaseReference usersRef;
     List<nameAndisActive> activeUsersList;
     ActivePersonsAdapter adapter;
     RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_person);

        // Initialize Firebase Realtime Database reference
        usersRef = FirebaseDatabase.getInstance().getReference().child("Mechanic");

        recyclerView = findViewById(R.id.recycleview);
      //  recyclerView.setHasFixedSize(true);
        activeUsersList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

       // recyclerView.setAdapter(new ActivePersonsAdapter(getApplicationContext(),activeUsersList));


        // Fetch active users from Firebase Realtime Database
        usersRef.addValueEventListener(new ValueEventListener() {
          @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                activeUsersList.clear();
                adapter = new ActivePersonsAdapter(getApplicationContext(),activeUsersList);
                int i=1;

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    nameAndisActive user = userSnapshot.getValue(nameAndisActive.class);

              // String hello = userSnapshot.child("name").getValue(String.class);
             //  String hello1 = userSnapshot.child("email").getValue(String.class);
                    if (user != null && user.getIsOnline()==1) {
                        activeUsersList.add(user);
                    }


                     //activeUsersList.add(new nameAndisActive(hello,"hello1"));
                 //   activeUsersList.add(user);

               i++;
                }

                adapter.notifyDataSetChanged();

              recyclerView.setAdapter(adapter);

          }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database read error
            }
        });


    }
}