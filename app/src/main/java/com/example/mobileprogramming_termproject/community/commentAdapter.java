package com.example.mobileprogramming_termproject.community;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.example.mobileprogramming_termproject.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class commentAdapter extends RecyclerView.Adapter<commentAdapter.commentViewHolder> {
    private ArrayList<String> mDataset;
    private Activity activity;


    static class commentViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;
        commentViewHolder(CardView v){
            super(v);
            cardView = v;
        }
    }

    public commentAdapter(Activity activity, ArrayList<String> commentDataset){
        mDataset = commentDataset;
        this.activity = activity;
    }


    @NotNull
    @Override
    public commentAdapter.commentViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType){
        CardView cardView =(CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_post, parent,false);
        final commentAdapter.commentViewHolder commentViewHolder = new commentAdapter.commentViewHolder(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        return commentViewHolder;
    }

    @Override
    public void onBindViewHolder(@NotNull final commentAdapter.commentViewHolder holder, int position){

        String[] all = mDataset.get(position).split("//");
        CardView cardView = holder.cardView;
        TextView title = cardView.findViewById(R.id.commentContent);
        title.setText(all[0]);

        TextView publisher = cardView.findViewById(R.id.commentPublisher);
        publisher.setText(all[2]);

    }

    @Override
    public int getItemCount(){
        return mDataset.size();
    }

}
