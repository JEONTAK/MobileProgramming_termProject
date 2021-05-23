package com.example.mobileprogramming_termproject.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.mobileprogramming_termproject.MainActivity;
import com.example.mobileprogramming_termproject.R;
import com.example.mobileprogramming_termproject.adapter.freeAdapter;
import com.example.mobileprogramming_termproject.community.freeCommunityActivity;
import com.example.mobileprogramming_termproject.adapter.recipeAdapter;
import com.example.mobileprogramming_termproject.community.recipeCommunityActivity;
import com.example.mobileprogramming_termproject.menu.cost.category_cost_activity;
import com.example.mobileprogramming_termproject.menu.food.category_food_activity;
import com.example.mobileprogramming_termproject.menu.priceFragment;
import com.example.mobileprogramming_termproject.menu.tag.category_tag_activity;
import com.example.mobileprogramming_termproject.menu.tagFragment;
import com.example.mobileprogramming_termproject.ui.myPage.bookmarkActivity;
import com.example.mobileprogramming_termproject.ui.searchResult.searchResultFragment;
import com.example.mobileprogramming_termproject.writingContent.FreePostInfo;
import com.example.mobileprogramming_termproject.writingContent.RecipePostInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

import me.relex.circleindicator.CircleIndicator;

public class HomeFragment extends Fragment   {

    MainActivity activity;
    ImageSliderAdapter image_adapter;
    ViewPager viewpager;
    CircleIndicator indicator;
    Context context;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        activity=(MainActivity)getActivity();

    }
    @Override
    public void onDetach(){
        super.onDetach();
        activity=null;
    }
//    private com.example.mobileprogramming_termproject.menu.foodFragment FoodFragment=new foodFragment();
     private com.example.mobileprogramming_termproject.menu.tagFragment TagFragment=new tagFragment();
     private com.example.mobileprogramming_termproject.menu.priceFragment PriceFragment=new priceFragment();
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView HotPost;
    private RecyclerView FreePost;
    private SearchView mSearchView;
    private searchResultFragment SearchResultFragment=new searchResultFragment();



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
        //context=getContext();
        homeViewModel =
                new ViewModelProvider(this).get(com.example.mobileprogramming_termproject.ui.home.HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        root.findViewById(R.id.buttonRecipe).setOnClickListener(onClickListener);
        root.findViewById(R.id.buttonFree).setOnClickListener(onClickListener);
        root.findViewById(R.id.imageViewFood).setOnClickListener(onClickListener);
        root.findViewById(R.id.imageViewTag).setOnClickListener(onClickListener);
        root.findViewById(R.id.imageViewCost).setOnClickListener(onClickListener);
        root.findViewById(R.id.bookMarkBtn_home).setOnClickListener(onClickListener);
        mSearchView=root.findViewById(R.id.searchView);

        context=getContext();
        viewpager=root.findViewById(R.id.ad_viewPager);
        image_adapter=new ImageSliderAdapter(context);
        viewpager.setAdapter(image_adapter);
        indicator=root.findViewById(R.id.indicator);
        indicator.setViewPager(viewpager);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                activity.onFragmentChange(1,query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });




        firebaseFirestore= FirebaseFirestore.getInstance();//데이터베이스 선언



        HotPost = (RecyclerView)root.findViewById(R.id.hot_Post);
        HotPost.setHasFixedSize(true);
        HotPost.setLayoutManager(new GridLayoutManager(getActivity(),numberOfColumns));


        FreePost = (RecyclerView)root.findViewById(R.id.free_Post);
        FreePost.setHasFixedSize(true);
        FreePost.setLayoutManager(new GridLayoutManager(getActivity(),numberOfColumns));



        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        //인기 레시피에 리사이클러 뷰를 이용하여 레시피를 추천순으로 6개만 보여지게 함.
        CollectionReference recipeReference = firebaseFirestore.collection("recipePost");
        recipeReference
                .orderBy("recom", Query.Direction.DESCENDING).limit(6)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            ArrayList<RecipePostInfo> hot_postList = new ArrayList<>();
                            for(QueryDocumentSnapshot document : task.getResult()){
                                Log.d("로그: ", document.getId() + " => " + document.getData());
                                hot_postList.add(new RecipePostInfo(
                                        document.getData().get("titleImage").toString(),
                                        document.getData().get("title").toString(),
                                        document.getData().get("ingredient").toString(),
                                        (ArrayList<String>) document.getData().get("content"),
                                        document.getData().get("publisher").toString(),
                                        document.getData().get("userName").toString(),
                                        new Date(document.getDate("createdAt").getTime()),
                                        (Long) document.getData().get("recom"),
                                        document.getData().get("recipeId").toString(),
                                        (ArrayList<String>) document.getData().get("recomUserId"),
                                        (Long) document.getData().get("price"),
                                        document.getData().get("foodCategory").toString(),
                                        document.getData().get("tagCategory").toString()
                                ));
                            }

                            //리사이클러 뷰를 사용하여 카드뷰로 추가
                            RecyclerView.Adapter mAdapter1 = new recipeAdapter(getActivity(), hot_postList);
                            HotPost.setAdapter(mAdapter1);
                        }
                        else{
                            Log.d("로그: ", "Error getting documents: " , task.getException());

                        }
                    }
                });

        //자유 게시판에 리사이클러 뷰를 이용하여 게시글을 작성일자 최신순으로 8개만 출력.
        CollectionReference freePostReference = firebaseFirestore.collection("freePost");
        freePostReference
                .orderBy("createdAt", Query.Direction.DESCENDING).limit(8)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            ArrayList<FreePostInfo> free_postList = new ArrayList<>();
                            for(QueryDocumentSnapshot document : task.getResult()){
                                Log.d("로그: ", document.getId() + " => " + document.getData());
                                free_postList.add(new FreePostInfo(
                                        document.getData().get("title").toString(),
                                        document.getData().get("content").toString(),
                                        document.getData().get("publisher").toString(),
                                        document.getData().get("userName").toString(),
                                        new Date(document.getDate("createdAt").getTime()),
                                        (Long) document.getData().get("recom"),
                                        (ArrayList<String>) document.getData().get("comment"),
                                        document.getData().get("postId").toString(),
                                        (ArrayList<String>) document.getData().get("recomUserId")
                                ));
                            }

                            //리사이클러 뷰를 사용하여 카드뷰로 추가
                            RecyclerView.Adapter mAdapter2 = new freeAdapter(getActivity(), free_postList);
                            FreePost.setAdapter(mAdapter2);
                        }
                        else{
                            Log.d("로그: ", "Error getting documents: " , task.getException());

                        }
                    }
                });


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
                case R.id.bookMarkBtn_home:
                    myStartActivity(bookmarkActivity.class);
            }
        }
    };

    private void myStartActivity(Class c){
        Intent intent=new Intent(getActivity(), c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


}