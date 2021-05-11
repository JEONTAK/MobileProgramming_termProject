package com.example.mobileprogramming_termproject.Member;

import java.util.ArrayList;

//회원 정보 저장
public class MemberInfo {

    private String name;
    private String phoneNumber;
    private String Date;
    private String adress;
    private String photoUrl;
    private ArrayList<String> bookmarkRecipe;

    public MemberInfo(String name, String phoneNumber, String adress, String Date, String photoUrl, ArrayList<String> bookmarkRecipe)
    {
        this.name=name;
        this.phoneNumber=phoneNumber;
        this.Date=Date;
        this.adress=adress;
        this.photoUrl = photoUrl;
        this.bookmarkRecipe = bookmarkRecipe;
    }
    public MemberInfo(String name, String phoneNumber, String adress, String Date)
    {
        this.name=name;
        this.phoneNumber=phoneNumber;
        this.Date=Date;
        this.adress=adress;
    }
    public MemberInfo(String name, String photoUrl)
    {
        this.name=name;
        this.photoUrl = photoUrl;
    }
    public MemberInfo(String name)
    {
        this.name=name;
    }


    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name=name;
    }

    public String getPhoneNumber(){
        return this.phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber=phoneNumber;
    }

    public String getDate(){
        return this.Date;
    }
    public void setDate(String Date){
        this.Date=Date;
    }

    public String getAdress(){
        return this.adress;
    }
    public void setAdress(String adress){
        this.adress=adress;
    }

    public String getPhotoUrl(){
        return this.photoUrl;
    }
    public void setPhotoUrl(String photoUrl){
        this.photoUrl = photoUrl;
    }

    public ArrayList<String> getBookmarkRecipe(){return this.bookmarkRecipe;}
    public void setBookmarkRecipe(ArrayList<String> bookmarkRecipe){this.bookmarkRecipe = bookmarkRecipe;}

}
