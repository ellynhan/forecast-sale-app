package com.example.ready.Pages;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;

public class ThirdPage extends Fragment {
    private LineChart lineChart;
    private PieChart piechartDay;
    private PieChart piechartWeather;
    private DBHelper db;
    private DateTime dt;
    private ArrayList<Menu> menus;
    private ArrayList<String> Ndays;
    private ArrayList<ArrayList<Sale>> sales;
    private TextView errorNoticeLineChart;
    private TextView errorNoticePieChartWeather;
    private TextView errorNoticePieChartDay;
    private LinearLayout lineChartLayout;
    private int days;
    private int currMenu; //0전체 메뉴, 1~ : menuId + 1
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.third_page, container, false);
        db = DBHelper.getInstance(v.getContext());
        dt = new DateTime();
        lineChartLayout = v.findViewById(R.id.lineChart_layout);
        Ndays = dt.getNdays(31,1);
        sales = new ArrayList<>();
        lineChart = v.findViewById(R.id.lineChart);
        piechartDay = v.findViewById(R.id.piechart_day);
        piechartWeather = v.findViewById(R.id.piechart_weather);
        errorNoticeLineChart = v.findViewById(R.id.lineChart_error);
        errorNoticePieChartDay = v.findViewById(R.id.piechartDay_error);
        errorNoticePieChartWeather = v.findViewById(R.id.piechartWeather_error);
        menus = db.getMenu();
        ArrayList<String> menuList = new ArrayList<String>();
        days = 7;
        currMenu = 0;
        final Spinner stats_spinner = v.findViewById(R.id.spinner_stats_standard);
        Spinner period_spinner = v.findViewById(R.id.spinner_period);
        menuList.add("전체 메뉴");
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
        for(int i=0; i<menus.size(); i++){
            menuList.add(menus.get(i).menu_name);
        }
        if(menus.size()==0){
            errorNoticeLineChart.setVisibility(View.VISIBLE);
            errorNoticeLineChart.setText("메뉴 데이터가 없습니다.");
            errorNoticePieChartWeather.setText("메뉴 데이터가 없습니다.");
            errorNoticePieChartDay.setText("메뉴 데이터가 없습니다.");
            lineChartLayout.setVisibility(View.GONE);
            piechartDay.setVisibility(View.GONE);
            piechartWeather.setVisibility(View.GONE);
        }else{
            //7일간의 데이터가 있는지 확인
            Boolean fullData = Boolean.TRUE;
            for(int i=0; i<31; i++){
                ArrayList<Sale> oneDaySale = db.getSaleWithDate(Ndays.get(i));
                if(oneDaySale.size()==0 && i<7){
                    fullData = Boolean.FALSE;
                    break;
                }
                if(oneDaySale.size()==0){
                    sales.add(sales.get(sales.size()-1));
                }else{
                    sales.add(oneDaySale);
                }
            }

            if(fullData==Boolean.FALSE){
                errorNoticeLineChart.setVisibility(View.VISIBLE);
                errorNoticePieChartDay.setVisibility(View.VISIBLE);
                errorNoticePieChartWeather.setVisibility(View.VISIBLE);
                lineChartLayout.setVisibility(View.GONE);
                piechartWeather.setVisibility(View.GONE);
                piechartDay.setVisibility(View.GONE);
            }
            else{
                errorNoticeLineChart.setVisibility(View.GONE);
                errorNoticePieChartDay.setVisibility(View.GONE);
                errorNoticePieChartWeather.setVisibility(View.GONE);
                lineChartLayout.setVisibility(View.VISIBLE);
                piechartWeather.setVisibility(View.VISIBLE);
                piechartDay.setVisibility(View.VISIBLE);
                stats_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        currMenu = (int) id;
                        setNdaysLineChart(days,currMenu);
                        try {
                            setPieChartByDay(7,currMenu);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        lineChart.notifyDataSetChanged();
                        lineChart.invalidate();
                        piechartDay.notifyDataSetChanged();
                        piechartDay.invalidate();
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
                            isChanged = 1;
                        }
                        if(isChanged == 1){
                            setNdaysLineChart(days,currMenu);
                            lineChart.setMinimumWidth(100*days);
                            lineChart.notifyDataSetChanged();
                            lineChart.invalidate();
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // literally nothing.
                    }
                });
                setPieChartByWeather(7,0);
            }
        }
        return v;
    }

    public void setNdaysLineChart(int d, int menuId){
        // x축 설정
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawLabels(true);
        xAxis.setDrawAxisLine(false);
        ArrayList<String> reversed = dt.getNdays(d,0);
        Collections.reverse(reversed);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(reversed));
        ArrayList<ArrayList<Entry>> menuEntryArray = new ArrayList<>();
        ArrayList<LineDataSet> ls = new ArrayList<LineDataSet>();
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        String[] colorCode = {"#79CBB5","#48B0A6","#4897A6"};
        for(int i=0; i<menus.size(); i++){
            menuEntryArray.add(new ArrayList<Entry>());
        }
        for(int i=0; i<d; i++){
            ArrayList<Sale> s = db.getSaleWithDate(Ndays.get(d-1-i));
            for(int j=0; j<s.size(); j++){
                menuEntryArray.get(j).add(new Entry(i,s.get(j).qty));
            }
        }
        for(int i=0; i<menus.size(); i++){
            if(menuId==0||menuId==i+1){
                ls.add(new LineDataSet(menuEntryArray.get(i),menus.get(i).menu_name));
            }
        }

        for(int i=0; i<ls.size(); i++){
            dataSets.add(ls.get(i));
            ls.get(i).setColor(Color.parseColor(colorCode[i]));
            ls.get(i).setCircleColor(Color.parseColor(colorCode[i]));
            ls.get(i).setLineWidth(4);
            ls.get(i).setValueTextSize(16);
            ls.get(i).setDrawFilled(true);
            ls.get(i).setFillDrawable(this.getContext().getDrawable(R.drawable.gradient_blue_green));
        }
        lineChart.setData(new LineData(dataSets));
        lineChart.getDescription().setEnabled(false);
        Legend legend = lineChart.getLegend();
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        lineChart.setExtraOffsets(0, 0, 0, 10);
        lineChart.animateY(1000);
    }

    public void setPieChartByDay(int d, int menuId) throws ParseException {
        piechartDay.setUsePercentValues(true);
        piechartDay.getDescription().setEnabled(false);
        piechartDay.setDragDecelerationFrictionCoef(0.95f);
        piechartDay.setDrawHoleEnabled(false);
        piechartDay.setHoleColor(Color.BLACK);
        piechartDay.setTransparentCircleRadius(61f);

        int[] sumPerDayOfWeek = {0,0,0,0,0,0,0};//일:0 월:1 화:2 수:3 목:4 금:5 토:6
        int[] numsPerDayOfWeek = {0,0,0,0,0,0,0};
        float[]  salesPerDayOfWeek = {0,0,0,0,0,0,0};
        String[] dayOfWeek = {"일요일","월요일","화요일","수요일","목요일","금요일","토요일"};
        for(int i=0; i<d; i++){
            int dow = dt.getDayOfWeek2(Ndays.get(i));
            numsPerDayOfWeek[dow]+=1;
            for(int j=0; j<sales.get(i).size(); j++){
                if(menuId==0||sales.get(i).get(j).menu_id==menuId){ //여기 뭔가 이상한데 동작은 잘함.
                    sumPerDayOfWeek[dow]+=sales.get(i).get(j).qty;
                }
            }
        }
        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();
        for(int i=0; i<7; i++){
            salesPerDayOfWeek[i]=(float)sumPerDayOfWeek[i]/numsPerDayOfWeek[i];
            yValues.add(new PieEntry(salesPerDayOfWeek[i],dayOfWeek[i]));
        }
        PieDataSet dataSet = new PieDataSet(yValues,"");
        dataSet.setColors(Color.parseColor("#D5F5E3"),Color.parseColor("#ABEBC6"),
                Color.parseColor("#58D68D"),Color.parseColor("#28B463"),Color.parseColor("#138D75"),
                Color.parseColor("#0E6655"),Color.parseColor("#0B5345"));
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        PieData data = new PieData((dataSet));
        data.setValueTextSize(20f);
        data.setValueTextColor(Color.YELLOW);
        piechartDay.animateX(1500);
        piechartDay.setData(data);
    }

    public void setPieChartByWeather(int d, int menuId){
        piechartWeather.setUsePercentValues(true);
        piechartWeather.getDescription().setEnabled(false);
        piechartWeather.setDragDecelerationFrictionCoef(0.95f);
        piechartWeather.setDrawHoleEnabled(false);
        piechartWeather.setHoleColor(Color.BLACK);
        piechartWeather.setTransparentCircleRadius(61f);

        int[] sumPerWeather = {0,0,0,0,0,0,0,0};//일:0 월:1 화:2 수:3 목:4 금:5 토:6
        int[] numsPerWeather = {0,0,0,0,0,0,0,0};
        float[]  salesPerWeather = {0,0,0,0,0,0,0,0};
        String[] weathers = {"맑음","비","비/눈","눈","소나기","빗방울","빗방울/눈날림","눈날림"};
        for(int i=0; i<d; i++){
            for(int j=0; j<sales.get(i).size(); j++){
                if(menuId==0||sales.get(i).get(j).menu_id==menuId){
                    int w = sales.get(i).get(j).rain;
                    numsPerWeather[w]+=1;
                    sumPerWeather[w]+=sales.get(i).get(j).qty;
                }
            }
        }
        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();
        for(int i=0; i<8; i++){
            if(sumPerWeather[i]!=0){
                salesPerWeather[i]=(float)sumPerWeather[i]/numsPerWeather[i];
                yValues.add(new PieEntry(salesPerWeather[i],weathers[i]));
            }
        }
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
}
