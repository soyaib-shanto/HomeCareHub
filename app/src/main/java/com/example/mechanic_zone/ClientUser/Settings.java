package com.example.mechanic_zone.ClientUser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.mechanic_zone.R;

public class Settings extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        TextView emailReset = findViewById(R.id.emailReset);
        TextView passwordReset = findViewById(R.id.passwordReset);
        TextView nameReset = findViewById(R.id.nameReset);

        emailReset.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Email_Reset.class)));
        passwordReset.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Password_Reset.class)));

        nameReset.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Name_Reset.class)));


    }
}