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
            UserPost userPost = new UserPost("name "+i,"location"+i,"about"+i,""+i,"catalog"+i);
            //userPostListData.add(userPost);
            addUserPost(userPost,()->{
                System.out.println("kdjfkd");
            });
        }
    }

   // List<User> data = new LinkedList<User>();
   // List<UserPost> userPostListData = new LinkedList<UserPost>();


//    public void getAllPosts(GetAllPostsListener listener){
//        //  return userPostListData;
//        modelFirebase.getAllPosts(listener);
//    }

    MutableLiveData<List<UserPost>> listLiveDataPost = new MutableLiveData<List<UserPost>>();

    MutableLiveData<List<User>>listLiveDataUser = new MutableLiveData<List<User>>();
    //------------------------------------POST------------------------------------//

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
        modelFirebase.addUserPost(userPost,listener);
    }

    public interface GetPostById{
        void onComplete(UserPost userPost);
    }

    public UserPost getPostById(String postId, GetPostById listener) {
        modelFirebase.getPostById(postId,listener);
        return null;
    }


//    public int getPostByPosition(String PostId) throws Exception {
//        for(int i = 0 ; i< data.size() ; i++)
//        {
//            if(data.get(i).getId().equals(PostId))
//                return i;
//        }
//        throw new Exception("no id matched");
//    }
    public interface DeletePostById{
        void onComplete();
    }
    public void deletePostById(String postId, DeletePostById listener){
        modelFirebase.deletePostById(postId,listener);
    }
    //------------------------------------END POST------------------------------------//
    //--------------------------------------------------------------------------------//



    //--------------------------------------------------------------------------------//
    //------------------------------------USER------------------------------------//

    public LiveData<List<User>>getAllUsers(){

        if(listLiveDataUser.getValue() == null){
            refreshPostList();
        }
        return listLiveDataUser;
    }

    public void refreshUserList(){
    }

    public interface AddUserListener{
        void onComplete();
    }
    public void addUser (User user,AddUserListener listener){
        modelFirebase.addUser(user,listener);
    }

    public interface GetUserById{
        void onComplete(User user);
    }

    public User getUserById(String userId, GetUserById listener) {
        modelFirebase.getUserById(userId,listener);
        return null;
    }


    public interface DeleteUserById{
        void onComplete();
    }
    public void deleteUserById(String userId, DeleteUserById listener){
        modelFirebase.deleteUserById(userId,listener);
    }
    //------------------------------------END USER------------------------------------//


}
