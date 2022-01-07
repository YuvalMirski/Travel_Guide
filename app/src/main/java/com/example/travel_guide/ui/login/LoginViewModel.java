package com.example.travel_guide.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.travel_guide.model.Model;
import com.example.travel_guide.model.User;

public class LoginViewModel extends ViewModel {

    LiveData<User> userLiveData;
    String userId;

    public LoginViewModel() {
        Model.instance.getUserIdFromFB(new Model.GetUserId() {
            @Override
            public void onComplete(String id) {
                userId = id;
                userLiveData = Model.instance.getUser(id);
            }
        });
    }
    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }
    public String getUserId() {
        return userId;
    }

}
