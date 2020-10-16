package com.example.appbtle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ServiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        //startService();
    }

    public void startService(View view)
    {
        Intent intent = new Intent(this, btservice.class);
        startService(intent);
    }

    public void stopService(View view)
    {
        Intent intent = new Intent(this, btservice.class);
        stopService(intent);
    }
}