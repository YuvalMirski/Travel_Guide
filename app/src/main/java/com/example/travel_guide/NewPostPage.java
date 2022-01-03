package com.example.travel_guide;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.travel_guide.model.Model;
import com.example.travel_guide.model.UserPost;

import java.util.Random;

// here User will be able to create a new post

public class NewPostPage extends Fragment {

    EditText postName;
    EditText location;
    EditText type;
    EditText about;

    String new_name,new_location, new_about, new_id, new_category;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_post_page, container, false);

        // the new id will be size of post list (last post +1)
        // need to add check if the id not exist
        if(Model.instance.getAllPosts().getValue() != null) // there aren't any posts
             new_id = String.valueOf(Model.instance.getAllPosts().getValue().size());
        else{
            //TODO :: change logic of both conditions we need id that not exist
            // the way it should be is that User creates Post, rn we are creating them in a different way
            new_id = getRand(50,500);
        }

        postName = view.findViewById(R.id.post_name_post_page_new_et);
        location = view.findViewById(R.id.location_post_page_new_et);
        type = view.findViewById(R.id.type_post_page_new_et);
        about = view.findViewById(R.id.about_post_page_new_et);

        Button addPost = view.findViewById(R.id.add_post_page_new_btn);
        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_name = postName.getText().toString();
                new_location = location.getText().toString();
                new_category = type.getText().toString();
                new_about = about.getText().toString();
                UserPost un = new UserPost(new_name,new_location,new_about,new_id,new_category);

                Model.instance.addUserPost(un,()->{
                    Navigation.findNavController(postName).navigateUp();
                });
            }
        });

        return view;
    }

    public String getRand(int n1, int n2) {
        Random rand = new Random();
        int min = n1, max = n2;
        return String.valueOf(rand.nextInt(max - min + 1) + min);
    }
}