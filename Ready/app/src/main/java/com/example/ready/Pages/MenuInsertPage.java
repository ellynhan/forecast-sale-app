package com.example.ready.Pages;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.ready.DB.DBHelper;
import com.example.ready.DB.Model.Menu;
import com.example.ready.R;

import java.util.ArrayList;

public class MenuInsertPage extends Fragment {
    private View v;
    private Button addBtn, editBtn;
    private TableLayout rootTable;
    private DBHelper dbHelper;
    private Boolean ISDELETE = false;

    private ArrayList<Button> deleteBtns = new ArrayList<>();
    private ArrayList<Integer> deleteIndex = new ArrayList<>();
    private ArrayList<TableRow> menuAddRow = new ArrayList<>();
    private ArrayList<EditText> menuEditText = new ArrayList<>();
    private ArrayList<Menu> menus = new ArrayList<>();

    private final int COLUMN = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.menu_page, container, false);

        rootTable = v.findViewById(R.id.tableLayout);
        dbHelper = DBHelper.getInstance(v.getContext());

        menus = dbHelper.getMenu();
        for(int i = 0; i < menus.size(); i++)
            deleteIndex.add(0);

        tableLayoutInit();
        buttonInit();

        return v;
    }

    private void tableLayoutInit() {
//        try {
//            System.out.println(menus.size());
//            rootTable.removeViews(1, menus.size());
//        }
//        catch(Exception e) {
//            e.printStackTrace();
//        }

        // insert data to table
        for(int i = 0; i < menus.size(); i++) {
            TableRow tableRow = new TableRow(v.getContext());
            tableRow.setBackgroundResource(R.drawable.menu_insert_shape);
            tableRow.setTag(menus.get(i)._id);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
            ));

            // menu name
            TextView textView = new TextView(v.getContext());
            textView.setText(menus.get(i).menu_name);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(20);

            tableRow.addView(textView);

            // price
            textView = new TextView(v.getContext());
            textView.setText(String.valueOf(menus.get(i).menu_price));
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(20);

            tableRow.addView(textView);

            rootTable.addView(tableRow);
        }
    }

    private void buttonInit() {
        editBtn = v.findViewById(R.id.btn_menu_edit);

        final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        final LinearLayout menuLayout = v.findViewById(R.id.menu_layout);
        final TableLayout deleteTable = v.findViewById(R.id.menu_delete_layout);
        final TableLayout.LayoutParams tlParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        );
        final TableLayout.LayoutParams trParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        );

        // button margin options
        tlParams.topMargin = Math.round(50 * displayMetrics.density);
        tlParams.leftMargin = Math.round(-40 * displayMetrics.density);
        trParams.bottomMargin = Math.round(14 * displayMetrics.density);

        // add delete button in view
        System.out.println("menus: " + menus.size());
        deleteBtns.clear();
        for (int i = 0; i < menus.size(); i++) {
            TableRow tableRow = new TableRow(v.getContext());
            deleteTable.setLayoutParams(tlParams);

            tableRow.setLayoutParams(trParams);

            final Button deleteBtnTmp = new Button(v.getContext());
            deleteBtnTmp.setBackgroundResource(R.drawable.ic_baseline_close_24);
            deleteBtnTmp.setId(i);

            tableRow.addView(deleteBtnTmp);
            deleteTable.addView(tableRow);
            deleteBtnTmp.setVisibility(Button.INVISIBLE);

            deleteBtns.add(deleteBtnTmp);
        }

        // add 'add' button in view
        final Button addBtnTmp = new Button(v.getContext());
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.CENTER_HORIZONTAL;
        addBtnTmp.setLayoutParams(params);
        addBtnTmp.setBackgroundResource(R.drawable.ic_baseline_add_24);

        addBtnTmp.setTag("addBtn");

        menuLayout.addView(addBtnTmp);
        addBtn = v.findViewWithTag("addBtn");
        addBtn.setVisibility(Button.INVISIBLE);

        editBtn.setOnClickListener(editOnClickListener);
        addBtn.setOnClickListener(addOnClickListener);

        for(int i = 0; i < deleteBtns.size(); i++)
            deleteBtns.get(i).setOnClickListener(deleteClickListener);
    }

    private View.OnClickListener editOnClickListener = new View.OnClickListener() {
        boolean isEdit = false;

        @Override
        public void onClick(View v) {
            if (!isEdit) {
                editBtn.setText("편집 저장");

                for (int i = 0; i < menus.size(); i++)
                    deleteBtns.get(i).setVisibility(Button.VISIBLE);
                addBtn.setVisibility(Button.VISIBLE);

                isEdit = true;
            }

            else {
                editBtn.setText("메뉴 편집");

                // button invisible
                for (int i = 0; i < menus.size(); i++)
                    deleteBtns.get(i).setVisibility(View.INVISIBLE);
                addBtn.setVisibility(View.INVISIBLE);

                // delete editText & db insert
                for (int i = 0, j = 0; i < menuAddRow.size(); i++, j += 2) {
                    menuAddRow.get(i).setVisibility(EditText.GONE);

                    try {
                        Menu menu = new Menu(
                                0,
                                menuEditText.get(j).getText().toString(),
                                Integer.parseInt(menuEditText.get(j + 1).getText().toString())
                        );
                        dbHelper.insertMenu(menu);
                        System.out.println("DB INSERT");
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                        System.out.println("DB INSERT FAIL");
                    }
                }

                //db delete
                if(ISDELETE) {
                    dbHelper.deleteMenu(deleteIndex);
                    System.out.println("DB DELETE");
                }

                menuAddRow.clear();
                menuEditText.clear();

                isEdit = false;
            }
        }
    };

    private View.OnClickListener addOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TableRow tableRow = new TableRow(v.getContext());
            tableRow.setBackgroundResource(R.drawable.menu_insert_shape);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
            ));

            for(int i = 0; i < COLUMN; i++) {
                EditText editText = new EditText(v.getContext());
                editText.setTextColor(Color.BLACK);
                editText.setGravity(Gravity.CENTER);
                editText.setTextSize(20);

                menuEditText.add(editText);
                tableRow.addView(editText);
            }

            menuAddRow.add(tableRow);
            rootTable.addView(tableRow);
        }
    };

    private View.OnClickListener deleteClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int id = v.getId();
            final TableRow row = rootTable.findViewWithTag(menus.get(id)._id);

            if(deleteIndex.get(id) == 0) {
                deleteIndex.set(id, menus.get(id)._id);
                row.setBackgroundResource(R.drawable.menu_delete_shape);
                ISDELETE = true;
            }
            else {
                deleteIndex.set(id, 0);
                row.setBackgroundResource(R.drawable.menu_insert_shape);
                ISDELETE = false;
            }
        }
    };
}
