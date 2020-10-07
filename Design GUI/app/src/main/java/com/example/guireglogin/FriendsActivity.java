package com.example.guireglogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class FriendsActivity extends AppCompatActivity {

    private Button done_go_home;
    private FloatingActionButton floating_plus;

    //Temp array in place of actual friend list
    String[] friendArray;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        done_go_home = (Button) findViewById(R.id.done_go_back);
        floating_plus = (FloatingActionButton) findViewById(R.id.floating_plus);
        addContactsToList();

        //Friend list adapters and views
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, friendArray);

        ListView listView = (ListView) findViewById(R.id.friend_list);
        listView.setAdapter(adapter);

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
                Snackbar.make(view, "Snackbar test", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

    }


    public void backToHome(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
    public void addContactsToList(){
        try {
            Cursor phone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

            int i = 0;
            while(phone.moveToNext()){
                friendArray[i] = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                //NrArray[i] = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }

        }catch(Exception e) {
            e.printStackTrace();
        }
    }


}