package com.example.travel_guide.model;

import androidx.navigation.Navigation;

import com.example.travel_guide.PostPage;

import java.util.LinkedList;
import java.util.List;

public class Model {
    public static final Model instance = new Model();

    ModelFirebase modelFirebase = new ModelFirebase();

    //List<UserPost> userPostListData;
    private Model(){
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

    public interface GetAllPostsListener{
        void onComplete(List<UserPost> list);
    }
    public void getAllPosts(GetAllPostsListener listener){
        //  return userPostListData;
        modelFirebase.getAllPosts(listener);
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
