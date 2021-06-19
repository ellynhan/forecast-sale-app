package com.example.ready.Pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class FirstPage extends Fragment {
    public static FirstPage newInstance() { return new FirstPage(); }
    private TextView todayDate,tomorrowDate,weekRange,todayWeather, tomorrowWeahter;
    private RecyclerView recyclerViewToday, recyclerViewTomorrow;
    private RecyclerViewAdapter adapterToday, adapterTomorrow;
    private ArrayList<RecyclerViewItem> foreCastTodayItems,foreCastTomorrowItems;

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
        return view;
    }
}
