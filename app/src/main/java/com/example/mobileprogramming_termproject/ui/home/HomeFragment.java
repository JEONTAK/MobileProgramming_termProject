package com.example.mobileprogramming_termproject.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobileprogramming_termproject.R;

public class HomeFragment extends Fragment   {
    private final int priceFragment = 1;
    private final int tagFragment = 2;
    private final int foodFragment = 3;

    private com.example.mobileprogramming_termproject.ui.home.HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(com.example.mobileprogramming_termproject.ui.home.HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        ImageView but_food=(ImageView)root.findViewById(R.id.imageViewFood);

        but_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment homeFragment=new HomeFragment();

            }
        });




        //        final TextView textView = root.findViewById(R.id.);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

//        View food_category_btn = root.findViewById(R.id.food_btn);
//         food_category_btn.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View v) {
//                Intent intent =new Intent(getActivity(),food_category.class);
//                startActivity(intent);
//             }
//         });


        return root;
    }



}