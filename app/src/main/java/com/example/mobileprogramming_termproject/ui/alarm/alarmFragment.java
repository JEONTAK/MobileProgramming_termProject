package com.example.mobileprogramming_termproject.ui.alarm;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileprogramming_termproject.R;
import com.example.mobileprogramming_termproject.adapter.CustomAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class alarmFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private CustomAdapter customAdapter;
    private ArrayList<AlarmItem> mAlarmItems;
    private DBHelper mDBHelper;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseUser user;
    private FirebaseFirestore db;

    ArrayList<String> alarm_id,alarm_title,alarm_content,alarm_date;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        loadRecentDB();
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.alarm_recycle);
        mDBHelper=new DBHelper(getActivity());
        alarm_id=new ArrayList<>();
        alarm_title=new ArrayList<>();
        alarm_content=new ArrayList<>();
        alarm_date=new ArrayList<>();


        storeDataInArrays();



        customAdapter=new CustomAdapter(getActivity(),alarm_id,alarm_title,alarm_content,
                alarm_date);
        mRecyclerView.setAdapter(customAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


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
//                String  compare=getToken();
//                if(cursor.getString(3).equals(compare)) {
//                    alarm_id.add(cursor.getString(0));
//                    alarm_title.add(cursor.getString(1));
//                    alarm_content.add(cursor.getString(2));
//                    alarm_token.add(cursor.getString(3));
//                }
                alarm_id.add(cursor.getString(0));
                alarm_title.add(cursor.getString(1));
                alarm_content.add(cursor.getString(2));
                alarm_date.add(cursor.getString(3));


            }
        }
    }

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






}




