package com.example.guireglogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class loginactivity extends AppCompatActivity {

    private Button login;
    private Button reg;
    private TextView lostpass;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginactivity);

        login = (Button) findViewById(R.id.login);
        //Insert corresponding login code before actually calling for openHomeActivity
        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                openHomeActivity();
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

    public void openHomeActivity()
    {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void openRegActivity()
    {
        Intent intent = new Intent(this, regactivity.class);
        startActivity(intent);
    }

    public void lostPass(){
        Toast.makeText(this, "Placeholder toast for lost password", Toast.LENGTH_SHORT).show();
    }

}