package com.example.mobileprogramming_termproject.ui.community;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class communityViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public communityViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is community fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}