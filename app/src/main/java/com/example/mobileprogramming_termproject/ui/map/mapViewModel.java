package com.example.mobileprogramming_termproject.ui.map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class mapViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public mapViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is map fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}