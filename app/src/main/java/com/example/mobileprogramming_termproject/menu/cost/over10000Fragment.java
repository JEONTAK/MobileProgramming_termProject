package com.example.mobileprogramming_termproject.menu.cost;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.mobileprogramming_termproject.R;


public class over10000Fragment extends Fragment {
    View view;
    public over10000Fragment(){

    }
    public static  over10000Fragment newInstance(){
        over10000Fragment over10 =new over10000Fragment();
        return over10;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_cost_over_10000,container,false);
        return view;
    }
}

