package com.example.mobileprogramming_termproject.ui.alarm;

public class AlarmItem {
    private int id;
    private String title;
    private String content;
    private String writeDate;
    private String token;

    public AlarmItem() {

    }
    public AlarmItem(int id ,String title,String content,String token) {
            this.id=id;
            this.title=title;
            this.content=content;
            this.token=token;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String text) {
        this.content = content;
    }

    public String getWriteDate() {
        return writeDate;
    }

    public void setWriteDate(String writeDate) {
        this.writeDate = writeDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
