package com.example.guireglogin;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class btservice extends Service {

    private static final boolean DEBUG = false;
    private static final String TAG = "BluetoothService";
    private static final int UUID_INDEX = 0;
    private static final ParcelUuid uuidfilter = ParcelUuid.fromString("00000000-0000-0000-1a2b-3c4d5e6f1a2b");
    private static final ParcelUuid uuidmask = ParcelUuid.fromString("00000000-0000-0000-1111-111111111111");
    private static ParcelUuid tempuuid = null;

    private String UserID;
    private String LSB = "-1a2b-3c4d5e6f1a2b";
    private String MSB;
    private String userid;
    private String token;
    private UUID id;
    private ParcelUuid uuid;
    private AdvertiseSettings aSetting;
    private AdvertiseData aData;
    private ScanSettings sSettings;
    private ScanCallback scanCallback;
    private AdvertiseCallback advertiserCallback;
    private BluetoothLeScanner mBluetoothLeScanner;
    private BluetoothLeAdvertiser mBluetoothAdvertiser;
    private ArrayList<ScanFilter> filterlista;
    private ArrayList<String> addresslist;
    private ArrayList<Long> idlist;
    private ArrayList<String> lastchecklist;
    private List<ParcelUuid> uuid_list;
    private ArrayList<String> interactionsList;
    public int NumberOfInteractions = 0;
    private String tempstring, reverstemp;


    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            startMyOwnForeground();
        }

        else
            startForeground(1, new Notification());
    }

    //Creating a notification channel and start it in the foreground so the user knows when its tracking.
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground(){
        String NOTIFICATION_CHANNEL_ID = "application 7.0";
        String channelName = "Corona APP";
        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        channel.setLightColor(Color.YELLOW);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        manager.createNotificationChannel(channel);

        NotificationCompat.Builder notificationbuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Tracking corona...")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();

        startForeground(101, notification);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        if(DEBUG){Log.i(TAG,"OnStartCommand!");}

        lastchecklist = new ArrayList<String>();
        addresslist = new ArrayList<String>();
        idlist = new ArrayList<Long>();

        Bundle data = intent.getExtras();
        if(data == null){
            Log.e(TAG, "Data is null");
        }
        else{
            Log.i(TAG, "Data is not null");
            userid = (String) data.get("UserID");
            token = (String) data.get("Token");
        }
        String reverseid = new StringBuilder(userid).reverse().toString();

        MSB = reverseid + LSB;

        tempuuid = ParcelUuid.fromString(MSB);

        id = new UUID(tempuuid.getUuid().getMostSignificantBits(), uuidfilter.getUuid().getLeastSignificantBits()); //Our unik id combinde with an MSB of ah uuid together with our unik userID
        filterlista = new ArrayList<ScanFilter>();     //The list with our filters
        addresslist = new ArrayList<String>();           //A list were we store all the people we have been nearby


        if(DEBUG){Log.i(TAG, "Buildning settings for advertiser...");}
        //Settings for the advertiser
        aSetting = new AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_POWER)
                .setConnectable(false)
                .build();
        if(DEBUG){Log.i(TAG, "Buildning DONE!");}

        if(DEBUG){Log.i(TAG, "Buildning data for advertiser...");}
        //Data for the advertiser
        aData = new AdvertiseData.Builder()
                .addServiceUuid(new ParcelUuid(id))
                .setIncludeDeviceName(false)
                .build();
        if(DEBUG){Log.i(TAG, "Buildning DONE!");}

        if(DEBUG){Log.i(TAG, "Buildning setting for scanner...");}
        //Settings for the scanner
        sSettings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
                .build();
        if(DEBUG){Log.i(TAG, "Buildning DONE!");}


        if(DEBUG){Log.i(TAG, "Buildning filter...");}
        //Creat a filter for the scanning
        ScanFilter sFilter = new ScanFilter.Builder()
                .setServiceUuid(uuidfilter,uuidmask)
                .build();
        filterlista.add(sFilter);
        if(DEBUG){Log.i(TAG, "Buildning DONE!");}

        //Creating our callbacks
        callbacks();
        //Start the scanning
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                if(DEBUG){Log.i(TAG,"Stop scan");}
                stopLEScan();
                if(DEBUG){Log.i(TAG,"Do magic!");}
                domagic();
                getStatus();
                if(DEBUG){Log.i(TAG,"Start scan");}
                scanLeDevice();

                handler.postDelayed(this, 10000);
            }
        };
        handler.post(runnable);


        if(DEBUG){Log.i(TAG, "Scanning....");}

        return START_STICKY;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void callbacks(){
        //Start of advertiser callbacks
        advertiserCallback = new AdvertiseCallback() {
            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                super.onStartSuccess(settingsInEffect);
                if(DEBUG){Log.i(TAG,"Successful advertising!");}
            }

            @Override
            public void onStartFailure(int errorCode) {
                super.onStartFailure(errorCode);
                if(DEBUG){Log.e(TAG,"Advertisning FAILED Error Code:"+String.valueOf(errorCode));}
            }
            //End of advertiser callbacks
        };

        scanCallback = new ScanCallback() {
            //scanning callbacks
            @Override
            public void onScanResult(int callbackType, final ScanResult result) {
                super.onScanResult(callbackType, result);
                if (DEBUG) {Log.i(TAG, "Found Device");}

                if (result == null || result.getDevice() == null) {
                    if (DEBUG) { Log.e(TAG, "DEVICE: NULL"); }
                    return;
                }

                else {
                    get_uuid(result);
                    Log.i(TAG,String.valueOf(result.getRssi()));
                    Log.i(TAG,String.valueOf(uuid.toString()));
                    if(uuid.getUuid().getLeastSignificantBits() == uuidfilter.getUuid().getLeastSignificantBits()
                            && !idlist.contains(uuid.getUuid().getMostSignificantBits()))

                    {

                        tempstring = uuid.getUuid().toString();
                        tempstring = tempstring.substring(0,18);
                        reverstemp = new StringBuilder(tempstring).reverse().toString();
                        Log.e("Borde funka", reverstemp);
                        idlist.add(uuid.getUuid().getMostSignificantBits());
                        addresslist.add(reverstemp);
                        Log.i("id",String.valueOf(addresslist));
                    }
                }

            }
            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
            }

            @Override
            public void onScanFailed(int errorCode) {
                if (DEBUG){Log.e(TAG, "Scan FAILED Error Code:"+String.valueOf(errorCode)); }
                super.onScanFailed(errorCode);
            }
        };
        //End of callbacks
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void scanLeDevice(){

        if(DEBUG){Log.i(TAG, "Advertising....");}
        //Creating the advertiser
        mBluetoothAdvertiser.startAdvertising(aSetting,aData, advertiserCallback);
        if(DEBUG){Log.i(TAG, "Advertising DONE");}

        if (DEBUG) {Log.i(TAG, "Scanning...");}
        //Start the scan
        mBluetoothLeScanner.startScan(filterlista, sSettings, scanCallback);

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void stopLEScan(){
        mBluetoothAdvertiser.stopAdvertising(advertiserCallback);
        mBluetoothLeScanner.stopScan(scanCallback);
    }

    //A funcation to got the result since its a list.
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void get_uuid(final ScanResult result){
        uuid_list = result.getScanRecord().getServiceUuids();
        uuid = uuid_list.get(UUID_INDEX);
        Log.e(TAG, String.valueOf(uuid));
        Log.e(TAG, String.valueOf(uuid.getUuid().getLeastSignificantBits()));
    }

    public String stringconverter(String scanuuid){
        String reversetemp, temp;
        temp = scanuuid.substring(0,18);
        reversetemp = new StringBuilder(temp).reverse().toString();
        return reversetemp;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void domagic(){
        ArrayList<String> temp = new ArrayList<String>();
        temp.addAll(addresslist);
        temp.removeAll(lastchecklist);
        if(temp != null){
            lastchecklist.removeAll(lastchecklist);
            lastchecklist.addAll(addresslist);
            int i = 0;
            interactionsList = new ArrayList<String>();
            while (i < temp.size()){

                interactionsList.add(temp.get(i));
                NumberOfInteractions++;
                i++;

            }
            if(!temp.isEmpty()) {
                System.out.println("In PushInteractions");
                pushInteractoins();
            }
            System.out.println("Temp is empty");
        }
    }

    private void getStatus(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://app.zenofob.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderAPI jsonPlaceHolderApi = retrofit.create((JsonPlaceHolderAPI.class));
        Call<statusValues> call;
        call = jsonPlaceHolderApi.getStatus(token);

        call.enqueue(new Callback<statusValues>() {
            @Override
            public void onResponse(Call<statusValues> call, Response<statusValues> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "getStatus: Error\n");
                    Log.d(TAG, "Code: " + response.code() + "\n");
                    return;
                }
                Log.d(TAG, "getStatus: Code: " + response.code() + "\n");
                statusValues GETValues = response.body();
                System.out.println("I am positive " + GETValues.contact);
                if(GETValues.contact== true){
                    NotificationFunc();
                }
                System.out.println("I am symptomious " + GETValues.unconfirmed_contact);
                if(GETValues.unconfirmed_contact== true){
                    NotificationFunc2();
                }

            }

            @Override
            public void onFailure(Call<statusValues> call, Throwable t) {
                Log.d(TAG, "getStatus: Very Error\n");
                Log.d(TAG, t.getMessage());
            }
        });
    }

    private void pushInteractoins(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://app.zenofob.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderAPI jsonPlaceHolderApi = retrofit.create((JsonPlaceHolderAPI.class));
        Call<setStatus> call;

        call = jsonPlaceHolderApi.pushInteractions(token, interactionsList);

        call.enqueue(new Callback<setStatus>() {
            @Override
            public void onResponse(Call<setStatus> call, Response<setStatus> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "PushInteractions Error\n");
                    Log.d(TAG, "Code: " + response.code() + "\n");
                    return;
                }
                Log.d(TAG, "GET Code: " + response.code() + "\n");
                Log.d(TAG, "In Push Interactions");

            }

            @Override
            public void onFailure(Call<setStatus> call, Throwable t) {
                Log.d(TAG, "In PushInteractions: Very Error\n");
                Log.d(TAG, t.getMessage());
            }
        });
    }

    private void NotificationFunc(){
        notificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Channel")
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)

                //Fredrikusan ändra här*********************************************************Someone you encountered has have tested positve for COVID-19
                .setContentText(getResources().getString(R.string.encounterpositive));
                //******************************************************************************

        Intent notificationIntent = new Intent(this, HomeActivity.class);
        PendingIntent contextIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contextIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    private void notificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Corona channel";
            String description = "Corona channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Channel", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void NotificationFunc2(){
        notificationChannel2();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Channel")
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)


                //Fredrikusan ändra här***************************************************Someone you encountered have symptoms for COVID-19
                .setContentText(getResources().getString(R.string.encountersymptoms));
                //*************************************************************************


        Intent notificationIntent = new Intent(this, HomeActivity.class);
        PendingIntent contextIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contextIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    private void notificationChannel2(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Corona channel";
            String description = "Corona channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Channel", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDestroy(){
        domagic();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {return null;}

}

