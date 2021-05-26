package com.example.mobileprogramming_termproject.ui.alarm;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileprogramming_termproject.MainActivity;
import com.example.mobileprogramming_termproject.R;
import com.example.mobileprogramming_termproject.adapter.noticeAdapter;
import com.example.mobileprogramming_termproject.ui.searchResult.CustomAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class alarmFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private noticeAdapter mNotiAdapter;
    private ArrayList<AlarmItem> mAlarmItems;
    private DBHelper mDBHelper;
    private TextView tv;
    private RecyclerView.LayoutManager mLayoutManager;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_alarm, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.alarm_recycle);
        mAlarmItems=new ArrayList<>();
        tv=view.findViewById(R.id.getSqlDB);

         return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        printTable();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDBHelper=new DBHelper(getActivity());
//        loadRecentDB();



    }
    private void printTable() {

        Cursor cursor = mDBHelper.readRecordAlarm();
        String result = "";
        String title=null;
        String content=null;
        String token=null;


        result += "row 개수 : " + cursor.getCount() + "\n";
        while (cursor.moveToNext()) {
               title = cursor.getString(cursor.getColumnIndex("alarm_title"));
              content = cursor.getString(cursor.getColumnIndex("alarm_content"));
              token = cursor.getString(cursor.getColumnIndex("alarm_token"));
                result=title+content+token;
            AlarmItem item=new AlarmItem();
            item.setTitle(title);
            Log.v("item",item.getTitle());
            item.setContent(content);
            item.setToken(token);
            mNotiAdapter.addItem(item);
            mRecyclerView.setAdapter(mNotiAdapter);
            mRecyclerView.setHasFixedSize(true);
         }
         tv.setText(result);

        cursor.close();
    }

    private void loadRecentDB() {
//        저장된 DB를 가져온다
        mAlarmItems=mDBHelper.getTodoList();
        if(mNotiAdapter==null){
            mNotiAdapter=new noticeAdapter(mAlarmItems,getActivity());
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setAdapter(mNotiAdapter);

        }
    }






}




