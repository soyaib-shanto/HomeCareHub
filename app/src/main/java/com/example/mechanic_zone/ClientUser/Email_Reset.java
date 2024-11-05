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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Email_Reset extends AppCompatActivity {

    EditText oldPas,NewGmail;
    Button SaveE;
    private String uid,emailD="",emailnew="",passD="";

    private FirebaseDatabase database2= FirebaseDatabase.getInstance();
    private DatabaseReference reference2 = database2.getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_reset);
        oldPas = findViewById(R.id.old_PassU_Email);
        NewGmail = findViewById(R.id.newEmail);
        SaveE = findViewById(R.id.saveEmail);

        SaveE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldP =  oldPas.getText().toString();
                String NewEmail =  NewGmail.getText().toString();


                FirebaseAuth ffAuth = FirebaseAuth.getInstance();
                FirebaseUser cntUser = ffAuth.getCurrentUser();

                if(cntUser != null)
                {
                    uid = cntUser.getUid();

                    reference2.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // Handle the retrieved data
                            if (dataSnapshot.exists()) {
                                // User data exists for the given UID

                                emailD = dataSnapshot.child("email").getValue(String.class);
                                passD = dataSnapshot.child("password").getValue(String.class);

                                if(oldP.equals(passD)) {

                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                    if (user != null) {
                                        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldP);
                                        user.reauthenticate(credential)
                                                .addOnSuccessListener(aVoid -> {
                                                    user.updateEmail(NewEmail)
                                                            .addOnSuccessListener(aVoid1 -> {
                                                                // Email address updated successfully
                                                                Log.d("UpdateEmail", "Email address updated successfully");
                                                                Toast.makeText(getApplicationContext(), "Email changed", Toast.LENGTH_SHORT).show();
                                                                reference2.child(uid).child("email").setValue(NewEmail);
                                                                startActivity(new Intent(getApplicationContext(), Navigation_Drawar.class));

                                                            })
                                                            .addOnFailureListener(e -> {
                                                                // Error occurred while updating email address
                                                                Log.e("UpdateEmail", "Error updating email address: " + e.getMessage());
                                                            });
                                                })
                                                .addOnFailureListener(e -> {
                                                    // Re-authentication failed
                                                    Log.e("UpdateEmail", "Re-authentication failed: " + e.getMessage());
                                                });
                                    } else {
                                        // User is not authenticated
                                        Log.e("UpdateEmail", "User not authenticated");
                                    }

                                        //resetPassword(emailD,NewEmail);
                                       // reference2.child(uid).child("email").setValue(NewEmail);

                                     //   Toast.makeText(getApplicationContext(), "Email changed", Toast.LENGTH_SHORT).show();
                                        //startActivity(new Intent(getApplicationContext(), Navigation_Drawar.class));

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

    private void resetPassword(String userEmail, String newemail) {
        FirebaseAuth.getInstance().fetchSignInMethodsForEmail(userEmail)
                .addOnSuccessListener(signInMethodsResult -> {
                    if (signInMethodsResult.getSignInMethods().size() > 0) {
                        // User exists, reset password
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            user.updateEmail(newemail)
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