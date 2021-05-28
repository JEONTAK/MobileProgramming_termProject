package com.example.mobileprogramming_termproject.service;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileprogramming_termproject.adapter.alarmAdapter;
 import com.example.mobileprogramming_termproject.ui.alarm.AlarmItem;
import com.example.mobileprogramming_termproject.ui.alarm.DBHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class fcm {
    private static final MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
    private static final String uri = "https://fcm.googleapis.com/fcm/send";
//    private static final String serverKey = "AIzaSyAwtepc6PDundsOzbJOqx5SqHY8Iibw6v8";
private static final String serverKey = "AAAAWUUtQPo:APA91bFgT7PJ24--WfXai6HCGtCW2EDvAJOuM3H2BA_IGshJdJbjwZ5_PrfhVWpc2bFW_iTHgWrlYyAhTAZHYWdGPoVAIXZ1zjlsn8u8CZxu9YX1kBIwS9qZG3KGEMLwleD2igiZYQXf";

    private OkHttpClient httpClient;
    private Gson gson;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser user;
    private RecyclerView mRv_alarm;
     private ArrayList<AlarmItem> mAlarmItems;
    private DBHelper mDBHelper;
    private alarmAdapter mAdapter;




    DocumentReference docRef;

    public fcm() {
        gson = new Gson();
        httpClient = new OkHttpClient();
    }

    String getToken;
    //싱글톤 패턴
    public static fcm getInstance() {
        return new fcm();
    }

    public void sendMessage(String publisher,  String title,String message) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        String user = publisher;

        docRef = firebaseFirestore.collection("users").document(user);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        getToken = document.getData().get("token").toString();
                        Log.d("getToken",getToken);




                NotificationModel notificationModel = new NotificationModel();

                notificationModel.notification.title = title;
                notificationModel.notification.message = message;
                notificationModel.to = getToken;
                        String currentTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

//                         insert UI

//                        AlarmItem item =new AlarmItem() ;
//                        item.setTitle(title.getText().toString());
//                        item.setContent(content.getText().toString());
//                        item.setWriteDate(currentTime);
//
//                        mAdapter.addItem(item);
//
//                        mRv_alarm.smoothScrollToPosition(0);


                Log.d("noti.toToken",notificationModel.to);
                Log.d("noti.title",notificationModel.notification.title);
                Log.d("noti.message",notificationModel.notification.message);

//            오류나면 순서 바꾸기
                RequestBody requestBody = RequestBody.create(gson.toJson(notificationModel),MediaType.parse("application/json; charset=utf-8"));

                Request request = new Request.Builder().
                        addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "key="+serverKey)
                        .url(uri).post(requestBody).build();
                OkHttpClient okHttpClient = new OkHttpClient();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("DD","error");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
//                        passMessage(title,message);
                        Log.e("DD",response.body().string());
                    }
                });

            }
                }
            }
        });
    }

//    public void passMessage(String title,String text) {
//
//        firebaseFirestore = FirebaseFirestore.getInstance();
//
//        FirebaseMessaging.getInstance().getToken()
//                .addOnCompleteListener(new OnCompleteListener<String>() {
//                    @Override
//                    public void onComplete(@NonNull Task<String> token) {
//
//                        firebaseFirestore.collection("Alarm").document().update("tokenTo", token.getResult());
//                        firebaseFirestore.collection("Alarm").document().update("text", text);
//                        firebaseFirestore.collection("Alarm").document().update("title", title);
//
//                    }
//                }
//                );
//
//
//    }
}
