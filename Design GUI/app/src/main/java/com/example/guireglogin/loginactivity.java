package com.example.guireglogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class loginactivity extends AppCompatActivity {

    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginactivity);

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                openHomeActivity();
            }
        });

    }

    public void openHomeActivity()
    {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

}