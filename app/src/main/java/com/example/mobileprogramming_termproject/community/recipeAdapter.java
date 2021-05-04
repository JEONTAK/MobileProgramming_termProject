package com.example.mobileprogramming_termproject.community;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobileprogramming_termproject.R;
import com.example.mobileprogramming_termproject.writingContent.RecipePostInfo;


import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import static com.example.mobileprogramming_termproject.Util.isStorageUrl;


public class recipeAdapter extends RecyclerView.Adapter<recipeAdapter.recipeViewHolder> {
    private ArrayList<RecipePostInfo> mDataset;
    private Activity activity;


    static class recipeViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;
        recipeViewHolder(Activity activity, CardView v, RecipePostInfo recipePostInfo){
            super(v);
            cardView = v;
        }
    }

    public recipeAdapter(Activity activity, ArrayList<RecipePostInfo> recipeDataset){
        mDataset = recipeDataset;
        this.activity = activity;
    }

    @Override
    public int getItemViewType(int position){
        return position;
    }

    @NotNull
    @Override
    public recipeAdapter.recipeViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType){
        CardView cardView =(CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe_post, parent,false);
        final recipeViewHolder recipeViewHolder = new recipeViewHolder(activity, cardView, mDataset.get(viewType));
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(activity, recipeInformationActivity.class);
                intent.putExtra("recipePostInfo", mDataset.get(recipeViewHolder.getAdapterPosition()));
                activity.startActivity(intent);

            }
        });
        return recipeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NotNull final recipeViewHolder holder, int position){
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        CardView cardView = holder.cardView;
        cardView.setLayoutParams(layoutParams);
        ImageView titleImage = cardView.findViewById(R.id.recipeTitleImage);
        String titleImagePath = mDataset.get(position).getTitleImage();
        if(isStorageUrl(titleImagePath)){
            Glide.with(activity).load(titleImagePath).override(1000).thumbnail(0.1f).into(titleImage);
        }
        TextView title = cardView.findViewById(R.id.recipeTitle);
        title.setText(mDataset.get(position).getTitle());

        TextView publisher = cardView.findViewById(R.id.recipePublisher);
        publisher.setText(mDataset.get(position).getPublisher());

        TextView createdAt = cardView.findViewById(R.id.recipeCreatedAt);
        createdAt.setText(new SimpleDateFormat("MM-dd hh:mm", Locale.KOREA).format(mDataset.get(position).getCreatedAt()));

        TextView recom = cardView.findViewById(R.id.recipeRecom);
        recom.setText("추천수 : " + (int) mDataset.get(position).getRecom());

    }

    @Override
    public int getItemCount(){
        return mDataset.size();
    }

}
