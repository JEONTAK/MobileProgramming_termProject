package com.example.mobileprogramming_termproject.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileprogramming_termproject.R;
import com.example.mobileprogramming_termproject.community.freeInformationActivity;
import com.example.mobileprogramming_termproject.listener.OnCommentListener;
import com.example.mobileprogramming_termproject.listener.OnPostListener;
import com.example.mobileprogramming_termproject.writingContent.FreePostInfo;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

//자유게시판에서 댓글을 가져오기 위한 어댑터
public class myCommentAdapter extends RecyclerView.Adapter<myCommentAdapter.mycommentViewHolder> {
    //게시글의 댓글을 저장하기 위한 arraylist
    private ArrayList<String> mDataset;
    private ArrayList<FreePostInfo> mDataset2;
    private Activity activity;

    private OnCommentListener onCommentListener;

    static class mycommentViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;
        mycommentViewHolder(CardView v){
            super(v);
            cardView = v;
        }
    }

    public myCommentAdapter(Activity activity, ArrayList<String> commentDataset, ArrayList<FreePostInfo> postDataset){
        mDataset = commentDataset;
        mDataset2= postDataset;
        this.activity = activity;
    }

    public void setOnCommentListener(OnCommentListener onCommentListener){
        this.onCommentListener = onCommentListener;
    }

    //카드뷰를 생성하여 그곳에 데이터를 집어넣어 완성시킴
    @NotNull
    @Override
    public myCommentAdapter.mycommentViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType){
        CardView cardView =(CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mycomment_post, parent,false);
        final myCommentAdapter.mycommentViewHolder mycommentViewHolder = new myCommentAdapter.mycommentViewHolder(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(activity, freeInformationActivity.class);
                intent.putExtra("freePostInfo", mDataset2.get(mycommentViewHolder.getAdapterPosition()));
                activity.startActivity(intent);
            }
        });

        cardView.findViewById(R.id.menuBtn_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v, mycommentViewHolder.getAdapterPosition());
            }
        });

        return mycommentViewHolder;
    }

    //카드뷰 안에 들어갈 목록
    //댓글 카드뷰에는 댓글 내용과 작성자가 들어감.
    @Override
    public void onBindViewHolder(@NotNull final myCommentAdapter.mycommentViewHolder holder, int position){

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

    public void showPopup(View v, int position){
        PopupMenu popup = new PopupMenu(activity, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.delete:
                        onCommentListener.onDelete(position);
                        return true;
                    default:
                        return false;
                }
            }
        });

        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.commentmenu, popup.getMenu());
        popup.show();
    }

}
