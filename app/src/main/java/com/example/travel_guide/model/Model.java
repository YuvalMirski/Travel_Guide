package com.example.travel_guide.model;

import java.util.LinkedList;
import java.util.List;

public class Model {
    public static final Model instance = new Model();

    private Model(){
        for(int i=0;i<10;i++){
            //User u = new User();
            //data.add(u);
        }
    }

    List<User> data = new LinkedList<User>();

    public List<User> getAllStudents(){
        return data;
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
