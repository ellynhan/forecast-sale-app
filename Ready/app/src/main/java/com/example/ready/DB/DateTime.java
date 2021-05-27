package com.example.ready.DB;
import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class DateTime {
    public String getCurrentDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        return currentDate; //2021xxxx ~
    }

    public String getYesterday(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        String yesterday = sdf.format(new Date().getTime()-24*3600*1000);
        return yesterday; //2021xxxx ~
    }

    public String getCurrentTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH", Locale.getDefault());
        String currentTime = sdf.format(new Date());
        int currTime = Integer.parseInt(currentTime);
        return currentTime; //00, 01, 02...
    }

    public int getDayOfWeek(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return day; //일:1 월:2 화:3 수:4 목:5 금:6 토:7
    }



}
