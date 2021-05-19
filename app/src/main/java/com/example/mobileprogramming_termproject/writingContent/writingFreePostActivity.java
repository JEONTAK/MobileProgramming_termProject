package com.example.mobileprogramming_termproject.writingContent;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.bumptech.glide.Glide;
import com.example.mobileprogramming_termproject.MainActivity;
import com.example.mobileprogramming_termproject.Member.MemberInfo;
import com.example.mobileprogramming_termproject.R;
import com.example.mobileprogramming_termproject.community.freeCommunityActivity;
import com.example.mobileprogramming_termproject.ui.home.HomeFragment;
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

import java.util.ArrayList;
import java.util.Date;

import static com.example.mobileprogramming_termproject.Util.isStorageUrl;
import static com.example.mobileprogramming_termproject.Util.showToast;


public class writingFreePostActivity extends AppCompatActivity {

    private static final String TAG ="writingFreePostActivity";
    //유저 선언
    private FirebaseUser user;
    //로딩창 선언
    private RelativeLayout loaderLayout;
    //Firebase 선언
    private FirebaseFirestore firebaseFirestore;

    private EditText editTitle_Free;

    private EditText editContent_Free;

    private FreePostInfo freePostInfo;
    private ArrayList<String> comment;
    private ArrayList<String> recomUser;
    private long recom;
    //dbUploader

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_free_post);

        loaderLayout = findViewById(R.id.loaderLayout);
        //현재의 User data 받아옴.
        user = FirebaseAuth.getInstance().getCurrentUser();
        //파이어베이스 가져옴
        firebaseFirestore = FirebaseFirestore.getInstance();
        findViewById(R.id.confirmBtn).setOnClickListener(onClickListener);
        findViewById(R.id.goBackBtn).setOnClickListener(onClickListener);
        editTitle_Free = findViewById(R.id.editTitle_Free);
        editContent_Free = findViewById(R.id.editContent_Free);
        freePostInfo = (FreePostInfo)getIntent().getSerializableExtra("freePostInfo");
        postInit();
    }

    //클릭시 발생하는 리스너
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //뒤로가기 버튼
                case R.id.goBackBtn:
                    myStartActivity(MainActivity.class);
                    break;
                //게시글 입력 버튼
                case R.id.confirmBtn:
                    bulletinUpload();
                    break;
            }
        }

    };

    //게시글 FireBase에 업로드 하기위함
    private void bulletinUpload(){
        //제목값 받아옴
        final String title = editTitle_Free.getText().toString();
        //내용 받아옴
        final String content = editContent_Free.getText().toString();

        //나중에 댓글 삽입 위해서 arrayList 선언
        //만약 제목 또는 내용이 공백아닐경우
        if(title.length() > 0 && content.length()> 0){
            //데이터가 firebase에 업로드 될때까지 로딩창 띄움
            loaderLayout.setVisibility(View.VISIBLE);
            Log.d(TAG, "게시글 업로드 중" );



            if(freePostInfo != null){
                recom = freePostInfo.getRecom();
                recomUser = freePostInfo.getRecomUserId();
                comment = freePostInfo.getComment();
            }
            else{
                recom = 0;
                comment = new ArrayList<>();
                recomUser = new ArrayList<>();
            }

            //저장할 위치 선언
            final DocumentReference documentReference = freePostInfo
                    == null ? firebaseFirestore.collection("freePost").document()
                    : firebaseFirestore.collection("freePost").document(freePostInfo.getPostId());

            //유저 이름을 받아오기 위하여 데이터베이스에 연결하여 유저 아이디 이용 검색
            firebaseFirestore.collection("users").document(user.getUid()).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            Log.d(TAG, "다큐먼트 실행");
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    MemberInfo userInfo = new MemberInfo(
                                            document.getData().get("name").toString(),
                                            document.getData().get("phoneNumber").toString(),
                                            document.getData().get("adress").toString(),
                                            document.getData().get("date").toString(),
                                            document.getData().get("photoUrl").toString(),
                                            document.getData().get("nickname").toString(),
                                            (ArrayList<String>) document.getData().get("bookmarkRecipe")
                                    );
                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                    //검색하여 얻은 유저 이름을 이용하여 freePost 데이터 베이스에 게시글 저장.
                                    //freepostInfo 형식으로 저장.
                                    FreePostInfo freePostInfo = new FreePostInfo(title, content, user.getUid(), userInfo.getName(), new Date(), recom, comment, documentReference.getId(), recomUser);
                                    dbUploader(documentReference, freePostInfo);
                                } else {
                                    Log.d(TAG, "No such document");
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });
        }
        else{
            showToast(writingFreePostActivity.this ,"내용을 정확히 입력해주세요!");
        }
    }

    //게시글 등록
    private void dbUploader(DocumentReference documentReference , FreePostInfo freePostInfo){
        documentReference.set(freePostInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        loaderLayout.setVisibility(View.GONE);
                        showToast(writingFreePostActivity.this ,"게시글 등록 성공!");
                        Log.w(TAG,"Success writing document" + documentReference.getId());
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loaderLayout.setVisibility(View.GONE);
                showToast(writingFreePostActivity.this ,"게시글 등록 실패.");
                Log.w(TAG,"Error writing document", e);
            }
        });
    }

    private void postInit(){
        if(freePostInfo != null){
            editTitle_Free.setText(freePostInfo.getTitle());
            editContent_Free.setText(freePostInfo.getContent());
        }
    }


    private void myStartActivity(Class c){
        Intent intent=new Intent( this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


}