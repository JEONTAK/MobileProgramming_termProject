package com.example.mobileprogramming_termproject.ui.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import  com.example.mobileprogramming_termproject.R;

public class menuFragment extends Fragment {

    private com.example.mobileprogramming_termproject.ui.menu.menuViewModel homeViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(com.example.mobileprogramming_termproject.ui.menu.menuViewModel.class);
        View root = inflater.inflate(R.layout.fragment_menu, container, false);
//        final TextView textView = root.findViewById(R.id.);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        LinearLayout food_layout=(LinearLayout)root.findViewById(R.id.food_layout);
        LinearLayout cost_layout=(LinearLayout)root.findViewById(R.id.cost_layout);
        LinearLayout purpose_layout=(LinearLayout)root.findViewById(R.id.purpose_layout );
        ScrollView scrollView1=(ScrollView)root.findViewById(R.id.scroll1);
        ScrollView scrollView2=(ScrollView)root.findViewById(R.id.scroll2);
//음식 비용 목적으로 버튼 3개 나누기
        ImageButton button_food=(ImageButton)root.findViewById(R.id.imageBut_food);
        ImageButton button_cost=(ImageButton)root.findViewById(R.id.imageBut_cost);
        ImageButton button_purpose=(ImageButton)root.findViewById(R.id.imageBut_purpose);
//음식 카테고리
        ImageButton imageBut_burger=(ImageButton)root.findViewById(R.id.imageBut_burger);
        ImageButton imageBut_noodles=(ImageButton)root.findViewById(R.id.imageBut_noodles);
        ImageButton imageBut_rice=(ImageButton)root.findViewById(R.id.imageBut_rice);
        ImageButton imageBut_etc1=(ImageButton)root.findViewById(R.id.imageBut_etc1);
// 비용 카테고리
        ImageButton imageBut_under2=(ImageButton)root.findViewById(R.id.imageBut_under2);
        ImageButton imageBut_bet2_3=(ImageButton)root.findViewById(R.id.imageBut_bet2_3);
        ImageButton imageBut_bet3_4=(ImageButton)root.findViewById(R.id.imageBut_bet3_4);
        ImageButton imageBut_bet4_5=(ImageButton)root.findViewById(R.id.imageBut_bet4_5);

// 목적 카테고리

        ImageButton imageBut_diet=(ImageButton)root.findViewById(R.id.imageBut_diet);
        ImageButton imageBut_candy=(ImageButton)root.findViewById(R.id.imageBut_candy);
        ImageButton imageBut_hot=(ImageButton)root.findViewById(R.id.imageBut_hot);
        ImageButton imageBut_etc2=(ImageButton)root.findViewById(R.id.imageBut_etc2);

//        음식 누르면 음식 카테고리 보이게 하기
        button_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food_layout.setVisibility(v.VISIBLE);
                cost_layout.setVisibility(v.GONE);
                purpose_layout.setVisibility(v.GONE);
            }

        });
        scrollView1.setVisibility(View.INVISIBLE);
        scrollView2.setVisibility(View.INVISIBLE);
        imageBut_burger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView1.setVisibility(v.VISIBLE);
                scrollView2.setVisibility(v.GONE);
            }
        });
        imageBut_noodles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView1.setVisibility(v.GONE);
                scrollView2.setVisibility(v.VISIBLE);
            }
        });

        imageBut_rice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView1.setVisibility(v.GONE);
                scrollView2.setVisibility(v.VISIBLE);
            }
        });

        imageBut_etc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView1.setVisibility(v.GONE);
                scrollView2.setVisibility(v.VISIBLE);
            }
        });

        //비용 누르면 비용 카테고리 보이게 하기

        button_cost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food_layout.setVisibility(v.GONE);
                cost_layout.setVisibility(v.VISIBLE);
                purpose_layout.setVisibility(v.GONE);
            }
        });

        scrollView1.setVisibility(View.INVISIBLE);
        scrollView2.setVisibility(View.INVISIBLE);
        imageBut_under2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView1.setVisibility(v.VISIBLE);
                scrollView2.setVisibility(v.GONE);
            }
        });
        imageBut_bet2_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView1.setVisibility(v.GONE);
                scrollView2.setVisibility(v.VISIBLE);
            }
        });

        imageBut_bet3_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView1.setVisibility(v.GONE);
                scrollView2.setVisibility(v.VISIBLE);
            }
        });

        imageBut_bet4_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView1.setVisibility(v.GONE);
                scrollView2.setVisibility(v.VISIBLE);
            }
        });

        //목적 누르면 목적 카테고리 보이게 하기

        button_purpose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food_layout.setVisibility(v.GONE);
                cost_layout.setVisibility(v.GONE);
                purpose_layout.setVisibility(v.VISIBLE);
            }
        });

        imageBut_diet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView1.setVisibility(v.VISIBLE);
                scrollView2.setVisibility(v.GONE);
            }
        });
        imageBut_candy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView1.setVisibility(v.GONE);
                scrollView2.setVisibility(v.VISIBLE);
            }
        });

        imageBut_hot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView1.setVisibility(v.GONE);
                scrollView2.setVisibility(v.VISIBLE);
            }
        });

        imageBut_etc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView1.setVisibility(v.GONE);
                scrollView2.setVisibility(v.VISIBLE);
            }
        });









        return root;
    }




}