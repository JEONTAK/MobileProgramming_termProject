package com.example.mobileprogramming_termproject;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.mobileprogramming_termproject.menu.priceFragment;
import com.example.mobileprogramming_termproject.menu.tagFragment;
import  com.example.mobileprogramming_termproject.ui.alarm.alarmFragment;
import com.example.mobileprogramming_termproject.ui.home.HomeFragment;
import com.example.mobileprogramming_termproject.ui.map.mapFragment;
import com.example.mobileprogramming_termproject.ui.myPage.myPageFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private HomeFragment homeFragment = new HomeFragment();
    private mapFragment MapFragment = new mapFragment();
    private myPageFragment MyPageFragment = new myPageFragment();
    private alarmFragment AlarmFragment=new alarmFragment();
    Fragment PriceFragment;
    Fragment TagFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.nav_host_fragment, homeFragment).commitAllowingStateLoss();

        PriceFragment=new priceFragment();
        TagFragment=new tagFragment();
//        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
//
//        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                int position = tab.getPosition();
//
//                Fragment selected = null;
//                if(position == 0){
//
//                    selected = PriceFragment;
//
//                }else if (position == 1){
//
//                    selected = TagFragment;
//
//                }
//
//                getSupportFragmentManager().beginTransaction().replace(R.id.frame, selected).commit();
//            }

//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });

//        FragmentTransaction transaction2 = fragmentManager.beginTransaction();
//        transaction2.add(R.id.nav_host_fragment,homeFragment.newInstane()).commit();

        BottomNavigationView bottomNavigationView  = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        bottomNavigationView .setOnNavigationItemSelectedListener(new ItemSelectedListener());



    }
    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch(menuItem.getItemId())
            {
                case R.id.navigation_home:
                    transaction.replace(R.id.nav_host_fragment, homeFragment).commitAllowingStateLoss();
                    break;
                case R.id.navigation_alarm:
                    transaction.replace(R.id.nav_host_fragment,AlarmFragment).commitAllowingStateLoss();
                    break;

                case R.id.navigation_mypage:
                    transaction.replace(R.id.nav_host_fragment, MyPageFragment).commitAllowingStateLoss();
                    break;
                case R.id.navigation_map:
                    transaction.replace(R.id.nav_host_fragment, MapFragment).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }
    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment,fragment);
        fragmentTransaction.commit();
    }

}