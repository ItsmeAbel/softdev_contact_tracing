package com.example.guireglogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MyInfoActivity extends AppCompatActivity {

    private Button change_password;
    private Button save_and_apply;
    private Button done_go_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        change_password = (Button) findViewById(R.id.change_password);
        done_go_back = (Button) findViewById(R.id.done_go_back);
        //save_and_apply= (Button) findViewById(R.id.save_and_apply);

        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
        done_go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToHome();
            }
        });
        /*save_and_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitToServer();
            }
        }); */
    }

    public void backToHome(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    /*public void commitToServer(){
        Toast.makeText(this, "server response", Toast.LENGTH_SHORT).show();
    }*/

    public void changePassword(){
        Toast.makeText(this, "open new fragment", Toast.LENGTH_SHORT).show();
    }
}