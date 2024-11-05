package com.example.mechanic_zone.ServiceProvider;

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
import com.example.mechanic_zone.Model.ServiceProviderModel.mechanic_database;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignupMechanic extends AppCompatActivity {

    TextView swtosignin;
    EditText mName,mTitle,mEmail,mPassword,mPhone;
    Button mSignupbtnM;
    private ProgressBar mProgressbar;
    private FirebaseAuth fAuth;
    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_mechanic);

        mName = findViewById(R.id.NameM);
        mTitle = findViewById(R.id.title_M);
        mPhone = findViewById(R.id.phoneM);
        mEmail = findViewById(R.id.email_M);
        mPassword = findViewById(R.id.password_M);
        mSignupbtnM = findViewById(R.id.signupbuttonM);
        mProgressbar = findViewById(R.id.progressbarM);
        fAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        swtosignin = findViewById(R.id.switchTosignInn);


        swtosignin.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),LoginMechanic.class)));

        mSignupbtnM.setOnClickListener(v -> {
            String Name = mName.getText().toString();
            String Title = mTitle.getText().toString();
            String Phone = mPhone.getText().toString();
            String Email = mEmail.getText().toString();
            String Password = mPassword.getText().toString();

            if(TextUtils.isEmpty(Name))
            {
                mName.setError("Name is Required");
                return;
            }

            if(TextUtils.isEmpty(Title))
            {
                mTitle.setError("Name is Required");
                return;
            }

            if(TextUtils.isEmpty(Phone))
            {
                mPhone.setError("Phone is Required");
                return;
            }

            if(TextUtils.isEmpty(Email))
            {
                mEmail.setError("Email is Required");
                return;
            }
            if(TextUtils.isEmpty(Password))
            {
                mPassword.setError("Password is Required");
                return;
            }

            if(mPassword.length() < 6)
            {
                mPassword.setError("Password must be greater 5 digit");
                return;
            }
            if(mPhone.length() < 11)
            {
                mPhone.setError("Phone must be greater than 10 digit");
            }
            mProgressbar.setVisibility(View.VISIBLE);

            //Registration of user

            fAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(SignupMechanic.this, task -> {

                if(task.isSuccessful())
                {
                    mechanic_database mechanic = new mechanic_database(Name,Title,Phone,Email,Password);
                    String id = Objects.requireNonNull(task.getResult().getUser()).getUid();
                    database.getReference().child("Mechanic").child(id).setValue(mechanic);

                    Toast.makeText(SignupMechanic.this, "user added successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),MapsActivity_Mechanic.class));
                }

                else
                {
                    Toast.makeText(SignupMechanic.this, "Error" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    mProgressbar.setVisibility(View.INVISIBLE);
                }

            });


        });





    }
}