package com.example.guireglogin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ChangeStatus extends AppCompatActivity {

    private Button status_close;
    private Button healthyStatus;
    private Button sickStatus;
    private Button symptomsStatus;
    private String token;
    private String UserID;
    private boolean healthBool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_status);

        UserID = getIntent().getStringExtra("UserID");
        Log.e("Debug","In HomeActvity " + UserID + "\n");
        token = getIntent().getStringExtra("Token");
        Log.e("Debug","In Changestatus " + token + "\n");

        //Close the activity/return home
        status_close = (Button) findViewById(R.id.status_close);
        status_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               backToHome();
            }
        });

        //Symptoms Button (COVID-19 SYMPTOMS BUTTON)
        symptomsStatus = (Button) findViewById(R.id.symptoms_status);
        symptomsStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSymptomious(token);
                symptomiousToast();
                backToHome();
            }
        });

        //Sick Button (COVID-19 POSITIVE BUTTON)
        sickStatus = (Button) findViewById(R.id.sick_status);
        sickStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSick(token);
                sickToast();
                backToHome();

            }
        });
    }

    private void setSick(String token){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://app.zenofob.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderAPI jsonPlaceHolderApi = retrofit.create((JsonPlaceHolderAPI.class));
        Call<setStatus> call;
        Map<String, Boolean> fields = new HashMap<>();
        fields.put("infected", true);

        call = jsonPlaceHolderApi.setSickStatus(token, fields);

        call.enqueue(new Callback<setStatus>() {
            @Override
            public void onResponse(Call<setStatus> call, Response<setStatus> response) {
                if (!response.isSuccessful()) {
                    Log.d("debug", "Code: " + response.code() + "\n");
                    return;
                }
                Log.d("debug", "Code: " + response.code() + "\n");
                Log.d("debug", "Success");

            }
            @Override
            public void onFailure(Call<setStatus> call, Throwable t) {
                Log.d("debug", t.getMessage());
            }
        });
    }

    private void setSymptomious(String token){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://app.zenofob.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderAPI jsonPlaceHolderApi = retrofit.create((JsonPlaceHolderAPI.class));
        Call<setStatus> call;
        Map<String, Boolean> fields = new HashMap<>();
        fields.put("unconfirmed_infected", true);

        call = jsonPlaceHolderApi.setSickStatus(token, fields);

        call.enqueue(new Callback<setStatus>() {
            @Override
            public void onResponse(Call<setStatus> call, Response<setStatus> response) {
                if (!response.isSuccessful()) {
                    Log.d("debug", "Code: " + response.code() + "\n");
                    return;
                }
                Log.d("debug", "Code: " + response.code() + "\n");
                Log.d("debug", "Success");

            }
            @Override
            public void onFailure(Call<setStatus> call, Throwable t) {
                Log.d("debug", t.getMessage());
            }
        });
    }

    public void backToHome(){
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("UserID", UserID);
        intent.putExtra("Token", token);
        startActivity(intent);
    }

    private void sickToast(){
        //You are COVID-19 positive
        Toast.makeText(this, getResources().getString(R.string.covidpos), Toast.LENGTH_SHORT).show();

    }

    private void symptomiousToast(){
        //You have got COVID-19 symptoms
        Toast.makeText(this, getResources().getString(R.string.covidsympt), Toast.LENGTH_SHORT).show();

    }

}
