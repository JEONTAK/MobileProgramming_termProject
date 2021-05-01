package com.example.mobileprogramming_termproject;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.mobileprogramming_termproject.ui.community.communityFragment;
import com.example.mobileprogramming_termproject.ui.home.HomeFragment;
import com.example.mobileprogramming_termproject.ui.map.mapFragment;
import com.example.mobileprogramming_termproject.ui.menu.menuFragment;
import com.example.mobileprogramming_termproject.ui.myPage.myPageFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private HomeFragment homeFragment = new HomeFragment();
    private mapFragment MapFragment = new mapFragment();
    private menuFragment MenuFragment = new menuFragment();
    private myPageFragment MyPageFragment = new myPageFragment();
    private communityFragment CommunityFragment = new communityFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.nav_host_fragment, homeFragment).commitAllowingStateLoss();

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
                case R.id.navigation_menu:
                    transaction.replace(R.id.nav_host_fragment, MenuFragment).commitAllowingStateLoss();
                    break;
                case R.id.navigation_community:
                    transaction.replace(R.id.nav_host_fragment, CommunityFragment).commitAllowingStateLoss();
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
}