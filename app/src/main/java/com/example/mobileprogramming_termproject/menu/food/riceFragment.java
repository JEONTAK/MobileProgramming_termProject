package com.example.mobileprogramming_termproject.menu.food;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mobileprogramming_termproject.R;


public class riceFragment extends Fragment {
    View view;
    public riceFragment(){

    }
    public static riceFragment newInstance(){
        riceFragment RiceFragment=new riceFragment();
        return RiceFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view=inflater.inflate(R.layout.fragment_food_meal,container,false);
         return view;
    }
}