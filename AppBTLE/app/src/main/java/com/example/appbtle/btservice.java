package com.example.appbtle;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
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
import android.os.IBinder;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class btservice extends Service {


    private static final boolean DEBUG = true;
    private static final String TAG = "BluetoothService";
    private static final int UUID_INDEX = 0;
    private static final ParcelUuid uuidfilter = ParcelUuid.fromString("576a92c8-08a7-11eb-0000-000000000000");
    private static final ParcelUuid uuidmask = ParcelUuid.fromString("11111111-1111-1111-0000-00000000");

    private int userid;
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
    private ArrayList<Long> addresslist;
    private List<ParcelUuid> uuid_list;

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            startMyOwnForeground();
        }

        else
            startForeground(1, new Notification());
    }

    private void startMyOwnForeground(){
        String NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";
        String channelName = "Corona APP";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Tracking corona...")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();

        startForeground(2, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();

        userid = 124121231;
        id = new UUID(uuidfilter.getUuid().getMostSignificantBits(), userid);

        Log.e(TAG, String.valueOf(id));
        Log.e(TAG, String.valueOf(id.getLeastSignificantBits()));

        filterlista = new ArrayList<ScanFilter>();
        addresslist = new ArrayList<Long>();

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

        callbacks();


        scanLeDevice();
        if(DEBUG){Log.i(TAG, "Scanning....");}

        return START_STICKY;
    }


    private void callbacks(){

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
                    if(uuid.getUuid().getMostSignificantBits() == uuidfilter.getUuid().getMostSignificantBits()
                        && !addresslist.contains(uuid.getUuid().getLeastSignificantBits()))

                    {
                        addresslist.add(uuid.getUuid().getLeastSignificantBits());
                        Log.i("id",String.valueOf(uuid.getUuid().getLeastSignificantBits()));
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

    private void scanLeDevice(){

        if(DEBUG){Log.i(TAG, "Advertising....");}
        mBluetoothAdvertiser.startAdvertising(aSetting,aData, advertiserCallback);
        if(DEBUG){Log.i(TAG, "Advertising DONE");}
        if (DEBUG) {Log.i(TAG, "Scanning...");}
        mBluetoothLeScanner.startScan(filterlista, sSettings, scanCallback);
        if (DEBUG) {Log.i(TAG, "Scanning DONE!");}
        if (DEBUG) {Log.i(TAG, "Scanning again...");}

    }

    private void get_uuid(final ScanResult result){
        uuid_list = result.getScanRecord().getServiceUuids();
        uuid = uuid_list.get(UUID_INDEX);
    }

    @Override
    public void onDestroy(){
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {return null;}

}

