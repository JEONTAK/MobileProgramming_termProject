package com.example.mobileprogramming_termproject.menu.cost;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.mobileprogramming_termproject.R;
import com.example.mobileprogramming_termproject.adapter.FragmentAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class category_cost_activity extends AppCompatActivity {
    private ViewPager vp;

    FragmentAdapter adapter=new FragmentAdapter(getSupportFragmentManager(),1);
     private ArrayList<String> tabNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_price);

       vp=(ViewPager)findViewById(R.id.price_container);
       setupViewPager(vp);

       TabLayout tab=findViewById(R.id.tab_cost);
       tab.setupWithViewPager(vp);

    }

    private void setupViewPager(ViewPager vp) {
        adapter.addFragment(new under5000Fragment(), "5000원 미만");
        adapter.addFragment(new between5000_10000Fragment(), "5000원 이상 10000원 미만");
        adapter.addFragment(new over10000Fragment(), "10000원 이상 ");
        vp.setAdapter(adapter);
    }

}
