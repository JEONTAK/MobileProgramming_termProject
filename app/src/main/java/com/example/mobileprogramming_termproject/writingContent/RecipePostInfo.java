package com.example.mobileprogramming_termproject.writingContent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

//레시피 글을 저장하기위한 형식
//타이틀 이미지, 제목, 재료, 설명, 작성자, 작성일자, 추천수, 레시피글 id, 추천 유저, 가격, 음식카테고리, 태그카테고리를 저장.
public class RecipePostInfo implements Serializable {
    private String titleImage;
    private String title;
    private String ingredient;
    private ArrayList<String> content;
    private String publisher;
    private Date createdAt;
    private long recom;
    private String recipeId;
    private ArrayList<String> recomUserId;
    private long price;
    private String foodCategory;
    private String tagCategory;


    public RecipePostInfo(String titleImage, String title, String ingredient, ArrayList<String> content, String publisher,
                          Date createdAt , long recom, String recipeId, ArrayList<String> recomUserId,
                          long price, String foodCategory, String tagCategory) {
        this.titleImage = titleImage;
        this.title = title;
        this.ingredient = ingredient;
        this.content = content;
        this.publisher = publisher;
        this.createdAt = createdAt;
        this.recom = recom;
        this.recipeId = recipeId;
        this.recomUserId = recomUserId;
        this.price = price;
        this.foodCategory = foodCategory;
        this.tagCategory = tagCategory;
    }

    public RecipePostInfo(long recom){
        this.recom = recom;
    }


    public String getTitleImage(){ return this.titleImage;}
    public void setTitleImage(String titleImage){this.titleImage = titleImage;}
    public String getIngredient(){ return this.ingredient;}
    public void setIngredient(String ingredient){this.ingredient = ingredient;}
    public String getTitle(){ return this.title;}
    public void setTitle(String title){this.title = title;}
    public ArrayList<String> getContent(){ return  this.content;}
    public void setContent(ArrayList<String> content){this.content = content;}
    public String getPublisher(){return this.publisher;}
    public void setPublisher(String publisher){this.publisher = publisher;}
    public Date getCreatedAt(){return this.createdAt;}
    public void setCreatedAt(Date publisher){this.createdAt = createdAt;}
    public long getRecom(){ return this.recom;}
    public void setRecom(long recom){this.recom = recom;}
    public String getRecipeId(){ return this.recipeId;}
    public void setRecipeId(String title){this.recipeId = recipeId;}
    public ArrayList<String> getRecomUserId(){ return  this.recomUserId;}
    public void setRecomUserId(ArrayList<String> content){this.recomUserId = recomUserId;}
    public long getPrice(){ return this.price;}
    public void setPrice(long price){this.price = price;}
    public String getFoodCategory(){ return this.foodCategory;}
    public void setFoodCategory(String foodCategory){this.foodCategory = foodCategory;}
    public String getTagCategory(){ return this.tagCategory;}
    public void setTagCategory(String tagCategory){this.tagCategory = tagCategory;}

}
