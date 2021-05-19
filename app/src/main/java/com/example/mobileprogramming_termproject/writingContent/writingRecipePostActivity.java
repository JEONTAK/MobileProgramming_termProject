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
import com.example.mobileprogramming_termproject.MainActivity;
import com.example.mobileprogramming_termproject.Member.MemberInfo;
import com.example.mobileprogramming_termproject.R;
import com.example.mobileprogramming_termproject.ui.home.HomeFragment;
import com.example.mobileprogramming_termproject.ui.myPage.myRecipeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

import static com.example.mobileprogramming_termproject.Util.isStorageUrl;
import static com.example.mobileprogramming_termproject.Util.showToast;


public class writingRecipePostActivity extends AppCompatActivity {

    private static final String TAG ="레시피 작성 화면";
    //유저 선언
    private FirebaseUser user;

    private FirebaseFirestore firebaseFirestore;
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

    private EditText editIngredient_Recipe;

    private EditText editTitle_Recipe;

    private EditText recipePrice;

    private RecipePostInfo recipePostInfo;

    private StorageReference storageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_recipe_post);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        //파이어베이스에 있는 데이터베이스 가져옴
        firebaseFirestore = FirebaseFirestore.getInstance();


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


        editIngredient_Recipe = findViewById(R.id.editIngredient_Recipe);
        editTitle_Recipe = findViewById(R.id.editTitle_Recipe);
        recipePrice = findViewById(R.id.recipePrice);

        findViewById(R.id.imageModify).setOnClickListener(onClickListener);
        findViewById(R.id.imageDelete).setOnClickListener(onClickListener);
        editIngredient_Recipe.setOnFocusChangeListener(onFocusChangeListener);
        //이미지를 원하는 위치에 넣귀 위하여 위치를 가져 오기 위함.
        editTitle_Recipe.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    selectedEditText = (EditText) v;
                }
            }
        });

        recipePostInfo = (RecipePostInfo)getIntent().getSerializableExtra("recipePostInfo");
        postInit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode){
            case 0:
                //이미지 추가 버튼 눌렀을 때
                if(resultCode == Activity.RESULT_OK){
                    //이미지 경로 string으로 저장.
                    String profilePath = data.getStringExtra("profilePath");
                    //설명 List에 추가
                    pathList.add(profilePath);

                    //레이아웃을 꽉차게 하기 위하여 미리 설정
                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    //새 리니어 레이아웃 생성(여기에 이미지, 설명 edittext를 집어넣은후 기존 parent에 삽입할 예정)
                    LinearLayout linearLayout = new LinearLayout(writingRecipePostActivity.this);
                    linearLayout.setLayoutParams(layoutParams);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);

                    //만약 parent(설명을 포함하는 가장 큰 레이아웃)이 0이면 바로 집어넣음.
                    if(parent.getChildCount() == 0){
                        parent.addView(linearLayout);
                    }
                    //이미 parent에 child 레이아웃이 있을경우, 위치를 유저가 선택한 위치로 가 집어넣음.
                    else{
                        for(int i = 0 ; i < parent.getChildCount() ; i++){
                            if(parent.getChildAt(i) == selectedEditText.getParent()){
                                parent.addView(linearLayout, i + 1);
                                break;
                            }
                        }
                    }

                    //이미지 뷰도 이와 같이 생성하여 선택한 곳에 집어넣음
                    ImageView imageView = new ImageView(writingRecipePostActivity.this);
                    imageView.setLayoutParams(layoutParams);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    //이미지 클릭시, 수정 삭제 버튼이 나와 다른 이미지로 바꾸거나, 이미지를 삭제 할 수 있음.
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            backBtnLayout.setVisibility(View.VISIBLE);
                            selectedImageView = (ImageView) v;
                        }
                    });

                    //이미지 출력
                    Glide.with(this).load(profilePath).override(1000).into(imageView);
                    //이미지 추가
                    linearLayout.addView(imageView);
                    //이미지에 대한 설명 추가
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
                //타이틀 이미지 버튼 눌렀을때
                if(resultCode == Activity.RESULT_OK){
                    //타이틀 이미지 관련 코드
                    //타이틀 이미지를 이미지 버튼에 출력
                    String profilePath = data.getStringExtra("profilePath");
                    titleImagePath=profilePath;
                    Glide.with(this).load(profilePath).override(1000).into(titleImage);
                }
                break;
            case 2:
                //이미지 수정 버튼 눌렀을때
                if(resultCode == Activity.RESULT_OK){
                    String profilePath = data.getStringExtra("profilePath");
                    pathList.set(parent.indexOfChild((View) selectedImageView.getParent()),profilePath);
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
                    myStartActivity(MainActivity.class);
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
                    View selectedView = (View) selectedImageView.getParent();


                    String[] imageList = pathList.get(parent.indexOfChild(selectedView)).split("\\?");
                    String[] imageList2 = imageList[0].split("%2F");
                    String imageName = imageList2[imageList2.length - 1];

                    // Create a reference to the file to delete
                    StorageReference imageRef = storageRef.child("recipePost/" + recipePostInfo.getRecipeId() + "/" + imageName);

                    // Delete the file
                    imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "스토리지에서 이미지 파일 삭제 성공");
                            // File deleted successfully
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.w(TAG, "스토리지에서 이미지 파일 삭제 실패");
                            // Uh-oh, an error occurred!
                        }
                    });

                    pathList.remove(parent.indexOfChild(selectedView));
                    parent.removeView(selectedView);
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

    private boolean isNotNullText() {
        for (int i = 0; i < parent.getChildCount(); i++) {
            //설명이 담겨있는 레이아웃에서 현재 레이아웃만 따로 가져옴.
            LinearLayout linearLayout = (LinearLayout) parent.getChildAt(i);
            //가져온 레이아웃에는 이미지, 글내용 2개가 들어있거나, 이미지만 들어있음.
            for (int ii = 0; ii < linearLayout.getChildCount(); ii++) {
                View view = linearLayout.getChildAt(ii);
                if (view instanceof EditText) {
                    String text = ((EditText) view).getText().toString();
                    if (text.length() == 0) {
                        showToast(writingRecipePostActivity.this, "레시피에는 설명이 모두 들어가야 해요!");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean isNumberPrice(){
        String recipePrice = ((EditText)findViewById(R.id.recipePrice)).getText().toString();

        return true;

    }

    //파이어베이스에 데이터 업로드 하기 위함.
    private void bulletinUpload(){
        successCount = 0;
        //제목 저장.
        final String title = ((EditText)findViewById(R.id.editTitle_Recipe)).getText().toString();
        //레시피 재료 저장.
        final String recipe_ingredient = ((EditText)findViewById(R.id.editIngredient_Recipe)).getText().toString();
        //레시피 가격 저장.
        final String recipePrice = ((EditText)findViewById(R.id.recipePrice)).getText().toString();
        //만약 레시피의 제목, 내용 재료 가격 모두 공백이 아닐경우 업로드 실행.


        if(titleImagePath != null && title.length() > 0 && pathList.size() > 0 && recipe_ingredient.length() > 0 && recipePrice.length() > 0 && isNotNullText() == true){

            //업로드를 하고있는 것을 보여주기 위한 로딩창 띄움.
            loaderLayout.setVisibility(View.VISIBLE);
            //레시피 설명을 담기 위한 arraylist
            ArrayList<String> contentsList = new ArrayList<>();
            //현재 유저 데이터 가져옴
            user = FirebaseAuth.getInstance().getCurrentUser();
            //Storage 선언
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();


            //저장할 위치 선언
            final DocumentReference documentReference = recipePostInfo
                    == null ? firebaseFirestore.collection("recipePost").document()
                    : firebaseFirestore.collection("recipePost").document(recipePostInfo.getRecipeId());


            Log.d("타이틀",titleImagePath);
            //타이틀이미지 경로 저장.
            String[] titleArray;
            String titleName;
            StorageReference titleImagesRef;
            if(!isStorageUrl(titleImagePath)){
                titleArray = titleImagePath.split("\\.");
                titleImagesRef = storageRef.child("recipePost/" + documentReference.getId() + "/title" +titleArray[titleArray.length - 1]);
                try{
                    //타이틀 이미지를 storage에 저장함
                    InputStream stream = new FileInputStream(new File(titleImagePath));
                    StorageMetadata metadata = new StorageMetadata.Builder().setCustomMetadata("title", "title").build();
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
                                                }else{
                                                    showToast(writingRecipePostActivity.this ,"레시피에는 설명이 모두 들어가야 해요!");
                                                    loaderLayout.setVisibility(View.GONE);
                                                    break;
                                                }
                                            }
                                            //만약 가져온 값이 이미지라면, arraylist에 이미지를 넣어주고, 그 이미지를 storage에 저장함.
                                            else if(!isStorageUrl(pathList.get(pathCount))){

                                                String path = pathList.get(pathCount);
                                                successCount++;
                                                contentsList.add(path);
                                                Log.d("로그 : " , "이미지 " + path);

                                                //이미지를 storage에 저장하기 위한 코드
                                                String[] pathArray = path.split("\\.");
                                                final StorageReference mountainImagesRef = storageRef.child("recipePost/" + documentReference.getId() + "/" + pathCount + pathArray[pathArray.length - 1]);
                                                Log.d("로그 : " , "mountainImagesRef " + path);

                                                try{
                                                    InputStream stream = new FileInputStream(new File(path));
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
                                                                    successCount--;
                                                                    Log.e("로그","" +successCount);
                                                                    if(successCount == 0){
                                                                        ArrayList<String> recomUser = new ArrayList<>();
                                                                        //유저 아이디를 통해 데이터베이스에 접근하여 이름을 가져옴.
                                                                        firebaseFirestore.collection("users").document(user.getUid()).get()
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
                                                                                                        (ArrayList<String>) document.getData().get("bookmarkRecipe")
                                                                                                );
                                                                                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                                                                                //recipepostinfo 형식으로 저장.
                                                                                                RecipePostInfo recipePostInfo = new RecipePostInfo(titleImagePath, title, recipe_ingredient ,contentsList,
                                                                                                        user.getUid(), userInfo.getName(), new Date(), 0, documentReference.getId(), recomUser, Long.parseLong(recipePrice), foodCategory, tagCategory);
                                                                                                //업로드 실행
                                                                                                dbUploader(documentReference, recipePostInfo);
                                                                                            } else {
                                                                                                Log.d(TAG, "No such document");
                                                                                            }
                                                                                        } else {
                                                                                            Log.d(TAG, "get failed with ", task.getException());
                                                                                        }
                                                                                    }
                                                                                });
                                                                        for(int a = 0 ; a < contentsList.size(); a++){
                                                                            Log.e(TAG + "로그: ", "콘텐츠: " +contentsList.get(a));
                                                                        }
                                                                        successCount++;
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
                                            else{
                                                String path = pathList.get(pathCount);
                                                contentsList.add(path);
                                                pathCount++;
                                            }

                                        }
                                    }
                                    //만약 설명 전체를 다 올렷다면, 값을 recipepostinfo 형식으로 저장하여 파이어베이스에 데이터 업로드
                                    if(successCount == 0){
                                        ArrayList<String> recomUser = new ArrayList<>();
                                        //유저 아이디를 통해 데이터베이스에 접근하여 이름을 가져옴.
                                        firebaseFirestore.collection("users").document(user.getUid()).get()
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
                                                                        (ArrayList<String>) document.getData().get("bookmarkRecipe")
                                                                );
                                                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                                                //recipepostinfo 형식으로 저장.
                                                                RecipePostInfo recipePostInfo = new RecipePostInfo(titleImagePath, title, recipe_ingredient ,contentsList,
                                                                        user.getUid(), userInfo.getName(), new Date(), 0, documentReference.getId(), recomUser, Long.parseLong(recipePrice), foodCategory, tagCategory);
                                                                //업로드 실행
                                                                dbUploader(documentReference, recipePostInfo);
                                                            } else {
                                                                Log.d(TAG, "No such document");
                                                            }
                                                        } else {
                                                            Log.d(TAG, "get failed with ", task.getException());
                                                        }
                                                    }
                                                });
                                        for(int a = 0 ; a < contentsList.size(); a++){
                                            Log.e(TAG + "로그: ", "콘텐츠: " +contentsList.get(a));
                                        }
                                        successCount++;
                                    }
                                }
                            });
                        }
                    });
                } catch (FileNotFoundException e) {
                    Log.e("로그","에러:" + e.toString());
                }
            }else{

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
                            }else{
                                showToast(writingRecipePostActivity.this ,"레시피에는 설명이 모두 들어가야 해요!");
                                loaderLayout.setVisibility(View.GONE);
                                break;
                            }
                        }
                        //만약 가져온 값이 이미지라면, arraylist에 이미지를 넣어주고, 그 이미지를 storage에 저장함.
                        else if(!isStorageUrl(pathList.get(pathCount))){
                            String path = pathList.get(pathCount);
                            successCount++;
                            contentsList.add(path);
                            Log.d("로그 : " , "이미지 " + path);

                            //이미지를 storage에 저장하기 위한 코드
                            String[] pathArray = path.split("\\.");
                            final StorageReference mountainImagesRef = storageRef.child("recipePost/" + documentReference.getId() + "/" + pathCount + pathArray[pathArray.length - 1]);
                            Log.d("로그 : " , "mountainImagesRef " + path);

                            try{
                                InputStream stream = new FileInputStream(new File(path));
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
                                                successCount--;
                                                Log.e("로그","" +successCount);
                                                //만약 설명 전체를 다 올렷다면, 값을 recipepostinfo 형식으로 저장하여 파이어베이스에 데이터 업로드
                                                if(successCount == 0){
                                                    ArrayList<String> recomUser = new ArrayList<>();
                                                    //유저 아이디를 통해 데이터베이스에 접근하여 이름을 가져옴.
                                                    firebaseFirestore.collection("users").document(user.getUid()).get()
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
                                                                                    (ArrayList<String>) document.getData().get("bookmarkRecipe")
                                                                            );
                                                                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                                                            //recipepostinfo 형식으로 저장.
                                                                            RecipePostInfo recipePostInfo = new RecipePostInfo(titleImagePath, title, recipe_ingredient ,contentsList,
                                                                                    user.getUid(), userInfo.getName(), new Date(), 0, documentReference.getId(), recomUser, Long.parseLong(recipePrice), foodCategory, tagCategory);
                                                                            //업로드 실행
                                                                            dbUploader(documentReference, recipePostInfo);
                                                                        } else {
                                                                            Log.d(TAG, "No such document");
                                                                        }
                                                                    } else {
                                                                        Log.d(TAG, "get failed with ", task.getException());
                                                                    }
                                                                }
                                                            });
                                                    for(int a = 0 ; a < contentsList.size(); a++){
                                                        Log.e(TAG + "로그: ", "콘텐츠: " +contentsList.get(a));
                                                    }
                                                    successCount++;
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
                        else{
                            String path = pathList.get(pathCount);
                            contentsList.add(path);
                            pathCount++;
                        }

                    }
                }
                //만약 설명 전체를 다 올렷다면, 값을 recipepostinfo 형식으로 저장하여 파이어베이스에 데이터 업로드
                if(successCount == 0){
                    ArrayList<String> recomUser = new ArrayList<>();
                    //유저 아이디를 통해 데이터베이스에 접근하여 이름을 가져옴.
                    firebaseFirestore.collection("users").document(user.getUid()).get()
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
                                                    (ArrayList<String>) document.getData().get("bookmarkRecipe")
                                            );
                                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                            //recipepostinfo 형식으로 저장.
                                            RecipePostInfo recipePostInfo = new RecipePostInfo(titleImagePath, title, recipe_ingredient ,contentsList,
                                                    user.getUid(), userInfo.getName(), new Date(), 0, documentReference.getId(), recomUser, Long.parseLong(recipePrice), foodCategory, tagCategory);
                                            //업로드 실행
                                            dbUploader(documentReference, recipePostInfo);
                                        } else {
                                            Log.d(TAG, "No such document");
                                        }
                                    } else {
                                        Log.d(TAG, "get failed with ", task.getException());
                                    }
                                }
                            });
                    for(int a = 0 ; a < contentsList.size(); a++){
                        Log.e(TAG + "로그: ", "콘텐츠: " +contentsList.get(a));
                    }
                    successCount++;
                }
            }

        }
        //만약 레시피의 제목, 내용 재료 가격중 하나라도 공백일경우 실행 X
        else{
            showToast(writingRecipePostActivity.this , "레시피를 정확히 입력해주세요!");
            showToast(writingRecipePostActivity.this ,"레시피에 대표 이미지, 제목, 재료, 이미지에 대한 설명 또는 가격을 써야해요!");
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

    private void postInit(){
        if(recipePostInfo != null){
            Glide.with(this).load(recipePostInfo.getTitleImage()).override(1000).into(titleImage);
            titleImagePath = recipePostInfo.getTitleImage();
            editTitle_Recipe.setText(recipePostInfo.getTitle());
            editIngredient_Recipe.setText(recipePostInfo.getIngredient());

            ArrayList<String> contentsList = recipePostInfo.getContent();
            for(int i = 0 ; i < contentsList.size() ; i++){
                String contents = contentsList.get(i);
                if(isStorageUrl(contents)){
                    pathList.add(contents);
                    Log.w(TAG,"내용" + contents);

                    //레이아웃을 꽉차게 하기 위하여 미리 설정
                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    //새 리니어 레이아웃 생성(여기에 이미지, 설명 edittext를 집어넣은후 기존 parent에 삽입할 예정)
                    LinearLayout linearLayout = new LinearLayout(writingRecipePostActivity.this);
                    linearLayout.setLayoutParams(layoutParams);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);

                    parent.addView(linearLayout);

                    //이미지 뷰도 이와 같이 생성하여 선택한 곳에 집어넣음
                    ImageView imageView = new ImageView(writingRecipePostActivity.this);
                    imageView.setLayoutParams(layoutParams);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    //이미지 클릭시, 수정 삭제 버튼이 나와 다른 이미지로 바꾸거나, 이미지를 삭제 할 수 있음.
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            backBtnLayout.setVisibility(View.VISIBLE);
                            selectedImageView = (ImageView) v;
                        }
                    });

                    //이미지 출력
                    Glide.with(this).load(contents).override(1000).into(imageView);
                    //이미지 추가
                    linearLayout.addView(imageView);
                    //이미지에 대한 설명 추가
                    EditText editText = new EditText(writingRecipePostActivity.this);
                    editText.setLayoutParams(layoutParams);
                    editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
                    editText.setHint("사진에대한 설명을 입력해주세요!!");
                    if(i < contentsList.size() - 1){
                        String nextContents = contentsList.get(i + 1);
                        if(!isStorageUrl(nextContents)){
                            editText.setText(nextContents);
                        }
                    }
                    editText.setOnFocusChangeListener(onFocusChangeListener);
                    linearLayout.addView(editText);
                    linearLayout.setPadding(5,5,5,5);
                }
            }
            recipePrice.setText(Integer.toString((int)recipePostInfo.getPrice()));
        }
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