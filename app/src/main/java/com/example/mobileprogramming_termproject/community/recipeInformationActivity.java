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
import com.example.mobileprogramming_termproject.Member.MemberInfo;
import com.example.mobileprogramming_termproject.R;
import com.example.mobileprogramming_termproject.adapter.recipeAdapter;
import com.example.mobileprogramming_termproject.service.fcm;
import com.example.mobileprogramming_termproject.ui.alarm.DBHelper;
import com.example.mobileprogramming_termproject.writingContent.RecipePostInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
    private final String TAG = "????????? ??????";
    //?????????????????? ??????
    private FirebaseFirestore firebaseFirestore;
    //??????????????? ??? ??????
    private RecyclerView recom_recipe;
    //????????? ?????? ???????????? ??????
    private RecipePostInfo recipePostInfo;
    //?????? ?????? ????????????
    private MemberInfo memberInfo;
    //?????? ??????
    private ImageButton RecomBtn;
    //????????? ??????
    private ImageButton BookmarkBtn;
    //
    private DocumentReference dr;

    private FirebaseAuth firebaseAuth;

 //fcm ??????
    fcm fcm2 = new fcm();
//DB ??????
    private DBHelper mDBHelper;


    //???????????????????????? ?????? ?????? ?????????????????? ??????.
    FirebaseUser firebaseUser;
    //?????? ?????????
    String user;
    //????????? ?????????
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_information);

        mDBHelper=new DBHelper(this);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //?????????????????? ?????? ??????
        user = firebaseUser.getUid();


    }
//????????? ????????????
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.recipeRecomBtn:

                    ArrayList<String> newRecomUserId = new ArrayList<>();
                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //?????????????????? ?????? ??????
                    user = firebaseUser.getUid();
                    id = recipePostInfo.getRecipeId();
                    //                    ????????? ?????? ????????????????????????

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
                    //                        ????????? ???????????? ??????

                    else{
                        recipePostInfo.setRecom(recipePostInfo.getRecom() + 1);
                        newRecomUserId = recipePostInfo.getRecomUserId();
                        newRecomUserId.add(user);
                        recipePostInfo.setRecomUserId(newRecomUserId);

                        fcm2.sendMessage(recipePostInfo.getPublisher(),recipePostInfo.getTitle()+" ???????????? ?????????????????????.",firebaseUser.getEmail()+"?????? ??????");
                        String sendTitle=recipePostInfo.getTitle()+" ???????????? ????????????????????? .";
                        String sendText=recipePostInfo.getUserName()+"??? ?????????";
                        String currentTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();

                        mDBHelper.InsertAlarm(sendTitle,sendText,user,currentTime);
//                                    DB ??????


                        if(id == null){
                            dr = firebaseFirestore.collection("recipePost").document();

                        }else{
                            dr =firebaseFirestore.collection("recipePost").document(id);

                        }
                        dbUploader(dr, recipePostInfo, 1);
                        break;
                    }
                case R.id.bookMarkBtn:
                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //?????????????????? ?????? ??????
                    user = firebaseUser.getUid();
                    id = recipePostInfo.getRecipeId();
                    firebaseFirestore.collection("users").document(user).get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    Log.d(TAG, "???????????? ??????");
                                    if (task.isSuccessful()) {
                                        ArrayList<String> bookmarkRecipe = new ArrayList<>();
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            MemberInfo userInfo = new MemberInfo(
                                                    document.getData().get("name").toString(),
                                                    document.getData().get("phoneNumber").toString(),
                                                    document.getData().get("adress").toString(),
                                                    document.getData().get("date").toString(),
                                                    document.getData().get("photoUrl").toString(),
                                                    document.getData().get("nickname").toString(),
                                                    (ArrayList<String>) document.getData().get("bookmarkRecipe"),
                                                    document.getData().get("token").toString()
                                            );
                                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                            if(userInfo.getBookmarkRecipe()==null){
                                                bookmarkRecipe.add(id);
                                                userInfo.setBookmarkRecipe(bookmarkRecipe);
                                                if(user == null){
                                                    dr = firebaseFirestore.collection("users").document();

                                                }else{
                                                    dr =firebaseFirestore.collection("users").document(user);
                                                }
                                                Log.d(TAG, "?????? ????????? : " + user);
                                                dbUploader(dr, bookmarkRecipe, 0);
                                            }

                                            else if(userInfo.getBookmarkRecipe().contains(id))
                                            {
                                                bookmarkRecipe = userInfo.getBookmarkRecipe();
                                                bookmarkRecipe.remove(id);
                                                userInfo.setBookmarkRecipe(bookmarkRecipe);
                                                if(user == null){
                                                    dr = firebaseFirestore.collection("users").document();

                                                }else{
                                                    dr =firebaseFirestore.collection("users").document(user);

                                                }
                                                Log.d(TAG, "?????? ????????? : " + user);
                                                dbUploader(dr, bookmarkRecipe, 0);
                                            }
                                            else{
                                                bookmarkRecipe = userInfo.getBookmarkRecipe();
                                                bookmarkRecipe.add(id);
                                                userInfo.setBookmarkRecipe(bookmarkRecipe);
                                                if(user == null){
                                                    dr = firebaseFirestore.collection("users").document();

                                                }else{
                                                    dr =firebaseFirestore.collection("users").document(user);

                                                }
                                                Log.d(TAG, "?????? ????????? : " + user);
                                                dbUploader(dr, bookmarkRecipe, 1);
                                            }
                                        } else {
                                            Log.d(TAG, "No such document");
                                        }
                                    } else {
                                        Log.d(TAG, "get failed with ", task.getException());
                                    }
                                }
                            });
                    break;
            }
        }
    };

    //???????????? ???????????? ??????????????? ??????????????????, ?????? ???????????? ??????????????? ?????? ???????????? ???????????? ????????? ?????? resume????????? ?????? ??????.

    @Override
    protected void onResume() {
        super.onResume();

        firebaseFirestore= FirebaseFirestore.getInstance();//?????????????????? ??????

        //????????? ?????? ????????? ????????? ?????????????????? ???????????? ??????.
        TextView isRecom = findViewById(R.id.isRecomText);
        RecomBtn = findViewById(R.id.recipeRecomBtn);
        RecomBtn.setOnClickListener(onClickListener);
        BookmarkBtn = findViewById(R.id.bookMarkBtn);
        BookmarkBtn.setOnClickListener(onClickListener);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //?????????????????? ?????? ??????
        user = firebaseUser.getUid();

        //????????? data ?????????
        recipePostInfo = (RecipePostInfo) getIntent().getSerializableExtra("recipePostInfo");
        //????????? ?????? ??????
        ArrayList<String> recomUser = recipePostInfo.getRecomUserId();
        //?????? ?????? ????????? ???????????? ?????? ????????? ????????????
        if(recomUser.contains(user))
        {
            RecomBtn.setImageTintList(ColorStateList.valueOf(Color.RED));
            isRecom.setText("?????? ????????? ???????????????!");

        }
        //????????? ????????????
        else{
            RecomBtn.setImageTintList(ColorStateList.valueOf(Color.BLACK));
            isRecom.setText("??? ???????????? ????????? ????????? ???????????????!");

        }

        //?????? ?????????????????? ???????????? ????????? ??????????????? ????????? ?????????????????? ???????????? ??????.
        //?????????????????????????????? ?????? ?????????.
        firebaseFirestore.collection("users").document(user).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Log.d(TAG, "???????????? ??????");
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                MemberInfo userInfo = new MemberInfo(
                                        document.getData().get("name").toString(),
                                        document.getData().get("phoneNumber").toString(),
                                        document.getData().get("adress").toString(),
                                        document.getData().get("date").toString(),
                                        document.getData().get("photoUrl").toString(),
                                        document.getData().get("nickname").toString(),
                                        (ArrayList<String>) document.getData().get("bookmarkRecipe"),
                                        document.getData().get("token").toString()
//????????? ??????????????????
                                );
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                                if(userInfo.getBookmarkRecipe()==null)
                                {

                                }
                                //?????? ??????????????? ????????? ????????????
                                else if(userInfo.getBookmarkRecipe().contains(recipePostInfo.getRecipeId())){
                                    BookmarkBtn.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                                    BookmarkBtn.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
                                }
                                //?????? ??????????????? ???????????? ????????????
                                else{
                                    BookmarkBtn.setImageTintList(ColorStateList.valueOf(Color.BLACK));
                                    BookmarkBtn.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                                }
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });


        //????????? ?????? ????????? ?????? ??????
        //??????
        String id = getIntent().getStringExtra("id");
        Log.d("??????: ", "" + getIntent().getStringExtra("id"));

        ImageView recipeInfoTitleImage = findViewById(R.id.titleImage);

        String infoTitleImagePath = recipePostInfo.getTitleImage();
        if(isStorageUrl(infoTitleImagePath)){
            Glide.with(this).load(infoTitleImagePath).override(1000).thumbnail(0.1f).into(recipeInfoTitleImage);
        }
        Log.d("??????","" + recipePostInfo.getTitleImage());

        TextView recipeInfoTitle = findViewById(R.id.recipeIntoTitle);
        recipeInfoTitle.setText(recipePostInfo.getTitle());
        Log.d("??????","" + recipePostInfo.getTitle());

        TextView recipeRecomNum = findViewById(R.id.recomNumber);
        recipeRecomNum.setText(Integer.toString((int) recipePostInfo.getRecom()));
        Log.d("??????","" + recipePostInfo.getRecom());

        TextView recipeCreatedAt = findViewById(R.id.recipeInfoCreatedAt);
        recipeCreatedAt.setText(new SimpleDateFormat("MM-dd hh:mm", Locale.KOREA).format(recipePostInfo.getCreatedAt()));
        Log.d("??????","" + recipePostInfo.getCreatedAt());

        TextView recipeIngredient = findViewById(R.id.editRecipe_ingredient);
        recipeIngredient.setText(recipePostInfo.getIngredient());

        TextView recipePrice = findViewById(R.id.recipeInfoCost);
        recipePrice.setText(Long.toString(recipePostInfo.getPrice()));


        LinearLayout recipeContentLayout = findViewById(R.id.recipeContentLayout);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ArrayList<String> recipeContentList = recipePostInfo.getContent();
        Log.d("??????","" + recipePostInfo.getContent());
        if(recipeContentLayout.getChildCount()==0){

            for(int i = 0 ; i < recipeContentList.size() ; i++){
                Log.d("??????","" + recipeContentList.get(i));
                String recipeContent = recipeContentList.get(i);
                if(isStorageUrl(recipeContent)){
                    ImageView imageView = new ImageView(this);
                    imageView.setLayoutParams(layoutParams);
                    imageView.setAdjustViewBounds(true);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY); //????????? ????????????
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

        //???

        //?????? ??????????????? ????????? ?????? ??????
        //??????

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
                                Log.d("??????: ", document.getId() + " => " + document.getData());
                                recipe_postList.add(new RecipePostInfo(
                                        document.getData().get("titleImage").toString(),
                                        document.getData().get("title").toString(),
                                        document.getData().get("ingredient").toString(),
                                        (ArrayList<String>) document.getData().get("content"),
                                        document.getData().get("publisher").toString(),
                                        document.getData().get("userName").toString(),
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
                            Log.d("??????: ", "Error getting documents: ", task.getException());

                        }
                    }
                });

        //???
    }
    //??????, ?????? ????????? ???????????? ????????????????????? ??????????????? ????????????.
        private void dbUploader(DocumentReference documentReference , RecipePostInfo recipePostInfo, int requestCode){
            //???????????? ?????? ??????
            if(requestCode == 0){
            documentReference.set(recipePostInfo)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showToast(recipeInformationActivity.this ,"????????? ???????????????!");
                            Log.w(TAG,"Success writing document" + documentReference.getId());
                            onResume();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showToast(recipeInformationActivity.this ,"??????????????? ???????????????!");
                    Log.w(TAG,"Error writing document", e);
                }
            });
        }
            //????????? ??????
            else{
            documentReference.set(recipePostInfo)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showToast(recipeInformationActivity.this ,"??? ???????????? ???????????????!");
                            Log.w(TAG,"Success writing document" + documentReference.getId());
                            String meesage="hi";
//                            ((MainActivity)MainActivity.mContext).sendPostToFCM(data,meesage);
                            onResume();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showToast(recipeInformationActivity.this ,"????????? ????????? ???????????????!");
                    Log.w(TAG,"Error writing document", e);
                }
            });
        }

    }


    //????????????, ???????????? ????????? ???????????? ????????????????????? ??????????????? ????????????.
    private void dbUploader(DocumentReference documentReference , ArrayList<String> bookmarkReceipe, int requestCode){
        //???????????? ?????? ?????? ??????
        if(requestCode == 0){
            documentReference.update("bookmarkRecipe",bookmarkReceipe)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showToast(recipeInformationActivity.this ,"??????????????? ???????????????!");
                            Log.w(TAG,"Success writing document" + documentReference.getId());
                            onResume();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showToast(recipeInformationActivity.this ,"??????????????? ????????? ???????????????!");
                    Log.w(TAG,"Error writing document", e);
                }
            });
        }
        //??????????????? ??????
        else{
            documentReference.update("bookmarkRecipe",bookmarkReceipe)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showToast(recipeInformationActivity.this ,"??? ???????????? ?????????????????????!");
                            Log.w(TAG,"Success writing document" + documentReference.getId());
                            onResume();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showToast(recipeInformationActivity.this ,"??????????????? ???????????????!");
                    Log.w(TAG,"Error writing document", e);
                }
            });
        }

    }


}



