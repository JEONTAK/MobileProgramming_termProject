package com.example.mobileprogramming_termproject.menu.food;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.mobileprogramming_termproject.R;


public class dessertFragment extends Fragment {
     View view;
    public dessertFragment(){

    }
    public static dessertFragment newInstance(){
        dessertFragment DessertFragment=new dessertFragment();
        return DessertFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_food_dessert,container,false);
        return view;
    }
}
