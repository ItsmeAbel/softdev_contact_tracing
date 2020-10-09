package com.example.testapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView textViewResult;
    private JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult = findViewById(R.id.text_view_result);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://app.zenofob.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create((JsonPlaceHolderApi.class));

        createUser("Erik123", "lösen123");

    }

    private void createUser(String email, String password){
        Map<String, String> fields = new HashMap<>();
        fields.put("email", email );
        fields.put("password", password );

        Call<User> call = jsonPlaceHolderApi.createUser(fields);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.isSuccessful()){
                    textViewResult.setText("Code: " + response.code());
                    return;
                }

                User userResponse = response.body();

                String content = "";
                content += "Code: " + response.code() + "\n";

                textViewResult.setText(content);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }
}