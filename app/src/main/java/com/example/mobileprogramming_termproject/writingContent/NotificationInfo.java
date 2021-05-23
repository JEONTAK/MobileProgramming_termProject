package com.example.mobileprogramming_termproject.writingContent;

import com.example.mobileprogramming_termproject.service.NotificationModel;

import java.io.Serializable;
import java.util.Date;

public class NotificationInfo implements Serializable {
    private String title;
    private String content;
    private String publisher;
    private String userName;

    public NotificationInfo(String title, String content, String publisher, String userName ){
        this.title = title;
        this.content = content;
        this.publisher = publisher;
        this.userName = userName;

    }

    public String getTitle(){ return this.title;}
    public void setTitle(String title){this.title = title;}
    public String getContent(){ return  this.content;}
    public void setContent(String content){this.content = content;}
    public String getPublisher(){return this.publisher;}
    public void setPublisher(String publisher){this.publisher = publisher;}
    public String getUserName(){return this.userName;}
    public void setUserName(String userName){this.userName = userName;}
}
