package com.example.mobileprogramming_termproject.community;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileprogramming_termproject.R;
import com.example.mobileprogramming_termproject.writingContent.FreePostInfo;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

//자유게시판의 글을 카드뷰로 보여주기 위한 어댑터
public class freeAdapter extends RecyclerView.Adapter<freeAdapter.freeViewHolder> {
    //자유게시판 글 데이터
    private ArrayList<FreePostInfo> mDataset;
    private Activity activity;

    static class freeViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;
        freeViewHolder(Activity activity, CardView v, FreePostInfo freePostInfo){
            super(v);
            cardView = v;
        }
    }

    public freeAdapter(Activity activity, ArrayList<FreePostInfo> freeDataset){
        mDataset = freeDataset;
        this.activity = activity;
    }

    //카드뷰를 생성하여 그곳에 데이터를 집어넣어 완성시킴
    @NotNull
    @Override
    public freeAdapter.freeViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType){
        CardView cardView =(CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_free_post, parent,false);
        final freeViewHolder freeViewHolder = new freeViewHolder(activity, cardView, mDataset.get(viewType));
        //카드뷰를 클릭할경우, 그 게시글로 activity가 넘어감.
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(activity, freeInformationActivity.class);
                intent.putExtra("freePostInfo", mDataset.get(freeViewHolder.getAdapterPosition()));
                activity.startActivity(intent);
            }
        });
        return freeViewHolder;
    }

    //카드뷰 안에 들어갈 목록
    //자유게시판 게시글 카드뷰에는 제목, 작성자, 작성 날짜, 추천수가 저장되어 띄워짐.
    @Override
    public void onBindViewHolder(@NotNull final freeViewHolder holder, int position){

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        CardView cardView = holder.cardView;
        cardView.setLayoutParams(layoutParams);

        TextView title = cardView.findViewById(R.id.freeTitle);
        title.setText(mDataset.get(position).getTitle());

        TextView createdAt = cardView.findViewById(R.id.freeCreatedAt);
        createdAt.setText(new SimpleDateFormat("MM-dd hh:mm", Locale.KOREA).format(mDataset.get(position).getCreatedAt()));

        TextView freePublisher = cardView.findViewById(R.id.freePublisher);
        freePublisher.setText(mDataset.get(position).getUserName());

        TextView recom = cardView.findViewById(R.id.freeRecom);
        recom.setText("추천수 : " + (int) mDataset.get(position).getRecom());

    }

    @Override
    public int getItemCount(){
        return mDataset.size();
    }

}
