package com.example.mobileprogramming_termproject.menu.cost;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.mobileprogramming_termproject.R;
import com.example.mobileprogramming_termproject.adapter.recipeAdapter;
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


public class between5000_10000Fragment extends Fragment {
    //파이어베이스 선언
    private FirebaseFirestore firebaseFirestore;
    //레시피 글을 카드뷰로 띄워주기 위한 리사이클러 뷰 선언
    private RecyclerView price5to10Recipe;
    //카드뷰를 행마다 2개씩 나오게 하기위함.
    final int numberOfColumns = 2;

    View view;
    public between5000_10000Fragment(){

    }
    public static  between5000_10000Fragment newInstance(){
        between5000_10000Fragment bet5_10 =new between5000_10000Fragment();
        return bet5_10;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_cost_between5000_10000,container,false);

        firebaseFirestore= FirebaseFirestore.getInstance();

        //리사이클러뷰 작성
        price5to10Recipe = view.findViewById(R.id.price_5to10_recipe);
        price5to10Recipe.setHasFixedSize(true);
        price5to10Recipe.setLayoutManager(new GridLayoutManager(getActivity(),numberOfColumns));

        return view;
    }


    //5000원 이상, 10000원 미만 카테고리에 내용이 추가가 될 경우 바로바로 업데이트 해주기 위해 resume함수에 넣어 관리.
    @Override
    public void onResume(){
        super.onResume();

        //recipePost에 있는 data를 가져오기 위함.
        CollectionReference collectionReference = firebaseFirestore.collection("recipePost");
        collectionReference
                //추천 높은 순으로 정렬
                .orderBy("price", Query.Direction.DESCENDING)
                //레시피 중 5000원 이상, 10000원 미만 레시피만 가져오기 위함.
                .whereLessThan("price",10000)
                .whereGreaterThanOrEqualTo("price",5000)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //각 게시글의 정보를 가져와 arrayList에 저장.
                            ArrayList<RecipePostInfo> recipe_postList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("로그: ", document.getId() + " => " + document.getData());
                                recipe_postList.add(new RecipePostInfo(
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

                            //recipeAdapter를 이용하여 리사이클러 뷰로 내용 띄움.
                            RecyclerView.Adapter mAdapter = new recipeAdapter(getActivity(), recipe_postList);
                            price5to10Recipe.setAdapter(mAdapter);
                        } else {
                            Log.d("로그: ", "Error getting documents: ", task.getException());

                        }
                    }
                });
    }

}
