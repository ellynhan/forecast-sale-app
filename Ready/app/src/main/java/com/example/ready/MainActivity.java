package com.example.ready;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

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
    final private FirstPage firstPage = new FirstPage();
    final private SecondPage secondPage = new SecondPage();
    final private ThirdPage thirdPage= new ThirdPage();
//    final private MenuPage menuPage = new MenuPage();
//    final private SalePage salePage = new SalePage();

    final private FragmentManager fragmentManager = getSupportFragmentManager();

    private NavigationView navigationView;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private View menuPage, salePage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        DBHelper dbHelper = DBHelper.getInstance(this);

        this.initializeLayout();
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

    private void drawerLayoutInit() {
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open,
                R.string.close
        );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        final View headerView = navigationView.getHeaderView(0);
        final Button closeBtn = headerView.findViewById(R.id.btn_close);
        final Button homeBtn = headerView.findViewById(R.id.btn_home);
        final LinearLayout menuGroup = headerView.findViewById(R.id.menu_btn_group);
        final LinearLayout saleGroup = headerView.findViewById(R.id.sale_btn_group);
        final ImageView menuTabImage = headerView.findViewById(R.id.menu_btn_image);
        final ImageView saleTabImage = headerView.findViewById(R.id.sale_btn_image);
        final TextView menuTabText = headerView.findViewById(R.id.menu_btn_text);
        final TextView saleTabText = headerView.findViewById(R.id.sale_btn_text);

        menuPage =  headerView.findViewById(R.id.menu_page);
        salePage = headerView.findViewById(R.id.sale_page);

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

    // table test
    private void tableLayoutInit() {
        final TableLayout tableLayout = menuPage.findViewById(R.id.tableLayout);
        final String[] menuName = new String[] { "도너츠", "꽈배기" };
        final String[] price = new String[] { "1000", "2000" };
        int count = 0;

        // insert data to table
        for(int i = 0; i < 2; i++, count++) {
            TableRow row = new TableRow(this);
            row.setBackgroundResource(R.drawable.menu_insert_shape);
            row.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
            ));

            // menu name
            TextView textView = new TextView(this);
            textView.setText(menuName[count]);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(20);

            row.addView(textView);

            // price
            textView = new TextView(this);
            textView.setText(price[count]);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(20);

            row.addView(textView);

            tableLayout.addView(row);
        }

        final Button editBtn = menuPage.findViewById(R.id.btn_menu_edit);

        editBtn.setOnClickListener(new View.OnClickListener() {
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            final LinearLayout menuLayout = menuPage.findViewById(R.id.menu_layout);
            final TableLayout tableLayout = menuPage.findViewById(R.id.menu_delete_layout);
            final TableLayout.LayoutParams tlParams = new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
            );
            final TableLayout.LayoutParams trParams = new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
            );

            @Override
            public void onClick(View v) {
                editBtn.setText("편집 저장 ");

                // mediaquery?
                tlParams.topMargin = Math.round(50 * displayMetrics.density);
                tlParams.leftMargin = Math.round(-40 * displayMetrics.density);
                trParams.bottomMargin = Math.round(10 * displayMetrics.density);

                for(int i = 0; i < 2; i++) {
                    tableLayout.setLayoutParams(tlParams);

                    TableRow tableRow = new TableRow(v.getContext());
                    tableRow.setLayoutParams(trParams);

                    final Button deleteBtn = new Button(v.getContext());
                    deleteBtn.setBackgroundResource(R.drawable.ic_baseline_close_24);

                    tableRow.addView(deleteBtn);
                    tableLayout.addView(tableRow);
                }

                final Button addBtn = new Button(v.getContext());
                final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.gravity = Gravity.CENTER_HORIZONTAL;
                addBtn.setLayoutParams(params);
                addBtn.setBackgroundResource(R.drawable.ic_baseline_add_24);

                menuLayout.addView(addBtn);
            }
        });
    }

    private void initializeLayout() {
        bottomNavigationViewInit();
        toolbarInit();
        drawerLayoutInit();
        tableLayoutInit();
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