package com.example.mobileprogramming_termproject.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileprogramming_termproject.MainActivity;
import com.example.mobileprogramming_termproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    Button mLoginBtn;
    TextView mResigettxt;
    TextView mPasswordResettxt;
    EditText mEmailText, mPasswordText;
    private String tokenValue;

    private FirebaseAuth firebaseAuth;
    private RelativeLayout loaderLayout;


    // Firebase - Realtime Database

    private FirebaseDatabase mFirebaseDatabase;
    private ChildEventListener mChildEventListener;
    private FirebaseUser user;
    private FirebaseFirestore db;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);


        firebaseAuth =  FirebaseAuth.getInstance();
        //버튼 및 텍스트 등록하기
        mResigettxt = findViewById(R.id.register_t2);
        mPasswordResettxt= findViewById(R.id.register_t3);
        mLoginBtn = findViewById(R.id.login_btn);
        mEmailText = findViewById(R.id.emailEt);
        mPasswordText = findViewById(R.id.passwordEdt);
        loaderLayout = findViewById(R.id.loaderLayout);

//        if (firebaseAuth.getCurrentUser() != null){
////            setUserInfo();
//                Toast.makeText(getApplicationContext(), "자동 로그인", Toast.LENGTH_SHORT).show();
////                startActivity(new Intent(getApplicationContext(), MainActivity.class));
//
//            finish();
//        }

            //가입 버튼이 눌리면
        mResigettxt.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //intent함수를 통해 register액티비티 함수를 호출한다.
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));

            }
        });

        //비밀번호 재설정 버튼이 눌리면
        mPasswordResettxt.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //intent함수를 통해 PasswordResetActivity 함수를 호출한다.
                startActivity(new Intent(LoginActivity.this,PasswordResetActivity.class));

            }
        });



        //로그인 버튼이 눌리면
        mLoginBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                loaderLayout.setVisibility(View.VISIBLE);
                String email = mEmailText.getText().toString().trim();
                String pwd = mPasswordText.getText().toString().trim();
                if (email.length() > 0 && pwd.length() > 0 ) {
                    firebaseAuth.signInWithEmailAndPassword(email, pwd)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        loaderLayout.setVisibility(View.GONE);
                                        passPushTokenToServer();

                                         Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    } else {
                                        loaderLayout.setVisibility(View.GONE);
                                        Toast.makeText(LoginActivity.this, "로그인 오류", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else{
                    loaderLayout.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "이메일과 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    void passPushTokenToServer() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                   @Override
                   public void onComplete(@NonNull Task<String> token) {

                       db.collection("users").document(user.getUid()).update("token", token.getResult());
                       Log.d("token",token.getResult());
                   }
               }
            );


    }

}