package com.example.mobileprogramming_termproject.ui.alarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.mobileprogramming_termproject.MainActivity;

import java.util.ArrayList;


public class DBHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "alarm.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "alarm";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "alarm_title";
    private static final String COLUMN_CONTENT = "alarm_content";
    private static final String COLUMN_TOKEN = "alarm_token";
    private static final int DB_VERSION=1;


    public DBHelper(@Nullable Context context ) {

        super(context, DATABASE_NAME, null, DB_VERSION);
        this.context=context;
    }




    @Override
    public void onCreate(SQLiteDatabase db) {
//        데이터 베이스가 생성이 될떄 호출
//        데이터베이스 -> 테이블 ->컬럼 -> 값
//        AUTOINCREMENT 는 자동 업로드
        db.execSQL("CREATE TABLE IF NOT EXISTS alarm(id INTEGER PRIMARY KEY AUTOINCREMENT,title TEXT NOT NULL," +
                "content TEXT NOT NULL,token TEXT NOT NULL )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

//    SELECT 문 (하일 목록 조회)




//    insert 문 할일 목록 DB 만든다

    public void InsertAlarm(String _title,String _content ,String _token ){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_TITLE,_title);
        cv.put(COLUMN_CONTENT,_content);
        cv.put(COLUMN_TOKEN,_token);
        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1)
        {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context, "데이터 추가 성공", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readRecordAlarm(){
        SQLiteDatabase db=getReadableDatabase();
        String[]projection={
                COLUMN_ID,
                COLUMN_TITLE,
                COLUMN_CONTENT,
                COLUMN_TOKEN
        };

        Cursor cursor=db.query(
            TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null

        );
        return cursor;
    }
    public ArrayList<AlarmItem> getTodoList(){
        ArrayList<AlarmItem>alarmItems=new ArrayList<>();

        SQLiteDatabase db=getReadableDatabase();
        String[]projection={
                COLUMN_ID,
                COLUMN_TITLE,
                COLUMN_CONTENT,
                COLUMN_TOKEN
        };

        Cursor cursor=db.query(
                TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null

        );
        if(cursor.getCount()!=0){
//            조회된 데이터가 무조건 있다.
//            데이터를 하나씩 이동
            while(cursor.moveToNext()){
                int id=cursor.getInt(cursor.getColumnIndex("id"));
                String title=cursor.getString(cursor.getColumnIndex("title"));
                String content=cursor.getString(cursor.getColumnIndex("content"));
                String token=cursor.getString(cursor.getColumnIndex("token"));

                AlarmItem alarmItem=new AlarmItem();
                alarmItem.setId(id);
                alarmItem.setTitle(title);
                alarmItem.setContent(content);
                alarmItem.setWriteDate(token);
                alarmItems.add(alarmItem);
            }
            cursor.close();

        }
        return alarmItems;
    }


//    //     UPDATE 문 (할일 목록을 수정한다).
//    public void UpdateAlarm(String _title,String _content,String _writeDate,String _beforeDate){
//        SQLiteDatabase db=getWritableDatabase();
//        db.execSQL("UPDATE AlarmList SET title='"+_title+"',content='"+_content+"',  writeDate='"+_writeDate+"'WHERE writeDate='" +_beforeDate+"'");
//    }
//
////    DELETE 문 (할일 목록을 제거한다)
//
//    public void DeleteAlarm(String _beforeDate){
//        SQLiteDatabase db=getWritableDatabase();
//        db.execSQL("DELETE FROM AlarmList WHERE writeDate= '" +_beforeDate +"'");
//    }

}
