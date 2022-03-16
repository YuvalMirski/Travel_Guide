package com.example.travel_guide;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.travel_guide.model.Model;
import com.example.travel_guide.model.User;

public class NewPostPageViewModel extends ViewModel {

    LiveData<User> userLiveData;
    String userId;

    public NewPostPageViewModel() { }

    public void updateUser(String userId){
        this.userId = userId;
        userLiveData = Model.instance.getUser(userId);
    }

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }
}