package com.example.ready.DB;
import android.content.Context;
import android.text.format.DateUtils;

import java.security.cert.CollectionCertStoreParameters;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class DateTime {
    public int getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH", Locale.getDefault());
        String now = sdf.format(new Date().getTime());
        return Integer.parseInt(now);
    }

    public String getTomorrowDate(){
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE,1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        String tomorrow = sdf.format(c.getTime());
        return tomorrow; //2021xxxx ~
    }

    public String getTodayDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        return currentDate; //2021xxxx ~
    }

    public String getToday() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault());
        String today = sdf.format(new Date().getTime());
        return today;
    }

    public String getTomorrow(){
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE,1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault());
        String tomorrow = sdf.format(c.getTime());

        return tomorrow;
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
        return day;
    }

    public int getDayOfWeek2(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault());
        Date d = sdf.parse(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        int day = calendar.get(Calendar.DAY_OF_WEEK); //일:1 월:2 화:3 수:4 목:5 금:6 토:7
        return day-1; //일:0 월:1 화:2 수:3 목:4 금:5 토:6
    }

    public ArrayList<String> getNdays(int n, int isFull){
        ArrayList<String> days = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault());
        int year, month, day;
        String today = sdf.format(new Date().getTime());
        year = Integer.parseInt(today.substring(0,4));
        month = Integer.parseInt(today.substring(6,8));
        day = Integer.parseInt(today.substring(10,12));
        int[] daysPermonth = {31,28,31,30,31,30,31,31,30,31,30,31};
        if((year % 4 == 0 && year % 100 != 0) || year % 400 == 0){
            daysPermonth[1]=29;
        }
        for(int i=1; i<=n; i++){
            day=day-1;
            if(day==0){
                day = daysPermonth[month-1];
                month=month-1;
                if(month==0){
                    month = 12;
                    year = year -1;
                }
            }
            String monthStr = String.valueOf(month);
            String dayStr = String.valueOf(day);
            if(month<10){
                monthStr="0"+monthStr;
            }
            if(day<10){
                dayStr="0"+dayStr;
            }
            String result=monthStr+"월 "+dayStr+"일";
            if(isFull==1){
                result = year+"년 "+result;
            }
            days.add(result);
        }

//        Collections.reverse(days);
        return days;
    }
}
