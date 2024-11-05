package com.example.mechanic_zone.ServiceProvider;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginMechanic extends AppCompatActivity {

    TextView swtTosignUp;

    private TextView mTosignUp;
    private EditText mEmail,mPassword;
    Button mloginbtnM;
    private FirebaseAuth fAuth;

     ProgressBar mprogressbarM2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_mechanic);

        swtTosignUp = findViewById(R.id.switchTosignUpp);

        mEmail = findViewById(R.id.email_m);
        mPassword = findViewById(R.id.password_m);
        mloginbtnM = findViewById(R.id.signInM);
        mprogressbarM2 = findViewById(R.id.progressBarM2);
        fAuth = FirebaseAuth.getInstance();

        swtTosignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SignupMechanic.class));
            }
        });


        mloginbtnM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = mEmail.getText().toString().trim();
                String Password = mPassword.getText().toString().trim();

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
                    mPassword.setError("Password must be greater than 5 digit");
                    return;
                }
                mprogressbarM2.setVisibility(View.VISIBLE);

                //authenticate user

                fAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(LoginMechanic.this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(LoginMechanic.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MapsActivity_Mechanic.class));
                        }
                        else
                        {
                            Toast.makeText(LoginMechanic.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            mprogressbarM2.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });
    }
}