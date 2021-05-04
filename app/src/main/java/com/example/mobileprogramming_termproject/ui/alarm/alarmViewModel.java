package com.example.mobileprogramming_termproject.ui.alarm;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class alarmViewModel {
    private MutableLiveData<String> mText;

    public alarmViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is bookmark fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
