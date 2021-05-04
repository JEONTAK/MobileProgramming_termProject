package com.example.mobileprogramming_termproject.community;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.mobileprogramming_termproject.R;
import com.example.mobileprogramming_termproject.writingContent.RecipePostInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.example.mobileprogramming_termproject.Util.isStorageUrl;
import static com.example.mobileprogramming_termproject.Util.showToast;


public class recipeInformationActivity extends AppCompatActivity {
    private final String TAG = "레시피 정보";
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView recom_recipe;
    private RecipePostInfo recipePostInfo;
    private ImageButton RecomBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_information);


    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.recipeRecomBtn:
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //파이어베이스 유저 선언
                    String user = firebaseUser.getUid();
                    String id = recipePostInfo.getRecipeId();
                    ArrayList<String> newRecomUserId = new ArrayList<>();
                    DocumentReference dr;
                    if(recipePostInfo.getRecomUserId().contains(user))
                    {
                        recipePostInfo.setRecom(recipePostInfo.getRecom() - 1);
                        newRecomUserId = recipePostInfo.getRecomUserId();
                        newRecomUserId.remove(user);
                        recipePostInfo.setRecomUserId(newRecomUserId);
                        if(id == null){
                            dr = firebaseFirestore.collection("recipePost").document();

                        }else{
                            dr =firebaseFirestore.collection("recipePost").document(id);

                        }
                        dbUploader(dr, recipePostInfo, 0);
                        break;
                    }
                    else{
                        recipePostInfo.setRecom(recipePostInfo.getRecom() + 1);
                        newRecomUserId = recipePostInfo.getRecomUserId();
                        newRecomUserId.add(user);
                        recipePostInfo.setRecomUserId(newRecomUserId);
                        if(id == null){
                            dr = firebaseFirestore.collection("recipePost").document();

                        }else{
                            dr =firebaseFirestore.collection("recipePost").document(id);

                        }
                        dbUploader(dr, recipePostInfo, 1);
                        break;
                    }

            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        TextView isRecom = findViewById(R.id.isRecomText);
        RecomBtn = findViewById(R.id.recipeRecomBtn);
        RecomBtn.setOnClickListener(onClickListener);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //파이어베이스 유저 선언
        String user = firebaseUser.getUid();
        recipePostInfo = (RecipePostInfo) getIntent().getSerializableExtra("recipePostInfo");

        ArrayList<String> recomUser = recipePostInfo.getRecomUserId();
        if(recomUser.contains(user))
        {
            RecomBtn.setImageTintList(ColorStateList.valueOf(Color.RED));
            isRecom.setText("이미 추천한 레시피에요!");

        }
        else{
            RecomBtn.setImageTintList(ColorStateList.valueOf(Color.BLACK));
            isRecom.setText("이 레시피가 좋다면 추천을 눌러주세요!");

        }

        String id = getIntent().getStringExtra("id");
        Log.d("로그: ", "" + getIntent().getStringExtra("id"));

        ImageView recipeInfoTitleImage = findViewById(R.id.titleImage);

        String infoTitleImagePath = recipePostInfo.getTitleImage();
        if(isStorageUrl(infoTitleImagePath)){
            Glide.with(this).load(infoTitleImagePath).override(1000).thumbnail(0.1f).into(recipeInfoTitleImage);
        }
        Log.d("로그","" + recipePostInfo.getTitleImage());

        TextView recipeInfoTitle = findViewById(R.id.recipeIntoTitle);
        recipeInfoTitle.setText(recipePostInfo.getTitle());
        Log.d("로그","" + recipePostInfo.getTitle());

        TextView recipeRecomNum = findViewById(R.id.recomNumber);
        recipeRecomNum.setText(Integer.toString((int) recipePostInfo.getRecom()));
        Log.d("로그","" + recipePostInfo.getRecom());

        TextView recipeCreatedAt = findViewById(R.id.recipeInfoCreatedAt);
        recipeCreatedAt.setText(new SimpleDateFormat("MM-dd hh:mm", Locale.KOREA).format(recipePostInfo.getCreatedAt()));
        Log.d("로그","" + recipePostInfo.getCreatedAt());

        TextView recipeIngredient = findViewById(R.id.editRecipe_ingredient);
        recipeIngredient.setText(recipePostInfo.getIngredient());

        TextView recipePrice = findViewById(R.id.recipeInfoCost);
        recipePrice.setText(Long.toString(recipePostInfo.getPrice()));


        LinearLayout recipeContentLayout = findViewById(R.id.recipeContentLayout);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ArrayList<String> recipeContentList = recipePostInfo.getContent();
        Log.d("로그","" + recipePostInfo.getContent());
        if(recipeContentLayout.getChildCount()==0){

            for(int i = 0 ; i < recipeContentList.size() ; i++){
                Log.d("로그","" + recipeContentList.get(i));
                String recipeContent = recipeContentList.get(i);
                if(isStorageUrl(recipeContent)){
                    ImageView imageView = new ImageView(this);
                    imageView.setLayoutParams(layoutParams);
                    //imageView.setAdjustViewBounds(true);
                    //imageView.setScaleType(ImageView.ScaleType.FIT_XY); //이미지 꽉차게함
                    recipeContentLayout.addView(imageView);
                    Glide.with(this).load(recipeContent).override(1000).thumbnail(0.1f).into(imageView);
                }else{
                    TextView textView = new TextView(this);
                    textView.setLayoutParams(layoutParams);
                    textView.setText(recipeContent);
                    recipeContentLayout.addView(textView);
                }
            }
        }


        firebaseFirestore= FirebaseFirestore.getInstance();//데이터베이스 선언

        recom_recipe = findViewById(R.id.recom_recipe);
        recom_recipe.setHasFixedSize(true);
        recom_recipe.setLayoutManager(new LinearLayoutManager(recipeInformationActivity.this, LinearLayoutManager.HORIZONTAL, false));
        CollectionReference collectionReference = firebaseFirestore.collection("recipePost");
        collectionReference
                .orderBy("recom", Query.Direction.DESCENDING).limit(4)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<RecipePostInfo> recipe_postList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("로그: ", document.getId() + " => " + document.getData());
                                recipe_postList.add(new RecipePostInfo(
                                        document.getData().get("titleImage").toString(),
                                        document.getData().get("title").toString(),
                                        document.getData().get("ingredient").toString(),
                                        (ArrayList<String>) document.getData().get("content"),
                                        document.getData().get("publisher").toString(),
                                        new Date(document.getDate("createdAt").getTime()),
                                        (Long) document.getData().get("recom"),
                                        document.getData().get("recipeId").toString(),
                                        (ArrayList<String>) document.getData().get("recomUserId"),
                                        (Long) document.getData().get("price"),
                                        document.getData().get("foodCategory").toString(),
                                        document.getData().get("tagCategory").toString()
                                        ));
                            }

                            RecyclerView.Adapter mAdapter = new recipeAdapter(recipeInformationActivity.this, recipe_postList);
                            recom_recipe.setAdapter(mAdapter);
                        } else {
                            Log.d("로그: ", "Error getting documents: ", task.getException());

                        }
                    }
                });


    }

        private void dbUploader(DocumentReference documentReference , RecipePostInfo recipePostInfo, int requestCode){
        if(requestCode == 0){
            documentReference.set(recipePostInfo)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showToast(recipeInformationActivity.this ,"추천을 취소했어요!");
                            Log.w(TAG,"Success writing document" + documentReference.getId());
                            onResume();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showToast(recipeInformationActivity.this ,"추천취소에 실패했어요!");
                    Log.w(TAG,"Error writing document", e);
                }
            });
        }else{
            documentReference.set(recipePostInfo)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showToast(recipeInformationActivity.this ,"이 레시피을 추천했어요!");
                            Log.w(TAG,"Success writing document" + documentReference.getId());
                            onResume();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showToast(recipeInformationActivity.this ,"레시피 추천에 실패했어요!");
                    Log.w(TAG,"Error writing document", e);
                }
            });
        }

    }


}



