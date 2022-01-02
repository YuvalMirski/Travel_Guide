package com.example.travel_guide;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.travel_guide.model.Model;
import com.example.travel_guide.model.UserPost;


public class EditPostPage extends Fragment {

    // note - it is not possible to change Post id

    EditText postName;
    EditText location;
    EditText type;
    EditText about;
    String new_name,new_location, new_about, new_id, new_category;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_post_page, container, false);

        String postId = EditPostPageArgs.fromBundle(getArguments()).getPostId();
        new_id = postId;
        Model.instance.getPostById(postId, new Model.GetPostById() {
            @Override
            public void onComplete(UserPost userPost) {
                postName.setText(userPost.getName());
                location.setText(userPost.getLocation());
                type.setText(userPost.getCategory());
                about.setText(userPost.getAbout());
            }
        });

        postName = view.findViewById(R.id.post_name_post_page_edit_et);
        location = view.findViewById(R.id.location_post_page_edit_et);
        type = view.findViewById(R.id.type_post_page_edit_et);
        about = view.findViewById(R.id.about_post_page_edit_et);

        Button saveBtn = view.findViewById(R.id.save_post_page_edit_delete_btn);
        Button deleteBtn = view.findViewById(R.id.delete_post_page_edit_btn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
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

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.instance.deletePostById(postId,()->{
                    Navigation.findNavController(v).navigate(EditPostPageDirections.actionEditPostPageToPostListRvFragment());
                });
            }
        });

        return view;

    }
}