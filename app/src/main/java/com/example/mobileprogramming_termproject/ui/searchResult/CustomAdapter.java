package com.example.mobileprogramming_termproject.ui.searchResult;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobileprogramming_termproject.R;
import com.example.mobileprogramming_termproject.community.recipeInformationActivity;
import com.example.mobileprogramming_termproject.writingContent.FreePostInfo;
import com.example.mobileprogramming_termproject.writingContent.RecipePostInfo;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private ArrayList<RecipePostInfo> arrayList;
    private Context context;
    private Activity activity;
    //어댑터에서 액티비티 액션을 가져올 때 context가 필요한데 어댑터에는 context가 없다.
    //선택한 액티비티에 대한 context를 가져올 때 필요하다.

    public CustomAdapter(Activity activity,ArrayList<RecipePostInfo> arrayList) {
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    //실제 리스트뷰가 어댑터에 연결된 다음에 뷰 홀더를 최초로 만들어낸다.
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        //카드뷰를 클릭할경우, 그 게시글로 activity가 넘어감.
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(activity, recipeInformationActivity.class);
                intent.putExtra("recipePostInfo", arrayList.get(holder.getAdapterPosition()));
                activity.startActivity(intent);

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getTitleImage())
                .into(holder.iv_image);
        holder.tv_title.setText(arrayList.get(position).getTitle());
        holder.tv_likeNum.setText(String.valueOf(arrayList.get(position).getRecom()));
    }

    @Override
    public int getItemCount() {
        // 삼항 연산자
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_image;
        TextView tv_title;
        TextView tv_likeNum;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_image = itemView.findViewById(R.id.iv_image);
            this.tv_title = itemView.findViewById(R.id.tv_title);
            this.tv_likeNum = itemView.findViewById(R.id.tv_likeNum);
        }
    }
}
