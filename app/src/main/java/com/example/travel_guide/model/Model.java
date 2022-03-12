package com.example.travel_guide.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.travel_guide.MyApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {
    public static final Model instance = new Model();
    ModelFirebase modelFirebase = new ModelFirebase();
    public Executor executor = Executors.newFixedThreadPool(1);
    public Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());

    //User currentUser;
    MutableLiveData<User> LiveDataUser = new MutableLiveData<User>();

    public User getCurrentUser() {
        return LiveDataUser.getValue();
        //return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.LiveDataUser.setValue(currentUser);
       // this.currentUser = currentUser;
    }

    public Long getLastUpdateDate() {
        return MyApplication.getContext()
                .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                .getLong(UserPost.LAST_UPDATE, 0);
    }


    public enum PostListLoadingState { //indicate the possible states
        loading,
        loaded
    }

    MutableLiveData<PostListLoadingState> postListLoadingState = new MutableLiveData();

    public LiveData<PostListLoadingState> getPostListLoadingState() {
        return postListLoadingState;
    }


    //List<UserPost> userPostListData;
    private Model() {
        postListLoadingState.setValue(PostListLoadingState.loaded);
    }

    MutableLiveData<List<UserPost>> listLiveDataPost = new MutableLiveData<List<UserPost>>();

    //------------------------------------POST------------------------------------//

    public LiveData<List<UserPost>> getCategoryPosts(String categoryName, String userId, String location) {
        // if (listLiveDataPost.getValue() == null) {
        Long lastUpdateDate = getLastUpdateDate();
        //refreshCategoryPage(categoryName, userId, location);
        postListLoadingState.setValue(PostListLoadingState.loading);

        if (categoryName.equals("userSavedPost"))
            modelFirebase.getAllPosts(lastUpdateDate, new ModelFirebase.GetAllPostsListener() {
                @Override
                public void onComplete(List<UserPost> list) {
                    sortSavedPost(list, userId);
                    postListLoadingState.postValue(PostListLoadingState.loaded);
                }
            });
        else {
            modelFirebase.getAllPosts(new ModelFirebase.GetAllPostsListener() {
                @Override
                public void onComplete(List<UserPost> list) {


                    if (!categoryName.equals("allCategories"))
                        listLiveDataPost.setValue(sortCategory(list, categoryName, userId, location));

                    else //all categories
                        listLiveDataPost.setValue(list);
                    postListLoadingState.postValue(PostListLoadingState.loaded);

                }
            });
        }
        return listLiveDataPost;
    }

    private List<UserPost> sortCategory(List<UserPost> list, String categoryName, String userId, String location) {
        List<UserPost> lst = new ArrayList<UserPost>();
        if (!location.equals("")) {

            if (categoryName.equals("userCreatePosts")) {

                for (UserPost us : list) {
                    if (us.getUserId().equals(userId) && us.getLocation().equals(location))
                        lst.add(us);
                }
            } else {
                for (UserPost us : list) {
                    if (us.getCategory().equals(categoryName) && (us.getLocation().equals(location)))
                        lst.add(us);

                }
            }

        } else {
            if (categoryName.equals("userCreatePosts")) {

                for (UserPost us : list) {
                    if (us.getUserId().equals(userId))
                        lst.add(us);
                }
            } else {
                for (UserPost us : list) {
                    if (us.getCategory().equals(categoryName))
                        lst.add(us);
                }
            }
        }

        return lst;
    }

    private List<UserPost> sortSavedPost(List<UserPost> list, String userid) {
        List<UserPost> lst = new ArrayList<UserPost>();
        List<String> lstSaved = getUser(userid).getValue().getLstSaved();

        for (String spp : lstSaved) {
            for (UserPost us : list) {
                if (spp.equals(us.getId()))
                    lst.add(us);
            }
        }
        SaveInRoom(lst);
        return lst;
    }

    public void SaveInRoom(List<UserPost> lst) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Long lud = new Long(0);

                for (UserPost us : lst) {
                    System.out.println("list size : " + lst.size());
                    //AppLocalDB.db.userPostDao().delete(us);
                    AppLocalDB.db.userPostDao().insertAll(us);
                    if (lud < us.getUpdateDate()) {
                        lud = us.getUpdateDate();
                    }
                }
                //update last local update date
                MyApplication.getContext()
                        .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                        .edit().putLong(UserPost.LAST_UPDATE, lud).commit();

                List<UserPost> userPostList = AppLocalDB.db.userPostDao().getAll(); // get all data from local db
                listLiveDataPost.postValue(userPostList);// post will pass it to main thread
            }
        });
    }

    public void deleteSaveFromRoom(UserPost us) {
        executor.execute(() -> {
            Long lud = new Long(0);
            if (lud > us.getUpdateDate()) {
                lud = us.getUpdateDate();
            }
            MyApplication.getContext()
                    .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                    .edit().putLong(UserPost.LAST_UPDATE, lud).commit();
            AppLocalDB.db.userPostDao().delete(us);
        });
    }


    public void refreshCategoryPage(String category, String userId, String location) {
        getCategoryPosts(category, userId, location);
    }

    public void refreshPostList() {
        postListLoadingState.setValue(PostListLoadingState.loading);

        Long lastUpdateDate = getLastUpdateDate();

        modelFirebase.getAllPosts(lastUpdateDate, new ModelFirebase.GetAllPostsListener() {
            @Override
            public void onComplete(List<UserPost> list) {
                listLiveDataPost.setValue(list);
                postListLoadingState.setValue(PostListLoadingState.loaded);
            }
        });
    }

    public interface AddPostListener {
        void onComplete();
    }

    public void addUserPost(UserPost userPost, AddPostListener listener) {
        modelFirebase.addUserPost(userPost, new AddPostListener() {
            @Override
            public void onComplete() {
                refreshPostList();
                listener.onComplete();
            }
        });
    }

    public void updateUserPost(UserPost userPost, AddPostListener listener) {
        modelFirebase.updateUserPost(userPost, listener);
    }

    public interface GetPostById {
        void onComplete(UserPost userPost);
    }

    public UserPost getPostById(String postId, GetPostById listener) {
        modelFirebase.getPostById(postId, listener);
        return null;
    }


//    public interface DeletePostById {
//        void onComplete();
//    }

    public void deletePostById(UserPost userPost, AddPostListener listener) {
        //modelFirebase.deletePostById(postId,listener);
        // delete post from local db
        //executor.execute(()->{AppLocalDB.db.userPostDao().delete(userPost);});
        deleteSaveFromRoom(userPost);
        modelFirebase.updateUserPost(userPost, listener);
    }
    //------------------------------------END POST------------------------------------//
    //--------------------------------------------------------------------------------//


    //--------------------------------------------------------------------------------//
    //------------------------------------USER------------------------------------//

    public LiveData<User> getUser(String id) {

        if (LiveDataUser.getValue() == null) {
            refreshUser(id);
        }
        return LiveDataUser;
    }


    public void refreshUser(String id) {
        modelFirebase.getUserById(id, new GetUserById() {
            @Override
            public void onComplete(User user) {
                LiveDataUser.setValue(user);
            }
        });
    }

    public interface AddUserListener {
        void onComplete();
    }

    public interface AddUserToFBListener {
        void onComplete(String msg);
    }

    public interface OnCompleteGeneralListener {
        void onComplete(User user);
    }
//    public void addUser (User user,AddUserListener listener){
//        modelFirebase.addUser(user,listener);
//    }

    public void createUserWithEmail(String password,User user, AddUserToFBListener listener) {
        modelFirebase.createUserWithEmail(password, user, listener);
    }

    public void isUserIn(Model.OnCompleteGeneralListener listener) {
        modelFirebase.isUserIn(listener);
    }

    public void userSignIn(String email, String password, Model.OnCompleteGeneralListener listener) {
        modelFirebase.userSignIn(email, password, listener);
    }



//    public static void deleteCache(Context context) {
//        try {
//            File dir = context.getCacheDir();
//            deleteDir(dir);
//        } catch (Exception e) { e.printStackTrace();}
//    }
//    public static boolean deleteDir(File dir) {
//        if (dir != null && dir.isDirectory()) {
//            String[] children = dir.list();
//            for (int i = 0; i < children.length; i++) {
//                boolean success = deleteDir(new File(dir, children[i]));
//                if (!success) {
//                    return false;
//                }
//            }
//            return dir.delete();
//        } else if(dir!= null && dir.isFile()) {
//            return dir.delete();
//        } else {
//            return false;
//        }
//    }


    public void signOut(String userid) {
        executor.execute(() -> {
            Long lud = new Long(0);
            for (UserPost us : AppLocalDB.db.userPostDao().getAll()) {
                deleteSaveFromRoom(us);
            }
            MyApplication.getContext()
                    .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                    .edit().putLong(UserPost.LAST_UPDATE, lud).commit();

        });

        modelFirebase.signOut();
    }

    public void updateUser(User user, AddUserListener listener) {

        modelFirebase.updateUser(user, listener);
    }

    public interface GetUserById {
        void onComplete(User user);
    }

    public interface GetConnectedUser {
        void onComplete(User user);
    }

    public void getConnectedUser(GetConnectedUser listener) {
        modelFirebase.getConnectedUser(listener);
    }


    public void getUserById(String userId, GetUserById listener) {
//        if (LiveDataUser.getValue().getId().equals(userId))
////            listener.onComplete(LiveDataUser.getValue());
//        if(currentUser != null)
//            listener.onComplete(currentUser);
//        else
            modelFirebase.getUserById(userId, listener);
    }

    public interface DeleteUserById {
        void onComplete();
    }

    public void deleteUserById(String userId, DeleteUserById listener) {
        modelFirebase.deleteUserById(userId, listener);
    }

    public void initFireBaseAuto() {
        modelFirebase.initFireBaseAuto();
    }

    public interface GetUserId {
        void onComplete(String id);
    }

    public void getUserIdFromFB(GetUserId listener) {
        modelFirebase.getUserIdFromFB(listener);
    }

    public interface SaveImageListener {
        void onComplete(String url);
    }

    public void saveImage(Bitmap imageBitmap, String imageName, String savePath, SaveImageListener listener) {
        modelFirebase.saveImage(imageBitmap, imageName, savePath, listener);
    }


    //------------------------------------END USER------------------------------------//

    //------------------------------------Authentication------------------------------------//


    public boolean isSignedIn() {
        return modelFirebase.isSignedIn();
    }


}
