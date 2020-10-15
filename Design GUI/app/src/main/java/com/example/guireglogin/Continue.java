package com.example.guireglogin;

import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



public class Continue extends AppCompatActivity {

    public void isTrue(){
        // hoppa till loginscreen DONE
        // Få toasts att fungera från current thread
        Log.d("Debug", "True");

        openLoginActivity();
    }
    public void isFalse(){
        // hoppa till regscreen
        Log.d("Debug", "False");
    }

    public void openLoginActivity(){

        Intent intent = new Intent(this, loginactivity.class);
        startActivity(intent);
        finish();
    }



}
