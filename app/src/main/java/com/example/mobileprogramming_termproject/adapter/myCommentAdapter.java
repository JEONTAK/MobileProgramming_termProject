package com.example.mobileprogramming_termproject.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileprogramming_termproject.R;
import com.example.mobileprogramming_termproject.community.freeInformationActivity;
import com.example.mobileprogramming_termproject.writingContent.FreePostInfo;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

//자유게시판에서 댓글을 가져오기 위한 어댑터
public class myCommentAdapter extends RecyclerView.Adapter<myCommentAdapter.commentViewHolder> {
    //게시글의 댓글을 저장하기 위한 arraylist
    private ArrayList<String> mDataset;
    private ArrayList<FreePostInfo> mDataset2;
    private Activity activity;


    static class commentViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;
        commentViewHolder(CardView v){
            super(v);
            cardView = v;
        }
    }

    public myCommentAdapter(Activity activity, ArrayList<String> commentDataset,ArrayList<FreePostInfo> postDataset){
        mDataset = commentDataset;
        mDataset2=postDataset;
        this.activity = activity;
    }


    //카드뷰를 생성하여 그곳에 데이터를 집어넣어 완성시킴
    @NotNull
    @Override
    public myCommentAdapter.commentViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType){
        CardView cardView =(CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mycomment_post, parent,false);
        final myCommentAdapter.commentViewHolder commentViewHolder = new myCommentAdapter.commentViewHolder(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(activity, freeInformationActivity.class);
                intent.putExtra("freePostInfo", mDataset2.get(commentViewHolder.getAdapterPosition()));
                activity.startActivity(intent);
            }
        });
        return commentViewHolder;
    }

    //카드뷰 안에 들어갈 목록
    //댓글 카드뷰에는 댓글 내용과 작성자가 들어감.
    @Override
    public void onBindViewHolder(@NotNull final myCommentAdapter.commentViewHolder holder, int position){



        String[] all = mDataset.get(position).split("//");
        CardView cardView = holder.cardView;
        TextView title = cardView.findViewById(R.id.commentContent);
        title.setText(all[0]);

        TextView publisher = cardView.findViewById(R.id.commentPublisher);
        publisher.setText(all[2]);

        String postName=mDataset2.get(position).getTitle();
        TextView postTitle= cardView.findViewById(R.id.commentPost);
        postTitle.setText(postName);

    }

    @Override
    public int getItemCount(){
        return mDataset.size();
    }

}
