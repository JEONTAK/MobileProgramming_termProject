package com.example.mobileprogramming_termproject.ui.myPage;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.mobileprogramming_termproject.Gallery.GalleryActivity;
import com.example.mobileprogramming_termproject.Member.MemberInfo;
import com.example.mobileprogramming_termproject.R;
import com.example.mobileprogramming_termproject.login.LoginActivity;
import com.google.android.gms.tasks.Continuation;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class myPageFragment extends Fragment {

    private myPageViewModel myPageViewModel;
    private static final String TAG = "MemberInitActivity";
    private ImageView profileImageVIew;
    private TextView nickname;
    private String profilePath;
    private FirebaseUser user;
    private DocumentReference document;
    private String original_nickname;
    private String orignal_url;
    FirebaseAuth fAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Resources res=getResources();
        String[] LIST_MENU2=res.getStringArray(R.array.mypage_list);
        View root = inflater.inflate(R.layout.fragment_mypage, container, false);

        ArrayAdapter Adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,LIST_MENU2) ;

        ListView listview = (ListView) root.findViewById(R.id.ListView) ;
        listview.setAdapter(Adapter) ;
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // ?????? ?????? ...

            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                // get TextView's Text.
                String strText = (String) parent.getItemAtPosition(position);
                switch (strText){
                    case"?????????": {
                        myStartActivity2(bookmarkActivity.class);
                        break;
                    }
                    case"?????? ??? ???": {
                        myStartActivity2(myPostActivity.class);
                        break;
                    }
                    case"?????? ??? ?????????": {
                        myStartActivity2(myRecipeActivity.class);
                        break;
                    }
                    case"?????? ??? ??????": {
                        myStartActivity2(myCommentActivity.class);
                        break;
                    }
                    case"?????? ??????":{
                        fAuth=FirebaseAuth.getInstance();
                        fAuth.signOut();
                        startToast("?????? ?????? ???????????????");
                        Intent intent=new Intent(getActivity(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        getActivity().finish();
                    }

                }
            }
        });

        return root;
    }
    @Override
    public void onStart() {
        super.onStart();
        nickname=getActivity().findViewById(R.id.nicknameTextView);
        profileImageVIew= getActivity().findViewById(R.id.profileImageVIew2);
        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(user.getUid());
        Log.v("????????????",user.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .priority(Priority.HIGH);
                MemberInfo memberInfo = documentSnapshot.toObject(MemberInfo.class);
                original_nickname=memberInfo.getNickname();
                orignal_url=memberInfo.getPhotoUrl();
                Glide.with(getActivity()).load(orignal_url).override(500).thumbnail(1f).apply(options).into(profileImageVIew);
                nickname.setText(original_nickname);
                Log.v("??????",orignal_url+original_nickname);

            }

        });

        getActivity().findViewById((R.id.profileImageVIew2)).setOnClickListener((onClickListener));
        getActivity().findViewById((R.id.completeButton)).setOnClickListener((onClickListener));
        getActivity().findViewById((R.id.EditButton)).setOnClickListener((onClickListener));
        getActivity().findViewById(R.id.getProfileButton).setOnClickListener(onClickListener);
        getActivity().findViewById(R.id.galleryButton).setOnClickListener(onClickListener);


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        File tempFile = new File(getActivity().getFilesDir().getAbsolutePath(), "temp_image");
        switch (requestCode) {
            case 3: {
                if (resultCode == Activity.RESULT_OK) {
                    profilePath = data.getStringExtra("profilePath");
                    Bitmap bmp = BitmapFactory.decodeFile(profilePath);
                    profile_image_update();
                }
                break;
            }
        }
    }




    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button completeButton=(Button) getActivity().findViewById((R.id.completeButton));
            Button editButton=(Button)getActivity().findViewById((R.id.EditButton));
            EditText nicknameEditText=(EditText)getActivity().findViewById((R.id.nicknameEditText));
            TextView nicknameTextView=(TextView) getActivity().findViewById((R.id.nicknameTextView));

            switch (v.getId()) {
                case R.id.EditButton:
                    completeButton.setVisibility(View.VISIBLE);
                    nicknameEditText.setVisibility(View.VISIBLE);
                    editButton.setVisibility(View.GONE);
                    nicknameTextView.setVisibility(View.GONE);
                    break;
                case R.id.completeButton:
                    completeButton.setVisibility(View.GONE);
                    nicknameEditText.setVisibility(View.GONE);
                    editButton.setVisibility(View.VISIBLE);
                    nicknameTextView.setVisibility(View.VISIBLE);
                    profile_nickname_Update();
                    break;
                case R.id.profileImageVIew2:
                    if (ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {//?????? ????????? ?????? ????????????????????????,
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    }else{//?????? ????????? ????????? ????????????
                        myStartActivity(GalleryActivity.class);
                    }
                    break;
            }
        }
    };

    //????????? ?????? ????????? ??? ?????? ??????
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    myStartActivity(GalleryActivity.class);
                } else {
                    startToast("????????? ????????? ?????????");
                }
            }
        }
    }

    private void profile_image_update(){
        final String name = ((TextView) getActivity().findViewById(R.id.nicknameTextView)).getText().toString();
        if(profilePath == null){

        }
        else {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            user = FirebaseAuth.getInstance().getCurrentUser();
            final StorageReference mountainImagesRef = storageRef.child("users/" + user.getUid() + "/profileImage.jpg");
            try {
                Log.v("????????????",profilePath);
                InputStream stream = new FileInputStream(new File(profilePath));
                UploadTask uploadTask = mountainImagesRef.putStream(stream);
                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return mountainImagesRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            Glide.with(getActivity()).load(downloadUri).override(5000).into(profileImageVIew);
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("users").document(user.getUid()).update("photoUrl",downloadUri.toString())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            startToast("???????????? ????????? ?????????????????????.");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            startToast("???????????? ????????? ?????????????????????.");
                                            Log.w(TAG, "Error writing document", e);
                                        }
                                    });
                        } else {
                            startToast("??????????????? ???????????? ?????????????????????.");
                        }
                    }
                });
            } catch (FileNotFoundException e) {
                Log.e("??????", "??????: " + e.toString());
            }
        }
    }


    //???????????? ????????????
    private void profile_nickname_Update() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        final String name = ((EditText) getActivity().findViewById(R.id.nicknameEditText)).getText().toString();
        nickname.setText(name);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(user.getUid()).update("nickname",name)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startToast("???????????? ????????? ?????????????????????.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        startToast("???????????? ????????? ?????????????????????.");
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    //??????????????? ????????????????????? ??????
    private void uploader(MemberInfo memberInfo){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(user.getUid()).set(memberInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startToast("???????????? ????????? ?????????????????????.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        startToast("???????????? ????????? ?????????????????????.");
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    //????????? ????????? ??????
    private void startToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    //?????? ?????? ??????
    private void myStartActivity(Class c) {
        Intent intent = new Intent(getActivity(), c);
        startActivityForResult(intent, 3);
    }
    private void myStartActivity2(Class c) {
        Intent intent = new Intent(getActivity(), c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


}