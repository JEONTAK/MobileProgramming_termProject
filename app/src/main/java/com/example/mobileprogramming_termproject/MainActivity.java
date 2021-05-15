package com.example.mobileprogramming_termproject;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mobileprogramming_termproject.firebase.UserData;
import com.example.mobileprogramming_termproject.firebase.notificationData;
import com.example.mobileprogramming_termproject.menu.priceFragment;
import com.example.mobileprogramming_termproject.menu.tagFragment;
import  com.example.mobileprogramming_termproject.ui.alarm.alarmFragment;
import com.example.mobileprogramming_termproject.ui.home.HomeFragment;
import com.example.mobileprogramming_termproject.ui.map.mapFragment;
import com.example.mobileprogramming_termproject.ui.myPage.myPageFragment;
import com.example.mobileprogramming_termproject.ui.searchResult.searchResultFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity  {
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private HomeFragment homeFragment = new HomeFragment();
    private mapFragment MapFragment = new mapFragment();
    private myPageFragment MyPageFragment = new myPageFragment();
    private alarmFragment AlarmFragment=new alarmFragment();
    private searchResultFragment SearchResultFragment=new searchResultFragment();
    Fragment PriceFragment;
    Fragment TagFragment;
//알림 관련 데이터베이스 설정
    private static FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;
    private ArrayAdapter<String> mAdapter;
    private EditText mEdtMessage;
//    외부에서 액티비티 참고 위해
    public static Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //알림 관련 데이터베이스 설정
        initFirebaseDatabase();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.nav_host_fragment, homeFragment).commitAllowingStateLoss();

        PriceFragment=new priceFragment();
        TagFragment=new tagFragment();


        BottomNavigationView bottomNavigationView  = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        bottomNavigationView .setOnNavigationItemSelectedListener(new ItemSelectedListener());

        mContext=this;

    }
//    알림 관련 데이터베이스 설정
    private void initFirebaseDatabase() {

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("message");
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String message = dataSnapshot.getValue(String.class);
                mAdapter.add(message);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String message = dataSnapshot.getValue(String.class);
                mAdapter.remove(message);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mDatabaseReference.addChildEventListener(mChildEventListener);
    }



    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch(menuItem.getItemId())
            {
                case R.id.navigation_home:
                    transaction.replace(R.id.nav_host_fragment, homeFragment).commitAllowingStateLoss();
                    break;
                case R.id.navigation_alarm:
                    transaction.replace(R.id.nav_host_fragment,AlarmFragment).commitAllowingStateLoss();
                    break;

                case R.id.navigation_mypage:
                    transaction.replace(R.id.nav_host_fragment, MyPageFragment).commitAllowingStateLoss();
                    break;
                case R.id.navigation_map:
                    transaction.replace(R.id.nav_host_fragment, MapFragment).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }


    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment,fragment);
        fragmentTransaction.commit();
    }
    public void onFragmentChange(int index,String query){
        if(index==0){

        }
        else if(index==1){

            Toast.makeText(this, "검색어 : "+query, Toast.LENGTH_LONG).show();
            searchResultFragment myFragment = new searchResultFragment();

            Bundle bundle = new Bundle(1); // 파라미터의 숫자는 전달하려는 값의 갯수
            bundle.putString("search_content", query);
            myFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,myFragment).commit();
        }
    }

//    fcm 보내기
    private static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    private static final String SERVER_KEY = "AAAAWUUtQPo:APA91bFgT7PJ24--WfXai6HCGtCW2EDvAJOuM3H2BA_IGshJdJbjwZ5_PrfhVWpc2bFW_iTHgWrlYyAhTAZHYWdGPoVAIXZ1zjlsn8u8CZxu9YX1kBIwS9qZG3KGEMLwleD2igiZYQXf";
    public void sendPostToFCM(final notificationData Data, final String message) {
        mFirebaseDatabase.getReference("users")
                .child(Data.userEmail.substring(0, Data.userEmail.indexOf('@')))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
//                    userdata 오류
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final UserData userData = dataSnapshot.getValue(UserData.class);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    // FMC 메시지 생성 start
                                    JSONObject root = new JSONObject();
                                    JSONObject notification = new JSONObject();
                                    notification.put("body", message);
                                    notification.put("title", getString(R.string.app_name));
                                    root.put("notification", notification);
//                                    이부분 바꿈 확인할것 userData.fcmToken
                                    root.put("to", userData.fcmToken);
                                    // FMC 메시지 생성 end

                                    URL Url = new URL(FCM_MESSAGE_URL);
                                    HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
                                    conn.setRequestMethod("POST");
                                    conn.setDoOutput(true);
                                    conn.setDoInput(true);
                                    conn.addRequestProperty("Authorization", "key=" + SERVER_KEY);
                                    conn.setRequestProperty("Accept", "application/json");
                                    conn.setRequestProperty("Content-type", "application/json");
                                    OutputStream os = conn.getOutputStream();
                                    os.write(root.toString().getBytes("utf-8"));
                                    os.flush();
                                    conn.getResponseCode();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

}