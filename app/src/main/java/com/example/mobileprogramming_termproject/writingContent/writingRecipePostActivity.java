package com.example.mobileprogramming_termproject.writingContent;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import com.example.mobileprogramming_termproject.Gallery.GalleryActivity;
import com.example.mobileprogramming_termproject.R;
import com.example.mobileprogramming_termproject.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import static com.example.mobileprogramming_termproject.Util.showToast;


public class writingRecipePostActivity extends AppCompatActivity {

    private static final String TAG ="레시피 작성 화면";
    //유저 선언
    private FirebaseUser user;
    //레시피 내용중 설명 정보를 담기 위한 arraylist
    private ArrayList<String> pathList = new ArrayList<>();
    //이미지 수정, 삭제를 위한 레이아웃
    private RelativeLayout backBtnLayout;
    //로딩 창 레이아웃
    private RelativeLayout loaderLayout;
    //설명에 있는 여러 이미지와 글을 담기 위한 레이아웃
    private LinearLayout parent;
    //이미지를 클릭했을때 이미지 위치, 정보를 얻기 위한 이미지뷰
    private ImageView selectedImageView;
    //글을 선택했을때 글의 위치, 정보를 얻기 위한 edittext
    private EditText selectedEditText;
    //pathcount : 업로드 해야하는 전체 횟수, successCount : 현재까지 업로드 성공한 횟수
    private int pathCount , successCount;
    //타이틀 이미지 넣기 위한 이미지버튼
    private ImageButton titleImage;
    //타이틀 이미지 경로값
    private String titleImagePath;
    //음식 카테고리 위한 스피너
    private Spinner foodSpinner;
    //태그 카테고리 위한 스피너
    private Spinner tagSpinner;
    //음식 카테고리 저장 위한 String
    private String foodCategory;
    //태그 카테고리 저장 위한 String
    private String tagCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_post);

        backBtnLayout = findViewById(R.id.backBtnLayout);
        parent = findViewById(R.id.contentsLayout);
        backBtnLayout.setVisibility(View.GONE);
        loaderLayout = findViewById(R.id.loaderLayout);
        backBtnLayout.setOnClickListener(onClickListener);
        findViewById(R.id.confirmBtn).setOnClickListener(onClickListener);
        findViewById(R.id.goBackBtn).setOnClickListener(onClickListener);
        findViewById(R.id.addImageBtn).setOnClickListener(onClickListener);
        titleImage = findViewById(R.id.addTitleImageBtn);
        titleImage.setOnClickListener(onClickListener);

        foodSpinner = (Spinner) findViewById(R.id.foodCategorySpinner);
        tagSpinner = (Spinner) findViewById(R.id.tagCategorySpinner);

        //음식 카테고리 스피너에서 값을 얻어옴
        foodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                foodCategory = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        
        //태그 카테고리 스피너에서 값을 얻어옴
        tagSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tagCategory = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        findViewById(R.id.imageModify).setOnClickListener(onClickListener);
        findViewById(R.id.imageDelete).setOnClickListener(onClickListener);
        findViewById(R.id.editIngredient_Recipe).setOnFocusChangeListener(onFocusChangeListener);
        //이미지를 원하는 위치에 넣귀 위하여 위치를 가져 오기 위함.
        findViewById(R.id.editTitle_Recipe).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    selectedEditText = (EditText) v;
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode){
            case 0:
                if(resultCode == Activity.RESULT_OK){
                    //
                    String profilePath = data.getStringExtra("profilePath");
                    pathList.add(profilePath);

                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    LinearLayout linearLayout = new LinearLayout(writingRecipePostActivity.this);
                    linearLayout.setLayoutParams(layoutParams);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    if(parent.getChildCount() == 0){
                        parent.addView(linearLayout);
                    }else{
                        for(int i = 0 ; i < parent.getChildCount() ; i++){
                            if(parent.getChildAt(i) == selectedEditText.getParent()){
                                parent.addView(linearLayout, i + 1);
                                break;
                            }
                        }
                    }
                    ImageView imageView = new ImageView(writingRecipePostActivity.this);
                    imageView.setLayoutParams(layoutParams);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            backBtnLayout.setVisibility(View.VISIBLE);
                            selectedImageView = (ImageView) v;
                        }
                    });
                    Glide.with(this).load(profilePath).override(1000).into(imageView);
                    linearLayout.addView(imageView);

                    EditText editText = new EditText(writingRecipePostActivity.this);
                    editText.setLayoutParams(layoutParams);
                    editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
                    editText.setHint("사진에대한 설명을 입력해주세요!!");
                    editText.setOnFocusChangeListener(onFocusChangeListener);
                    linearLayout.addView(editText);
                    linearLayout.setPadding(5,5,5,5);

                }
                break;
            case 1:
                if(resultCode == Activity.RESULT_OK){
                    String profilePath = data.getStringExtra("profilePath");
                    titleImagePath = profilePath;
                    Glide.with(this).load(profilePath).override(1000).into(titleImage);
                }
                break;
            case 2:
                if(resultCode == Activity.RESULT_OK){
                    String profilePath = data.getStringExtra("profilePath");
                    Glide.with(this).load(profilePath).override(1000).into(selectedImageView);
                }
                break;

        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //뒤로가기 버튼 -> 홈화면으로 돌아감.
                case R.id.goBackBtn:
                    myStartActivity(HomeFragment.class);
                    break;
                    //작성 버튼 -> 파이어베이스에 업로드
                case R.id.confirmBtn:
                    bulletinUpload();
                    break;
                    //이미지 추가 버튼 -> 갤러리 액티비티로 넘어감.
                case R.id.addImageBtn:
                    myStartActivity(GalleryActivity.class,"image", 0);
                    break;
                    //타이틀 이미지 추가 버튼 -> 갤러리 액티비티로 넘어감.
                case R.id.addTitleImageBtn:
                    myStartActivity(GalleryActivity.class,"image", 1);
                    break;
                    //이미지 수정, 삭제 레이아웃 -> 레이아웃이 클릭 전에 보일경우, 보이지 않게 바꿔줌.
                case R.id.backBtnLayout:
                    if(backBtnLayout.getVisibility() == View.VISIBLE){
                        backBtnLayout.setVisibility(View.GONE);
                    }
                    break;
                    //이미지 수정 버튼 -> 갤러리 액티비티로 감. 레이아웃 보이지 않게 바꿈.
                case R.id.imageModify:
                    myStartActivity(GalleryActivity.class,"image", 2);
                    backBtnLayout.setVisibility(View.GONE);
                    break;
                //이미지 삭제 버튼 -> 선택한 이미지 제거함. 레이아웃 보이지 않게 바꿈.
                case R.id.imageDelete:
                    parent.removeView((View)selectedImageView.getParent());
                    backBtnLayout.setVisibility(View.GONE);
                    break;

            }
        }

    };

    //이미지를 원하는 위치에 넣귀 위하여 위치를 가져 오기 위함.
    View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener(){
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus){
                selectedEditText = (EditText) v;
            }
        }
    };


    //파이어베이스에 데이터 업로드 하기 위함.
    private void bulletinUpload(){
        //제목 저장.
        final String title = ((EditText)findViewById(R.id.editTitle_Recipe)).getText().toString();
        //레시피 재료 저장.
        final String recipe_ingredient = ((EditText)findViewById(R.id.editIngredient_Recipe)).getText().toString();
        //레시피 가격 저장.
        final long recipePrice = Long.parseLong(((EditText)findViewById(R.id.recipePrice)).getText().toString());
        //만약 레시피의 제목, 내용 재료 가격 모두 공백이 아닐경우 업로드 실행.
        if(title.length() > 0 && pathList.size() > 0 && recipe_ingredient.length() > 0 && recipePrice > 0){
            //업로드를 하고있는 것을 보여주기 위한 로딩창 띄움.
            loaderLayout.setVisibility(View.VISIBLE);
            //레시피 설명을 담기 위한 arraylist
            ArrayList<String> contentsList = new ArrayList<>();
            //현재 유저 데이터 가져옴
            user = FirebaseAuth.getInstance().getCurrentUser();
            //Storage 선언
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            //파이어베이스에 있는 데이터베이스 가져옴
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            //저장할 위치 선언
            final DocumentReference documentReference = firebaseFirestore.collection("recipePost").document();

            //이미지 경로 저장.
            String[] titleArray = titleImagePath.split("\\.");
            
            final StorageReference titleImagesRef = storageRef.child("recipePost/" + documentReference.getId() + "/title" +titleArray[titleArray.length - 1]);

            try{
                //이미지를 storage에 저장함
                InputStream stream = new FileInputStream(new File(titleImagePath));
                StorageMetadata metadata = new StorageMetadata.Builder().setCustomMetadata("title", "" + titleImagePath).build();
                UploadTask uploadTask = titleImagesRef.putStream(stream,metadata);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("로그 : " , "실패 " + titleImagePath);

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        titleImagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                //이미지 경로를 uri로 바꿔 다시 저장. -> uri로 바꾸지 않으면 app에서 보이지 않음.
                                titleImagePath = uri.toString();
                                Log.d("로그 : " , "titleImagePath " + titleImagePath);

                            }
                        });
                    }
                });
            } catch (FileNotFoundException e) {
                Log.e("로그","에러:" + e.toString());
            }


            Log.d("로그 : " , "titleImagePath " + titleImagePath);

            //레시피 설명을 arraylist에 저장하기 위함.
            //이미지의 수만큼 for문 실행
            for(int i = 0 ;  i < parent.getChildCount() ; i++){
                //설명이 담겨있는 레이아웃에서 현재 레이아웃만 따로 가져옴.
                LinearLayout linearLayout = (LinearLayout)parent.getChildAt(i);
                //가져온 레이아웃에는 이미지, 글내용 2개가 들어있거나, 이미지만 들어있음.
                for(int ii = 0 ; ii < linearLayout.getChildCount(); ii++){
                    View view = linearLayout.getChildAt(ii);
                    //만약 가져온 것이 글이라면, 바로 넣어줌.
                    if(view instanceof EditText){
                        String text = ((EditText)view).getText().toString();
                        if(text.length() > 0){
                            contentsList.add(text);
                        }
                    }
                    //만약 가져온 값이 이미지라면, arraylist에 이미지를 넣어주고, 그 이미지를 storage에 저장함.
                    else{
                        contentsList.add(pathList.get(pathCount));
                        Log.d("로그 : " , "이미지 " + pathList.get(pathCount));
                        
                        //이미지를 storage에 저장하기 위한 코드
                        String[] pathArray = pathList.get(pathCount).split("\\.");
                        final StorageReference mountainImagesRef = storageRef.child("recipePost/" + documentReference.getId() + "/" + pathCount + pathArray[pathArray.length - 1]);
                        Log.d("로그 : " , "mountainImagesRef " + pathList.get(pathCount));

                        try{
                            InputStream stream = new FileInputStream(new File(pathList.get(pathCount)));
                            StorageMetadata metadata = new StorageMetadata.Builder().setCustomMetadata("index", ""+ (contentsList.size() - 1)).build();
                            UploadTask uploadTask = mountainImagesRef.putStream(stream,metadata);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    final int index = Integer.parseInt(taskSnapshot.getMetadata().getCustomMetadata("index"));
                                    mountainImagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            contentsList.set(index, uri.toString());
                                            //업로드 성공횟수를 증가시킴
                                            successCount++;
                                            Log.e("로그","" +successCount);
                                            //만약 설명 전체를 다 올렷다면, 값을 recipepostinfo 형식으로 저장하여 파이어베이스에 데이터 업로드
                                            if(pathList.size() == successCount){
                                                ArrayList<String> recomUser = new ArrayList<>();
                                                //recipepostinfo 형식으로 저장.
                                                RecipePostInfo recipePostInfo = new RecipePostInfo(titleImagePath, title, recipe_ingredient ,contentsList,
                                                        user.getUid(), new Date(), 0, documentReference.getId(), recomUser, recipePrice, foodCategory, tagCategory);
                                                //업로드 실행
                                                dbUploader(documentReference, recipePostInfo);
                                                for(int a = 0 ; a < contentsList.size(); a++){
                                                    Log.e("로그: ", "콘텐츠: " +contentsList.get(a));
                                                }
                                            }
                                        }
                                    });
                                }
                            });
                        } catch (FileNotFoundException e) {
                            Log.e("로그","에러:" + e.toString());
                        }
                        pathCount++;
                    }
                }

            }
        }
        //만약 레시피의 제목, 내용 재료 가격중 하나라도 공백일경우 실행 X
        else{
            showToast(writingRecipePostActivity.this , "레시피를 정확히 입력해주세요!");
            showToast(writingRecipePostActivity.this ,"레시피에는 최소 한개 이상의 이미지가 들어가야 해요!");
        }
    }

    //레시피를 파이어베이스에 업로드 하기 위함.
    private void dbUploader(DocumentReference documentReference , RecipePostInfo recipePostInfo){
        documentReference.set(recipePostInfo)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //등록이 끝나면 로딩창 보이지 않게 함.
                    loaderLayout.setVisibility(View.GONE);
                    showToast(writingRecipePostActivity.this ,"게시글 등록 성공!");
                    Log.w(TAG,"Success writing document" + documentReference.getId());
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            loaderLayout.setVisibility(View.GONE);
            showToast(writingRecipePostActivity.this ,"게시글 등록 실패.");
            Log.w(TAG,"Error writing document", e);
        }
        });
    }

    private void myStartActivity(Class c){
        Intent intent=new Intent( this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void myStartActivity(Class c, String media, int requestCode){
        Intent intent=new Intent( this, c);
        intent.putExtra("media",media);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, requestCode);
    }

}

