package com.example.mobileprogramming_termproject.menu.cost;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.mobileprogramming_termproject.R;


public class between5000_10000Fragment extends Fragment {
    View view;
    public between5000_10000Fragment(){

    }
    public static  between5000_10000Fragment newInstance(){
        between5000_10000Fragment bet5_10 =new between5000_10000Fragment();
        return bet5_10;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_cost_between5000_10000,container,false);
        return view;
    }
}
