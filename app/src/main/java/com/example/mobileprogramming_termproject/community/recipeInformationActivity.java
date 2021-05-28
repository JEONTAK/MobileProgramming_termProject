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
import com.google.firebase.auth.UserInfo;
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
    private final String TAG = "레시피 정보";
    //데이터베이스 선언
    private FirebaseFirestore firebaseFirestore;
    //리사이클러 뷰 선언
    private RecyclerView recom_recipe;
    //게시글 정보 가져오기 위함
    private RecipePostInfo recipePostInfo;
    //유저 정보 가져오기
    private MemberInfo memberInfo;
    //추천 버튼
    private ImageButton RecomBtn;
    //북마크 버튼
    private ImageButton BookmarkBtn;
    //
    private DocumentReference dr;

    private FirebaseAuth firebaseAuth;

 //fcm 전송
    fcm fcm2 = new fcm();
//DB 설정
    private DBHelper mDBHelper;


    //파이어베이스에서 유저 정보 가져오기위해 선언.
    FirebaseUser firebaseUser;
    //유저 아이디
    String user;
    //레시피 아이디
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_information);

        mDBHelper=new DBHelper(this);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //파이어베이스 유저 선언
        user = firebaseUser.getUid();


    }
//레시피 추천기능
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.recipeRecomBtn:

                    ArrayList<String> newRecomUserId = new ArrayList<>();
                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //파이어베이스 유저 선언
                    user = firebaseUser.getUid();
                    id = recipePostInfo.getRecipeId();
                    //                    이름이 이미 포함되어있는경우

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
                    //                        새로운 아이디일 경우

                    else{
                        recipePostInfo.setRecom(recipePostInfo.getRecom() + 1);
                        newRecomUserId = recipePostInfo.getRecomUserId();
                        newRecomUserId.add(user);
                        recipePostInfo.setRecomUserId(newRecomUserId);

                        fcm2.sendMessage(recipePostInfo.getPublisher(),recipePostInfo.getTitle()+" 레시피가 추천되었습니다.",firebaseUser.getEmail()+"님의 추천");
                        String sendTitle=recipePostInfo.getTitle()+" 레시피를 추천하였습니다 .";
                        String sendText=recipePostInfo.getUserName()+"의 레시피";
                        String currentTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();

                        mDBHelper.InsertAlarm(sendTitle,sendText,user,currentTime);
//                                    DB 정버


                        if(id == null){
                            dr = firebaseFirestore.collection("recipePost").document();

                        }else{
                            dr =firebaseFirestore.collection("recipePost").document(id);

                        }
                        dbUploader(dr, recipePostInfo, 1);
                        break;
                    }
                case R.id.bookMarkBtn:
                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //파이어베이스 유저 선언
                    user = firebaseUser.getUid();
                    id = recipePostInfo.getRecipeId();
                    firebaseFirestore.collection("users").document(user).get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    Log.d(TAG, "다큐먼트 실행");
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
                                                Log.d(TAG, "유저 아이디 : " + user);
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
                                                Log.d(TAG, "유저 아이디 : " + user);
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
                                                Log.d(TAG, "유저 아이디 : " + user);
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

    //게시글에 추천수가 증가했거나 감소했을경우, 또는 게시글이 작성되었을 경우 바로바로 업데이트 해주기 위해 resume함수에 넣어 관리.

    @Override
    protected void onResume() {
        super.onResume();

        firebaseFirestore= FirebaseFirestore.getInstance();//데이터베이스 선언

        //유저가 이미 추천을 했는지 하지않았는지 알려주기 위함.
        TextView isRecom = findViewById(R.id.isRecomText);
        RecomBtn = findViewById(R.id.recipeRecomBtn);
        RecomBtn.setOnClickListener(onClickListener);
        BookmarkBtn = findViewById(R.id.bookMarkBtn);
        BookmarkBtn.setOnClickListener(onClickListener);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //파이어베이스 유저 선언
        user = firebaseUser.getUid();

        //게시글 data 가져옴
        recipePostInfo = (RecipePostInfo) getIntent().getSerializableExtra("recipePostInfo");
        //추천한 유저 명단
        ArrayList<String> recomUser = recipePostInfo.getRecomUserId();
        //만약 현재 유저가 게시글에 이미 추천을 눌렀다면
        if(recomUser.contains(user))
        {
            RecomBtn.setImageTintList(ColorStateList.valueOf(Color.RED));
            isRecom.setText("이미 추천한 레시피에요!");

        }
        //누르지 않았다면
        else{
            RecomBtn.setImageTintList(ColorStateList.valueOf(Color.BLACK));
            isRecom.setText("이 레시피가 좋다면 추천을 눌러주세요!");

        }

        //현재 어플리케이션 실행중인 유저가 즐겨찾기를 했는지 하지않았는지 확인하기 위함.
        //파이어베이스를통하여 값을 받아옴.
        firebaseFirestore.collection("users").document(user).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Log.d(TAG, "다큐먼트 실행");
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
//토큰을 받아야합니다
                                );
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                                if(userInfo.getBookmarkRecipe()==null)
                                {

                                }
                                //만약 즐겨찾기를 해놓은 상태이면
                                else if(userInfo.getBookmarkRecipe().contains(recipePostInfo.getRecipeId())){
                                    BookmarkBtn.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                                    BookmarkBtn.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
                                }
                                //만약 즐겨찾기를 하지않은 상태이면
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


        //게시글 정보 띄우기 위한 코드
        //시작
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

        //끝

        //다른 추천레시피 띄우기 위한 코드
        //시작

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
                            Log.d("로그: ", "Error getting documents: ", task.getException());

                        }
                    }
                });

        //끝
    }
    //추천, 추천 취소시 바로바로 데이터베이스에 업로드하여 반영해줌.
        private void dbUploader(DocumentReference documentReference , RecipePostInfo recipePostInfo, int requestCode){
            //추천취소 하는 경우
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
        }
            //추천할 경우
            else{
            documentReference.set(recipePostInfo)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showToast(recipeInformationActivity.this ,"이 레시피을 추천했어요!");
                            Log.w(TAG,"Success writing document" + documentReference.getId());
                            String meesage="hi";
//                            ((MainActivity)MainActivity.mContext).sendPostToFCM(data,meesage);
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


    //즐겨찾기, 즐겨찾기 취소시 바로바로 데이터베이스에 업로드하여 반영해줌.
    private void dbUploader(DocumentReference documentReference , ArrayList<String> bookmarkReceipe, int requestCode){
        //즐겨찾기 취소 하는 경우
        if(requestCode == 0){
            documentReference.update("bookmarkRecipe",bookmarkReceipe)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showToast(recipeInformationActivity.this ,"즐겨찾기를 삭제했어요!");
                            Log.w(TAG,"Success writing document" + documentReference.getId());
                            onResume();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showToast(recipeInformationActivity.this ,"즐겨찾기를 취소에 실패했어요!");
                    Log.w(TAG,"Error writing document", e);
                }
            });
        }
        //즐겨찾기할 경우
        else{
            documentReference.update("bookmarkRecipe",bookmarkReceipe)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showToast(recipeInformationActivity.this ,"이 레시피을 즐겨찾기했어요!");
                            Log.w(TAG,"Success writing document" + documentReference.getId());
                            onResume();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showToast(recipeInformationActivity.this ,"즐겨찾기에 실패했어요!");
                    Log.w(TAG,"Error writing document", e);
                }
            });
        }

    }


}



