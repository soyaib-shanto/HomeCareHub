package com.example.mechanic_zone.ClientUser;

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

public class LoginUser extends AppCompatActivity {

    TextView mTosignUp;
    private EditText mEmail,mPassword;
    Button mloginbtn;
    private FirebaseAuth fAuth;
    private ProgressBar mprogressbar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);
        mEmail = findViewById(R.id.email_userL);
        mPassword = findViewById(R.id.password_userL);
        mloginbtn = findViewById(R.id.loginbuttonU);
        mprogressbar1 = findViewById(R.id.progressbar1);
        fAuth = FirebaseAuth.getInstance();


        mTosignUp=findViewById(R.id.switchToSignUp);

        mTosignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignupUser.class));
            }
        });

        mloginbtn.setOnClickListener(new View.OnClickListener() {
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
                mprogressbar1.setVisibility(View.VISIBLE);

                //authenticate user

                fAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(LoginUser.this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(LoginUser.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Navigation_Drawar.class));
                        }
                        else
                        {
                            Toast.makeText(LoginUser.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            mprogressbar1.setVisibility(View.INVISIBLE);

                        }
                    }
                });
            }
        });


    }
}