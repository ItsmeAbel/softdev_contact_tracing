package com.example.guireglogin;

import android.icu.text.SimpleDateFormat;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Interactions {

    public String identifier;
    public String date;

    
    @RequiresApi(api = Build.VERSION_CODES.N)
    public Interactions(String identifier){

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        this.date = df.format(c);

        this.identifier = identifier;
    }
}
