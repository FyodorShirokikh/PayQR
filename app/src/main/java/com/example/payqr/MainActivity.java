package com.example.payqr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void EnterInApp(View view) {

        Intent enterIntent = new Intent(MainActivity.this, Profiles.class);
        startActivity(enterIntent);
    }

}