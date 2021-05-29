package com.example.mobileprogramming_termproject.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobileprogramming_termproject.Member.MemberInfo;
import com.example.mobileprogramming_termproject.R;
import com.example.mobileprogramming_termproject.community.recipeInformationActivity;
import com.example.mobileprogramming_termproject.ui.myPage.myCommentActivity;
import com.example.mobileprogramming_termproject.writingContent.FreePostInfo;
import com.example.mobileprogramming_termproject.writingContent.RecipePostInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.content.ContentValues.TAG;
import static com.example.mobileprogramming_termproject.Util.isStorageUrl;
import static com.example.mobileprogramming_termproject.Util.showToast;

//레시피 게시판의 글을 카드뷰로 보여주기 위한 어댑터
public class recipeAdapter extends RecyclerView.Adapter<recipeAdapter.recipeViewHolder> {
    //레시피게시판 글 데이터
    private ArrayList<RecipePostInfo> mDataset;
    private Activity activity;
    private FirebaseFirestore firebaseFirestore;
    //파이어베이스에서 유저 정보 가져오기위해 선언.
    FirebaseUser firebaseUser;
    //유저 아이디
    String user;
    //레시피 아이디
    String id;




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

    //카드뷰를 생성하여 그곳에 데이터를 집어넣어 완성시킴
    @NotNull
    @Override
    public recipeAdapter.recipeViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType){
        CardView cardView =(CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe_post, parent,false);
        final recipeViewHolder recipeViewHolder = new recipeViewHolder(activity, cardView, mDataset.get(viewType));
        //카드뷰를 클릭할경우, 그 게시글로 activity가 넘어감.
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

    //카드뷰 안에 들어갈 목록
    //레시피게시판 게시글 카드뷰에는 제목, 타이틀 이미지 , 작성자, 작성 날짜, 추천수가 저장되어 띄워짐.
    @Override
    public void onBindViewHolder(@NotNull final recipeViewHolder holder, int position){
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        CardView cardView = holder.cardView;
        cardView.setLayoutParams(layoutParams);
        ImageView titleImage = cardView.findViewById(R.id.recipeTitleImage);
        String titleImagePath = mDataset.get(position).getTitleImage();
        if(isStorageUrl(titleImagePath)){
            Glide.with(activity).load(titleImagePath).centerCrop().into(titleImage);
        }

        ImageView profileImage=cardView.findViewById(R.id.profileImageVIew2);
        String publisher=mDataset.get(position).getPublisher();

        firebaseFirestore= FirebaseFirestore.getInstance();
        DocumentReference dr = firebaseFirestore.collection("users").document(publisher);
        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                       String photoURL=document.getData().get("photoUrl").toString();
                       Glide.with(activity).load(photoURL).centerCrop().into(profileImage);
                    }
                }
            }
        });

        TextView recipetag=cardView.findViewById(R.id.RecipeTag);
        recipetag.setText("#"+(mDataset.get(position).getTagCategory()));

        TextView title = cardView.findViewById(R.id.recipeTitle);
        title.setText(mDataset.get(position).getTitle());

        TextView userName = cardView.findViewById(R.id.recipePublisher);
        userName.setText(mDataset.get(position).getUserName());

        TextView createdAt = cardView.findViewById(R.id.recipeCreatedAt);
        createdAt.setText(new SimpleDateFormat("MM-dd hh:mm", Locale.KOREA).format(mDataset.get(position).getCreatedAt()));

        TextView recom = cardView.findViewById(R.id.recipeRecom);
        recom.setText("추천수 : " + (int) mDataset.get(position).getRecom());

//        Button scrap=cardView.findViewById(R.id.ScrapButton);
//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //파이어베이스 유저 선언
//        user = firebaseUser.getUid();
//        id = mDataset.get(position) .getRecipeId();
//        firebaseFirestore.collection("users").document(user).get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        DocumentReference dr;
//                        Log.d(TAG, "다큐먼트 실행");
//                        if (task.isSuccessful()) {
//                            ArrayList<String> bookmarkRecipe = new ArrayList<>();
//                            DocumentSnapshot document = task.getResult();
//                            if (document.exists()) {
//                                MemberInfo userInfo = new MemberInfo(
//                                        document.getData().get("name").toString(),
//                                        document.getData().get("phoneNumber").toString(),
//                                        document.getData().get("adress").toString(),
//                                        document.getData().get("date").toString(),
//                                        document.getData().get("photoUrl").toString(),
//                                        document.getData().get("nickname").toString(),
//                                        (ArrayList<String>) document.getData().get("bookmarkRecipe"),
//                                        document.getData().get("token").toString()
//                                );
//                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//                                if (userInfo.getBookmarkRecipe() == null) {
//                                    scrap.setText("스크랩");
//                                } else if (userInfo.getBookmarkRecipe().contains(id)) {
//                                    scrap.setText("스크랩 완료");
//                                } else {
//                                    scrap.setText("스크랩");
//                                }
//
//                            }
//                        }
//                    }
//                });
//
//        scrap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //파이어베이스 유저 선언
//                user = firebaseUser.getUid();
//                id = mDataset.get(position) .getRecipeId();
//                firebaseFirestore.collection("users").document(user).get()
//                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                DocumentReference dr;
//                                Log.d(TAG, "다큐먼트 실행");
//                                if (task.isSuccessful()) {
//                                    ArrayList<String> bookmarkRecipe = new ArrayList<>();
//                                    DocumentSnapshot document = task.getResult();
//                                    if (document.exists()) {
//                                        MemberInfo userInfo = new MemberInfo(
//                                                document.getData().get("name").toString(),
//                                                document.getData().get("phoneNumber").toString(),
//                                                document.getData().get("adress").toString(),
//                                                document.getData().get("date").toString(),
//                                                document.getData().get("photoUrl").toString(),
//                                                document.getData().get("nickname").toString(),
//                                                (ArrayList<String>) document.getData().get("bookmarkRecipe"),
//                                                document.getData().get("token").toString()
//                                        );
//                                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//                                        if(userInfo.getBookmarkRecipe()==null){
//                                            bookmarkRecipe.add(id);
//                                            userInfo.setBookmarkRecipe(bookmarkRecipe);
//                                            dr =firebaseFirestore.collection("users").document(user);
//                                            Log.d(TAG, "유저 아이디 : " + user);
//                                            dbUploader(dr, bookmarkRecipe, 0);
//                                            scrap.setText("스크랩 완료");
//                                        }
//                                        else if(userInfo.getBookmarkRecipe().contains(id))
//                                        {
//                                            bookmarkRecipe = userInfo.getBookmarkRecipe();
//                                            bookmarkRecipe.remove(id);
//                                            userInfo.setBookmarkRecipe(bookmarkRecipe);
//
//                                            dr =firebaseFirestore.collection("users").document(user);
//
//                                            Log.d(TAG, "유저 아이디 : " + user);
//                                            dbUploader(dr, bookmarkRecipe, 0);
//                                            scrap.setText("스크랩");
//                                        }
//                                        else{
//                                            bookmarkRecipe = userInfo.getBookmarkRecipe();
//                                            bookmarkRecipe.add(id);
//                                            userInfo.setBookmarkRecipe(bookmarkRecipe);
//                                            dr =firebaseFirestore.collection("users").document(user);
//                                            Log.d(TAG, "유저 아이디 : " + user);
//                                            dbUploader(dr, bookmarkRecipe, 1);
//                                        }
//                                    } else {
//                                        Log.d(TAG, "No such document");
//                                    }
//                                } else {
//                                    Log.d(TAG, "get failed with ", task.getException());
//                                }
//                            }
//                        });
//            }
//        });


    }

    @Override
    public int getItemCount(){
        return mDataset.size();
    }


//    //즐겨찾기, 즐겨찾기 취소시 바로바로 데이터베이스에 업로드하여 반영해줌.
//    private void dbUploader(DocumentReference documentReference , ArrayList<String> bookmarkReceipe, int requestCode){
//        //즐겨찾기 취소 하는 경우
//        if(requestCode == 0){
//            documentReference.update("bookmarkRecipe",bookmarkReceipe)
//                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            showToast(activity ,"즐겨찾기를 삭제했어요!");
//                            Log.w(TAG,"Success writing document" + documentReference.getId());
//                            Intent intent=new Intent(activity,activity.getClass());
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            activity.startActivity(intent);
//
//
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    showToast(activity ,"즐겨찾기를 취소에 실패했어요!");
//                    Log.w(TAG,"Error writing document", e);
//
//                }
//            });
//        }
//        //즐겨찾기할 경우
//        else{
//            documentReference.update("bookmarkRecipe",bookmarkReceipe)
//                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            showToast(activity ,"즐겨찾기에 추가했어요!");
//                            Log.w(TAG,"Success writing document" + documentReference.getId());
//                            Intent intent=new Intent(activity,activity.getClass());
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            activity.startActivity(intent);
//
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    showToast(activity ,"즐겨찾기를 추가에 실패했어요!");
//                    Log.w(TAG,"Error writing document", e);
//
//                }
//            });
//        }
//
//    }


}
