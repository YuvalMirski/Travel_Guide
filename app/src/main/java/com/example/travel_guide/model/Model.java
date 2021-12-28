package com.example.travel_guide.model;

import com.example.travel_guide.PostPage;

import java.util.LinkedList;
import java.util.List;

public class Model {
    public static final Model instance = new Model();

    //List<UserPost> userPostListData;
    private Model(){
        for(int i=0;i<10;i++){
            PostPage a = new PostPage();
            //User u = new User();
            //data.add(u);
            UserPost userPost = new UserPost("name "+i,"location"+i,"type"+i,"about"+i,""+i,"catalog"+i);
            userPostListData.add(userPost);
        }
    }

    List<User> data = new LinkedList<User>();
    List<UserPost> userPostListData = new LinkedList<UserPost>();

    public List<UserPost> getAllPosts(){
        return userPostListData;
    }

    public void addStudent(User student){
        data.add(student);
    }

    public User getStudentById(String studentId) {
        for(User s : data)
        {
            if(s.getId().equals(studentId))
                return s;
        }

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
