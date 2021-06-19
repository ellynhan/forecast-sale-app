package com.example.ready.Pages;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import java.util.ArrayList;

public class FirstPage extends Fragment {
    public static FirstPage newInstance() { return new FirstPage(); }
    private TextView todayDate,tomorrowDate,weekRange,todayWeather, tomorrowWeahter;
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
        tomorrowWeahter = view.findViewById(R.id.tomorrow_weather_textview);
        weekRange = view.findViewById(R.id.week_range_textview);
        barChart = view.findViewById(R.id.barchart);
        adapterToday = new RecyclerViewAdapter();
        adapterTomorrow = new RecyclerViewAdapter();
        foreCastTodayItems = new ArrayList<>();
        foreCastTomorrowItems = new ArrayList<>();
        dt = new DateTime();
        db = DBHelper.getInstance(view.getContext());
        w = new Weather();
        System.out.println(dt.getTime());
        ArrayList<Menu> menus = db.getMenu();
        for(int i=0; i<menus.size(); i++){
            foreCastTodayItems.add(new RecyclerViewItem(menus.get(i).menu_name,i+1));
            foreCastTomorrowItems.add(new RecyclerViewItem(menus.get(i).menu_name,i+1));
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
        setbarChart();
        return view;
    }

    public void setbarChart(){
        ArrayList<BarDataSet> bs = new ArrayList<>();
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        ArrayList<BarEntry> entries1 = new ArrayList<>();
        ArrayList<BarEntry> entries2 = new ArrayList<>();
        ArrayList<String> x = new ArrayList<>();
        x.add("지난 7일 평균");
        x.add("전체 평균");
        entries1.add(new BarEntry(0,150f));
        entries2.add(new BarEntry(1,130f));
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
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawLabels(true);
        xAxis.setDrawAxisLine(false);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(x));
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
