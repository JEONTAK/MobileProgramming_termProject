package com.example.mobileprogramming_termproject.ui.alarm;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobileprogramming_termproject.MainActivity;
import com.example.mobileprogramming_termproject.R;



public class alarmFragment extends Fragment {

    private alarmViewModel alarmViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_alarm, container, false);

//추후에 물어보기
//        LinearLayout default_layout = (LinearLayout) root.findViewById(R.id.default_community);
//        LinearLayout addWriting_layout = (LinearLayout) root.findViewById(R.id.add_layout);
//        LinearLayout writing_layout = (LinearLayout) root.findViewById(R.id.writingPlace);
//        default_layout.setVisibility(View.VISIBLE);
//        writing_layout.setVisibility(View.GONE);
//        root.findViewById(R.id.addWritingBtn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                default_layout.setVisibility(View.GONE);
//                writing_layout.setVisibility(View.VISIBLE);
//                addWriting_layout.setVisibility(View.GONE);
//
//            }
//        });
//
//        root.findViewById(R.id.goBackBtn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                default_layout.setVisibility(View.VISIBLE);
//                writing_layout.setVisibility(View.GONE);
//                addWriting_layout.setVisibility(View.VISIBLE);
//            }
//        });


        // final TextView textView1 = root.findViewById(R.id.community);
        // communityViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
        //     @Override
        //     public void onChanged(@Nullable String s) {
        //         textView1.setText(s);
        //     }
        // });

        return root;
    }
}