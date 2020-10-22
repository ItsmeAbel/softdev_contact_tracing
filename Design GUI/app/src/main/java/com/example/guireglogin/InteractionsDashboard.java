package com.example.guireglogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class InteractionsDashboard extends AppCompatActivity {

    private String UserID;
    private String token;
    private Button gohome;
    private TextView interactint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interactions_dashboard);

        UserID = getIntent().getStringExtra("UserID");
        Log.e("Debug","In InteractDash " + UserID + "\n");
        token = getIntent().getStringExtra("Token");
        Log.e("Debug","In InteractDash " + token + "\n");

        gohome = (Button) findViewById(R.id.gohome);
        gohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gohome();
            }
        });

        //Do your int magic here:
        interactint = (TextView) findViewById(R.id.interactint);


    }

    private void gohome(){
        Intent intent = new Intent(InteractionsDashboard.this, HomeActivity.class);
        intent.putExtra("UserID", UserID);
        intent.putExtra("Token", token);
        startActivity(intent);
    }


}