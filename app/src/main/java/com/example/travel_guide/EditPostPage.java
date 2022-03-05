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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.travel_guide.model.Model;
import com.example.travel_guide.model.UserPost;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Arrays;


public class EditPostPage extends Fragment {

    //TODO:: only the post creator will be able to edit it's post

    EditText postName, location, about;
    ImageView postImg;
    Bitmap imageBitmap;
    String new_name,new_location, new_about, new_id, new_category,userId, imageUrl;
    Spinner categorySpinner, citySpinner;
    String[] categoryArr, cityArr;
    UserPost currentPost;
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
                currentPost = userPost;
                currentPost.setId(postId);
                postName.setText(userPost.getName());
                new_location = userPost.getLocation();
                new_category = userPost.getCategory();
                about.setText(userPost.getAbout());
                imageUrl = userPost.getPostImgUrl();

                int categoryIndex = Arrays.asList(categoryArr).indexOf(new_category);
                categorySpinner.setSelection(categoryIndex);
                int cityIndex = Arrays.asList(cityArr).indexOf(new_location);
                citySpinner.setSelection(cityIndex);

                if(userPost.getPostImgUrl()!=null) {
                    Picasso.get()
                            .load(imageUrl)
                            .into(postImg);
                }
            }
        });

        Model.instance.getUserIdFromFB(id -> userId = id);

        postName = view.findViewById(R.id.post_name_post_page_edit_et);
        about = view.findViewById(R.id.about_post_page_edit_et);
        postImg = view.findViewById(R.id.picture_post_page_edit_);
        initSpinners(view);

        Button saveBtn = view.findViewById(R.id.save_post_page_edit_delete_btn);
        Button deleteBtn = view.findViewById(R.id.delete_post_page_edit_btn);
        ImageButton galleryBtn = view.findViewById(R.id.editPost_gallery_imb);
        galleryBtn.setOnClickListener(v -> openGallery());

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_name = postName.getText().toString();
                new_location = citySpinner.getSelectedItem().toString();
                new_category = categorySpinner.getSelectedItem().toString();
                new_about = about.getText().toString();

                UserPost userPost = new UserPost(new_name,new_location,new_about,new_category,userId);
                userPost.setId(postId);

                if(imageBitmap!=null) {
                    Model.instance.saveImage(imageBitmap, new_name+ ".jpg", "post_pics",url -> {
                        userPost.setPostImgUrl(url);
                        Model.instance.updateUserPost(userPost,()->{
                            Navigation.findNavController(postName).navigateUp();
                        });
                    });
                }
                else {
                    userPost.setPostImgUrl(imageUrl);
                    Model.instance.updateUserPost(userPost, () -> {
                        Navigation.findNavController(postName).navigateUp();
                    });
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPost.setIsDeleted("delete");
                Model.instance.deletePostById(currentPost,()->{
//                    Navigation.findNavController(postName).navigateUp();
                    Navigation.findNavController(v).navigate(EditPostPageDirections.actionGlobalHomePageNav(userId));
                });
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
                    postImg.setImageBitmap(imageBitmap);
                }
            }
        }
    }

    public void initSpinners(View view)
    {
        categorySpinner = (Spinner) view.findViewById(R.id.spinner_category_editPostPage);
        ArrayAdapter<CharSequence> adapterCategory = ArrayAdapter.createFromResource(getContext(), R.array.CategoryList, R.layout.spinner_item);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapterCategory);
        categoryArr = getResources().getStringArray(R.array.CategoryList);

        citySpinner = (Spinner) view.findViewById(R.id.spinner_location_editPost);
        ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(getContext(), R.array.CityList, R.layout.spinner_item);
        adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(adapterCity);
        cityArr = getResources().getStringArray(R.array.CityList);
    }
}