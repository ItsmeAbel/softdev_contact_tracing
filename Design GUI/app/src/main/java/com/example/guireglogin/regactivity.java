package com.example.guireglogin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class regactivity extends AppCompatActivity {

    private Button reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regactivity);

        reg = (Button) findViewById(R.id.register);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registermethod();
            }
        });
    }

    public void registermethod(){
        Toast.makeText(this, "Placeholder Toast, server response here", Toast.LENGTH_SHORT).show();
    }

}