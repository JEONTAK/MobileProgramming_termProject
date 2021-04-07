package com.example.mobileprogramming_termproject.ui.recipe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class recipeViewModel {
    private MutableLiveData<String> mText;

    public recipeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is recipe fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}