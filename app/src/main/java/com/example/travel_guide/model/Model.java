package com.example.travel_guide.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;

import com.example.travel_guide.PostPage;

import java.util.LinkedList;
import java.util.List;

public class Model {
    public static final Model instance = new Model();
    ModelFirebase modelFirebase = new ModelFirebase();


    public enum PostListLoadingState{ //indicate the posible states
        loading,
        loaded
    }

    MutableLiveData<PostListLoadingState> postListLoadingState = new MutableLiveData();

    public LiveData<PostListLoadingState> getPostListLoadingState() {
        return postListLoadingState;
    }




    //List<UserPost> userPostListData;
    private Model(){
        postListLoadingState.setValue(PostListLoadingState.loaded);
        for(int i=0;i<10;i++){
            PostPage a = new PostPage();
            //User u = new User();
            //data.add(u);
            UserPost userPost = new UserPost("name "+i,"location"+i,"type"+i,"about"+i,""+i,"catalog"+i);
            userPostListData.add(userPost);
            addUserPost(userPost,()->{
                System.out.println("kdjfkd");
            });
        }
    }

    List<User> data = new LinkedList<User>();
    List<UserPost> userPostListData = new LinkedList<UserPost>();


//    public void getAllPosts(GetAllPostsListener listener){
//        //  return userPostListData;
//        modelFirebase.getAllPosts(listener);
//    }

    MutableLiveData<List<UserPost>> listLiveDataPost = new MutableLiveData<List<UserPost>>();

    public LiveData<List<UserPost>>getAllPosts(){

        if(listLiveDataPost.getValue() == null){
        refreshPostList();
        }
        return listLiveDataPost;
    }

    // go to firebase
    public void refreshPostList(){
        postListLoadingState.setValue(PostListLoadingState.loading);
        modelFirebase.getAllPosts(new ModelFirebase.GetAllPostsListener() {
            @Override
            public void onComplete(List<UserPost> list) {
                listLiveDataPost.setValue(list);
                postListLoadingState.setValue(PostListLoadingState.loaded);

            }
        });
    }

    public interface AddPostListener{
        void onComplete();
    }
    public void addUserPost (UserPost userPost,AddPostListener listener){
        //data.add(student);
        modelFirebase.addUserPost(userPost,listener);
    }

    public interface GetPostById{
        void onComplete(UserPost userPost);
    }

    public UserPost getPostById(String postId, GetPostById listener) {
//        for(User s : data)
//        {
//            if(s.getId().equals(studentId))
//                return s;
//        }
//
//        return null;
        modelFirebase.getPostById(postId,listener);
        return null;
    }


    public int getStudentByPosition(String studentId) throws Exception {
        for(int i = 0 ; i< data.size() ; i++)
        {
            if(data.get(i).getId().equals(studentId))
                return i;
        }
        throw new Exception("no id matched");
    }

    public void deleteStudentById(String studentId){
        for(int i = 0 ; i< data.size() ; i++)
        {
            if(data.get(i).getId().equals(studentId))
            {
                data.remove(i);
                return;
            }
        }
    }
}
