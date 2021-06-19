package com.example.ready.Pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ready.R;

public class ToolbarMenuPage extends AppCompatActivity {
    final private MenuInsertPage menuInsertPage = new MenuInsertPage();
    final private SaleInsertPage saleInsertPage = new SaleInsertPage();
    final private FragmentManager fragmentManager = getSupportFragmentManager();
    final private FirstPage firstPage = new FirstPage();
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar_menu_page);
        Button closeBtn = findViewById(R.id.btn_close);
        Button homeBtn = findViewById(R.id.btn_home);
        LinearLayout menuBtn = findViewById(R.id.menu_btn_group);
        LinearLayout saleBtn = findViewById(R.id.sale_btn_group);
        final ImageView menuImage = findViewById(R.id.menu_btn_image);
        final ImageView saleImage = findViewById(R.id.sale_btn_image);
        final TextView menuText = findViewById(R.id.menu_btn_text);
        final TextView saleText = findViewById(R.id.sale_btn_text);

        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.insert_frame_layout, menuInsertPage).commitAllowingStateLoss();

        closeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.animation_hold,R.anim.animation_to_left);
            }
        });
        homeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.animation_hold,R.anim.animation_to_left);
            }
        });


        saleBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                saleImage.setColorFilter(Color.BLACK);
                saleText.setTextColor(Color.BLACK);
                menuImage.setColorFilter(Color.GRAY);
                menuText.setTextColor(Color.GRAY);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.insert_frame_layout, saleInsertPage);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        menuBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                menuImage.setColorFilter(Color.BLACK);
                menuText.setTextColor(Color.BLACK);
                saleImage.setColorFilter(Color.GRAY);
                saleText.setTextColor(Color.GRAY);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.insert_frame_layout, menuInsertPage);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
}