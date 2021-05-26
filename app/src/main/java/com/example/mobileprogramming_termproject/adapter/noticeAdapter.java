package com.example.mobileprogramming_termproject.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileprogramming_termproject.R;
import com.example.mobileprogramming_termproject.ui.alarm.AlarmItem;
import com.example.mobileprogramming_termproject.ui.alarm.DBHelper;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


//자유게시판에서 댓글을 가져오기 위한 어댑터
public class noticeAdapter extends RecyclerView.Adapter<noticeAdapter.noticeViewHolder> {
    //게시글의 댓글을 저장하기 위한 arraylist
    private ArrayList<AlarmItem> mAlarmItems=null;
    private Activity activity;
    private Fragment fragment;
    private Context mContext;
    private DBHelper mDBHelper;



    public noticeAdapter(ArrayList<AlarmItem> mAlarmItems, Context mContext) {
        this.mAlarmItems = mAlarmItems;
        this.mContext=mContext;
        mDBHelper=new DBHelper(mContext);

    }

    static class noticeViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;
        public ImageView imageView;
        public TextView textTitle;
        public TextView textContent;
        public TextView textTime;
        noticeViewHolder(CardView v){
            super(v);
            cardView = v;
             textTitle=v.findViewById(R.id.alarm_title);
            textContent=v.findViewById(R.id.alarm_content);
            textTime=v.findViewById(R.id.alarm_time);
        }
    }




    //카드뷰를 생성하여 그곳에 데이터를 집어넣어 완성시킴
    @NotNull
    @Override
    public noticeAdapter.noticeViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType){
        CardView cardView =(CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm, parent,false);
        final noticeAdapter.noticeViewHolder noticeViewHolder = new  noticeAdapter.noticeViewHolder(cardView);

        return noticeViewHolder;
    }

    //카드뷰 안에 들어갈 목록
//    소공꺼 카드뷰 참조하기 
    //댓글 카드뷰에는 댓글 내용과 작성자가 들어감.
    @Override
    public void onBindViewHolder(@NotNull final noticeAdapter.noticeViewHolder holder, int position){
         holder.textTitle.setText(mAlarmItems.get(position).getTitle());
        holder.textContent.setText(mAlarmItems.get(position).getContent());

    }

    @Override
    public int getItemCount(){
        return mAlarmItems.size();
    }

    public void addItem(AlarmItem _item){
        //최신으로 업데이트된다
        mAlarmItems.add(0,_item);
        notifyItemInserted(0);
    }

}

