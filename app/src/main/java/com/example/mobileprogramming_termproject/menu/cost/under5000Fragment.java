package com.example.mobileprogramming_termproject.menu.cost;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.mobileprogramming_termproject.R;


public class under5000Fragment extends Fragment {
    View view;
    public under5000Fragment(){

    }
    public static  under5000Fragment newInstance(){
        under5000Fragment under5 =new under5000Fragment();
        return under5;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_cost_under5000,container,false);
        return view;
    }
}
