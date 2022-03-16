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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.travel_guide.model.Model;
import com.example.travel_guide.model.User;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

//User Edit Page
public class EditUserPage extends Fragment {

    EditText userName, email, country;
    String new_userName, new_email, new_sex, new_country, new_id, avatarUrl;
    ImageView userAvatar;
    Bitmap imageBitmap;
    List<String>lstSaved,lstUserPosts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_user, container, false);

        String userId = EditUserPageArgs.fromBundle(getArguments()).getUserId();
        new_id = userId;
        Model.instance.getUserById(userId, new Model.GetUserById() {
            @Override
            public void onComplete(User user) {
                userName.setText(user.getUserName());
                email.setText(user.getEmail());
                new_sex = user.getSex();
                country.setText(user.getCountry());
                lstSaved = user.getLstSaved();
                lstUserPosts =user.getLstUserPosts();
                avatarUrl = user.getAvatarUrl();
                if(user.getAvatarUrl()!=null) {
                    Picasso.get()
                            .load(avatarUrl)
                            .into(userAvatar);
                }
            }
        });
        userName = view.findViewById(R.id.user_name_account_edit_et);
        email = view.findViewById(R.id.email_account_edit_et);
        country = view.findViewById(R.id.country_account_edit_et);
        userAvatar = view.findViewById(R.id.userAvater_edit_acount_imv);

        ImageButton galleryBtn = view.findViewById(R.id.gallery_editAccount_imb);
        galleryBtn.setOnClickListener(v -> openGallery());

        Button saveBtn = view.findViewById(R.id.save_account_edit_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBtn.setEnabled(false);
                new_userName = userName.getText().toString();
                new_email = email.getText().toString();
                new_country = country.getText().toString();
                User u = new User(new_userName,new_email,new_sex,new_country, lstSaved,lstUserPosts);
                u.setId(userId);

                if(new_userName!=null && new_email!=null && new_country!=null) {
                    if (imageBitmap != null) {
                        Model.instance.saveImage(imageBitmap, new_userName + ".jpg", "user_avatars", url -> {
                            u.setAvatarUrl(url);
                            Model.instance.updateUser(u, () -> Navigation.findNavController(userName).navigateUp());
                        });
                    } else {
                        u.setAvatarUrl(avatarUrl);
                        Model.instance.updateUser(u, () -> Navigation.findNavController(userName).navigateUp());
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "Wrong username, email or country!", Toast.LENGTH_LONG).show();
                    saveBtn.setEnabled(true);
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
                    userAvatar.setImageBitmap(imageBitmap);
                }
            }
        }
    }
}