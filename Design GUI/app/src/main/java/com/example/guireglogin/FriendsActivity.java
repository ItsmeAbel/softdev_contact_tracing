package com.example.guireglogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.view.View;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FriendsActivity extends AppCompatActivity implements AddFriend.AddFriendListener {

    private Button done_go_home;
    private FloatingActionButton floating_plus;
    private ListView listView;

    private ArrayList<FriendClass> list;

    private ArrayAdapter arrayAdapter;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        //Ladda vänlistan från SharedPreferences
        loadData();

        done_go_home = (Button) findViewById(R.id.done_go_back);
        floating_plus = (FloatingActionButton) findViewById(R.id.floating_plus);
        //addContactsToList();

        //Friend list adapters and views
        arrayAdapter = new ArrayAdapter<FriendClass>(this, R.layout.activity_listview, list);

        listView = findViewById(R.id.friend_list);
        listView.setAdapter(arrayAdapter);

        //Go back buttons listener
        done_go_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                done_go_home.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        backToHome();
                    }
                });
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

    }

    //Tillbaka till hemmenyn
    public void backToHome(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    //Test för att importera kontakter /Philip
    public void addContactsToList(){
        try {
            Cursor phone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

            int i = 0;
            while(phone.moveToNext()){
                //friendArray[i] = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                //NrArray[i] = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }

        }catch(Exception e) {
            e.printStackTrace();
        }
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

    }

}