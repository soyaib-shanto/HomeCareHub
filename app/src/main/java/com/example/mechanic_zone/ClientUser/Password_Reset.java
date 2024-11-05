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

public class Password_Reset extends AppCompatActivity {

    private EditText mOldpass,mNewpass,mConfirmnewpass;
    private Button msaveP;
    private String uid,pass="",email1="";
    private FirebaseDatabase database1= FirebaseDatabase.getInstance();
    private DatabaseReference reference1 = database1.getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.mechanic_zone.R.layout.password_reset);

        mOldpass = findViewById(com.example.mechanic_zone.R.id.old_PassP);
        mNewpass = findViewById(com.example.mechanic_zone.R.id.newPass);
        mConfirmnewpass = findViewById(com.example.mechanic_zone.R.id.confirm_pass);
        msaveP = findViewById(R.id.savePassword);


        msaveP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Opass = mOldpass.getText().toString();
                String Newp = mNewpass.getText().toString();
                String CnewP = mConfirmnewpass.getText().toString();

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

                                email1 = dataSnapshot.child("email").getValue(String.class);
                                pass = dataSnapshot.child("password").getValue(String.class);

                                if(Opass.equals(pass)) {
                                    if (Newp.equals(CnewP))
                                    {
                                        reference1.child(uid).child("password").setValue(Newp);
                                        resetPassword(email1, Newp);
                                        Toast.makeText(getApplicationContext(), "password changed", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), Navigation_Drawar.class));
                                    }
                                    else Toast.makeText(getApplicationContext(), "confirm password is incorrect", Toast.LENGTH_SHORT).show();


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


            }
        });


    }

    private void resetPassword(String userEmail, String newPassword) {
        FirebaseAuth.getInstance().fetchSignInMethodsForEmail(userEmail)
                .addOnSuccessListener(signInMethodsResult -> {
                    if (signInMethodsResult.getSignInMethods().size() > 0) {
                        // User exists, reset password
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            user.updatePassword(newPassword)
                                    .addOnSuccessListener(aVoid -> {
                                        // Password reset successful
                                        Log.d("ResetPassword", "Password reset successful");
                                    })
                                    .addOnFailureListener(e -> {
                                        // Password reset failed
                                        Log.e("ResetPassword", "Password reset failed: " + e.getMessage());
                                    });
                        } else {
                            // User is not authenticated
                            Log.e("ResetPassword", "User not authenticated");
                        }
                    } else {
                        // User does not exist
                        Log.e("ResetPassword", "User not found");
                    }
                })
                .addOnFailureListener(e -> {
                    // Error occurred
                    Log.e("ResetPassword", "Error: " + e.getMessage());
                });
    }


}