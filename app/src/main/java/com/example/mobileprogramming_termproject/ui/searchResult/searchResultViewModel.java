package com.example.mobileprogramming_termproject.ui.searchResult;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class searchResultViewModel {
    private MutableLiveData<String> mText;

    public searchResultViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is searchResult fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}