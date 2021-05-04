package com.example.mobileprogramming_termproject.menu;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.example.mobileprogramming_termproject.R;


public class foodFragment extends Fragment {




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_food_category, container, false);


        return root;
    }
}
