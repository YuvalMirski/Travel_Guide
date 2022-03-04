package com.example.travel_guide;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.travel_guide.model.Model;
import com.example.travel_guide.model.User;
import com.example.travel_guide.model.UserPost;

import java.util.List;

public class PostListRvViewModel extends ViewModel {

    LiveData<List<UserPost>> postListSaved;
    LiveData<List<UserPost>> categoryPostList;
    LiveData<User> userLiveData;


   public PostListRvViewModel(){
        //postList = Model.instance.getAllPosts();
   }
   public void demoCtor(String categoryName,String userId,String location){
       //postList = Model.instance.getAllPosts();
       categoryPostList = Model.instance.getCategoryPosts(categoryName,userId,location);
       userLiveData = Model.instance.getUser(userId);
   }

//    public LiveData<List<UserPost>> getPostList() {
//        return postList;
//    }

    public LiveData<List<UserPost>> getCategoryPostList() {
        //categoryPostList = Model.instance.getCategoryPosts(categoryName);
        return categoryPostList;
    }
    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }
}
