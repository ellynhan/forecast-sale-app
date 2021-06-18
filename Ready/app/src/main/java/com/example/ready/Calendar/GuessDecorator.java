package com.example.ready.Calendar;

import android.graphics.Color;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.text.SimpleDateFormat;

public class GuessDecorator implements DayViewDecorator {
    private int qty;
    private String date;

    public GuessDecorator(String date, int qty) {
        this.date = date;
        this.qty = qty;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
        String dateString = simpleDateFormat.format(day.getDate());

        return date.equals(dateString);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new AddTextToDates(String.valueOf(qty), Color.BLACK));
    }
}
