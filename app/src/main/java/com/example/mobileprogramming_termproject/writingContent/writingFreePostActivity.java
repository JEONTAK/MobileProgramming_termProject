package com.example.mobileprogramming_termproject.writingContent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.mobileprogramming_termproject.Member.MemberInfo;
import com.example.mobileprogramming_termproject.R;
import com.example.mobileprogramming_termproject.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;

import static com.example.mobileprogramming_termproject.Util.showToast;


public class writingFreePostActivity extends AppCompatActivity {

    private static final String TAG =" freePostActivity";
    private FirebaseUser user;
    private RelativeLayout loaderLayout;

    private int pathCount , successCount;
    //dbUploader

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_post);

        loaderLayout = findViewById(R.id.loaderLayout);
        
        findViewById(R.id.confirmBtn).setOnClickListener(onClickListener);
        findViewById(R.id.goBackBtn).setOnClickListener(onClickListener);


    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.goBackBtn:
                    myStartActivity(HomeFragment.class);
                    break;
                case R.id.confirmBtn:
                    bulletinUpload();
                    break;
            }
        }

    };

    private void bulletinUpload(){
        final String title = ((EditText)findViewById(R.id.editTitle_Free)).getText().toString();
        final String content = ((TextView)findViewById(R.id.editContent_Free)).getText().toString();
        final ArrayList<String> comment = new ArrayList<>();

        if(title.length() > 0 && content.length()> 0){
            loaderLayout.setVisibility(View.VISIBLE);
            Log.d("로그: ", " " );
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

            user = FirebaseAuth.getInstance().getCurrentUser();
            ArrayList<MemberInfo> userInfo = new ArrayList<>();
            String name = "";
            DocumentReference docRef = firebaseFirestore.collection("users").document(user.getUid());
            Log.d(TAG, "다큐먼트 선언");
            docRef.get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    Log.d(TAG, "다큐먼트 실행");
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            userInfo.add(new MemberInfo(
                                    document.getData().get("name").toString(),
                                    document.getData().get("phoneNumber").toString(),
                                    document.getData().get("adress").toString(),
                                    document.getData().get("date").toString(),
                                    document.getData().get("userId").toString()
                            ));

                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });

            Log.d(TAG, "다큐먼트 종료");
            name = userInfo.get(0).getName();
            ArrayList<String> recomUser = new ArrayList<>();
            final DocumentReference documentReference = firebaseFirestore.collection("freePost").document();
            FreePostInfo freePostInfo = new FreePostInfo(title, content, user.getUid(), name, new Date(), 0, comment, documentReference.getId(), recomUser);
            dbUploader(documentReference, freePostInfo);
        }
        else{
            showToast(writingFreePostActivity.this ,"내용을 정확히 입력해주세요!");
        }
    }

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


    private void myStartActivity(Class c){
        Intent intent=new Intent( this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


}

