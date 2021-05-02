package com.example.ready.Pages;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.ready.Calendar.EventDecorator;
import com.example.ready.Calendar.SaturdayDecorator;
import com.example.ready.Calendar.SundayDecorator;
import com.example.ready.Calendar.WeekDayDecorator;
import com.example.ready.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.Calendar;
import java.util.Collections;

public class SecondPage extends Fragment {
    private MaterialCalendarView materialCalendarView;
    private SlidingUpPanelLayout slidingUpPanelLayout;

    Calendar calendar = Calendar.getInstance();
    CalendarDay date = CalendarDay.from(calendar);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.second_menu, container, false);

        // calendar decorator
        materialCalendarView = v.findViewById(R.id.calendarView);
        materialCalendarView.setSelectedDate(CalendarDay.today());
        materialCalendarView.addDecorators(
                new SaturdayDecorator(),
                new SundayDecorator(),
                new WeekDayDecorator()
        );
        materialCalendarView.addDecorator(
                new EventDecorator(Color.RED, Collections.singleton(date))
        );

        // when date is clicked, show up slidingUpPanelLayout
        slidingUpPanelLayout = v.findViewById(R.id.sliding_layout);
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            }
        });


        return v;
    }
}
