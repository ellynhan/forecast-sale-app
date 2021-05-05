package com.example.ready;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.ready.Pages.FirstPage;
import com.example.ready.Pages.SecondPage;
import com.example.ready.Pages.ThirdPage;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager = getSupportFragmentManager();

    private FirstPage firstPage = new FirstPage();
    private SecondPage secondPage = new SecondPage();
    private ThirdPage thirdPage= new ThirdPage();
//    private MenuPage menuPage = new MenuPage();
//    private SalePage salePage = new SalePage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.initializeLayout();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, firstPage).commitAllowingStateLoss();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.dash_tab: {
                        transaction.replace(R.id.frame_layout, firstPage).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.forecast_tab: {
                        transaction.replace(R.id.frame_layout, secondPage).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.stats_tab: {
                        transaction.replace(R.id.frame_layout, thirdPage).commitAllowingStateLoss();
                        break;
                    }
                }

                return true;
            }
        });
    }

    private void initializeLayout() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_dehaze_24, null);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, Color.WHITE);

        getSupportActionBar().setHomeAsUpIndicator(drawable);


        final DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open,
                R.string.close
        );

        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        View headerView = navigationView.getHeaderView(0);
        Button closeBtn = headerView.findViewById(R.id.btn_close);
        Button homeBtn = headerView.findViewById(R.id.btn_home);
        final LinearLayout menuGroup = headerView.findViewById(R.id.menu_btn_group);
        final LinearLayout saleGroup = headerView.findViewById(R.id.sale_btn_group);
        final View menuPage =  headerView.findViewById(R.id.menu_page);
        final View salePage = headerView.findViewById(R.id.sale_page);
        final ImageView menuTabImage = headerView.findViewById(R.id.menu_btn_image);
        final ImageView saleTabImage = headerView.findViewById(R.id.sale_btn_image);
        final TextView menuTabText = headerView.findViewById(R.id.menu_btn_text);
        final TextView saleTabText = headerView.findViewById(R.id.sale_btn_text);


        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(Gravity.LEFT);
            }
        });
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(Gravity.LEFT);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frame_layout, firstPage).commitAllowingStateLoss();
            }
        });

        menuGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salePage.setVisibility(View.GONE);
                menuPage.setVisibility(View.VISIBLE);
                menuTabImage.setColorFilter(Color.BLACK);
                menuTabText.setTextColor(Color.BLACK);
                saleTabImage.setColorFilter(Color.parseColor("#767171"));
                saleTabText.setTextColor(Color.parseColor("#767171"));
            }
        });
        saleGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuPage.setVisibility(View.GONE);
                salePage.setVisibility(View.VISIBLE);
                saleTabImage.setColorFilter(Color.BLACK);
                saleTabText.setTextColor(Color.BLACK);
                menuTabImage.setColorFilter(Color.parseColor("#767171"));
                menuTabText.setTextColor(Color.parseColor("#767171"));
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }
}