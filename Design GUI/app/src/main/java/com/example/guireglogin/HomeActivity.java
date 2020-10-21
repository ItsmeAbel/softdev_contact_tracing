package com.example.guireglogin;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.ToolbarWidgetWrapper;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.guireglogin.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView nav_view;
    private Button change_status;
    private String token;


    private static final String TAG = "HomeActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        token = getIntent().getStringExtra("Token");
        Log.e("Debug","In HomeActvity " + token + "\n");

        //notification();

        toolbar = findViewById(R.id.toolbar);
        nav_view = findViewById(R.id.navigation_view);
        drawer = findViewById(R.id.drawer_layout);
        nav_view.setNavigationItemSelectedListener(this);
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

        startService();

        //Close Status button
        //close_status = (Button) findViewById(R.id.status_close);



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
            case R.id.my_info:
                initMyInfo();
                break;
            case R.id.maps_location:
                if(isServiceOK()){
                    initMaps();
                }
                break;
            case R.id.logout:
                Toast.makeText(this, "Logout Tost", Toast.LENGTH_SHORT).show();
                initLogOut();
                break;
            case R.id.friends:
                initFriends();
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
        startService(intent);
    }

    public void initMaps(){
        Intent intent = new Intent(HomeActivity.this, MapsActivity.class);
        intent.putExtra("Token", token);
        startActivity(intent);
    }
    public void initMyInfo(){
        Intent infoIntent = new Intent(this, MyInfoActivity.class);
        infoIntent.putExtra("Token", token);
        startActivity(infoIntent);
    }

    public void initLogOut() {
        Intent logoutIntent = new Intent(this, loginactivity.class);
        startActivity(logoutIntent);
    }

    public void initFriends(){
        Intent friendsIntent = new Intent(this, FriendsActivity.class);
        friendsIntent.putExtra("Token", token);
        startActivity(friendsIntent);
    }
    public void initLang(){
        Intent switchlangIntent = new Intent(this, LanguageActivity.class);
        switchlangIntent.putExtra("Token", token);
        startActivity(switchlangIntent);
    }

    public void initStatusChange(){

        Intent statusIntent = new Intent(this, ChangeStatus.class);
        statusIntent.putExtra("Token", token);
        startActivity(statusIntent);

    }
/*
    private void notification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

    }*/
}