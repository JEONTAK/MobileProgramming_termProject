package com.example.mobileprogramming_termproject.community;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.mobileprogramming_termproject.Member.MemberInfo;
import com.example.mobileprogramming_termproject.R;
import com.example.mobileprogramming_termproject.adapter.commentAdapter;
import com.example.mobileprogramming_termproject.writingContent.FreePostInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import static com.example.mobileprogramming_termproject.Util.showToast;


public class freeInformationActivity extends AppCompatActivity {

    private final String TAG = "자유게시글 정보 화면";
    //데이터베이스 선언
    private FirebaseFirestore firebaseFirestore;
    //게시글 정보 가져오기 위함
    private FreePostInfo freePostInfo;
    //유저 선언
    private FirebaseUser firebaseUser;
    //유저 아이디를 가져오기 위한 String값 선언
    private String user;
    //추천 버튼
    private ImageButton RecomBtn;
    //자유게시글 고유 Id 값 
    private String id;
    //리사이클러 뷰 선언
    private RecyclerView comment_view;
    //로딩창 선언
    private RelativeLayout loaderLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_information);
        loaderLayout = findViewById(R.id.loaderLayout);
        //파이어베이스에 저장된 데이터베이스 가져옴
        firebaseFirestore= FirebaseFirestore.getInstance();
        //현재 유저값 받아옴
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); 
        user = firebaseUser.getUid();
        Log.d(TAG, "유저 아이디 " + firebaseUser.getUid());
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //추천버튼 누를경우
                case R.id.freeRecomBtn:
                    //게시글 id 가져옴
                    id = freePostInfo.getPostId();
                    ArrayList<String> newRecomUserId = new ArrayList<>();
                    DocumentReference dr;
                    //만약 추천한 명단에 이미 유저가 있을경우 추천 취소
                    if(freePostInfo.getRecomUserId().contains(user))
                    {
                        freePostInfo.setRecom(freePostInfo.getRecom() - 1);
                        newRecomUserId = freePostInfo.getRecomUserId();
                        newRecomUserId.remove(user);
                        freePostInfo.setRecomUserId(newRecomUserId);
                        if(id == null){
                            dr = firebaseFirestore.collection("freePost").document();

                        }else{
                            dr =firebaseFirestore.collection("freePost").document(id);

                        }
                        dbUploader(dr, freePostInfo, 0);
                        break;
                    }
                    //만약 추천명단에 유저가 없을경우 추천
                    else{
                        freePostInfo.setRecom(freePostInfo.getRecom() + 1);
                        newRecomUserId = freePostInfo.getRecomUserId();
                        newRecomUserId.add(user);
                        freePostInfo.setRecomUserId(newRecomUserId);
                        if(id == null){
                            dr = firebaseFirestore.collection("freePost").document();

                        }else{
                            dr =firebaseFirestore.collection("freePost").document(id);

                        }
                        dbUploader(dr, freePostInfo, 1);
                        break;
                    }
                    //게시글에 댓글 작성 버튼
                case R.id.writingFreePost:
                    loaderLayout.setVisibility(View.VISIBLE);
                    firebaseFirestore.collection("users").document(user)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    MemberInfo userInfo = new MemberInfo(
                                            document.getData().get("name").toString(),
                                            document.getData().get("phoneNumber").toString(),
                                            document.getData().get("adress").toString(),
                                            document.getData().get("date").toString(),
                                            document.getData().get("photoUrl").toString(),
                                            (ArrayList<String>) document.getData().get("bookmarkRecipe")

                                    );
                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                                    id = freePostInfo.getPostId();
                                    String comment = ((EditText)findViewById(R.id.editComment)).getText().toString();
                                    ArrayList<String> newComment = new ArrayList<>();
                                    newComment = freePostInfo.getComment();
                                    String commentAndUser = comment + "//" + user + "//" + userInfo.getName();
                                    newComment.add(commentAndUser);
                                    freePostInfo.setComment(newComment);

                                    DocumentReference dr;

                                    if(id == null){
                                        dr = firebaseFirestore.collection("freePost").document();

                                    }else{
                                        dr =firebaseFirestore.collection("freePost").document(id);
                                    }
                                    //freepostInfo 형식으로 저장.
                                    dbUploader(dr, freePostInfo, 2);

                                } else {
                                    Log.d(TAG, "No such document");
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });
                    break;
            }
        }
    };

    //게시글에 추천수가 증가했거나 감소했을경우, 또는 댓글이 추가가 될 경우 바로바로 업데이트 해주기 위해 resume함수에 넣어 관리.
    @Override
    protected void onResume() {
        super.onResume();
        //유저가 이미 추천을 했는지 하지않았는지 알려주기 위함.
        TextView isRecom = findViewById(R.id.isRecomText_Free);
        RecomBtn = findViewById(R.id.freeRecomBtn);
        RecomBtn.setOnClickListener(onClickListener);
        findViewById(R.id.writingFreePost).setOnClickListener(onClickListener);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //파이어베이스 유저 선언
        String user = firebaseUser.getUid();
        //게시글 data 가져옴
        freePostInfo = (FreePostInfo) getIntent().getSerializableExtra("freePostInfo");
        //추천한 유저 명단
        ArrayList<String> recomUser = freePostInfo.getRecomUserId();
        
        //만약 현재 유저가 게시글에 이미 추천을 눌렀다면
        if(recomUser.contains(user))
        {
            RecomBtn.setImageTintList(ColorStateList.valueOf(Color.RED));
            isRecom.setText("이미 좋아요를 한 게시글이에요!");

        }
        //누르지 않았다면
        else{
            RecomBtn.setImageTintList(ColorStateList.valueOf(Color.BLACK));
            isRecom.setText("이 게시글이 좋다면 추천을 눌러주세요!");

        }
        //게시글 정보 띄우기 위한 코드
        //시작
        String id = getIntent().getStringExtra("id");
        Log.d("로그: ", "" + getIntent().getStringExtra("id"));

        TextView freeInfoTitle = findViewById(R.id.freeIntoTitle);
        freeInfoTitle.setText(freePostInfo.getTitle());
        Log.d("로그","" + freePostInfo.getTitle());

        TextView freeRecomNum = findViewById(R.id.recomNumber_Free);
        freeRecomNum.setText(Integer.toString((int) freePostInfo.getRecom()));
        Log.d("로그","" + freePostInfo.getRecom());

        TextView freeCreatedAt = findViewById(R.id.freeInfoCreatedAt);
        freeCreatedAt.setText(new SimpleDateFormat("MM월dd일 hh시mm분", Locale.KOREA).format(freePostInfo.getCreatedAt()));
        Log.d("로그","" + freePostInfo.getCreatedAt());

        TextView freeContent = findViewById(R.id.textContent_Free);
        freeContent.setText(freePostInfo.getContent());
        //끝
        
        //댓글 띄우기 위한 코드
        //시작
        LinearLayout freeCommentLayout = findViewById(R.id.freeCommentLayout);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ArrayList<String> freeContentList = freePostInfo.getComment();
        Log.d("로그","" + freePostInfo.getComment());

        comment_view = findViewById(R.id.freePostRecyclerView);
        comment_view.setHasFixedSize(true);
        comment_view.setLayoutManager(new LinearLayoutManager(freeInformationActivity.this));
        RecyclerView.Adapter mAdapter = new commentAdapter(freeInformationActivity.this, freeContentList);
        comment_view.setAdapter(mAdapter);
        //끝
    }

    //추천, 추천 취소, 댓글을 남길경우 바로바로 데이터베이스에 업로드하여 반영해줌.
        private void dbUploader(DocumentReference documentReference , FreePostInfo freePostInfo, int requestCode){
       //추천취소 하는 경우
        if(requestCode == 0){
            documentReference.set(freePostInfo)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showToast(freeInformationActivity.this ,"추천을 취소했어요!");
                            Log.w(TAG,"Success writing document" + documentReference.getId());
                            onResume();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showToast(freeInformationActivity.this ,"추천취소에 실패했어요!");
                    Log.w(TAG,"Error writing document", e);
                }
            });
        }
        //추천할 경우
        else if (requestCode == 1){
            documentReference.set(freePostInfo)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showToast(freeInformationActivity.this ,"이 게시글을 추천했어요!");
                            Log.w(TAG,"Success writing document" + documentReference.getId());
                            onResume();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showToast(freeInformationActivity.this ,"게시글 추천에 실패했어요!");
                    Log.w(TAG,"Error writing document", e);
                }
            });
        }
        //댓글을 남길 경우
        else{
            documentReference.set(freePostInfo)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            loaderLayout.setVisibility(View.GONE);
                            showToast(freeInformationActivity.this ,"댓글을 남겼어요!");
                            Log.w(TAG,"Success writing document" + documentReference.getId());
                            onResume();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showToast(freeInformationActivity.this ,"댓글을 남기는데 실패했어요!");
                    Log.w(TAG,"Error writing document", e);
                }
            });
        }

    }


}



