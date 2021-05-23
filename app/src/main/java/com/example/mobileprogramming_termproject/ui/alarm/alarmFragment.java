package com.example.mobileprogramming_termproject.ui.alarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

 import com.example.mobileprogramming_termproject.NotificationData;
import com.example.mobileprogramming_termproject.R;
import com.example.mobileprogramming_termproject.adapter.myCommentAdapter;
import com.example.mobileprogramming_termproject.adapter.noticeAdapter;
import com.example.mobileprogramming_termproject.service.NotificationModel;
import com.example.mobileprogramming_termproject.ui.myPage.myCommentActivity;

import java.util.ArrayList;

public class alarmFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private noticeAdapter notiAdapter;
    private ArrayList  arrayList;
    private RecyclerView.LayoutManager mLayoutManager;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_alarm, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.notificationView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(0);
        notiAdapter = new noticeAdapter(this,arrayList);
        mRecyclerView.setAdapter(notiAdapter);
         return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDataset();
    }

    private void initDataset() {
        //for Test
         arrayList=new ArrayList<>();
        arrayList.add(new NotificationData(R.drawable.ic_baseline_add_alert_24,"알림","알림입니다"));
        arrayList.add(new NotificationData(R.drawable.ic_baseline_add_alert_24,"알림","알림입니다"));

//        notiAdapter.notifyDataSetChanged();

    }

}




