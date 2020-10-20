package com.example.guireglogin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class BluetoothActivity extends AppCompatActivity {

    Intent btintent;
    BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
    public static final int BLUETOOTH_REQUEST_ENABLE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        btintent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

        if(btAdapter == null){

            Toast.makeText(this, "This unit do not support bluetooth!",Toast.LENGTH_LONG).show();

        }

        else if(btAdapter.isEnabled()){

            Toast.makeText(this, "Bluetooth is enabled",Toast.LENGTH_LONG).show();
            startLogin();

        }

        else if(!btAdapter.isEnabled()){

            Intent btintent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(btintent, BLUETOOTH_REQUEST_ENABLE);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){

            Toast.makeText(this, "Bluetooth is ON", Toast.LENGTH_SHORT).show();
            startLogin();

        }

        else if(resultCode == RESULT_CANCELED){

            Toast.makeText(this, "Bluetooth is off", Toast.LENGTH_SHORT).show();
            checkagain();

        }

    }

    private void startLogin(){
        Intent liintent = new Intent(this, loginactivity.class);
        startActivity(liintent);
    }
    private void checkagain(){
        Intent chintent = new Intent(this, BluetoothActivity.class);
        startActivity(chintent);
    }
}