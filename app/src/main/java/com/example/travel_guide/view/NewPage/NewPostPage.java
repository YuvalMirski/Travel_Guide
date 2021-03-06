package com.example.travel_guide.view.NewPage;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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

import java.io.IOException;

public class NewPostPage extends Fragment {

    String new_name, new_location, new_about, new_category, userId;
    EditText postName, about;
    ImageView postPic;
    Bitmap imageBitmap;
    Button addPostBtn;
    Spinner categorySpinner, citySpinner;
    String[] categoryArr, cityArr;
    NewPostPageViewModel viewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(NewPostPageViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_post_page, container, false);

        postName = view.findViewById(R.id.post_name_post_page_new_et);
        about = view.findViewById(R.id.about_post_page_new_et);
        postPic = view.findViewById(R.id.picture_post_page_new_);
        initSpinners(view);

        Model.instance.getUserIdFromFB(id -> userId = id);
        viewModel.updateUser(userId);

        ImageButton galleryBtn = view.findViewById(R.id.addPost_gallery_imb);
        galleryBtn.setOnClickListener(v -> openGallery());

        addPostBtn = view.findViewById(R.id.add_post_page_new_btn);
        addPostBtn.setOnClickListener(v -> addPostBtnAction(v));
        return view;
    }

    private void addPostBtnAction(View v) {
        addPostBtn.setEnabled(false);
        new_name = postName.getText().toString();
        new_location = citySpinner.getSelectedItem().toString();
        new_category = categorySpinner.getSelectedItem().toString().toLowerCase();
        new_about = about.getText().toString();
        UserPost userPost = new UserPost(new_name, new_location, new_about, new_category, userId);

        if (!new_name.equals("") && !new_about.equals("")) {
            if (imageBitmap != null) {
                Model.instance.saveImage(imageBitmap, new_name + ".jpg", "post_pics", url -> {
                    userPost.setPostImgUrl(url);
                    Model.instance.addUserPost(userPost, () -> {
                        if (!viewModel.getUserLiveData().getValue().getLstUserPosts().contains(userPost.getId())) {
                            viewModel.getUserLiveData().getValue().getLstUserPosts().add(userPost.getId());
                            User u = viewModel.userLiveData.getValue();
                            Model.instance.updateUser(u, () -> Navigation.findNavController(v).navigate(NewPostPageDirections.actionGlobalHomePageNav(userId)));
                        } else {
                            Navigation.findNavController(v).navigate(NewPostPageDirections.actionGlobalHomePageNav(userId));
                        }
                    });
                });
            } else {
                Toast.makeText(getContext(), "You must add post image", Toast.LENGTH_LONG).show();
                addPostBtn.setEnabled(true);
            }
        } else {
            Toast.makeText(getContext(), "You must add post name and description", Toast.LENGTH_LONG).show();
            addPostBtn.setEnabled(true);
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
                    postPic.setImageBitmap(imageBitmap);
                }
            }
        }
    }

    public void initSpinners(View view) {
        categorySpinner = (Spinner) view.findViewById(R.id.spinner_category_postPage);
        ArrayAdapter<CharSequence> adapterCategory = ArrayAdapter.createFromResource(getContext(), R.array.CategoryList, R.layout.spinner_item);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapterCategory);
        categoryArr = getResources().getStringArray(R.array.CategoryList);

        citySpinner = (Spinner) view.findViewById(R.id.spinner_city_newPage);
        ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(getContext(), R.array.CityList, R.layout.spinner_item);
        adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(adapterCity);
        cityArr = getResources().getStringArray(R.array.CityList);
    }
}