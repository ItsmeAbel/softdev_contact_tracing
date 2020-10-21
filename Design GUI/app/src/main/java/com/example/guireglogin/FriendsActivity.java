package com.example.guireglogin;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class FriendsActivity extends AppCompatActivity implements AddFriend.AddFriendListener {
    private final String KEY = "icemsgValue";
    private static int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;

    //private String tempstring = "0705146224;0767767326";
    //private String tempmsg = "EMERGENCY!";
    private String temp_number = "", SENT = "SMS_SENT", DELIVERED = "SMS_DELIVERED", custommessage = "";
    private ArrayList<String> emergency_numbers;

    private Button savemessage;
    private Button deletefriends;
    private EditText icemsg;
    private Button done_go_home;
    private Button emergency;
    private FloatingActionButton floating_plus;
    private ListView listView;
    private String token;
    private String UserID;

    private ArrayList<FriendClass> list;

    private ArrayAdapter arrayAdapter;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        UserID = getIntent().getStringExtra("UserID");
        Log.e("Debug","In HomeActvity " + UserID + "\n");
        token = getIntent().getStringExtra("Token");
        Log.e("Debug","In HomeActvity " + token + "\n");

        checkForSmsPermission();

        //Ladda vänlistan från SharedPreferences
        loadData();
        deletefriends = (Button) findViewById(R.id.delete);

        icemsg = (EditText) findViewById(R.id.icemsg);
        icemsg.setText(getValue());
        savemessage = (Button) findViewById(R.id.savemsg);


        emergency = (Button) findViewById(R.id.emergency);
        done_go_home = (Button) findViewById(R.id.done_go_back);
        floating_plus = (FloatingActionButton) findViewById(R.id.floating_plus);
        //addContactsToList();

        //Friend list adapters and views
        arrayAdapter = new ArrayAdapter<FriendClass>(this, R.layout.activity_listview, list.);

        listView = findViewById(R.id.friend_list);
        listView.setAdapter(arrayAdapter);


        //Save msg listener
        savemessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveFromEditText(icemsg.getText().toString());

            }
        });

        //Delete friends listener
        deletefriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteallfriends();
            }
        });

        //Go back buttons listener
        done_go_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToHome();
            }
        });

        //Floating Plus button listener
        floating_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addFriend();
                //Snackbar.make(view, "Snackbar test", Snackbar.LENGTH_LONG).setAction("Action", null).show();

            }
        });



        //Emergency button listener
        emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                custommessage = icemsg.getText().toString();
                sendSms(emergency_numbers, custommessage);
                Toast.makeText(FriendsActivity.this, "SMS OK", Toast.LENGTH_SHORT).show();
                //Toast.makeText(FriendsActivity.this, "Emergency toast", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private String getValue(){
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String savedValue = sharedPref.getString(KEY, "");
        return savedValue;
    }

    private void saveFromEditText(String text){
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KEY, text);
        editor.apply();
    }

    //Tillbaka till hemmenyn
    public void backToHome(){
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("UserID", UserID);
        intent.putExtra("Token", token);
        startActivity(intent);
    }

    public void addFriend(){
        AddFriend addfriend = new AddFriend();
        addfriend.show(getSupportFragmentManager(), "Example Dialog");
    }

    //Override:a metoden från AddFriendListener interface:t, det som dialogen tar emot ska sättas som respektive sträng för namn/nummer för FriendClass instansen
    @Override
    public void applyTexts(String name, String number) {

        list.add(new FriendClass(name, number));
        saveData();

        arrayAdapter.notifyDataSetChanged();

        //För att gå förbi visual error atm, starta om FriendActivity
        Intent intent = new Intent(this, FriendsActivity.class);
        startActivity(intent);


    }


    //Spara mot SharedPreferences som lista till gson objekt
    private void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString("task list", json);
        editor.apply();

    }

    //Ladda mot SharedPreferences som gson objekt tillbaks till lista.
    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList>() {}.getType();
        list = gson.fromJson(json, type);

        if (list == null){
            list = new ArrayList<>();
        }

        getEmergencynumbers(list);

    }

    private void sendSms(ArrayList emergency_numbers, String message) {


        for (int i = 0; i < list.size(); i++) {
            actualsendSMS(emergency_numbers.get(i).toString(), message);

        }
    }

    private void actualsendSMS(String number, String message) {
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
                SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(number, null, message, sentPI, deliveredPI);

    }

    private void getEmergencynumbers(ArrayList list) {
        if (list.size() != 0) {
            if (emergency_numbers == null)
            {
                emergency_numbers = new ArrayList<>();
            }
            for (int i = 0; i < list.size(); i++) {
                temp_number = "";
                int maxnr = 0;
                String temp = list.get(i).toString();
                for (int j = 0; j < temp.length(); j++) {
                    char c = temp.charAt(j);
                    if (Character.isDigit(c)) {
                        temp_number += new StringBuilder().append(c).toString();
                        maxnr++;
                    }
                    if (maxnr >= 10) {
                        emergency_numbers.add(i, temp_number);
                    }
                }

            }
        } else {
            //nothingheregoback
        }
    }

    private void checkForSmsPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS) !=
                PackageManager.PERMISSION_GRANTED) {
            //Log.d(TAG, getString(R.string.permission_not_granted));
            // Permission not yet granted. Use requestPermissions().
            // MY_PERMISSIONS_REQUEST_SEND_SMS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        } else {
            // Permission already granted.
        }
    }

    private void deleteallfriends(){
        list = null;
        saveData();

        Intent intent = new Intent(this, FriendsActivity.class);
        startActivity(intent);

    }

}