package com.example.guireglogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class loginactivity extends AppCompatActivity {

    private Button login;
    private Button reg;
    private TextView lostpass;
    private EditText usernameEdit;
    private EditText passwordEdit;
    private String user, password;
    private String LOG_TAG = "Debug";
    public String testToken;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginactivity);

        login = (Button) findViewById(R.id.login);
        //Insert corresponding login code before actually calling for openHomeActivity
        usernameEdit = (EditText) findViewById(R.id.emailusername);
        passwordEdit = (EditText) findViewById(R.id.password);
        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                loginMethod();
            }
        });
        reg = (Button) findViewById(R.id.gotoreg);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegActivity();
            }
        });
        lostpass = (TextView) findViewById(R.id.lostpass);
        lostpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lostPass();
            }
        });
    }

    private void loginMethod(){
        user = usernameEdit.getText().toString();
        password = passwordEdit.getText().toString();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://app.zenofob.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderAPI jsonPlaceHolderApi = retrofit.create((JsonPlaceHolderAPI.class));
        Call<User> call;
        Map<String, String> fields = new HashMap<>();
        fields.put("username", user);
        fields.put("password", password);
        call = jsonPlaceHolderApi.loginUserJson(fields);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Log.d(LOG_TAG, "Code: " + response.code() + "\n");
                    return;
                }
                User user = response.body();
                testToken = "Token " + user.token;
                getStatus();
                openHomeActivity();
                Log.d(LOG_TAG, "Body: " + user.token + "\n");
                Log.d(LOG_TAG, "Code: " + response.code() + "\n");
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(LOG_TAG, t.getMessage());
            }
        });
    }

    public void openRegActivity()
    {
        Intent intent = new Intent(this, regactivity.class);
        startActivity(intent);
    }

    public void lostPass(){
        Toast.makeText(this, "Placeholder toast for lost password", Toast.LENGTH_SHORT).show();
    }
    private void openHomeActivity() {

        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("Token", testToken);
        startActivity(intent);
        finish();
    }

    private void getStatus(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://app.zenofob.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderAPI jsonPlaceHolderApi = retrofit.create((JsonPlaceHolderAPI.class));
        Call<statusValues> call;
        call = jsonPlaceHolderApi.getStatus(testToken);

        call.enqueue(new Callback<statusValues>() {
            @Override
            public void onResponse(Call<statusValues> call, Response<statusValues> response) {
                if (!response.isSuccessful()) {
                    Log.d(LOG_TAG, testToken);
                    Log.d(LOG_TAG, "GET Error\n");
                    Log.d(LOG_TAG, "Code: " + response.code() + "\n");
                    return;
                }
                Log.d(LOG_TAG, "GET Code: " + response.code() + "\n");
                statusValues GETValues = response.body();
                Log.d(LOG_TAG, "Success GET\n");
                Log.d(LOG_TAG, "ResponseID " + GETValues.identifier + "\n");
                Log.d(LOG_TAG, "ResponseContact " + GETValues.contact + "\n");

            }

            @Override
            public void onFailure(Call<statusValues> call, Throwable t) {
                Log.d(LOG_TAG, "Very Error\n");
                Log.d(LOG_TAG, t.getMessage());
            }
        });
    }
}