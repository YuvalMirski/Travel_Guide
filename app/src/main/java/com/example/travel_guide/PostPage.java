package com.example.travel_guide;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.travel_guide.model.Model;
import com.example.travel_guide.model.UserPost;
import com.squareup.picasso.Picasso;


public class PostPage extends Fragment {
    TextView postName;
    TextView location;
    TextView type; //this is category
    TextView about;
    ImageView postImg;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_page, container, false);

        ImageView img = (ImageView) (view.findViewById(R.id.picture_post_page_details_));
       // img.setImageResource(R.drawable.);

        String postId = PostPageArgs.fromBundle(getArguments()).getPostId();

        Model.instance.getPostById(postId, new Model.GetPostById() {
            @Override
            public void onComplete(UserPost userPost) {
              postName.setText(userPost.getName());
              location.setText(userPost.getLocation());
              type.setText(userPost.getCategory());
              about.setText(userPost.getAbout());

                if(userPost.getPostImgUrl()!=null) {
                    Picasso.get()
                            .load(userPost.getPostImgUrl())
                            .into(postImg);
                }
            }
        });

        postName = view.findViewById(R.id.post_name_post_page_details_tv);
        location = view.findViewById(R.id.location_post_page_details_tv);
        type = view.findViewById(R.id.type_post_page_details_tv);
        about = view.findViewById(R.id.about_post_page_details_);
        postImg = view.findViewById(R.id.picture_post_page_details_);

        Button editBtn = view.findViewById(R.id.edit_post_page_details_btn);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(PostPageDirections.actionPostPageToEditPostPage(postId));
            }
        });

        return view;
    }
}