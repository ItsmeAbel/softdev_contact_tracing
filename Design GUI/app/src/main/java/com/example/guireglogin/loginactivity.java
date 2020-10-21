package com.example.guireglogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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
    private String testToken;
    private String UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginactivity);

        login = findViewById(R.id.login);
        //Insert corresponding login code before actually calling for openHomeActivity
        usernameEdit = findViewById(R.id.emailusername);
        passwordEdit = findViewById(R.id.password);
        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                NotificationFunc();
                loginMethod();
            }
        });
        reg = findViewById(R.id.gotoreg);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegActivity();
            }
        });
        lostpass = findViewById(R.id.lostpass);
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
        intent.putExtra("UserID", UserID);
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
                UserID=GETValues.identifier;
                openHomeActivity();
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

    private void NotificationFunc(){
        notificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Channel")
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentText("Someone you encountered has been affected by Corona");

        Intent notificationIntent = new Intent(this, HomeActivity.class);
        PendingIntent contextIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contextIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    private void notificationChannel(){
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Corona channel";
            String description = "Corona channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Channel", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}