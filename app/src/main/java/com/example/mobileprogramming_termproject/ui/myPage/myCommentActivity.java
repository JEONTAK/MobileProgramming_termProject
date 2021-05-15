package com.example.mobileprogramming_termproject.ui.myPage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileprogramming_termproject.Member.MemberInfo;
import com.example.mobileprogramming_termproject.R;
import com.example.mobileprogramming_termproject.adapter.commentAdapter;
import com.example.mobileprogramming_termproject.adapter.freeAdapter;
import com.example.mobileprogramming_termproject.adapter.myCommentAdapter;
import com.example.mobileprogramming_termproject.writingContent.FreePostInfo;
import com.example.mobileprogramming_termproject.writingContent.writingFreePostActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class myCommentActivity extends AppCompatActivity {
    private static final String TAG ="자유게시판 화면";
    //데이터베이스 선언
    private FirebaseFirestore firebaseFirestore;
    //리사이클러 뷰 선언
    private RecyclerView freeRecyclerView;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycomment);
        //파이어베이스에서 데이터베이스를 가져옴.
        firebaseFirestore= FirebaseFirestore.getInstance();

        freeRecyclerView = findViewById(R.id.post1);
        freeRecyclerView.setHasFixedSize(true);
        freeRecyclerView.setLayoutManager(new LinearLayoutManager(myCommentActivity.this));
    }

    //자유게시판에 내용이 추가가 될 경우 바로바로 업데이트 해주기 위해 resume함수에 넣어 관리.
    @Override
    protected void onResume(){
        super.onResume();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();//현재 유저목록 가져오기

        //freePost에 있는 data를 가져오기 위함.
        DocumentReference docRef = firebaseFirestore.collection("users").document(user.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                //현재 이름 가져오기

                MemberInfo memberInfo = documentSnapshot.toObject(MemberInfo.class);
                name=memberInfo.getName();
                firebaseFirestore.collection("freePost")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    ArrayList<String> commentList=new ArrayList<>();
                                    ArrayList<String> mycommentList=new ArrayList<>();
                                    ArrayList<FreePostInfo> mypostList=new ArrayList<>();
                                    FreePostInfo freePostInfo;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        freePostInfo=new FreePostInfo( document.getData().get("title").toString(),
                                                document.getData().get("content").toString(),
                                                document.getData().get("publisher").toString(),
                                                document.getData().get("userName").toString(),
                                                new Date(document.getDate("createdAt").getTime()),
                                                (Long) document.getData().get("recom"),
                                                (ArrayList<String>) document.getData().get("comment"),
                                                document.getData().get("postId").toString(),
                                                (ArrayList<String>) document.getData().get("recomUserId"));

                                        commentList=(ArrayList<String>) document.getData().get("comment");
                                        String all[]=new String[commentList.size()];
                                        for(String a:commentList){
                                            all=a.split("//");
                                            if(all[2].equals(name)){
                                                mycommentList.add(a);
                                                mypostList.add(freePostInfo);
                                            }
                                        }
                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                    }
                                    //freeAdapter를 이용하여 리사이클러 뷰로 내용 띄움.
                                    RecyclerView.Adapter mAdapter1 = new myCommentAdapter(myCommentActivity.this, mycommentList,mypostList);
                                    freeRecyclerView.setAdapter(mAdapter1);
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });


            }
        });

    }



    //activity를 실행하기 위한 함수.
    private void myStartActivity(Class c){
        Intent intent=new Intent( this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


}
