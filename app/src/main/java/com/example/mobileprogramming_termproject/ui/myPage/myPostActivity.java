package com.example.mobileprogramming_termproject.ui.myPage;

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
import com.example.mobileprogramming_termproject.adapter.myfreeAdapter;
import com.example.mobileprogramming_termproject.adapter.myrecipeAdapter;
import com.example.mobileprogramming_termproject.listener.OnPostListener;
import com.example.mobileprogramming_termproject.writingContent.FreePostInfo;
import com.example.mobileprogramming_termproject.writingContent.RecipePostInfo;
import com.example.mobileprogramming_termproject.writingContent.writingFreePostActivity;
import com.example.mobileprogramming_termproject.writingContent.writingRecipePostActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;

import static com.example.mobileprogramming_termproject.Util.isStorageUrl;
import static com.example.mobileprogramming_termproject.Util.showToast;

public class myPostActivity extends AppCompatActivity {
    private static final String TAG ="내 자유게시판 화면";
    //데이터베이스 선언
    private FirebaseFirestore firebaseFirestore;
    //리사이클러 뷰 선언
    private RecyclerView freeRecyclerView;

    private myfreeAdapter myfreeAdapter;
    private ArrayList<FreePostInfo> free_postList;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypost);
        //파이어베이스에서 데이터베이스를 가져옴.
        firebaseFirestore= FirebaseFirestore.getInstance();

        // Create a storage reference from our app
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        free_postList = new ArrayList<>();
        //freeAdapter를 이용하여 리사이클러 뷰로 내용 띄움.
        myfreeAdapter = new myfreeAdapter(myPostActivity.this, free_postList);
        myfreeAdapter.setOnPostListener(onPostListener);

        freeRecyclerView = findViewById(R.id.myPage_freePost_List);
        freeRecyclerView.setHasFixedSize(true);
        freeRecyclerView.setLayoutManager(new LinearLayoutManager(myPostActivity.this));

        freeRecyclerView.setAdapter(myfreeAdapter);
    }

    OnPostListener onPostListener = new OnPostListener() {

        @Override
        public void onModify(int position) {
            String id = free_postList.get(position).getPostId();
            Log.d(TAG, "수정" + id);
            myStartActivity(writingFreePostActivity.class, free_postList.get(position));
        }

        @Override
        public void onDelete(int position) {
            String id = free_postList.get(position).getPostId();

            Log.d(TAG, "삭제" + id);
            firebaseFirestore.collection("freePost").document(id)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            postUpdate();
                            showToast(myPostActivity.this ,"게시글 삭제에 성공했어요!");
                            Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showToast(myPostActivity.this ,"게시글 삭제에 실패했어요!");
                            Log.w(TAG, "Error deleting document", e);
                        }
                    });
        }
    };

    //자유게시판에 내용이 추가가 될 경우 바로바로 업데이트 해주기 위해 resume함수에 넣어 관리.
    @Override
    protected void onResume(){
        super.onResume();
        postUpdate();
    }

    private void postUpdate(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();//현재 유저목록 가져오기
        //freePost에 있는 data를 가져오기 위함.
        CollectionReference collectionReference = firebaseFirestore.collection("freePost");
        collectionReference
                .whereEqualTo("publisher",user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            free_postList.clear();
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

                            myfreeAdapter.notifyDataSetChanged();
                        } else {
                            Log.d("로그: ", "Error getting documents: ", task.getException());

                        }
                    }
                });
    }

    //activity를 실행하기 위한 함수.
    private void myStartActivity(Class c){
        Intent intent=new Intent( this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void myStartActivity(Class c, FreePostInfo freePostInfo){
        Intent intent=new Intent( this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("freePostInfo", freePostInfo);
        startActivity(intent);
    }

}
