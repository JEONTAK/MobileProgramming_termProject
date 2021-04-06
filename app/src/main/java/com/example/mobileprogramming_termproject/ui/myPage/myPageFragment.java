package com.example.mobileprogramming_termproject.ui.myPage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobileprogramming_termproject.R;

public class myPageFragment extends Fragment {

    private myPageViewModel myPageViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myPageViewModel =
                new ViewModelProvider(this).get(myPageViewModel.class);
        View root = inflater.inflate(R.layout.fragment_mypage, container, false);
//        final TextView textView = root.findViewById(R.id.text_notifications);
//        myPageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
         return root;
    }
}