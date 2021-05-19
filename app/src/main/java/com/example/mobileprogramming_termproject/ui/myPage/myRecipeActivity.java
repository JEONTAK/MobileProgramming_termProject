package com.example.mobileprogramming_termproject.ui.myPage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileprogramming_termproject.R;
import com.example.mobileprogramming_termproject.adapter.myrecipeAdapter;
import com.example.mobileprogramming_termproject.listener.OnPostListener;
import com.example.mobileprogramming_termproject.writingContent.RecipePostInfo;
import com.example.mobileprogramming_termproject.writingContent.writingRecipePostActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;

import static com.example.mobileprogramming_termproject.Util.isStorageUrl;
import static com.example.mobileprogramming_termproject.Util.showToast;

public class myRecipeActivity extends AppCompatActivity {
    final private String TAG = "내 레시피 목록화면";
    //파이어베이스 스토어 선언
    private FirebaseFirestore firebaseFirestore;
    //파이어베이스 Auth 선언


    //북마크 글을 카드뷰로 띄워주기 위한 리사이클러 뷰 선언
    private RecyclerView recipeRecyclerView;
    //카드뷰를 행마다 1개씩 나오게 하기위함.
    final int numberOfColumns = 2;

    private myrecipeAdapter myrecipeAdapter;
    private ArrayList<RecipePostInfo> recipe_postList;
    private StorageReference storageRef;
    private int successCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myrecipe);

        //파이어베이스에서 데이터베이스 가져옴
        firebaseFirestore= FirebaseFirestore.getInstance();

        // Create a storage reference from our app
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        recipe_postList = new ArrayList<>();
        //recipeAdapter를 이용하여 리사이클러 뷰로 내용 띄움.
        myrecipeAdapter = new myrecipeAdapter(myRecipeActivity.this, recipe_postList);
        myrecipeAdapter.setOnPostListener(onPostListener);

        //리사이클러뷰 작성
        recipeRecyclerView = findViewById(R.id.myPage_RecipePost_List);
        recipeRecyclerView.setHasFixedSize(true);
        recipeRecyclerView.setLayoutManager(new GridLayoutManager(myRecipeActivity.this,numberOfColumns));

        recipeRecyclerView.setAdapter(myrecipeAdapter);
    }

    OnPostListener onPostListener = new OnPostListener() {

        @Override
        public void onModify(int position) {
            String id = recipe_postList.get(position).getRecipeId();
            Log.d(TAG, "수정" + id);
            myStartActivity(writingRecipePostActivity.class, recipe_postList.get(position));
        }

        @Override
        public void onDelete(int position) {
            String id = recipe_postList.get(position).getRecipeId();

            String titleImageName = recipe_postList.get(position).getTitleImage();

            String[] imageList = titleImageName.split("\\?");
            String[] imageList2 = imageList[0].split("%2F");
            String imageName = imageList2[imageList2.length - 1];
            StorageReference titleImageRef = storageRef.child("recipePost/" + id + "/" + imageName);
            // Delete the file
            titleImageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "스토리지에서 타이틀 이미지 파일 삭제 성공");
                    // File deleted successfully
                    ArrayList<String> contentList = recipe_postList.get(position).getContent();
                    for(int i = 0 ; i < contentList.size() ; i++){
                        String contents = contentList.get(i);
                        if(isStorageUrl(contents)){
                            successCount++;
                            String[] list = contents.split("\\?");
                            String[] list2 = list[0].split("%2F");
                            String name = list2[list2.length - 1];

                            // Create a reference to the file to delete
                            StorageReference imageRef = storageRef.child("recipePost/" + id + "/" + name);

                            // Delete the file
                            imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "스토리지에서 이미지 파일 삭제 성공");
                                    successCount--;
                                    storeUploader(id);
                                    // File deleted successfully
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Log.w(TAG, "스토리지에서 이미지 파일 삭제 실패");
                                    // Uh-oh, an error occurred!
                                }
                            });
                        }
                    }
                    storeUploader(id);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.w(TAG, "스토리지에서 타이틀 이미지 파일 삭제 실패");
                    // Uh-oh, an error occurred!
                }
            });
        }
    };

    private void storeUploader(String id){
        if(successCount == 0){
            Log.d(TAG, "삭제" + id);
            firebaseFirestore.collection("recipePost").document(id)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            postUpdate();
                            showToast(myRecipeActivity.this ,"게시글 삭제에 성공했어요!");
                            Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showToast(myRecipeActivity.this ,"게시글 삭제에 실패했어요!");
                            Log.w(TAG, "Error deleting document", e);
                        }
                    });
        }
    }

    //레시피게시판에 내용이 추가가 될 경우 바로바로 업데이트 해주기 위해 resume함수에 넣어 관리.
    @Override
    protected void onResume(){
        super.onResume();
        postUpdate();
    }

    private void postUpdate(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.v("유저",user.getUid());
        //recipePost에 있는 data를 가져오기 위함.
        CollectionReference collectionReference = firebaseFirestore.collection("recipePost");
        collectionReference
                .whereEqualTo("publisher",user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            recipe_postList.clear();
                            //각 게시글의 정보를 가져와 arrayList에 저장.
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
                            myrecipeAdapter.notifyDataSetChanged();

                        } else {
                            Log.d("로그: ", "Error getting documents: ", task.getException());

                        }
                    }
                });
    }

    //activity를 실행하기 위한 함수.
    private void myStartActivity(Class c){
        Intent intent=new Intent( this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void myStartActivity(Class c, RecipePostInfo recipePostInfo){
        Intent intent=new Intent( this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("recipePostInfo", recipePostInfo);
        startActivity(intent);
    }

}