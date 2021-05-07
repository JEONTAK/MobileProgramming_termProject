package com.example.mobileprogramming_termproject.community;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileprogramming_termproject.R;
import com.example.mobileprogramming_termproject.writingContent.RecipePostInfo;
import com.example.mobileprogramming_termproject.writingContent.writingRecipePostActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class recipeCommunityActivity extends AppCompatActivity {
    //파이어베이스 선언
    private FirebaseFirestore firebaseFirestore;
    //레시피 글을 카드뷰로 띄워주기 위한 리사이클러 뷰 선언
    private RecyclerView recipeRecyclerView;
    //카드뷰를 행마다 2개씩 나오게 하기위함.
    final int numberOfColumns = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_bulletin);
        findViewById(R.id.recipePostBtn).setOnClickListener(onClickListener);

        //파이어베이스에서 데이터베이스 가져옴
        firebaseFirestore= FirebaseFirestore.getInstance();
        
        //리사이클러뷰 작성
        recipeRecyclerView = findViewById(R.id.post2);
        recipeRecyclerView.setHasFixedSize(true);
        recipeRecyclerView.setLayoutManager(new GridLayoutManager(recipeCommunityActivity.this,numberOfColumns));


    }

    //레시피게시판에 내용이 추가가 될 경우 바로바로 업데이트 해주기 위해 resume함수에 넣어 관리.
    @Override
    protected void onResume(){
        super.onResume();

        //recipePost에 있는 data를 가져오기 위함.
        CollectionReference collectionReference = firebaseFirestore.collection("recipePost");
        collectionReference
                //작성일자 내림차순을 정렬
                .orderBy("createdAt", Query.Direction.DESCENDING)
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
                            RecyclerView.Adapter mAdapter = new recipeAdapter(recipeCommunityActivity.this, recipe_postList);
                            recipeRecyclerView.setAdapter(mAdapter);
                        } else {
                            Log.d("로그: ", "Error getting documents: ", task.getException());

                        }
                    }
                });
    }

    //만약 게시글 작성 버튼을 누르면 작성 화면으로 넘어가게 하기 위한 리스너
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.recipePostBtn:
                    myStartActivity(writingRecipePostActivity.class);
                    break;
            }
        }
    };

    //activity를 실행하기 위한 함수.
    private void myStartActivity(Class c){
        Intent intent=new Intent( this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
