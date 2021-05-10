package com.example.mobileprogramming_termproject.menu.tag;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.mobileprogramming_termproject.R;
import com.example.mobileprogramming_termproject.adapter.FragmentAdapter;
import com.google.android.material.tabs.TabLayout;

public class category_tag_activity extends AppCompatActivity {
    private ViewPager vp;

    FragmentAdapter adapter=new FragmentAdapter(getSupportFragmentManager(),1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_tag);

        vp=(ViewPager)findViewById(R.id.tag_container);
        setupViewPager(vp);
        vp.setAdapter(adapter);

        TabLayout tab=findViewById(R.id.tab_tag);
        tab.setupWithViewPager(vp);

    }

    private void setupViewPager(ViewPager vp) {
        adapter.addFragment(new dietFragment(), "다이어트");
        adapter.addFragment(new bulkUpFragment(), "벌크업");
        adapter.addFragment(new sweetFragment(), "당충전");
        adapter.addFragment(new stressHotFragment(), "스트레스 HOT");
        vp.setAdapter(adapter);
    }
}
