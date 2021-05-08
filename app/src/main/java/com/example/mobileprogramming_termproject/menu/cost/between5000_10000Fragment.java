package com.example.mobileprogramming_termproject.menu.cost;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;


import com.example.mobileprogramming_termproject.R;


public class between5000_10000Fragment extends Fragment {
    private static final String TAG = between5000_10000Fragment.class.getSimpleName();

    CardView mCardView;
    public static between5000_10000Fragment newInstance() {
        between5000_10000Fragment fragment = new between5000_10000Fragment();
        fragment.setRetainInstance(true);
        return fragment;
    }
    public between5000_10000Fragment(){

    }
//    public static  between5000_10000Fragment newInstance(){
//        between5000_10000Fragment bet5_10 =new between5000_10000Fragment();
//        return bet5_10;
//    }

    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         return inflater.inflate(R.layout.fragment_cost_between5000_10000,container,false);

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCardView = (CardView) view.findViewById(R.id.cardview1);

    }
}
