package com.example.travel_guide.model;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.travel_guide.MyApplication;
import com.example.travel_guide.PostPage;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {
    public static final Model instance = new Model();
    ModelFirebase modelFirebase = new ModelFirebase();
    Executor executor = Executors.newFixedThreadPool(1);


    public enum PostListLoadingState{ //indicate the possible states
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
            UserPost userPost = new UserPost("name "+i,"location"+i,"about"+i,"catalog"+i);
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

        //get last local update date
        //TODO:: need to be at UserPost obj
        Long lastUpdateDate = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("PostsLastUpdateDate",0);

        // get from firebase all updated since last update date

//        modelFirebase.getAllPosts(lastUpdateDate,new ModelFirebase.GetAllPostsListener() {
//            @Override
//            public void onComplete(List<UserPost> list) {
//                executor.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        Long lud = new Long(0); // local update date
//                        Log.d("TAG","fb returned "+list.size());
//                        // add all record to local db
//                        for(UserPost us : list){
//                            AppLocalDB.db.userPostDao().insertAll(us);
//                            //AppLocalDB.db.userPostDao().delete(us);
//
//                            // update last local update date
//                            if(lud < us.getUpdateData()){
//                                lud = us.getUpdateData();
//                            }
//                        }
//
//                        // update last local update date
//                        MyApplication.getContext()
//                                .getSharedPreferences("TAG",Context.MODE_PRIVATE)
//                                .edit().putLong("PostsLastUpdateDate",lud).commit();
//                        // return all data to the caller
//                        List<UserPost>userPostList = AppLocalDB.db.userPostDao().getAll(); // get all date from local db
//
//                        listLiveDataPost.postValue(userPostList);// post will pass it to main thread
//                        postListLoadingState.postValue(PostListLoadingState.loaded);
//                    }
//                });
//
//            }
//        });

        modelFirebase.getAllPosts(lastUpdateDate, new ModelFirebase.GetAllPostsListener() {
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
    public void updateUserPost(UserPost userPost,AddPostListener listener){
        modelFirebase.updateUserPost(userPost,listener);
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
    public void updateUser (User user,AddUserListener listener){
        modelFirebase.updateUser(user,listener);
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
