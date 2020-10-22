package com.example.guireglogin;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.ToolbarWidgetWrapper;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView nav_view;
    private Button change_status;
    private String token;
    private String UserID;

    public ToggleButton onOff;
    private static final String TAG = "HomeActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    public static final int BLUETOOTH_REQ_CODE = 1;
    final BluetoothAdapter bAdapter = BluetoothAdapter.getDefaultAdapter(); //bluetooth adapter
    public int toggler = 0;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        UserID = getIntent().getStringExtra("UserID");
        Log.e("Debug","In HomeActvity " + UserID + "\n");
        token = getIntent().getStringExtra("Token");
        Log.e("Debug","In HomeActvity " + token + "\n");

        startService();
        toolbar = findViewById(R.id.toolbar);
        nav_view = findViewById(R.id.navigation_view);
        drawer = findViewById(R.id.drawer_layout);
        nav_view.setNavigationItemSelectedListener(this);
        onOff = findViewById(R.id.TS);
        //instansiearar allt som behövs instansiearas
        setSupportActionBar(toolbar);
        //om vi gör setSupportActionBar har vi en helt ny action bar som vi kommer ha mer kontroll över
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.nav_bar_open, R.string.nav_bar_close); // detta gör så att vi får typ en enum för att kunna öppna och stänga nav_baren
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        //Change Status Button
        change_status = (Button) findViewById(R.id.change_status);

        change_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(HomeActivity.this, "Status GUI placeholder", Toast.LENGTH_SHORT).show();
                initStatusChange();
            }
        });



        //Close Status button
        //close_status = (Button) findViewById(R.id.status_close);


        //if bluetooth is on, turn on the switch
        if (!bAdapter.isEnabled()) {
            // To set whether switch is on/off use:
            onOff.setChecked(false);
            toggler = 2;
        } else {
            // Bluetooth is enabled
            onOff.setChecked(true);
            toggler = 1;
        }


        onOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){

                    Toast.makeText(HomeActivity.this, "ON", Toast.LENGTH_SHORT).show();
                    btON();
                    toggler = 1;

                }else {
                    Toast.makeText(HomeActivity.this, "OFF", Toast.LENGTH_SHORT).show();
                    btOFF();
                    toggler = 2;

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)) //gravity compat gör så att den pushar ut eller in hela sliden utan att ändra storlek
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();

        super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) { //on click listener för alla knappar i navbaren är ett interface(implements) med ett switchcase för att se vilken knapp som blir tryckt
        switch (item.getItemId()){
            case R.id.change_language:
                //Toast.makeText(this, "you clicked language", Toast.LENGTH_LONG).show();
                initLang();
                break;
            case R.id.maps_location:
                if (toggler == 1){
                    if(isServiceOK()){
                        initMaps();
                    }
                    break;
                }else{
                    Toast.makeText(this, "You Need to turn Tracking On!", Toast.LENGTH_SHORT).show();
                    break;
                }
            case R.id.logout:
                Toast.makeText(this, "Logout Tost", Toast.LENGTH_SHORT).show();
                initLogOut();
                break;

            case R.id.Interactions:
                initInteractions();
                break;

            case R.id.friends:
                initFriends();
                break;
            case R.id.RTData:
                initDashboard1();
                break;
        }
        return true;
    }


    //CHECKS IF THE CORRECT GOOGLE PLAY SERVICES IS INSTALLED
    public boolean isServiceOK(){
        Log.d(TAG,"is service OK: Checking Google servieces Version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(HomeActivity.this);
        //if there are no errors
        if(available == ConnectionResult.SUCCESS){
            //make map request
            Log.d(TAG, "Is Service OK: Google Play Services is working!");
            return true;
        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){ //if there is a resolvable error
            Log.d(TAG, "Is ServiceOK: A resolvable error has occurred!");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(HomeActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else { //Unresolvable Error occurred
            Toast.makeText(this, "ERROR: Unresolvable error occurred!", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void startService()
    {
        Intent intent = new Intent(this, btservice.class);
        Bundle data = new Bundle();
        data.putString("UserID", UserID);
        data.putString("Token", token);
        intent.putExtras(data);
        startService(intent);
    }

    public void initInteractions(){
        Intent intent = new Intent(HomeActivity.this, InteractionsDashboard.class);
        intent.putExtra("UserID", UserID);
        intent.putExtra("Token", token);
        startActivity(intent);
    }

    public void initMaps(){
        Intent intent = new Intent(HomeActivity.this, MapsActivity.class);
        startActivity(intent);
    }
    public void initLogOut() {
        Intent logoutIntent = new Intent(this, loginactivity.class);
        startActivity(logoutIntent);
    }

    public void initFriends(){
        Intent friendsIntent = new Intent(this, FriendsActivity.class);
        friendsIntent.putExtra("UserID", UserID);
        friendsIntent.putExtra("Token", token);
        startActivity(friendsIntent);
    }
    public void initLang(){
        Intent switchlangIntent = new Intent(this, LanguageActivity.class);
        switchlangIntent.putExtra("UserID", UserID);
        switchlangIntent.putExtra("Token", token);
        startActivity(switchlangIntent);
    }

    public void initStatusChange(){

        Intent statusIntent = new Intent(this, ChangeStatus.class);
        statusIntent.putExtra("UserID", UserID);
        statusIntent.putExtra("Token", token);
        startActivity(statusIntent);

    }

    public void initDashboard1(){
        Toast.makeText(HomeActivity.this,"To Real-Time Dashboard",Toast.LENGTH_SHORT).show();
        Intent dashboard = new Intent(this,Real_Time_Data_Dashboard.class);
        startActivity(dashboard);

    }
    public void btON(){
        if(bAdapter == null)
        {
            Toast.makeText(HomeActivity.this,"Bluetooth Not Supported",Toast.LENGTH_SHORT).show();
        }
        else{
            if(!bAdapter.isEnabled()){
                Intent eintent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(eintent, BLUETOOTH_REQ_CODE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            //Toast.makeText(HomeActivity.this,"Bluetooth Is ON",Toast.LENGTH_SHORT).show();
            onOff.setChecked(true);

        }else{
            if(resultCode == RESULT_CANCELED){
                //Toast.makeText(HomeActivity.this,"Bluetooth Is OFF",Toast.LENGTH_SHORT).show();
                onOff.setChecked(false);
            }
        }
    }

    public void btOFF(){
        if (bAdapter.isEnabled()){
            bAdapter.disable(); //disable bluetooth
            onOff.setChecked(false);    //make the swicth off
        }
    }

}