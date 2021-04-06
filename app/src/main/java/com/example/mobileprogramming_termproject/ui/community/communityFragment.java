package com.example.mobileprogramming_termproject.ui.community;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobileprogramming_termproject.R;

public class communityFragment extends Fragment {

    private communityViewModel  communityViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        communityViewModel =
                new ViewModelProvider(this).get(communityViewModel.class);
        View root = inflater.inflate(R.layout.fragment_community, container, false);
//        final TextView textView1 = root.findViewById(R.id.community);
//        communityViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView1.setText(s);
//            }
//        });
        return root;
    }
}