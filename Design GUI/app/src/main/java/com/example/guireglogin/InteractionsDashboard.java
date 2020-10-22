package com.example.guireglogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InteractionsDashboard extends AppCompatActivity {

    private String UserID;
    private String token;
    private Button gohome;
    private TextView interactint;
    private TextView confirmedint;
    private TextView unconfirmedint;
    private String NumberOfInteractions = "0";
    private String ConfirmedInt = "0";
    private String UnconfirmedInt = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interactions_dashboard);

        interactint = (TextView) findViewById(R.id.interactint);
        unconfirmedint = (TextView) findViewById(R.id.unconfirmedint);
        confirmedint = (TextView) findViewById(R.id.confirmedint);

        UserID = getIntent().getStringExtra("UserID");
        Log.e("Debug","In InteractDash " + UserID + "\n");
        token = getIntent().getStringExtra("Token");
        Log.e("Debug","In InteractDash " + token + "\n");

        getStatus();

        gohome = (Button) findViewById(R.id.gohome);
        gohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gohome();
            }
        });


        //TÄNK PÅ ATT OM DU UPPDATERAR INNEHÅLLET AV TEXTVIEWSEN TROR JAG DU MÅSTE "INTENTA" OM INTERACTIONSDASHBOARD OCH GLÖM DÅ HELLER INTE USERID OCH TOKEN!!!
        //Do your total cases int magic here:
        interactint.setText(NumberOfInteractions);

        //Do your confirmed cases int magic here:
        confirmedint.setText(ConfirmedInt);

        //Do your unconfirmed cases int magic here:
        unconfirmedint.setText(UnconfirmedInt);

    }

    private void gohome(){
        Intent intent = new Intent(InteractionsDashboard.this, HomeActivity.class);
        intent.putExtra("UserID", UserID);
        intent.putExtra("Token", token);
        startActivity(intent);
    }

    private void getStatus(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://app.zenofob.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderAPI jsonPlaceHolderApi = retrofit.create((JsonPlaceHolderAPI.class));
        Call<statusValues> call;
        call = jsonPlaceHolderApi.getStatus(token);

        call.enqueue(new Callback<statusValues>() {
            @Override
            public void onResponse(Call<statusValues> call, Response<statusValues> response) {
                if (!response.isSuccessful()) {
                    Log.d("debug", "getStatus: Error\n");
                    Log.d("debug", "Code: " + response.code() + "\n");
                    return;
                }
                Log.d("debug", "getStatus: Code: " + response.code() + "\n");
                statusValues GETValues = response.body();
                System.out.println("I am positive " + GETValues.contact);

                //Här får vi värden
                NumberOfInteractions = GETValues.total_interactions;
                ConfirmedInt = GETValues.count_confirmed;
                UnconfirmedInt = GETValues.count_unconfirmed;

                System.out.println("Confirmed: " + GETValues.count_confirmed);
                System.out.println("Unconfirmed: " + GETValues.count_unconfirmed);
                System.out.println("Interactions: " + GETValues.total_interactions);

            }

            @Override
            public void onFailure(Call<statusValues> call, Throwable t) {
                Log.d("debug", "getStatus: Very Error\n");
                Log.d("debug", t.getMessage());
            }
        });
    }


}