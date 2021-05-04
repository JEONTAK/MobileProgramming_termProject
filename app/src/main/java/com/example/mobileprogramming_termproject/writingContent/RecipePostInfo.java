package com.example.mobileprogramming_termproject.writingContent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

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
