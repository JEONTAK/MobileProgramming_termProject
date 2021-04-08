package com.example.mobileprogramming_termproject.ui.convenience_category;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class convenience_categoryViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public convenience_categoryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is convenience_category fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}