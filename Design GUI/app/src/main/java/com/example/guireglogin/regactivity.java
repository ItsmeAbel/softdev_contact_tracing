package com.example.guireglogin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class regactivity extends AppCompatActivity {

    private Button reg;
    private EditText username;
    private EditText pass;
    private String user, passw;

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

    public void registermethod(){
        user = username.getText().toString();
        passw = pass.getText().toString();
        //stringsen från textboxesen som ska skickas till databasen?
    }

}