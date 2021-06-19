package com.example.ready.Pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ready.Pages.List.RecyclerViewAdapter;
import com.example.ready.Pages.List.RecyclerViewItem;
import com.example.ready.R;

import java.util.ArrayList;

public class FirstPage extends Fragment {
    public static FirstPage newInstance() { return new FirstPage(); }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first_page, container, false);
        RecyclerView recyclerViewToday = view.findViewById(R.id.recyclerView_today);
        RecyclerView recyclerViewTomorrow = view.findViewById(R.id.recyclerView_tomorrow);
        RecyclerViewAdapter adapterToday = new RecyclerViewAdapter();
        RecyclerViewAdapter adapterTomorrow = new RecyclerViewAdapter();

        ArrayList<RecyclerViewItem> foreCastTodayItems = new ArrayList<>();
        ArrayList<RecyclerViewItem> foreCastTomorrowItems = new ArrayList<>();
        ArrayList<String> menuNames = new ArrayList<>(); //이름 DB에서 불러와서 추가하는 거 구현 필요
        for(int i=0; i<5; i++){
            foreCastTodayItems.add(new RecyclerViewItem("꽈배기",i+1));
        }
        for(int i=0; i<5; i++){
            foreCastTomorrowItems.add(new RecyclerViewItem("꽈배기",i+1));
        }

        adapterToday.setItemList(foreCastTodayItems);
        adapterTomorrow.setItemList(foreCastTomorrowItems);
        recyclerViewToday.setAdapter(adapterToday);
        recyclerViewToday.setLayoutManager(new LinearLayoutManager(this.getContext(),RecyclerView.HORIZONTAL,false));
        recyclerViewTomorrow.setAdapter(adapterTomorrow);
        recyclerViewTomorrow.setLayoutManager(new LinearLayoutManager(this.getContext(),RecyclerView.HORIZONTAL,false));
        return view;
    }
}
