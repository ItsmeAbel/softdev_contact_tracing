package com.example.guireglogin;

import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



public class Continue extends AppCompatActivity {

    public void isTrue(){
<<<<<<< HEAD
        // hoppa till loginscreen

=======
        // hoppa till loginscreen DONE
        // Få toasts att fungera från current thread
>>>>>>> 8115bc7d822b63875c0e35c3c3d8fb11b29ef8d1
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
