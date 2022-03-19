package com.example.travel_guide.view.PostPage;

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
import android.widget.Toast;

import com.example.travel_guide.R;
import com.example.travel_guide.model.Model;
import com.example.travel_guide.model.User;
import com.example.travel_guide.model.UserPost;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Arrays;

public class EditPostPage extends Fragment {

    EditText postName, about;
    ImageView postImg;
    Bitmap imageBitmap;
    String new_name, new_location, new_about, new_id, new_category, userId, imageUrl, postId;
    Spinner categorySpinner, citySpinner;
    String[] categoryArr, cityArr;
    UserPost currentPost;
    Button saveBtn, deleteBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_post_page, container, false);

        postId = EditPostPageArgs.fromBundle(getArguments()).getPostId();
        new_id = postId;
        Model.instance.getPostById(postId, userPost -> getPostAction(userPost));
        Model.instance.getUserIdFromFB(id -> userId = id);

        postName = view.findViewById(R.id.post_name_post_page_edit_et);
        about = view.findViewById(R.id.about_post_page_edit_et);
        postImg = view.findViewById(R.id.picture_post_page_edit_);
        initSpinners(view);

        saveBtn = view.findViewById(R.id.save_post_page_edit_delete_btn);
        deleteBtn = view.findViewById(R.id.delete_post_page_edit_btn);
        ImageButton galleryBtn = view.findViewById(R.id.editPost_gallery_imb);

        galleryBtn.setOnClickListener(v -> openGallery());
        saveBtn.setOnClickListener(v -> saveBtnAction(v));
        deleteBtn.setOnClickListener(v -> deleteBtnAction(v));

        return view;
    }

    private void deleteBtnAction(View v) {
        currentPost.setIsDeleted("delete");
        User u = Model.instance.getUser(userId).getValue();
        u.getLstSaved().remove(postId);
        u.getLstUserPosts().remove(postId);
        Model.instance.updateUser(u, () -> System.out.println(""));

        Model.instance.deletePostById(currentPost, () -> {
            Navigation.findNavController(v).navigate(EditPostPageDirections.actionGlobalHomePageNav(userId));
        });
    }

    private void saveBtnAction(View v) {
        saveBtn.setEnabled(false);
        new_name = postName.getText().toString();
        new_location = citySpinner.getSelectedItem().toString();
        new_category = categorySpinner.getSelectedItem().toString().toLowerCase();
        new_about = about.getText().toString();

        UserPost userPost = new UserPost(new_name, new_location, new_about, new_category, userId);
        userPost.setId(postId);

        if ((!new_name.equals("") && !new_about.equals(""))) {
            if (imageBitmap != null) {
                Model.instance.saveImage(imageBitmap, new_name + ".jpg", "post_pics", url -> {
                    userPost.setPostImgUrl(url);
                    Model.instance.updateUserPost(userPost, () -> {
                        Navigation.findNavController(postName).navigateUp();
                    });
                });
            } else {
                userPost.setPostImgUrl(imageUrl);
                Model.instance.updateUserPost(userPost, () -> {
                    Navigation.findNavController(postName).navigateUp();
                });
            }
        } else {
            Toast.makeText(getContext(), "You must add post name and description", Toast.LENGTH_LONG).show();
            saveBtn.setEnabled(true);
        }
    }

    private void getPostAction(UserPost userPost) {
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

        if (userPost.getPostImgUrl() != null) {
            Picasso.get()
                    .load(imageUrl)
                    .into(postImg);
        }
    }

    final static int SELECT_PICTURE = 200;

    private void openGallery() {
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
                    imageBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (imageBitmap != null) {
                    postImg.setImageBitmap(imageBitmap);
                }
            }
        }
    }

    public void initSpinners(View view) {
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