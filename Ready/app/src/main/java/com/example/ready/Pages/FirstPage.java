package com.example.ready.Pages;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ready.DB.DBHelper;
import com.example.ready.DB.DateTime;
import com.example.ready.DB.Model.Menu;
import com.example.ready.DB.Weather;
import com.example.ready.Pages.List.RecyclerViewAdapter;
import com.example.ready.Pages.List.RecyclerViewItem;
import com.example.ready.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FirstPage extends Fragment {
    public static FirstPage newInstance() { return new FirstPage(); }
    private TextView todayDate,tomorrowDate,weekRange,todayWeather, tomorrowWeather,todayMM, tmrMM;
    private ImageView todayWeatherIcon, tomorrowWeatherIcon;
    private RecyclerView recyclerViewToday, recyclerViewTomorrow;
    private RecyclerViewAdapter adapterToday, adapterTomorrow;
    private ArrayList<RecyclerViewItem> foreCastTodayItems,foreCastTomorrowItems;
    private BarChart barChart;
    private DBHelper db;
    private DateTime dt;
    private Weather w;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first_page, container, false);
        recyclerViewToday = view.findViewById(R.id.recyclerView_today);
        recyclerViewTomorrow = view.findViewById(R.id.recyclerView_tomorrow);
        todayDate = view.findViewById(R.id.today_date_textview);
        tomorrowDate = view.findViewById(R.id.tomorrow_date_textview);
        todayWeather = view.findViewById(R.id.today_weather_textview);
        tomorrowWeather = view.findViewById(R.id.tomorrow_weather_textview);
        todayMM = view.findViewById(R.id.today_maxmin);
        tmrMM = view.findViewById(R.id.tmr_maxmin);
        todayWeatherIcon = view.findViewById(R.id.today_weather_icon);
        tomorrowWeatherIcon = view.findViewById(R.id.tomorrow_weather_icon);
        weekRange = view.findViewById(R.id.week_range_textview);
        barChart = view.findViewById(R.id.barchart);
        adapterToday = new RecyclerViewAdapter();
        adapterTomorrow = new RecyclerViewAdapter();
        foreCastTodayItems = new ArrayList<>();
        foreCastTomorrowItems = new ArrayList<>();
        dt = new DateTime();
        db = DBHelper.getInstance(view.getContext());
        w = new Weather();
        String[] weathers = {"맑음","비","비/눈","눈","소나기","빗방울","빗방울/눈날림","눈날림"};
        int [] weatherIcons = {R.drawable.sun, R.drawable.rainy, R.drawable.rainy, R.drawable.snowy, R.drawable.rainy,R.drawable.rainy,R.drawable.rainy,R.drawable.snowy};
        String pty="",tmx="", tmn="", tmr_tmx="", tmr_tmn="",tmr_pty="";
        String weatherResult = "";
        final int[] parseIndex ={0,0,0,0,0,0};
        final String[] searchKeyword = {"PTY","TMN","TMX","TMR_PTY","TMR_TMN","TMR_TMX"};
        Future<String> future = Executors.newSingleThreadExecutor().submit(new Callable<String>(){
            @Override
            public String call() throws Exception {
                String result=w.getWeather("98","77");
                return result;
            }
        });
        try {
            weatherResult = future.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int isOverTwoAm = 1;
        for(int i=0; i<6; i++){
            parseIndex[i]=weatherResult.indexOf(searchKeyword[i]);
            if(parseIndex[i] == -1){
                isOverTwoAm = 0;
                break;
            }
        }
        if(isOverTwoAm == 0){
            todayWeather.setText("정보 없음");
            tomorrowWeather.setText("정보 없음");
            todayMM.setText("새벽 2시 이후에 기상청 정보를 불러 옵니다.");
            tmrMM.setText("새벽 2시 이후에 기상청 정보를 불러옵니다. ");
        }else{
            pty = weatherResult.substring(parseIndex[0]+4,parseIndex[0]+5);
            tmn = weatherResult.substring(parseIndex[1]+4,parseIndex[2]);
            tmx = weatherResult.substring(parseIndex[2]+4,parseIndex[3]);
            tmr_pty = weatherResult.substring(parseIndex[3]+8,parseIndex[3]+9);
            tmr_tmn = weatherResult.substring(parseIndex[4]+8,parseIndex[5]);
            tmr_tmx = weatherResult.substring(parseIndex[5]+8,weatherResult.length());
            todayWeather.setText(weathers[Integer.parseInt(pty)]);
            tomorrowWeather.setText(weathers[Integer.parseInt(tmr_pty)]);
            todayWeatherIcon.setImageResource(weatherIcons[Integer.parseInt(pty)]);
            tomorrowWeatherIcon.setImageResource(weatherIcons[Integer.parseInt(pty)]);
            todayMM.setText("최고   최저\n"+tmx+"   "+tmn);
            tmrMM.setText("최고   최저\n"+tmr_tmx+"   "+tmr_tmn);
        }
        ArrayList<Menu> menus = db.getMenu();
        int[] firstFcst = {62,70,69};
        int[] secondFcst = {71,68,64};
        for(int i=0; i<menus.size(); i++){
            foreCastTodayItems.add(new RecyclerViewItem(menus.get(i).menu_name,firstFcst[i]));
            foreCastTomorrowItems.add(new RecyclerViewItem(menus.get(i).menu_name,secondFcst[i]));
        }

        adapterToday.setItemList(foreCastTodayItems);
        adapterTomorrow.setItemList(foreCastTomorrowItems);
        recyclerViewToday.setAdapter(adapterToday);
        recyclerViewToday.setLayoutManager(new LinearLayoutManager(this.getContext(),RecyclerView.HORIZONTAL,false));
        recyclerViewTomorrow.setAdapter(adapterTomorrow);
        recyclerViewTomorrow.setLayoutManager(new LinearLayoutManager(this.getContext(),RecyclerView.HORIZONTAL,false));

        ArrayList<String> sevendays = dt.getNdays(7,1);
        todayDate.setText(dt.getToday());
        tomorrowDate.setText(dt.getTomorrow());
        weekRange.setText(sevendays.get(6)+" ~ "+sevendays.get(0));
        setbarChart(70f*20000,82f*20000);
        return view;
    }

    public void setbarChart(float seven, float entire){
        ArrayList<BarDataSet> bs = new ArrayList<>();
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        ArrayList<BarEntry> entries1 = new ArrayList<>();
        ArrayList<BarEntry> entries2 = new ArrayList<>();
        ArrayList<String> x = new ArrayList<>();
        x.add("지난 7일 평균");
        x.add("전체 평균");
        entries1.add(new BarEntry(0,seven));
        entries2.add(new BarEntry(1,entire));
        String[] colorCode = {"#79CBB5","#48B0A6","#4897A6"};

        bs.add(new BarDataSet(entries1,x.get(0)));
        bs.add(new BarDataSet(entries2,x.get(1)));
        for(int i=0; i<bs.size(); i++){
            dataSets.add(bs.get(i));
            bs.get(i).setColor(Color.parseColor(colorCode[i]));
            bs.get(i).setBarBorderWidth(4f);
            bs.get(i).setValueTextSize(16);
            bs.get(i).setBarBorderColor(Color.TRANSPARENT);
        }
        float max = seven > entire? seven : entire;
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawLabels(true);
        xAxis.setDrawAxisLine(false);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(x));
        YAxis yAxisLeft = barChart.getAxisLeft();
        YAxis yAxisRight = barChart.getAxisRight();
        yAxisLeft.setAxisMinimum(0f);
        yAxisRight.setAxisMinimum(0f);
        yAxisLeft.setAxisMaximum(max+200000);
        yAxisRight.setAxisMaximum(max+200000);
        BarData barData = new BarData(dataSets);
        barData.setBarWidth(0.5f);
        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);
        Legend legend = barChart.getLegend();
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        barChart.setExtraOffsets(0, 0, 0, 10);
        barChart.animateXY(1500,1500);
    }

}
