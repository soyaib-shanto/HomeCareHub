package com.example.mechanic_zone.ClientUser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mechanic_zone.R;
import com.example.mechanic_zone.Model.ClientUsersModel.users_database;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignupUser extends AppCompatActivity {

    TextView mTosignIn;
    private EditText mName,mEmail,mPassword;
    Button mSignupbtn;
    private ProgressBar mProgressbar;
    private FirebaseAuth fAuth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_user);

        mName = findViewById(R.id.name_userR);
        mEmail = findViewById(R.id.email_userR);
        mPassword = findViewById(R.id.password_userR);
        mSignupbtn = findViewById(R.id.signupbuttonU);
        mTosignIn = findViewById(R.id.switchToSignIn);
         mProgressbar = findViewById(R.id.progressBar2);
         fAuth = FirebaseAuth.getInstance();
         database = FirebaseDatabase.getInstance();

        mTosignIn.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), LoginUser.class)));

        mSignupbtn.setOnClickListener(v -> {
            String Name = mName.getText().toString().trim();
            String Email = mEmail.getText().toString().trim();
            String Password = mPassword.getText().toString().trim();


            if(TextUtils.isEmpty(Name))
            {
                mName.setError("Name is Required");
                return;
            }
            if(TextUtils.isEmpty(Email))
            {
                mEmail.setError("Email is Required");
                return;
            }
            if(TextUtils.isEmpty(Name))
            {
                mPassword.setError("Password is Required");
                return;
            }

            if(mPassword.length() < 6)
            {
                mPassword.setError("Password must be greater 5 digit");
                return;
            }
            mProgressbar.setVisibility(View.VISIBLE);

            //Registration of user

            fAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(SignupUser.this, task -> {

                if(task.isSuccessful())
                {
                    users_database user = new users_database(Name,Email,Password);
                    String id = Objects.requireNonNull(task.getResult().getUser()).getUid();
                    DatabaseReference dt = FirebaseDatabase.getInstance().getReference().child("Users").child(id);
                    dt.setValue(true);
                    database.getReference().child("Users").child(id).setValue(user);

                    Toast.makeText(SignupUser.this, "user added successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),Navigation_Drawar.class));
                }

                else
                {
                    Toast.makeText(SignupUser.this, "Error" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    mProgressbar.setVisibility(View.INVISIBLE);
                }

            });



        });


    }
}