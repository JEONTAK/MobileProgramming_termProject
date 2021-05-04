package com.example.mobileprogramming_termproject.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileprogramming_termproject.R;
import com.example.mobileprogramming_termproject.community.freeCommunityActivity;
import com.example.mobileprogramming_termproject.community.recipeCommunityActivity;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeFragment extends Fragment   {

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView HotPost;
    private RecyclerView FreePost;
    final int numberOfColumns = 2;

    private final int priceFragment = 1;
    private final int tagFragment = 2;
    private final int foodFragment = 3;

    private com.example.mobileprogramming_termproject.ui.home.HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(com.example.mobileprogramming_termproject.ui.home.HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        ImageView but_food=(ImageView)root.findViewById(R.id.imageViewFood);

        root.findViewById(R.id.buttonRecipe).setOnClickListener(onClickListener);
        root.findViewById(R.id.buttonFree).setOnClickListener(onClickListener);


        HotPost = (RecyclerView)root.findViewById(R.id.hot_Post);
        HotPost.setHasFixedSize(true);
        HotPost.setLayoutManager(new GridLayoutManager(getActivity(),numberOfColumns));


        FreePost = (RecyclerView)root.findViewById(R.id.free_Post);
        FreePost.setHasFixedSize(true);
        FreePost.setLayoutManager(new GridLayoutManager(getActivity(),numberOfColumns));

        but_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment homeFragment=new HomeFragment();

            }
        });

        //        final TextView textView = root.findViewById(R.id.);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

//        View food_category_btn = root.findViewById(R.id.food_btn);
//         food_category_btn.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View v) {
//                Intent intent =new Intent(getActivity(),food_category.class);
//                startActivity(intent);
//             }
//         });


        return root;
    }



    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.buttonRecipe:
                    myStartActivity(recipeCommunityActivity.class);
                    break;
                case R.id.buttonFree:
                    myStartActivity(freeCommunityActivity.class);
                    break;
            }
        }
    };

    private void myStartActivity(Class c){
        Intent intent=new Intent(getActivity(), c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}