package com.example.ready.Pages;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ready.DB.DBHelper;
import com.example.ready.DB.DateTime;
import com.example.ready.DB.Model.Sale;
import com.example.ready.DB.Model.Menu;
import com.example.ready.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class ThirdPage extends Fragment {
    private LineChart chart;
    private PieChart piechartDay;
    private PieChart piechartWeather;
    private DBHelper db;
    private DateTime dt;
    private ArrayList<Menu> menus;
    private int days;
    private int currMenu;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.third_page, container, false);
        db = DBHelper.getInstance(v.getContext());
        dt = new DateTime();
        chart = v.findViewById(R.id.lineChart);
        piechartDay = v.findViewById(R.id.piechart_day);
        piechartWeather = v.findViewById(R.id.piechart_weather);
        menus = db.getMenu();
        ArrayList<String> menuList = new ArrayList<String>();
        days = 7;
        currMenu = 0;
        final Spinner stats_spinner = v.findViewById(R.id.spinner_stats_standard);
        Spinner period_spinner = v.findViewById(R.id.spinner_period);
        menuList.add("전체 메뉴");
        for(int i=0; i<menus.size(); i++){
            menuList.add(menus.get(i).menu_name);
        }

        ArrayAdapter<String> stats_adapter = new ArrayAdapter<String>(v.getContext(), R.layout.spinner_list, menuList);
        ArrayAdapter<CharSequence> period_adapter = ArrayAdapter.createFromResource(v.getContext(), R.array.array_selection_period, R.layout.spinner_list);
        stats_adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        period_adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        stats_spinner.setAdapter(stats_adapter);
        period_spinner.setAdapter(period_adapter);

        stats_spinner.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        period_spinner.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        stats_spinner.setBackgroundColor(Color.TRANSPARENT);
        period_spinner.setBackgroundColor(Color.TRANSPARENT);

        stats_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                System.out.println("Spinner listner");
                currMenu = (int) id;
                setNdaysLineChart(days,currMenu);
                chart.notifyDataSetChanged();
                chart.invalidate();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // literally nothing.
            }
        });
        period_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int isChanged = 0;
                if(id==0&&days!=7){
                    days = 7;
                    isChanged =1;
                }
                if(id!=0&&days!=30){
                    days = 30;
                    chart.setMinimumWidth(100*days);
                    isChanged = 1;
                }
                if(isChanged == 1){
                    setNdaysLineChart(days,currMenu);
                    chart.notifyDataSetChanged();
                    chart.invalidate();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // literally nothing.
            }
        });
        setPieChartByDay();
        setPieChartByWeather();
        return v;
    }

    public void setNdaysLineChart(int d, int menuId){
        // x축 설정
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawLabels(true);
        xAxis.setDrawAxisLine(false);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dt.getNdaysWOY(d)));

        //ArrayList<Menu> menus = db.getMenu();

        ArrayList<ArrayList<Entry>> m = new ArrayList<ArrayList<Entry>>();
        ArrayList<LineDataSet> ls = new ArrayList<LineDataSet>();
        ArrayList<String> Ndays = dt.getNdaysFull(d);
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        String[] colorCode = {"#79CBB5","#48B0A6","#4897A6"};
        for(int i=0; i<menus.size(); i++){
            m.add(new ArrayList<Entry>());
        }

        for(int i=0; i<d; i++){
            ArrayList<Sale> s = db.getSale(Ndays.get(i));
            for(int j=0; j<s.size(); j++){
                m.get(j).add(new Entry(i,s.get(j).qty));
            }
        }
        for(int i=0; i<menus.size(); i++){
            if(menuId==0||menuId==i+1){
                ls.add(new LineDataSet(m.get(i),menus.get(i).menu_name));
            }
        }
        for(int i=0; i<ls.size(); i++){
            dataSets.add(ls.get(i));
            ls.get(i).setColor(Color.parseColor(colorCode[i]));
            ls.get(i).setCircleColor(Color.parseColor(colorCode[i]));
            ls.get(i).setLineWidth(4);
            ls.get(i).setValueTextSize(16);
        }
        chart.setData(new LineData(dataSets));

        // hide description label
        chart.getDescription().setEnabled(false);

        // legend re-positioning
        Legend legend = chart.getLegend();
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        chart.setExtraOffsets(0, 0, 0, 10);
    }
    public void setPieChartByWeather(){
        piechartWeather.setUsePercentValues(true);
        piechartWeather.getDescription().setEnabled(false);
        piechartWeather.setDragDecelerationFrictionCoef(0.95f);

        piechartWeather.setDrawHoleEnabled(false);
        piechartWeather.setHoleColor(Color.BLACK);
        piechartWeather.setTransparentCircleRadius(61f);

        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();

        yValues.add(new PieEntry(12f,"없음"));
        yValues.add(new PieEntry(12f,"비"));
        yValues.add(new PieEntry(12f,"비/눈"));
        yValues.add(new PieEntry(12f,"눈"));
        yValues.add(new PieEntry(12f,"소나기"));
        yValues.add(new PieEntry(12f,"빗방울"));
        yValues.add(new PieEntry(12f,"빗방울/눈날림"));
        yValues.add(new PieEntry(12f,"눈날림"));
        PieDataSet dataSet = new PieDataSet(yValues,"");
        dataSet.setColors(Color.parseColor("#D5F5E3"),Color.parseColor("#ABEBC6"),Color.parseColor("#A2D9CE"),
                Color.parseColor("#58D68D"),Color.parseColor("#28B463"),Color.parseColor("#138D75"),
                Color.parseColor("#0E6655"),Color.parseColor("#0B5345"));
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(20f);
        data.setValueTextColor(Color.YELLOW);

        piechartWeather.setData(data);
    }
    public void setPieChartByDay(){
        piechartDay.setUsePercentValues(true);
        piechartDay.getDescription().setEnabled(false);
        piechartDay.setDragDecelerationFrictionCoef(0.95f);

        piechartDay.setDrawHoleEnabled(false);
        piechartDay.setHoleColor(Color.BLACK);
        piechartDay.setTransparentCircleRadius(61f);

        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();

        yValues.add(new PieEntry(15f,"월요일"));
        yValues.add(new PieEntry(15f,"화요일"));
        yValues.add(new PieEntry(15f,"수요일"));
        yValues.add(new PieEntry(15f,"목요일"));
        yValues.add(new PieEntry(15f,"금요일"));
        yValues.add(new PieEntry(15f,"토요일"));
        yValues.add(new PieEntry(15f,"일요일"));

        PieDataSet dataSet = new PieDataSet(yValues,"");
        dataSet.setColors(Color.parseColor("#D5F5E3"),Color.parseColor("#ABEBC6"),
                Color.parseColor("#58D68D"),Color.parseColor("#28B463"),Color.parseColor("#138D75"),
                Color.parseColor("#0E6655"),Color.parseColor("#0B5345"));
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
//        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(20f);
        data.setValueTextColor(Color.YELLOW);

        piechartDay.setData(data);
    }
}
