package com.example.mobileprogramming_termproject.menu.food;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.mobileprogramming_termproject.R;


public class drinkFragment extends Fragment {
    View view;
    public drinkFragment(){

    }
    public static drinkFragment newInstance(){
        drinkFragment DrinkFragment=new drinkFragment();
        return DrinkFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_food_drink,container,false);
        return view;
    }
}
