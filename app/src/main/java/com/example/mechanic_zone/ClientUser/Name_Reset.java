package com.example.mechanic_zone.ClientUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mechanic_zone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Name_Reset extends AppCompatActivity {

   private EditText moldPass,mnewname;
   private Button SaveN;
   private String uid="",pass="000";

   private FirebaseDatabase database1= FirebaseDatabase.getInstance();
   private DatabaseReference reference1 = database1.getReference("Users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_reset);

        moldPass = findViewById(R.id.old_PassU_Name);
        mnewname = findViewById(R.id.new_Name);
        SaveN = findViewById(R.id.saveName);



        SaveN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               String oldpassword1 = moldPass.getText().toString();
               String  Newname12 = mnewname.getText().toString();

                FirebaseAuth ffAuth = FirebaseAuth.getInstance();
                FirebaseUser cntUser = ffAuth.getCurrentUser();

                if(cntUser != null)
                {
                    uid = cntUser.getUid();

                    reference1.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // Handle the retrieved data
                            if (dataSnapshot.exists()) {
                                // User data exists for the given UID
                                pass = dataSnapshot.child("password").getValue(String.class);
                                if(oldpassword1.equals(pass))
                                {
                                    reference1.child(uid).child("name").setValue(Newname12);
                                    Toast.makeText(getApplicationContext(), "Name changed", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(),Navigation_Drawar.class));


                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Insert Correct Password", Toast.LENGTH_SHORT).show();
                                }

                            }

                            else Toast.makeText(getApplicationContext(), "datasnapshat not found", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle database error
                            Log.d("TAG", "Database Error: " + databaseError.getMessage());
                        }
                    });


                }
                else Toast.makeText(getApplicationContext(), "Current user not found", Toast.LENGTH_SHORT).show();


            }
        });

    }
}