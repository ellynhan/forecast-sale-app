package com.example.ready.DB;
import android.content.Context;
import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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

    public String getYesterdayFull(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault());
        String yesterday = sdf.format(new Date().getTime()-24*3600*1000);
        return yesterday;
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

    public ArrayList<String> get7daysFull(){
        ArrayList<String> days = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault());
        for(int i=1; i<=7; i++){
            days.add(sdf.format(new Date().getTime()-24*3600*1000*i));
        }
        return days;
    }

    public ArrayList<String> get7daysWOY(){
        ArrayList<String> days = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("MM월 dd일", Locale.getDefault());
        for(int i=7; i>=1; i--){
            days.add(sdf.format(new Date().getTime()-24*3600*1000*i));
        }
        return days;
    }

    public ArrayList<String> get30daysFull(){
        ArrayList<String> days = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault());
        for(int i=1; i<=30; i++){
            days.add(sdf.format(new Date().getTime()-24*3600*1000*i));
        }
        return days;
    }

    public ArrayList<String> getNdaysFull(int n){
        ArrayList<String> days = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault());
        for(int i=n; i>=1; i--){
            days.add(sdf.format(new Date().getTime()-24*3600*1000*i));
        }
        return days;
    }

    public ArrayList<String> getNdaysWOY(int n){
        ArrayList<String> days = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("MM월 dd일", Locale.getDefault());
        for(int i=n; i>=1; i--){
            days.add(sdf.format(new Date().getTime()-24*3600*1000*i));
        }
        return days;
    }

}
