package com.example.guireglogin;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Communication extends AppCompatActivity {

    private JsonPlaceHolderAPI jsonPlaceHolderApi;
    private String LOG_TAG = "DEBUG";
    private int content;

    public void createUser(String email, String password) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://app.zenofob.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderApi = retrofit.create((JsonPlaceHolderAPI.class));

        Map<String, String> fields = new HashMap<>();
        fields.put("email", email);
        fields.put("password", password);

        Call<User> call = jsonPlaceHolderApi.createUserJson(fields);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.isSuccessful()){
                    Log.d(LOG_TAG,"Code: " + response.code());
                    return;
                }

                String content = "";
                content += "Code: " + response.code() + "\n";

                Log.d(LOG_TAG, content);


            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                Log.d(LOG_TAG, t.getMessage());
            }
        });
    }

}