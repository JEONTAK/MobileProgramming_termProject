package com.example.mobileprogramming_termproject.ui.bookmark;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class bookmarkViewModel {
    private MutableLiveData<String> mText;

    public bookmarkViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is bookmark fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}