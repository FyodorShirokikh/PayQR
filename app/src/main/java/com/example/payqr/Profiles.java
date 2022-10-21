package com.example.payqr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Profiles extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        Toast.makeText(Profiles.this,"Not Allowed", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);
    }

    public void GenQRonCard1(View view) {
        Intent genQRIntent = new Intent(Profiles.this, GenQR.class);
        startActivity(genQRIntent);
    }

    public void BankDetails(View view) {
        Intent BankDetailsIntent = new Intent(Profiles.this, PinCode.class);
        startActivity(BankDetailsIntent);
    }

}