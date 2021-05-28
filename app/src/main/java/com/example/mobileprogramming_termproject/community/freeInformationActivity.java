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
import com.example.mobileprogramming_termproject.adapter.alarmAdapter;
import com.example.mobileprogramming_termproject.ui.alarm.DBHelper;
import com.example.mobileprogramming_termproject.writingContent.FreePostInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.example.mobileprogramming_termproject.service.fcm;

import static com.example.mobileprogramming_termproject.Util.showToast;


public class freeInformationActivity extends AppCompatActivity {

    private final String TAG = "자유게시글 정보";
    private FirebaseFirestore firebaseFirestore;
    private FirebaseDatabase mFirebaseDatabase;
    private FreePostInfo freePostInfo;
    private FirebaseUser firebaseUser;

    private String user;
    private ImageButton RecomBtn;
    private String id;
    private RecyclerView comment_view;
    private RelativeLayout loaderLayout;
    private DocumentReference dr;
    private DBHelper mDBHelper;
    private alarmAdapter mAdapter;



    String name;
    DocumentReference docRef;
    fcm fcm1 = new fcm();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_information);
        loaderLayout = findViewById(R.id.loaderLayout);
//        DB 선언
        mDBHelper=new DBHelper(this);


        firebaseFirestore= FirebaseFirestore.getInstance();//데이터베이스 선언
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //파이어베이스 유저 선언
        user = firebaseUser.getUid();
        Log.d(TAG, "유저 아이디 " + firebaseUser.getUid() + " aa " + user);
    }
//    댓글 추천기능
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.freeRecomBtn:
                    id = freePostInfo.getPostId();
                    ArrayList<String> newRecomUserId = new ArrayList<>();
//                    이름이 이미 포함되어있는경우
                    if (freePostInfo.getRecomUserId().contains(user)) {
                        freePostInfo.setRecom(freePostInfo.getRecom() - 1);
                        newRecomUserId = freePostInfo.getRecomUserId();
                        newRecomUserId.remove(user);
                        freePostInfo.setRecomUserId(newRecomUserId);

                        if (id == null) {
                            dr = firebaseFirestore.collection("freePost").document();

                        } else {
                            dr = firebaseFirestore.collection("freePost").document(id);

                        }
                        dbUploader(dr, freePostInfo, 0);
                        break;
//                        새로운 아이디일 경우
                    } else {
                        freePostInfo.setRecom(freePostInfo.getRecom() + 1);
                        newRecomUserId = freePostInfo.getRecomUserId();
                        newRecomUserId.add(user);
                        freePostInfo.setRecomUserId(newRecomUserId);
//                        fcm 알림 날리기

                        fcm1.sendMessage(freePostInfo.getPublisher(),freePostInfo.getTitle()+" 댓글이 추천되었습니다.",firebaseUser.getEmail()+"님의 추천");
//                        sqlite insert

                        String sendTitle=freePostInfo.getTitle()+" 댓글이 추천되었습니다.";
                        String sendText=freePostInfo.getUserName()+"의 게시글";
                        String currentTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

                        mDBHelper.InsertAlarm(sendTitle,sendText,currentTime);
                        passAlarm(sendTitle,sendText,currentTime,freePostInfo.getPublisher());







//

                        if (id == null) {
                            dr = firebaseFirestore.collection("freePost").document();

                        } else {
                            dr = firebaseFirestore.collection("freePost").document(id);

                        }
                        dbUploader(dr, freePostInfo, 1);
                        break;
                    }
                    //댓글기능
                case R.id.writingFreePost:
                    docRef = firebaseFirestore.collection("users").document(user);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    MemberInfo memberInfo = new MemberInfo(
                                            document.getData().get("name").toString(),
                                            document.getData().get("phoneNumber").toString(),
                                            document.getData().get("adress").toString(),
                                            document.getData().get("date").toString(),
                                            document.getData().get("photoUrl").toString(),
                                            document.getData().get("nickname").toString(),
                                            (ArrayList<String>) document.getData().get("bookmarkRecipe"),
                                            document.getData().get("token").toString()

                                    );

//                           fcm 알림날리기
                                    fcm1.sendMessage(freePostInfo.getPublisher(),freePostInfo.getTitle()+"에 댓글이 작성되었습니다.",firebaseUser.getEmail()+"님의 댓글");

                                    String sendTitle=freePostInfo.getTitle()+"에 댓글이 작성되었습니다.";
                                    String sendText=freePostInfo.getUserName()+"의 게시글에 댓글 ";
                                    String currentTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
//                                    sqlite insert

                                    mDBHelper.InsertAlarm(sendTitle,sendText,currentTime);


                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                    name = memberInfo.getName();
                                    loaderLayout.setVisibility(View.VISIBLE);
                                    id = freePostInfo.getPostId();
                                    String comment = ((EditText) findViewById(R.id.editComment)).getText().toString();
                                    ArrayList<String> newComment = new ArrayList<>();
                                    newComment = freePostInfo.getComment();
                                    String commentAndUser = comment + "//" + user + "//" + name;
                                    newComment.add(commentAndUser);
                                    freePostInfo.setComment(newComment);

                                    if (id == null) {
                                        dr = firebaseFirestore.collection("freePost").document();

                                    } else {
                                        dr = firebaseFirestore.collection("freePost").document(id);
                                    }
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

    @Override
    protected void onResume() {
        super.onResume();
        TextView isRecom = findViewById(R.id.isRecomText_Free);
        RecomBtn = findViewById(R.id.freeRecomBtn);
        RecomBtn.setOnClickListener(onClickListener);
        findViewById(R.id.writingFreePost).setOnClickListener(onClickListener);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //파이어베이스 유저 선언
        String user = firebaseUser.getUid();
        freePostInfo = (FreePostInfo) getIntent().getSerializableExtra("freePostInfo");


        ArrayList<String> recomUser = freePostInfo.getRecomUserId();
        if(recomUser.contains(user))
        {
            RecomBtn.setImageTintList(ColorStateList.valueOf(Color.RED));
            isRecom.setText("이미 좋아요를 한 게시글이에요!");

        }
        else{
            RecomBtn.setImageTintList(ColorStateList.valueOf(Color.BLACK));
            isRecom.setText("이 게시글이 좋다면 추천을 눌러주세요!");

        }

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


        LinearLayout freeCommentLayout = findViewById(R.id.freeCommentLayout);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ArrayList<String> freeContentList = freePostInfo.getComment();
        Log.d("로그","" + freePostInfo.getComment());

        comment_view = findViewById(R.id.freePostRecyclerView);
        comment_view.setHasFixedSize(true);
        comment_view.setLayoutManager(new LinearLayoutManager(freeInformationActivity.this));
        RecyclerView.Adapter mAdapter = new commentAdapter(freeInformationActivity.this, freeContentList);
        comment_view.setAdapter(mAdapter);

    }

        private void dbUploader(DocumentReference documentReference , FreePostInfo freePostInfo, int requestCode){
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
        }else if (requestCode == 1){
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
        }else{
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

    void passAlarm(String title,String content,String date,String uid) {
        Map<String, Object> data = new HashMap<>();
        data.put("content", content);
        data.put("title", title);
        data.put("date", date);
        data.put("uid",uid);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("alarm").document(firebaseUser.getUid()).set(data);



    }




}



