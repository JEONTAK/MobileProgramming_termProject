package com.example.mobileprogramming_termproject.menu.food;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.mobileprogramming_termproject.R;
import com.example.mobileprogramming_termproject.adapter.FragmentAdapter;
import com.google.android.material.tabs.TabLayout;

public class category_food_activity extends AppCompatActivity {
    private ViewPager vp;
//    private TabLayout tabLayout;
    FragmentAdapter adapter=new FragmentAdapter(getSupportFragmentManager(),1);
//    private ArrayList<String> tabNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_food);

        vp=(ViewPager)findViewById(R.id.food_container);
        setupViewPager(vp);

        TabLayout tab=findViewById(R.id.tab_food);
        tab.setupWithViewPager(vp);

    }

    private void setupViewPager(ViewPager vp) {
        adapter.addFragment(new mealFragment(), "식사");
        adapter.addFragment(new dessertFragment(), "디저트");
        adapter.addFragment(new drinkFragment(), "음료");
        adapter.addFragment(new otherFoodFragment(), "기타");
        vp.setAdapter(adapter);
    }

}
