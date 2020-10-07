package com.example.guireglogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



public class ChangeStatus extends AppCompatActivity {

    private Button status_close;
    private Button mild_status;
    private Button weak_status;
    private Button strong_status;
    private Button extreme_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_status);

        //Close the activity/return home
        status_close = (Button) findViewById(R.id.status_close);
        status_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        //Mild Status Button
        mild_status = (Button) findViewById(R.id.mild_status);
        mild_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Mild",Toast.LENGTH_SHORT).show();
            }
        });

        //Weak Status Button
        weak_status = (Button) findViewById(R.id.weaksymptoms_status);
        weak_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Weak",Toast.LENGTH_SHORT).show();
            }
        });

        //Strong Status Button
        strong_status = (Button) findViewById(R.id.strongsymptoms_status);
        strong_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Strong",Toast.LENGTH_SHORT).show();
            }
        });

        //Extreme Status Button
        extreme_status = (Button) findViewById(R.id.extreme_status);
        extreme_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Extreme",Toast.LENGTH_SHORT).show();
            }
        });

    }

}
