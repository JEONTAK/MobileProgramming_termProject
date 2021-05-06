package com.example.mobileprogramming_termproject.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileprogramming_termproject.R;
import com.example.mobileprogramming_termproject.community.freeCommunityActivity;
import com.example.mobileprogramming_termproject.community.recipeCommunityActivity;
import com.example.mobileprogramming_termproject.menu.cost.category_cost_activity;
import com.example.mobileprogramming_termproject.menu.food.category_food_activity;
import com.example.mobileprogramming_termproject.menu.priceFragment;
import com.example.mobileprogramming_termproject.menu.tag.category_tag_activity;
import com.example.mobileprogramming_termproject.menu.tagFragment;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeFragment extends Fragment   {
//    private com.example.mobileprogramming_termproject.menu.foodFragment FoodFragment=new foodFragment();
     private com.example.mobileprogramming_termproject.menu.tagFragment TagFragment=new tagFragment();
     private com.example.mobileprogramming_termproject.menu.priceFragment PriceFragment=new priceFragment();
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView HotPost;
    private RecyclerView FreePost;
    final int numberOfColumns = 2;

    private final int priceFragment = 1;
    private final int tagFragment = 2;
    private final int foodFragment = 3;

    private com.example.mobileprogramming_termproject.ui.home.HomeViewModel homeViewModel;
    public static HomeFragment newInstane(){
        return new HomeFragment();
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(com.example.mobileprogramming_termproject.ui.home.HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);



        root.findViewById(R.id.buttonRecipe).setOnClickListener(onClickListener);
        root.findViewById(R.id.buttonFree).setOnClickListener(onClickListener);
        root.findViewById(R.id.imageViewFood).setOnClickListener(onClickListener);
        root.findViewById(R.id.imageViewTag).setOnClickListener(onClickListener);
        root.findViewById(R.id.imageViewCost).setOnClickListener(onClickListener);


//        ImageView tag_but=(ImageView)root.findViewById(R.id.imageViewTag);
//        ImageView food_but=(ImageView)root.findViewById(R.id.imageViewFood);
//        ImageView cost_but=(ImageView)root.findViewById(R.id.imageViewCost);

//        tag_but.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((MainActivity)getActivity()).replaceFragment(TagFragment.newInstance());
//
//
//            }
//        });


        HotPost = (RecyclerView)root.findViewById(R.id.hot_Post);
        HotPost.setHasFixedSize(true);
        HotPost.setLayoutManager(new GridLayoutManager(getActivity(),numberOfColumns));


        FreePost = (RecyclerView)root.findViewById(R.id.free_Post);
        FreePost.setHasFixedSize(true);
        FreePost.setLayoutManager(new GridLayoutManager(getActivity(),numberOfColumns));



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
                case R.id.imageViewFood:
                    myStartActivity(category_food_activity.class);
                    break;
                case R.id.imageViewCost:
                    myStartActivity(category_cost_activity.class);
                    break;
                case R.id.imageViewTag:
                    myStartActivity(category_tag_activity.class);
                    break;
//                case R.id.imageViewTag:
//                    ((MainActivity)getActivity()).replaceFragment(TagFragment.newInstance());
//                    break;
//                case R.id.imageViewCost:
//                    ((MainActivity)getActivity()).replaceFragment(PriceFragment.newInstance());
//                    break;

            }
        }
    };

    private void myStartActivity(Class c){
        Intent intent=new Intent(getActivity(), c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


}