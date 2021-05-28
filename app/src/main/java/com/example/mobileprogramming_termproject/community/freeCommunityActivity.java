package com.example.mobileprogramming_termproject.community;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileprogramming_termproject.R;
import com.example.mobileprogramming_termproject.adapter.freeAdapter;
import com.example.mobileprogramming_termproject.writingContent.FreePostInfo;
import com.example.mobileprogramming_termproject.writingContent.writingFreePostActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class freeCommunityActivity extends AppCompatActivity {
    private static final String TAG ="자유게시판 화면";
    //데이터베이스 선언
    private FirebaseFirestore firebaseFirestore;
    //리사이클러 뷰 선언
    private RecyclerView freeRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_community);
        findViewById(R.id.freePostBtn).setOnClickListener(onClickListener);
        //파이어베이스에서 데이터베이스를 가져옴.
        firebaseFirestore= FirebaseFirestore.getInstance();

        freeRecyclerView = findViewById(R.id.post1);
        freeRecyclerView.setHasFixedSize(true);
        freeRecyclerView.setLayoutManager(new LinearLayoutManager(freeCommunityActivity.this));
    }

    //자유게시판에 내용이 추가가 될 경우 바로바로 업데이트 해주기 위해 resume함수에 넣어 관리.
    @Override
    protected void onResume(){
        super.onResume();

        //freePost에 있는 data를 가져오기 위함.
        CollectionReference collectionReference = firebaseFirestore.collection("freePost");
        collectionReference
                //작성일자 내림차순을 정렬
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<FreePostInfo> free_postList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //각 게시글의 정보를 가져와 arrayList에 저장.
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

                            //freeAdapter를 이용하여 리사이클러 뷰로 내용 띄움.
                            RecyclerView.Adapter mAdapter1 = new freeAdapter(freeCommunityActivity.this, free_postList);
                            freeRecyclerView.setAdapter(mAdapter1);
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
                case R.id.freePostBtn:
                    myStartActivity(writingFreePostActivity.class);
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
