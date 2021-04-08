package com.example.mobileprogramming_termproject.ui.recipe_category;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class recipe_categoryViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public recipe_categoryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is recipe_category fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}