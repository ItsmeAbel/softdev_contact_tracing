package com.example.guireglogin;

import android.graphics.Color;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.io.IOException;


public class GetDirections extends AsyncTask<Object,String,String> {

    GoogleMap mMap;
    String url;
    String googleDirectionsData;
    LatLng latLng;



    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap)objects[0];
        url = (String)objects[1];
        latLng = (LatLng)objects[2];



        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            googleDirectionsData = downloadUrl.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googleDirectionsData;
    }

    @Override
    protected void onPostExecute(String s) {

        String[] directionsList;
        DataParser parser = new DataParser();
        directionsList = parser.parseDirections(s);
        displayDirection(directionsList);
    }

    public void displayDirection(String[] directionsList) {

        int count = directionsList.length;
        for (String s : directionsList) {
            this.mMap.addPolyline(new PolylineOptions().color(Color.BLUE).width(10).addAll(PolyUtil.decode(s)));
        }

    }

}
