package com.example.ready.Pages;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.ready.Calendar.GuessDecorator;
import com.example.ready.Calendar.SaturdayDecorator;
import com.example.ready.Calendar.SundayDecorator;
import com.example.ready.Calendar.WeekDayDecorator;
import com.example.ready.DB.DBHelper;
import com.example.ready.DB.Model.Menu;
import com.example.ready.DB.Model.Sale;
import com.example.ready.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Collections;

public class SecondPage extends Fragment {
    private ArrayList<Menu> menus;
    private ArrayList<Sale> sales;

    private View v;
    private MaterialCalendarView materialCalendarView;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private DBHelper dbHelper;
    private RadioGroup radioGroup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.second_page, container, false);

        dbHelper = DBHelper.getInstance(v.getContext());

        materialCalendarView = v.findViewById(R.id.calendarView);
        slidingUpPanelLayout = v.findViewById(R.id.sliding_layout);
        radioGroup = v.findViewById(R.id.radioGroup);

        calendarInit();
        calendarDataLoad();
        radioGroupInit();

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
            }
        });
    }

    private void calendarDataLoad() {
        // sales = dbHelper.getSale();
//
//        materialCalendarView.addDecorators(
//                new GuessDecorator("20", Color.RED, Collections.singleton(date)),
//                new GuessDecorator("30", Color.RED, Collections.singleton(date)),
//                new GuessDecorator("10", Color.BLUE, Collections.singleton(date2)),
//                new GuessDecorator("5", Color.GREEN, Collections.singleton(date3))
//        );
    }

    private void radioGroupInit() {
        menus = dbHelper.getMenu();

        for(int i = 0; i < menus.size(); i++) {
            RadioButton radioButton = new RadioButton(v.getContext());
            radioButton.setLayoutParams(new RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.WRAP_CONTENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT
            ));
            radioButton.setId(menus.get(i)._id);
            radioButton.setText(menus.get(i).menu_name);

            radioGroup.addView(radioButton);
        }
    }
}
