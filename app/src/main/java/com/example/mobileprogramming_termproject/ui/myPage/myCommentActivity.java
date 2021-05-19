package com.example.mobileprogramming_termproject.ui.myPage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileprogramming_termproject.Member.MemberInfo;
import com.example.mobileprogramming_termproject.R;
import com.example.mobileprogramming_termproject.adapter.commentAdapter;
import com.example.mobileprogramming_termproject.adapter.freeAdapter;
import com.example.mobileprogramming_termproject.adapter.myCommentAdapter;
import com.example.mobileprogramming_termproject.adapter.myfreeAdapter;
import com.example.mobileprogramming_termproject.community.freeCommunityActivity;
import com.example.mobileprogramming_termproject.community.freeInformationActivity;
import com.example.mobileprogramming_termproject.listener.OnCommentListener;
import com.example.mobileprogramming_termproject.listener.OnPostListener;
import com.example.mobileprogramming_termproject.writingContent.FreePostInfo;
import com.example.mobileprogramming_termproject.writingContent.writingFreePostActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;

import static com.example.mobileprogramming_termproject.Util.showToast;

public class myCommentActivity extends AppCompatActivity {
    private static final String TAG ="자유게시판 화면";
    //데이터베이스 선언
    private FirebaseFirestore firebaseFirestore;
    //리사이클러 뷰 선언
    private RecyclerView freeRecyclerView;
    private String name;

    private myCommentAdapter myCommentAdapter;
    private StorageReference storageRef;

    private ArrayList<String> commentList;
    private ArrayList<String> mycommentList;
    private ArrayList<FreePostInfo> mypostList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycomment);
        //파이어베이스에서 데이터베이스를 가져옴.
        firebaseFirestore= FirebaseFirestore.getInstance();

        // Create a storage reference from our app
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        commentList = new ArrayList<>();
        mycommentList = new ArrayList<>();
        mypostList = new ArrayList<>();
        //freeAdapter를 이용하여 리사이클러 뷰로 내용 띄움.
        myCommentAdapter = new myCommentAdapter(myCommentActivity.this, mycommentList, mypostList);
        myCommentAdapter.setOnCommentListener(onCommentListener);

        freeRecyclerView = findViewById(R.id.myPage_comment_List);
        freeRecyclerView.setHasFixedSize(true);
        freeRecyclerView.setLayoutManager(new LinearLayoutManager(myCommentActivity.this));

        freeRecyclerView.setAdapter(myCommentAdapter);
    }

    OnCommentListener onCommentListener = new OnCommentListener() {
        @Override
        public void onDelete(int position) {
            String id = mypostList.get(position).getPostId();
            Log.d(TAG, "삭제" + id);
            //freePost에 있는 data를 가져오기 위함.
            DocumentReference dr = firebaseFirestore.collection("freePost").document(id);
            dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            FreePostInfo freePostInfo = new FreePostInfo(
                                    document.getData().get("title").toString(),
                                    document.getData().get("content").toString(),
                                    document.getData().get("publisher").toString(),
                                    document.getData().get("userName").toString(),
                                    new Date(document.getDate("createdAt").getTime()),
                                    (Long) document.getData().get("recom"),
                                    (ArrayList<String>) document.getData().get("comment"),
                                    document.getData().get("postId").toString(),
                                    (ArrayList<String>) document.getData().get("recomUserId")
                            );
                            ArrayList<String> postCommentList = freePostInfo.getComment();

                            if(postCommentList.contains(mycommentList.get(position))){
                                postCommentList.remove(mycommentList.get(position));
                                dbUploader(dr, freePostInfo);
                                postUpdate();

                            }
                            else{
                                showToast(myCommentActivity.this ,"게시글 삭제에 실패했어요!");

                            }

                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }
    };

    //자유게시판에 내용이 추가가 될 경우 바로바로 업데이트 해주기 위해 resume함수에 넣어 관리.
    @Override
    protected void onResume() {
        super.onResume();
        postUpdate();
    }



    private void postUpdate(){
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
                                    commentList.clear();
                                    mycommentList.clear();
                                    mypostList.clear();
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

                                    myCommentAdapter.notifyDataSetChanged();

                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });


            }
        });
    }

    //게시글 등록
    private void dbUploader(DocumentReference documentReference , FreePostInfo freePostInfo){
        documentReference.set(freePostInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showToast(myCommentActivity.this ,"게시글 삭제에 성공했어요!");
                        Log.w(TAG,"Success writing document" + documentReference.getId());
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showToast(myCommentActivity.this ,"게시글 삭제에 실패했어요!");
                Log.w(TAG,"Error writing document", e);
            }
        });
    }




}
