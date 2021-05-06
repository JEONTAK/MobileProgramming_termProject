package com.example.mobileprogramming_termproject.menu;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.example.mobileprogramming_termproject.R;


public class tagFragment extends Fragment {
    public static tagFragment newInstance() {
        return new tagFragment();
    }



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_category_tag, container, false);


        return root;
    }
}
