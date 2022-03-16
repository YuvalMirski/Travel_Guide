package com.example.travel_guide;

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
    TextView postName, location, category, about;
    ImageView postImg;
    String userNamePostId, userLoggedId;
    Button editBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_page, container, false);

        String postId = PostPageArgs.fromBundle(getArguments()).getPostId();
        userLoggedId = PostPageArgs.fromBundle(getArguments()).getUserId();

        Model.instance.getPostById(postId, new Model.GetPostById() {
            @Override
            public void onComplete(UserPost userPost) {
              postName.setText(userPost.getName());
              location.setText(userPost.getLocation());
              String categoryUserPost = userPost.getCategory().substring(0,1).toUpperCase() + userPost.getCategory().substring(1);
              category.setText(categoryUserPost);
              about.setText(userPost.getAbout());
              userNamePostId = userPost.getUserId();

              if(userPost.getPostImgUrl()!=null) {
                  Picasso.get()
                          .load(userPost.getPostImgUrl())
                          .into(postImg);
              }

              if(!userNamePostId.equals(userLoggedId)) {
                  editBtn.setVisibility(View.INVISIBLE);
              }
            }
        });

        postName = view.findViewById(R.id.post_name_post_page_details_tv);
        location = view.findViewById(R.id.location_post_page_details_tv);
        category = view.findViewById(R.id.type_post_page_details_tv);
        about = view.findViewById(R.id.about_post_page_details_tv);
        postImg = view.findViewById(R.id.picture_post_page_details_);

        category.setEnabled(false);
        about.setEnabled(false);

        editBtn = view.findViewById(R.id.edit_post_page_details_btn);
        editBtn.setOnClickListener(v -> Navigation.findNavController(v).navigate(PostPageDirections.actionPostPageToEditPostPage(postId)));

        return view;
    }
}