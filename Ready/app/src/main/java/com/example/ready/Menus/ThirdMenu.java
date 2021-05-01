package com.example.ready.Menus;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.ready.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ThirdMenu extends Fragment {
    private LineChart chart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.third_menu, container, false);
        ArrayList<String> days = new ArrayList<>();

        // x축 이름을 위한 최근 7일 가져오기
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
        for(int i = 1; i <= 7; i++) {
            Date day = calendar.getTime();

            SimpleDateFormat dateFormat = new SimpleDateFormat("d");
            days.add(dateFormat.format(day) + " 일");

            calendar.add(Calendar.DAY_OF_WEEK, 1);
        }

        chart = v.findViewById(R.id.lineChart);

        // x축 설정
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawLabels(true);
        xAxis.setDrawAxisLine(false);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));

        // graph data(DB)
        ArrayList<Entry> menu1 = new ArrayList<>();
        ArrayList<Entry> menu2 = new ArrayList<>();

//        for(int i = 0; i < 10; i++) {
//            float value = (float) (Math.random() * 10);
//            menu1.add(new Entry(i, value));
//        }
        menu1.add(new Entry(0, 10));
        menu1.add(new Entry(1, 15));
        menu1.add(new Entry(2, 5));
        menu1.add(new Entry(3, 30));
        menu1.add(new Entry(4, 20));
        menu1.add(new Entry(5, 10));
        menu1.add(new Entry(6, 50));

        menu2.add(new Entry(0, 5));
        menu2.add(new Entry(1, 10));
        menu2.add(new Entry(2, 15));
        menu2.add(new Entry(3, 20));
        menu2.add(new Entry(4, 25));
        menu2.add(new Entry(5, 30));
        menu2.add(new Entry(6, 35));

        // description
        LineDataSet set1, set2;
        set1 = new LineDataSet(menu1, "꽈배기");
        set2 = new LineDataSet(menu2, "볶음밥");

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);

        LineData data = new LineData(dataSets);

        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);

        set2.setColor(Color.GREEN);
        set2.setCircleColor(Color.GREEN);

        // hide description label
        chart.getDescription().setEnabled(false);

        // legend re-positioning
        Legend legend = chart.getLegend();
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        chart.setExtraOffsets(0, 0, 0, 10);

        chart.setData(data);

        return v;
    }
}
