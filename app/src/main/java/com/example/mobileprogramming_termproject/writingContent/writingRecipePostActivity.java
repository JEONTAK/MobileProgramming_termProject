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

    private static final String TAG =" bulletinActivity";
    private FirebaseUser user;
    private ArrayList<String> pathList = new ArrayList<>();
    private RelativeLayout backBtnLayout;
    private RelativeLayout loaderLayout;
    private LinearLayout parent;
    private ImageView selectedImageView;
    private EditText selectedEditText;



    private int pathCount , successCount;

    private ImageButton titleImage;
    private String titleImagePath;
    private Spinner foodSpinner;
    private Spinner tagSpinner;
    private String foodCategory;
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

        foodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                foodCategory = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
                case R.id.goBackBtn:
                    myStartActivity(HomeFragment.class);
                    break;
                case R.id.confirmBtn:
                    bulletinUpload();
                    break;
                case R.id.addImageBtn:
                    myStartActivity(GalleryActivity.class,"image", 0);
                    break;
                case R.id.addTitleImageBtn:
                    myStartActivity(GalleryActivity.class,"image", 1);
                    break;
                case R.id.backBtnLayout:
                    if(backBtnLayout.getVisibility() == View.VISIBLE){
                        backBtnLayout.setVisibility(View.GONE);
                    }
                    break;
                case R.id.imageModify:
                    myStartActivity(GalleryActivity.class,"image", 2);
                    backBtnLayout.setVisibility(View.GONE);
                    break;
                case R.id.imageDelete:
                    parent.removeView((View)selectedImageView.getParent());
                    backBtnLayout.setVisibility(View.GONE);
                    break;

            }
        }

    };

    View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener(){
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus){
                selectedEditText = (EditText) v;
            }
        }
    };


    private void bulletinUpload(){
        final String title = ((EditText)findViewById(R.id.editTitle_Recipe)).getText().toString();
        final String recipe_ingredient = ((EditText)findViewById(R.id.editIngredient_Recipe)).getText().toString();
        final long recipePrice = Long.parseLong(((EditText)findViewById(R.id.recipePrice)).getText().toString());
        if(title.length() > 0 && pathList.size() > 0 && recipe_ingredient.length() > 0 && recipePrice > 0){
            loaderLayout.setVisibility(View.VISIBLE);
            ArrayList<String> contentsList = new ArrayList<>();

            user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            final DocumentReference documentReference = firebaseFirestore.collection("recipePost").document();

            String[] titleArray = titleImagePath.split("\\.");
            final StorageReference titleImagesRef = storageRef.child("recipePost/" + documentReference.getId() + "/title" +titleArray[titleArray.length - 1]);
            try{
                InputStream stream = new FileInputStream(new File(titleImagePath));
                StorageMetadata metadata = new StorageMetadata.Builder().setCustomMetadata("title", "" + titleImagePath).build();
                UploadTask uploadTask = titleImagesRef.putStream(stream,metadata);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        titleImagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                titleImagePath = uri.toString();
                            }
                        });
                    }
                });
            } catch (FileNotFoundException e) {
                Log.e("로그","에러:" + e.toString());
            }


            for(int i = 0 ;  i < parent.getChildCount() ; i++){
                LinearLayout linearLayout = (LinearLayout)parent.getChildAt(i);
                for(int ii = 0 ; ii < linearLayout.getChildCount(); ii++){
                    View view = linearLayout.getChildAt(ii);
                    if(view instanceof EditText){
                        String text = ((EditText)view).getText().toString();
                        if(text.length() > 0){
                            contentsList.add(text);
                        }
                    }else{
                        contentsList.add(pathList.get(pathCount));
                        String[] pathArray = pathList.get(pathCount).split("\\.");
                        final StorageReference mountainImagesRef = storageRef.child("recipePost/" + documentReference.getId() + "/" + pathCount + pathArray[pathArray.length - 1]);
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
                                            successCount++;
                                            Log.e("로그","" +successCount);
                                            if(pathList.size() == successCount){
                                                ArrayList<String> recomUser = new ArrayList<>();
                                                RecipePostInfo recipePostInfo = new RecipePostInfo(titleImagePath, title, recipe_ingredient ,contentsList,
                                                        user.getUid(), new Date(), 0, documentReference.getId(), recomUser, recipePrice, foodCategory, tagCategory);
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
        else{
            showToast(writingRecipePostActivity.this , "레시피를 정확히 입력해주세요!");
            showToast(writingRecipePostActivity.this ,"레시피에는 최소 한개 이상의 이미지가 들어가야 해요!");
        }
    }

    private void dbUploader(DocumentReference documentReference , RecipePostInfo recipePostInfo){
        documentReference.set(recipePostInfo)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
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

