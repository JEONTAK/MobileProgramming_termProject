package com.example.mobileprogramming_termproject.writingContent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

//자유게시글 정보 를 저장하기 위한 유형
//제목, 내용, 작성자, 유저이름, 작성일자, 추천수, 댓글, 작성글 id, 추천유저 id값을 저장.
public class FreePostInfo implements Serializable {
    private String title;
    private String content;
    private String publisher;
    private String userName;
    private Date createdAt;
    private long recom;
    private ArrayList<String> comment;
    private String postId;
    private ArrayList<String> recomUserId;

    public FreePostInfo(String title, String content, String publisher, String userName, Date createdAt, long recom, ArrayList<String> comment, String postId, ArrayList<String> recomUserId){
        this.title = title;
        this.content = content;
        this.publisher = publisher;
        this.userName = userName;
        this.createdAt = createdAt;
        this.recom = recom;
        this.comment = comment;
        this.postId = postId;
        this.recomUserId = recomUserId;
    }

    public String getTitle(){ return this.title;}
    public void setTitle(String title){this.title = title;}
    public String getContent(){ return  this.content;}
    public void setContent(String content){this.content = content;}
    public String getPublisher(){return this.publisher;}
    public void setPublisher(String publisher){this.publisher = publisher;}
    public String getUserName(){return this.userName;}
    public void setUserName(String userName){this.userName = userName;}
    public Date getCreatedAt(){return this.createdAt;}
    public void setCreatedAt(Date publisher){this.createdAt = createdAt;}
    public long getRecom(){return this.recom;}
    public void setRecom(long recom){this.recom = recom;}
    public ArrayList<String> getComment(){return this.comment;}
    public void setComment(ArrayList<String> comment){this.comment = comment;}
    public String getPostId(){ return  this.postId;}
    public void setPostId(String postId){this.postId = postId;}
    public ArrayList<String> getRecomUserId(){return this.recomUserId;}
    public void setRecomUserId(ArrayList<String> recomUserId){this.recomUserId = recomUserId;}

}
