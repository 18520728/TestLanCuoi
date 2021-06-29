package com.example.genmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    //-----------------------A. Khai báo biến -------------------------
    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;

    private ImageButton btnUser, btnSetting;
    private EditText edtSearch;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //-----------------------b. Ánh xạ view -------------------------
        bottomNavigationView = findViewById(R.id.BottomNav);
        viewPager = findViewById(R.id.ViewPager);

        btnUser = findViewById(R.id.btnUser);
        btnSetting = findViewById(R.id.btnSetting);
        edtSearch = findViewById(R.id.edtSearch);

        //-----------------------C. Code xử lý -------------------------


        //NAVIGATION BAR------------------------
        //Xử lý ViewPager
        setOnViewPager();

        //Xử lý BottomNavigationBar
        setOnNavBar();

        //Xử lý quay về đúng TheLoaiFragment
        backToExactlyFragment();



        //Đi đến Search Activity
        goToSearchActivity();
        //Đi đến User Setting Activity
        goToUserActivity();
        //Đi đến Setting Activity
        goToSettingActivity();
    }


    // CÁC HÀM XỬ LÝ -----------------------------------------------------



    private void setOnNavBar() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.menuTrangChu:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.menuTheLoai:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.menuXepHang:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.menuCaNhan:
                        viewPager.setCurrentItem(3);
                        break;
                }
                return true;
            }
        });
    }

    private void setOnViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagerAdapter);
        //Xử lí sự kiện vuốt ngang màn hình
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch(position)
                {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.menuTrangChu).setChecked(true);
                        break;

                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.menuTheLoai).setChecked(true);
                        break;

                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.menuXepHang).setChecked(true);
                        break;

                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.menuCaNhan).setChecked(true);
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }



    private void backToExactlyFragment() {
        intent = getIntent();
        int index = intent.getIntExtra("current_fragment", 0);
        viewPager.setCurrentItem(index);

    }

    private void goToSearchActivity() {
        edtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, Search.class);

                intent.putExtra("current_fragment",viewPager.getCurrentItem());
                startActivity(intent);
                //finish();

            }
        });

    }

    private void goToUserActivity() {
        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, UserSetting.class);
                intent.putExtra("current_fragment",viewPager.getCurrentItem());
                startActivity(intent);
                //finish();
            }
        });

    }
    private void goToSettingActivity() {
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, Setting.class);
                intent.putExtra("current_fragment",viewPager.getCurrentItem());
                startActivity(intent);
                //finish();
            }
        });

    }

}