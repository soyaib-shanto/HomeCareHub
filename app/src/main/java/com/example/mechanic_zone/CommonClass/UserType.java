package com.example.mechanic_zone.CommonClass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mechanic_zone.ClientUser.LoginUser;
import com.example.mechanic_zone.R;
import com.example.mechanic_zone.ServiceProvider.LoginMechanic;

public class UserType extends AppCompatActivity {

    Button mUser,mMechanic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);

        mUser = findViewById(R.id.user);
        mMechanic = findViewById(R.id.mechanic);


        mUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginUser.class));
            }
        });

        mMechanic.setOnClickListener(new View.OnClickListener() {
              @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginMechanic.class));
            }
        });



    }

    @Override
    public void onBackPressed() {

            finishAffinity();
    }

}