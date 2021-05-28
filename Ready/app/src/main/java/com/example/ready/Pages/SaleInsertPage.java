package com.example.ready.Pages;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ready.Calendar.SaturdayDecorator;
import com.example.ready.Calendar.SundayDecorator;
import com.example.ready.Calendar.WeekDayDecorator;
import com.example.ready.DB.DBHelper;
import com.example.ready.DB.Model.Menu;
import com.example.ready.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class SaleInsertPage extends Fragment {
    private View v;
    private MaterialCalendarView materialCalendarView;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private TextView currentDateText;
    private TableLayout saleInsertTable;
    private ArrayList<Menu> menus = new ArrayList<>();
    private DBHelper dbHelper;
    private Button closeBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.sale_page, container, false);

        dbHelper = DBHelper.getInstance(v.getContext());

        materialCalendarView = v.findViewById(R.id.calendarView);
        slidingUpPanelLayout = v.findViewById(R.id.sliding_layout);
        saleInsertTable = v.findViewById(R.id.saleInsertTableLayout);

        calendarInit();
        saleInsertInit();

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
                saleDateInit(date);
            }
        });
    }

    private void saleInsertInit() {
        menus = dbHelper.getMenu();

        for(int i = 0; i < menus.size(); i++) {
            TableRow tableRow = new TableRow(v.getContext());
            tableRow.setBackgroundResource(R.drawable.menu_insert_shape);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
            ));

            // menu name
            TextView textView = new TextView(v.getContext());
            textView.setText(menus.get(i).menu_name);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(16);

            tableRow.addView(textView);

            // price
            EditText editText = new EditText(v.getContext());
            editText.setGravity(Gravity.CENTER);
            editText.setTextSize(16);

            tableRow.addView(editText);

            saleInsertTable.addView(tableRow);
        }

        closeBtn = v.findViewById(R.id.btn_close);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
    }

    private void saleDateInit(CalendarDay date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
        String dateString = simpleDateFormat.format(date.getDate());

        currentDateText = v.findViewById(R.id.currentDateText);
        currentDateText.setText(dateString);
    }

}
