package com.example.mobileprogramming_termproject.menu;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.example.mobileprogramming_termproject.R;


public class priceFragment extends Fragment {
    public static priceFragment newInstance() {
        return new priceFragment();
    }



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_category_price, container, false);


        return root;
    }
}
