package com.example.appbtle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Intent btintent;
    TextView bttextview;
    private String address;

    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    int BLUETOOTH_REQUEST_ENABLE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btintent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        bttextview = findViewById(R.id.bttextV);

        if (mBluetoothAdapter == null){

            Toast.makeText(MainActivity.this, "This unit do not support bluetooth!",Toast.LENGTH_LONG).show();
            btsupport();

        }

        else if(!mBluetoothAdapter.isEnabled()){

            Intent btintent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(btintent, BLUETOOTH_REQUEST_ENABLE);
        }

        else if(mBluetoothAdapter.isEnabled()){

            Toast.makeText(MainActivity.this, "Bluetooth is enabled",Toast.LENGTH_LONG).show();
            service_center();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Toast.makeText(MainActivity.this, "Bluetooth is ON", Toast.LENGTH_SHORT).show();
            service_center();
        }
        else if(resultCode == RESULT_CANCELED){
            Toast.makeText(MainActivity.this, "Bluetooth is OFF", Toast.LENGTH_SHORT).show();
            bttextview.setText("Bluetooth is disabled");
        }
    }

    public void activateBluetooth(View v){

        btintent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        bttextview = findViewById(R.id.bttextV);

        if (mBluetoothAdapter == null){

            Toast.makeText(MainActivity.this, "This unit do not support bluetooth!",Toast.LENGTH_LONG).show();
            btsupport();

        }

        else if(!mBluetoothAdapter.isEnabled()){

            Intent btintent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(btintent, BLUETOOTH_REQUEST_ENABLE);
        }

        else if(mBluetoothAdapter.isEnabled()){

            Toast.makeText(MainActivity.this, "Bluetooth is enabled",Toast.LENGTH_LONG).show();
            service_center();

        }

    }
    public void service_center(){

        Intent intent = new Intent(this, ServiceActivity.class);
        startActivity(intent);

    }

    public void btsupport(){

        Intent intent = new Intent(this, btsupport.class);
        startActivity(intent);

    }

}