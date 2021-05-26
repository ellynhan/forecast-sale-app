package com.example.ready;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.ready.DB.DBHelper;
import com.example.ready.DB.DateTime;
import com.example.ready.DB.Weather;
import com.example.ready.Pages.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import java.util.concurrent.Executors;
import org.json.JSONException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    final private FirstPage firstPage = new FirstPage();
    final private SecondPage secondPage = new SecondPage();
    final private ThirdPage thirdPage= new ThirdPage();

    final private MenuInsertPage menuInsertPage = new MenuInsertPage();
    final private SaleInsertPage saleInsertPage = new SaleInsertPage();

    final private FragmentManager fragmentManager = getSupportFragmentManager();

    final private Weather weather = new Weather();
    final private DateTime dateTime = new DateTime();
    private NavigationView navigationView;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;

//    private SQLiteDatabase db;
//    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        db = dbHelper.getReadableDatabase();

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

/* 날씨 API 요청하는 코드, 인증키 있어야 작동함. 작동원하면 재원한테 연락하시오!
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("NEW THREAD IS STARTED");
                try {
                    String str = weather.func("20210526","1100","98","77");
                    System.out.println("returns: "+str);
                } catch (IOException | JSONException e) {
                    System.out.println("ERROR IS CAUGHT");
                    e.printStackTrace();
                }
            }
        });
*/
        this.initializeLayout();
    }

    private void initializeLayout() {
        if (navigationView != null)  navigationView.setNavigationItemSelectedListener(this);
        bottomNavigationViewInit();
        toolbarInit();
    }

    public void bottomNavigationViewInit() {
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

    private void toolbarInit() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_dehaze_24, null);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, Color.WHITE);

        getSupportActionBar().setHomeAsUpIndicator(drawable);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Weather weather = new Weather();
        switch(item.getItemId()) {
            case R.id.item_menu: {
                transaction.replace(R.id.frame_layout, menuInsertPage).commitAllowingStateLoss();
                break;
            }

            case R.id.item_sale: {
                transaction.replace(R.id.frame_layout, saleInsertPage).commitAllowingStateLoss();
                break;
            }
        }
        drawerLayout.closeDrawers();

        return true;
    }
}