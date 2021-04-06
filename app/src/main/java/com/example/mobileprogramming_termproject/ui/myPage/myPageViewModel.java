package com.example.mobileprogramming_termproject.ui.myPage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class myPageViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public myPageViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is mypage fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}