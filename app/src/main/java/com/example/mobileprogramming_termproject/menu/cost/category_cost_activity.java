package com.example.mobileprogramming_termproject.menu.cost;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.mobileprogramming_termproject.R;
import com.example.mobileprogramming_termproject.menu.food.FragmentAdapter;
import com.example.mobileprogramming_termproject.menu.food.dessertFragment;
import com.example.mobileprogramming_termproject.menu.food.drinkFragment;
import com.example.mobileprogramming_termproject.menu.food.riceFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class category_cost_activity extends AppCompatActivity {
    private ViewPager vp;
         private TabLayout tabLayout;
    FragmentAdapter adapter=new FragmentAdapter(getSupportFragmentManager(),1);
     private ArrayList<String> tabNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_price);

       vp=(ViewPager)findViewById(R.id.container);
       setupViewPager(vp);



        TabLayout tab=findViewById(R.id.tab_cost);
        tab.setupWithViewPager(vp);

    }

    private void setupViewPager(ViewPager vp) {
        adapter.addFragment(new riceFragment(), "5000원 이하");
        adapter.addFragment(new dessertFragment(), "5000원~10000원");
        adapter.addFragment(new drinkFragment(), "10000원이상 ");
        vp.setAdapter(adapter);
    }

}
