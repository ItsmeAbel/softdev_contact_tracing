package com.example.guireglogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Locale;

public class LanguageActivity extends AppCompatActivity{
    private RadioGroup group;
    private Button button;
    private String langLoad;
    private Locale locale;
    private String token;
    private String UserID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lang);

        UserID = getIntent().getStringExtra("UserID");
        Log.e("Debug","In HomeActvity " + UserID + "\n");
        token = getIntent().getStringExtra("Token");
        Log.e("Debug","In HomeActvity " + token + "\n");

        group = findViewById(R.id.radio_group);
        button = findViewById(R.id.apply_button_id);

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                checkedButton(view);
            }
        });
    }
    public void checkedButton(View v){
        int radioId = group.getCheckedRadioButtonId();

        switch (radioId) {
            case R.id.lang_en:
                langLoad = "en";
                Toast.makeText(this, "do these even work?", Toast.LENGTH_SHORT).show();
                changelang(langLoad);
                break;

            case R.id.lang_sv:
                langLoad = "sv";
                Toast.makeText(this, "do these even work?", Toast.LENGTH_SHORT).show();
                changelang(langLoad);
                break;

            default:
                Toast.makeText(this, "nothing is selected", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    public void changelang(String langLoad) {
        locale = new Locale(langLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        Toast.makeText(this, "if this works im a god", Toast.LENGTH_SHORT).show();
        backtohome();
    }
    public void backtohome(){
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("UserID", UserID);
        intent.putExtra("Token", token);
        startActivity(intent);
    }
}
