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
    private String token;
    private boolean healthBool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_status);

        token = getIntent().getStringExtra("Token");
        Log.e("Debug","In Changestatus " + token + "\n");

        //Close the activity/return home
        status_close = (Button) findViewById(R.id.status_close);
        status_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        //Sick Button
        sickStatus = (Button) findViewById(R.id.sick_status);
        sickStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSick(token);
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

}
