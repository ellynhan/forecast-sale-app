package com.example.ready.Calendar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import com.example.ready.DB.DBHelper;
import com.example.ready.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class SaleDecorator implements DayViewDecorator {
    private Drawable drawable;
    private ArrayList<String> dates;
    private DBHelper dbHelper;

    public SaleDecorator(Context context) {
        drawable = ContextCompat.getDrawable(context, R.drawable.ic_baseline_check_24);
        dbHelper = DBHelper.getInstance(context);
        dates = dbHelper.getSaleDate();
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
        String dateString = simpleDateFormat.format(day.getDate());

        return dates.contains(dateString);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(drawable);
    }
}
