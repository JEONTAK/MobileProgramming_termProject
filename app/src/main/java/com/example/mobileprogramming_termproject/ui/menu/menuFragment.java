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
        Button button1=(Button)root.findViewById(R.id.button1);
        Button button2=(Button)root.findViewById(R.id.button2);
        Button button3=(Button)root.findViewById(R.id.button3);
        ImageButton imageButton1=(ImageButton)root.findViewById(R.id.star1);
        ImageButton imageButton2=(ImageButton)root.findViewById(R.id.star2);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food_layout.setVisibility(v.VISIBLE);
                cost_layout.setVisibility(v.GONE);
                purpose_layout.setVisibility(v.GONE);
            }
        });

        scrollView1.setVisibility(View.INVISIBLE);
        scrollView2.setVisibility(View.INVISIBLE);
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView1.setVisibility(v.VISIBLE);
                scrollView2.setVisibility(v.GONE);
            }
        });
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView1.setVisibility(v.GONE);
                scrollView2.setVisibility(v.VISIBLE);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food_layout.setVisibility(v.GONE);
                cost_layout.setVisibility(v.VISIBLE);
                purpose_layout.setVisibility(v.GONE);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food_layout.setVisibility(v.GONE);
                cost_layout.setVisibility(v.GONE);
                purpose_layout.setVisibility(v.VISIBLE);
            }
        });







        return root;
    }




}