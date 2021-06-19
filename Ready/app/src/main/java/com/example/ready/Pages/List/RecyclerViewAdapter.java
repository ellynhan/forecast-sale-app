package com.example.ready.Pages.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ready.R;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private ArrayList<RecyclerViewItem> recyclerViewItemList;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_item, viewGroup, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.onBind(recyclerViewItemList.get(position));
    }
    @Override
    public int getItemCount() {
        return recyclerViewItemList.size();
    }

    public void setItemList(ArrayList<RecyclerViewItem> list){
        this.recyclerViewItemList = list;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        Button btn;
        public ViewHolder(View view){
            super(view);
            btn = view.findViewById(R.id.Recycler_btn);
        }
        void onBind(RecyclerViewItem item){
            btn.setText(item.getMenuName()+" "+item.getQuantity()+"개");
        }
    }
}
