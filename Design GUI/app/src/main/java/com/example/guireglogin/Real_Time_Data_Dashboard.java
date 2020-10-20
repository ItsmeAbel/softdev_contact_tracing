package com.example.guireglogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.os.Handler;



public class Real_Time_Data_Dashboard extends AppCompatActivity {

WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.real_time_data_dashboard);

        myWebView = findViewById(R.id.mygreatwebView);
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.loadUrl("https://www.google.se/");

    }
}