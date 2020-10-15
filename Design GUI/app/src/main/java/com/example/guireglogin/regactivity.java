package com.example.guireglogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class regactivity extends AppCompatActivity {

    private Button reg;
    private EditText username;
    private EditText pass;
    private String user, passw;
    static final int createUser=1;
    private String LOG_TAG = "Debug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regactivity);
        username = (EditText) findViewById(R.id.regemailusername);
        pass = (EditText) findViewById(R.id.regpassword);
        reg = (Button) findViewById(R.id.register);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registermethod();
            }
        });

    }

    private void registermethod(){
        user = username.getText().toString();
        passw = pass.getText().toString();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://app.zenofob.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderAPI jsonPlaceHolderApi = retrofit.create((JsonPlaceHolderAPI.class));
        Call<User> call;
        Map<String, String> fields = new HashMap<>();
        fields.put("email", user);
        fields.put("password", passw);
        call = jsonPlaceHolderApi.createUserJson(fields);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Log.d(LOG_TAG, "Code: " + response.code() + "\n");
                    return;
                }
                Log.d(LOG_TAG, "Code: " + response.code() + "\n");
                openLoginActivity();
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(LOG_TAG, t.getMessage());
            }
        });

    }

    public void openLoginActivity(){
        Log.d(LOG_TAG, "Loginactivity");
        Intent intent = new Intent(regactivity.this , loginactivity.class);
        Log.d(LOG_TAG, "1");
        startActivity(intent);
        Log.d(LOG_TAG, "2");
        finish();
    }
}