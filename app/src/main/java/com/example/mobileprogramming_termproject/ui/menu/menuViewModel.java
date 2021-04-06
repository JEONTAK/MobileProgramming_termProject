package com.example.mobileprogramming_termproject.ui.menu;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class menuViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public menuViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is menu fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}