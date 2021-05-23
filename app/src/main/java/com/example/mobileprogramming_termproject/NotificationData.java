package com.example.mobileprogramming_termproject;

public class NotificationData {

    public int noti_profile;
    public String noti_title;
    public String noti_content;

    public NotificationData(int noti_profile, String noti_title, String noti_content) {
        this.noti_profile = noti_profile;
        this.noti_title = noti_title;
        this.noti_content = noti_content;
    }

    public int getNoti_profile() {
        return noti_profile;
    }

    public void setNoti_profile(int noti_profile) {
        this.noti_profile = noti_profile;
    }

    public String getNoti_title() {
        return noti_title;
    }

    public void setNoti_title(String noti_title) {
        this.noti_title = noti_title;
    }

    public String getNoti_content() {
        return noti_content;
    }

    public void setNoti_content(String noti_content) {
        this.noti_content = noti_content;
    }
}
