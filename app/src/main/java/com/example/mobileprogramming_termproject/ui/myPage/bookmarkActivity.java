package com.example.mobileprogramming_termproject.ui.myPage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileprogramming_termproject.Member.MemberInfo;
import com.example.mobileprogramming_termproject.R;
import com.example.mobileprogramming_termproject.adapter.myrecipeAdapter;
import com.example.mobileprogramming_termproject.writingContent.RecipePostInfo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

public class bookmarkActivity extends AppCompatActivity {
    //파이어베이스 스토어 선언
    private FirebaseFirestore firebaseFirestore;
    private ArrayList<String> bookmarkRecipe;
    private ArrayList<RecipePostInfo> recipe_postList;
    //북마크 글을 카드뷰로 띄워주기 위한 리사이클러 뷰 선언
    private RecyclerView recipeRecyclerView;
    //카드뷰를 행마다 1개씩 나오게 하기위함.
    final int numberOfColumns = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        //파이어베이스에서 데이터베이스 가져옴
        firebaseFirestore= FirebaseFirestore.getInstance();

        //리사이클러뷰 작성
        recipeRecyclerView = findViewById(R.id.myPage_RecipePost_List);
        recipeRecyclerView.setHasFixedSize(true);
        recipeRecyclerView.setLayoutManager(new GridLayoutManager(bookmarkActivity.this,numberOfColumns));
    }

    //레시피게시판에 내용이 추가가 될 경우 바로바로 업데이트 해주기 위해 resume함수에 넣어 관리.
    @Override
    protected void onResume(){
        super.onResume();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.v("유저북마크",user.getUid());

        DocumentReference docRef = firebaseFirestore.collection("users").document(user.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                MemberInfo memberInfo = documentSnapshot.toObject(MemberInfo.class);
                bookmarkRecipe = memberInfo.getBookmarkRecipe();
                Log.v("북마크레시피", "북마크는" + bookmarkRecipe.get(0));
                int count = 0;
                getBookmark(bookmarkRecipe);
            }
        });

    }

    //activity를 실행하기 위한 함수.
    private void myStartActivity(Class c){
        Intent intent=new Intent( this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    //bookmark한 내용을 recipe_post에 저장하는 함수

    public void getBookmark(ArrayList<String> bookmarkRecipe){
        ArrayList<RecipePostInfo> recipe_postList=new ArrayList<RecipePostInfo>();
        for(String a: bookmarkRecipe){
                    DocumentReference docRef2 = firebaseFirestore.collection("recipePost").document(a);
                    docRef2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Log.v("뭔데용", documentSnapshot.getData().get("titleImage").toString());
                            recipe_postList.add(new RecipePostInfo(
                                    documentSnapshot.getData().get("titleImage").toString(),
                                    documentSnapshot.getData().get("title").toString(),
                                    documentSnapshot.getData().get("ingredient").toString(),
                                    (ArrayList<String>) documentSnapshot.getData().get("content"),
                                    documentSnapshot.getData().get("publisher").toString(),
                                    documentSnapshot.getData().get("userName").toString(),
                                    new Date(documentSnapshot.getDate("createdAt").getTime()),
                                    (Long) documentSnapshot.getData().get("recom"),
                                    documentSnapshot.getData().get("recipeId").toString(),
                                    (ArrayList<String>) documentSnapshot.getData().get("recomUserId"),
                                    (Long) documentSnapshot.getData().get("price"),
                                    documentSnapshot.getData().get("foodCategory").toString(),
                                    documentSnapshot.getData().get("tagCategory").toString()
                            ));
                            if(a.equals(bookmarkRecipe.get(bookmarkRecipe.size()-1))){
                                adaptBookmark(recipe_postList);
                            }
                        }
                    });
          }
    }

    //adapter에 붙이는 함수
    public void adaptBookmark(ArrayList<RecipePostInfo> recipe_postList ){
        //recipeAdapter를 이용하여 리사이클러 뷰로 내용 띄움.
        RecyclerView.Adapter mAdapter = new myrecipeAdapter(bookmarkActivity.this, recipe_postList);
        recipeRecyclerView.setAdapter(mAdapter);
    }
}