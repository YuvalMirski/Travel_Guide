package com.example.travel_guide.view.HomePage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.travel_guide.model.Model;
import com.example.travel_guide.model.User;

public class HomePageViewModel extends ViewModel {

    LiveData<User> userLiveData;
    String userId;

    public HomePageViewModel() {
        Model.instance.getUserIdFromFB(new Model.GetUserId() {
            @Override
            public void onComplete(String id) {
                userId = id;
                userLiveData = Model.instance.getUser(id);
            }
        });
    }

    public LiveData<User> getUserLiveData() { return userLiveData; }
    public String getUserId() {
        return userId;
    }
}
