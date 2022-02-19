package com.example.travel_guide.model;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.travel_guide.MyApplication;

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
    }

    MutableLiveData<List<UserPost>> listLiveDataPost = new MutableLiveData<List<UserPost>>();

    MutableLiveData<User>LiveDataUser = new MutableLiveData<User>();
    //------------------------------------POST------------------------------------//

    public LiveData<List<UserPost>>getCategoryPosts(String categoryName,String userId,String location){
        if(listLiveDataPost.getValue() == null){
            refreshCategoryPage(categoryName,userId,location);
        }
        return listLiveDataPost;
    }


    public void refreshCategoryPage(String category,String userId,String location){

        if(category.equals("userSavedPost"))
            refreshPageSaved(userId);

      else if(!category.equals("allCategories"))
            refreshPageCategory(userId,category,location);

        else
            refreshPostList();
    }

    public void refreshPageSaved(String userid){

        List<String> lstSaved = getUser(userid).getValue().getLstSaved();

        postListLoadingState.setValue(PostListLoadingState.loading);
        Long lastUpdateDate = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("PostsLastUpdateDate",0);
        modelFirebase.getUserSavedPost(userid, lstSaved,lastUpdateDate, new ModelFirebase.GetAllPostsListener() {
            @Override
            public void onComplete(List<UserPost> list) {
                listLiveDataPost.setValue(list);
                postListLoadingState.setValue(PostListLoadingState.loaded);
            }
        });
    }
    public void refreshPageCategory(String userId,String category,String location){

        postListLoadingState.setValue(PostListLoadingState.loading);
        modelFirebase.getCategoryPosts(userId,category,location, new ModelFirebase.GetAllPostsListener() {
            @Override
            public void onComplete(List<UserPost> list) {
                listLiveDataPost.setValue(list);
                postListLoadingState.setValue(PostListLoadingState.loaded);
            }
        });
    }
    // go to firebase
    public void refreshPostList(){
        postListLoadingState.setValue(PostListLoadingState.loading);

        //get last local update date
        //TODO:: need to be at UserPost obj

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

        modelFirebase.getAllPosts( new ModelFirebase.GetAllPostsListener() {
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
        modelFirebase.addUserPost(userPost, new AddPostListener() {
            @Override
            public void onComplete() {
                refreshPostList();
                listener.onComplete();
            }
        });
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

    public LiveData<User>getUser(String id){

        if(LiveDataUser.getValue() == null){
            refreshUser(id);
        }
        return LiveDataUser;
    }



    public void refreshUser(String id){

        modelFirebase.getUserById(id, new GetUserById() {
            @Override
            public void onComplete(User user) {
                LiveDataUser.setValue(user);
            }
        });
    }

    public interface AddUserListener{
        void onComplete();
    }

    public interface AddUserToFBListener{
        void onComplete(String msg);
    }

    public interface OnCompleteGeneralListener{
        void onComplete(User user);
    }
//    public void addUser (User user,AddUserListener listener){
//        modelFirebase.addUser(user,listener);
//    }

    public void createUserWithEmail(User user,AddUserToFBListener listener) {
        modelFirebase.createUserWithEmail(user,listener);
    }
    public void userSignIn(String email, String password,Model.OnCompleteGeneralListener listener){
        modelFirebase.userSignIn(email,password,listener);
    }
    public void signOut(){
        modelFirebase.signOut();
    }

    public void updateUser (User user,AddUserListener listener){
        modelFirebase.updateUser(user,listener);
    }

    public interface GetUserById{
        void onComplete(User user);
    }

    public void getUserById(String userId, GetUserById listener) {
        modelFirebase.getUserById(userId,listener);
    }

    public interface DeleteUserById{
        void onComplete();
    }
    public void deleteUserById(String userId, DeleteUserById listener){
        modelFirebase.deleteUserById(userId,listener);
    }

    public void initFireBaseAuto(){
        modelFirebase.initFireBaseAuto();
    }

    public interface GetUserId{
        void onComplete(String id);
    }

    public void getUserIdFromFB(GetUserId listener) {
        modelFirebase.getUserIdFromFB(listener);
    }

    public interface SaveImageListener{
        void onComplete(String url);
    }
    public void saveImage(Bitmap imageBitmap, String imageName, String savePath, SaveImageListener listener) {
        modelFirebase.saveImage(imageBitmap, imageName, savePath, listener);
    }


    //------------------------------------END USER------------------------------------//


}
