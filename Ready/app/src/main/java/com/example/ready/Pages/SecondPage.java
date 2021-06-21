package com.example.ready.Pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.ready.Calendar.*;
import com.example.ready.DB.DBHelper;
import com.example.ready.DB.Model.Menu;
import com.example.ready.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.text.SimpleDateFormat;
import java.util.*;

public class SecondPage extends Fragment {
    private ArrayList<Menu> menus;
    private ArrayList<String> menuList = new ArrayList<>();

    private int menu_id;
    private View v;
    private TextView day, weather, menu_name, predict_value;
    private ImageView imageView;
    private MaterialCalendarView materialCalendarView;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private DBHelper dbHelper;
    private Spinner spinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.second_page, container, false);

        menuList.clear();
        dbHelper = DBHelper.getInstance(v.getContext());

        materialCalendarView = v.findViewById(R.id.calendarView);
        slidingUpPanelLayout = v.findViewById(R.id.sliding_layout);
        spinner = v.findViewById(R.id.spinner_menu);

        day = v.findViewById(R.id.day);
        weather = v.findViewById(R.id.weather);
        menu_name = v.findViewById(R.id.menu_name);
        predict_value = v.findViewById(R.id.predict_value);
        imageView = v.findViewById(R.id.weather_image);

        calendarInit();
        spinnerInit();

        return v;
    }

    private void calendarInit() {
        materialCalendarView.setSelectedDate(CalendarDay.today());
        materialCalendarView.addDecorators(
                new SaturdayDecorator(),
                new SundayDecorator(),
                new WeekDayDecorator()
        );
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                panelInit(date);
            }
        });
    }

    private void spinnerInit() {
        menus = dbHelper.getMenu();

        menuList.add("메뉴 선택");
        for(int i = 0; i < menus.size(); i++)
            menuList.add(menus.get(i).menu_name);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                v.getContext(),
                R.layout.spinner_list_black,
                menuList
        );

        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                menu_id = (int) id;
                System.out.println(menu_id);

                if(menu_id != 0) {
                    ArrayList<String> dates = dbHelper.getSaleDateWithId(menu_id);

                    materialCalendarView.removeDecorators();
                    materialCalendarView.invalidateDecorators();
                    materialCalendarView.addDecorators(
                            new SaturdayDecorator(),
                            new SundayDecorator(),
                            new WeekDayDecorator(),
                            new GuessDecorator(dates)
                    );

//                    ArrayList<Integer> qtys = dbHelper.getSaleQtyWithId(menu_id);

//                    for(int i = 0; i < dates.size(); i++) {
//                        String date = dates.get(i);
//                        int qty = qtys.get(i);
//
//                        System.out.println("DATE " + date + " QTY " + qty);
//
//                        materialCalendarView.addDecorator(
//                                new GuessDecorator(date, qty)
//                        );
//                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void panelInit(CalendarDay date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
        String dateString = simpleDateFormat.format(date.getDate());

        day.setText(dateString);

        if(menu_id != 0) {
            menu_name.setText(menuList.get(menu_id) + " ");

            try {
                ArrayList<Integer> sale = dbHelper.getSaleQtySkyWithIdAndDate(menu_id, dateString);
                predict_value.setText(String.valueOf(sale.get(0)));

                String sky = "";
                switch (sale.get(1)) {
                    case 0:
                        sky = "맑음";
                        imageView.setImageResource(R.drawable.sun);
                        break;
                    case 1:
                        sky = "흐림";
                        imageView.setImageResource(R.drawable.cloudy);
                        break;
                    case 2:
                        sky = "비";
                        imageView.setImageResource(R.drawable.rainy);
                        break;
                }
                weather.setText(sky);
            } catch(IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }
}
