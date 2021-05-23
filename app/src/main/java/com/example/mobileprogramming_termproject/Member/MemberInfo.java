package com.example.mobileprogramming_termproject.Member;

import java.util.ArrayList;

//회원 정보 저장
public class MemberInfo {

    private String name;
    private String phoneNumber;
    private String Date;
    private String adress;
    private String photoUrl;
    private String nickname;
    private String token;
    private ArrayList<String> bookmarkRecipe;

    public MemberInfo() {}

    public MemberInfo(String name, String phoneNumber, String adress, String Date, String photoUrl,String nickname,ArrayList<String> bookmarkRecipe,String token)
    {
        this.name=name;
        this.phoneNumber=phoneNumber;
        this.Date=Date;
        this.adress=adress;
        this.photoUrl = photoUrl;
        this.nickname=nickname;
        this.bookmarkRecipe = bookmarkRecipe;
        this.token=token;
    }
    public MemberInfo(String name, String phoneNumber, String adress, String Date)
    {
        this.name=name;
        this.phoneNumber=phoneNumber;
        this.Date=Date;
        this.adress=adress;
    }
    public MemberInfo(String name, String nickname, String photoUrl)
    {
        this.nickname=nickname;
        this.name=name;
        this.photoUrl = photoUrl;
    }
    public MemberInfo(String nickname,String photoUrl)
    {
        this.nickname=nickname;
        this.photoUrl = photoUrl;
    }
    public MemberInfo(String nickname)
    {
        this.nickname=nickname;

    }


    public String getName(){
        return name;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public String getDate(){
        return Date;
    }

    public String getAdress(){
        return adress;
    }

    public String getPhotoUrl(){
        return photoUrl;
    }

    public String getNickname(){
        return nickname;
    }

    public ArrayList<String> getBookmarkRecipe(){return this.bookmarkRecipe;}

    public void setBookmarkRecipe(ArrayList<String> bookmarkRecipe){this.bookmarkRecipe = bookmarkRecipe;}

    public String getToken(){return token;}

}
