package com.example.travel_guide;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.travel_guide.model.Model;
import com.example.travel_guide.model.UserPost;

import java.util.List;

public class PostListRvViewModel extends ViewModel {

    LiveData<List<UserPost>> postList;

   public PostListRvViewModel(){
        postList = Model.instance.getAllPosts();
    }

    public LiveData<List<UserPost>> getPostList() {
        return postList;
    }

//    public void setPostList(LiveData<List<UserPost>> postList) {
//        this.postList = postList;
//    }
}
