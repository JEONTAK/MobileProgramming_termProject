package com.example.mobileprogramming_termproject.ui.alarm;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileprogramming_termproject.R;
import com.example.mobileprogramming_termproject.adapter.CustomAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class alarmFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private CustomAdapter customAdapter;
    private ArrayList<AlarmItem> mAlarmItems;
    private DBHelper mDBHelper;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;
    String user;
    ArrayList<String> alarm_id,alarm_title,alarm_content,alarm_date,alarm_name;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        loadRecentDB();
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.alarm_recycle);
        mDBHelper=new DBHelper(getActivity());
        alarm_id=new ArrayList<>();
        alarm_title=new ArrayList<>();
        alarm_content=new ArrayList<>();
        alarm_name=new ArrayList<>();
        alarm_date=new ArrayList<>();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //파이어베이스 유저 선언
        user = firebaseUser.getUid();

        storeDataInArrays();



        customAdapter=new CustomAdapter(getActivity(),alarm_id,alarm_title,alarm_content,
                alarm_name,alarm_date );
        mRecyclerView.setAdapter(customAdapter);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
//        거꾸로 채우기
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }
//    배열에 SQLite 값 집어넣기

    void storeDataInArrays(){
        Cursor cursor=mDBHelper.readRecordAlarm();
        if(cursor.getCount()==0){
             Toast.makeText(getActivity(), "no data", Toast.LENGTH_SHORT).show();
        }else {
            while(cursor.moveToNext()){
                if(user.equals(cursor.getString(3))) {
//
                    alarm_id.add(cursor.getString(0));
                    alarm_title.add(cursor.getString(1));
                    alarm_content.add(cursor.getString(2));
                    alarm_name.add(cursor.getString(3));
                    alarm_date.add(cursor.getString(4));
                }
            }
        }
    }

/*
    String  getToken(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        final String[] myToken = new String[1];
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                                           @Override
                                           public void onComplete(@NonNull Task<String> token) {

                                                Log.d("token",token.getResult());
                                                myToken[0] =token.getResult();
                                           }
                                       }
                );

        return myToken.toString();
    }

*/





}




