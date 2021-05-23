package com.example.mobileprogramming_termproject.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileprogramming_termproject.NotificationData;
import com.example.mobileprogramming_termproject.R;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


//자유게시판에서 댓글을 가져오기 위한 어댑터
public class noticeAdapter extends RecyclerView.Adapter<noticeAdapter.noticeViewHolder> {
    //게시글의 댓글을 저장하기 위한 arraylist
    private ArrayList<NotificationData> mDataset=null;
    private Activity activity;
    private Fragment fragment;


    static class noticeViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;
        public ImageView imageView;
        public TextView textTitle;
        public TextView textContent;
        noticeViewHolder(CardView v){
            super(v);
            cardView = v;
            imageView=v.findViewById(R.id.noti_profile);
            textTitle=v.findViewById(R.id.noti_title);
            textContent=v.findViewById(R.id.noti_content);
        }
    }

    public noticeAdapter(Fragment fragment, ArrayList<NotificationData> commentDataset){
        mDataset = commentDataset;
        this.fragment=fragment;
//        this.activity = activity;
    }


    //카드뷰를 생성하여 그곳에 데이터를 집어넣어 완성시킴
    @NotNull
    @Override
    public noticeAdapter.noticeViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType){
        CardView cardView =(CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent,false);
        final noticeAdapter.noticeViewHolder noticeViewHolder = new  noticeAdapter.noticeViewHolder(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        return noticeViewHolder;
    }

    //카드뷰 안에 들어갈 목록
//    소공꺼 카드뷰 참조하기 
    //댓글 카드뷰에는 댓글 내용과 작성자가 들어감.
    @Override
    public void onBindViewHolder(@NotNull final noticeAdapter.noticeViewHolder holder, int position){
        holder.imageView.setImageResource(mDataset.get(position).noti_profile);
        holder.textTitle.setText(mDataset.get(position).noti_title);
        holder.textContent.setText(mDataset.get(position).noti_content);

    }

    @Override
    public int getItemCount(){
        return mDataset.size();
    }

}

