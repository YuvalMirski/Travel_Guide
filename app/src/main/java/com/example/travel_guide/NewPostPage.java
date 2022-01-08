package com.example.travel_guide;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.travel_guide.model.Model;
import com.example.travel_guide.model.UserPost;
import com.example.travel_guide.ui.signUp.SignUpDirections;

import java.io.IOException;
import java.util.Random;

// here User will be able to create a new post

public class NewPostPage extends Fragment {

    EditText postName;
    EditText location;
    EditText type;
    EditText about;
    ImageView postPic;
    Bitmap imageBitmap;

    String new_name,new_location, new_about, new_id, new_category;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_post_page, container, false);

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
        postPic = view.findViewById(R.id.picture_post_page_new_);

        ImageButton galleryBtn = view.findViewById(R.id.addPost_gallery_imb);
        galleryBtn.setOnClickListener(v -> openGallery());

        Button addPost = view.findViewById(R.id.add_post_page_new_btn);
        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_name = postName.getText().toString();
                new_location = location.getText().toString();
                new_category = type.getText().toString();
                new_about = about.getText().toString();
                UserPost userPost = new UserPost(new_name,new_location,new_about,new_category);

                if(imageBitmap!=null) {
                    Model.instance.saveImage(imageBitmap, new_name+ ".jpg", url -> {
                        userPost.setPostImgUrl(url);
                        Model.instance.addUserPost(userPost,()->{ Navigation.findNavController(v).navigate(NewPostPageDirections.actionNewPostPageToPostListRvFragment());
                             //Navigation.findNavController(v).navigate(NewPostPageDirections.actionNewPostPageToPostListRvFragment());
                        });
                    });
                }
                else
                {
                    //TODO:: alert "you have to add image"
                }
            }
        });

        return view;
    }


    final static int SELECT_PICTURE = 200;


    private void openGallery(){
        // Create intent for picking a photo from the gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                imageBitmap = null;
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),selectedImageUri);
                } catch (IOException e) { e.printStackTrace(); }

                if (imageBitmap != null) {
                    postPic.setImageBitmap(imageBitmap);
                }
            }
        }
    }













    public String getRand(int n1, int n2) {
        Random rand = new Random();
        int min = n1, max = n2;
        return String.valueOf(rand.nextInt(max - min + 1) + min);
    }
}