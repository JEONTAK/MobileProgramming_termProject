package com.example.mobileprogramming_termproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileprogramming_termproject.R;
import com.example.mobileprogramming_termproject.ui.alarm.AlarmItem;
import com.example.mobileprogramming_termproject.ui.alarm.DBHelper;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


//자유게시판에서 댓글을 가져오기 위한 어댑터
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewholder> {


    private Context context;
    private ArrayList alarm_title,alarm_content,alarm_id,alarm_date;

    public CustomAdapter(Context context,
                         ArrayList alarm_id,
                         ArrayList alarm_title,
                         ArrayList alarm_content,
                         ArrayList alarm_date){
        this.context=context;
        this.alarm_id=alarm_id;
        this.alarm_title=alarm_title;
        this.alarm_content=alarm_content;
        this.alarm_date=alarm_date;
     }
    @NotNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
       LayoutInflater inflater=LayoutInflater.from(context);
       View view=inflater.inflate(R.layout.item_alarm,parent,false);
        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewholder holder, int position) {
        holder.alarm_id_txt .setText(String.valueOf(alarm_id.get(position)));
        holder.alarm_title_txt.setText(String.valueOf(alarm_title.get(position)));
        holder.alarm_content_txt.setText(String.valueOf(alarm_content.get(position)));
        holder.alarm_date_txt.setText((String.valueOf(alarm_date.get(position))));
//        holder.alarm_token_txt .setText(String.valueOf(alarm_token .get(position)));


    }
//수정하기
    @Override
    public int getItemCount() {
        return  alarm_id.size();
    }

    public class MyViewholder extends  RecyclerView.ViewHolder {

        TextView alarm_id_txt,alarm_title_txt,alarm_content_txt,alarm_token_txt,alarm_date_txt;

        public MyViewholder(@NonNull @NotNull View itemView) {
            super(itemView);
            alarm_id_txt=itemView.findViewById(R.id.alarm_id_txt);
            alarm_title_txt=itemView.findViewById(R.id.alarm_title_txt);
            alarm_content_txt=itemView.findViewById(R.id.alarm_content_txt);
            alarm_date_txt=itemView.findViewById(R.id.alarm_date_txt);

        }
    }
    //게시글의 댓글을 저장하기 위한 arraylist

}

