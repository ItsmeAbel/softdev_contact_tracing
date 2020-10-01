package com.example.guireglogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.os.Handler;



public class MainActivity extends AppCompatActivity {

    Handler handler;
    Runnable runnable;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img = findViewById(R.id.img);
        img.animate().alpha(1000).setDuration(0);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent dsp = new Intent(MainActivity.this,loginactivity.class);
                startActivity(dsp);
                finish();
            }
        }, 1000);
    }

}