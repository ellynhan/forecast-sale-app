package com.example.ready;
import android.content.Intent;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.ready.Pages.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity{
    final private FirstPage firstPage = new FirstPage();
    final private SecondPage secondPage = new SecondPage();
    final private ThirdPage thirdPage = new ThirdPage();
    final private ForthPage forthPage = new ForthPage();
    final private FragmentManager fragmentManager = getSupportFragmentManager();
    private Button toolbarBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbarBtn = findViewById(R.id.toolbarBtn);
        toolbarBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ToolbarMenuPage.class);
                startActivity(intent);
                overridePendingTransition(R.anim.animation_to_right,R.anim.animation_hold);
            }
        });

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
        bottomNavigationViewInit();
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
                    case R.id.setting_tab: {
                        transaction.replace(R.id.frame_layout, forthPage).commitAllowingStateLoss();
                        break;
                    }
                }
                return true;
            }
        });
    }

}