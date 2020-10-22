package com.example.guireglogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class InteractionsDashboard extends AppCompatActivity {

    private String UserID;
    private String token;
    private Button gohome;
    private TextView interactint;
    private TextView confirmedint;
    private TextView unconfirmedint;

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


        //TÄNK PÅ ATT OM DU UPPDATERAR INNEHÅLLET AV TEXTVIEWSEN TROR JAG DU MÅSTE "INTENTA" OM INTERACTIONSDASHBOARD OCH GLÖM DÅ HELLER INTE USERID OCH TOKEN!!!
        //Do your total cases int magic here:
        interactint = (TextView) findViewById(R.id.interactint);

        //Do your confirmed cases int magic here:
        confirmedint = (TextView) findViewById(R.id.confirmedint);

        //Do your unconfirmed cases int magic here:
        unconfirmedint = (TextView) findViewById(R.id.unconfirmedint);

    }

    private void gohome(){
        Intent intent = new Intent(InteractionsDashboard.this, HomeActivity.class);
        intent.putExtra("UserID", UserID);
        intent.putExtra("Token", token);
        startActivity(intent);
    }


}