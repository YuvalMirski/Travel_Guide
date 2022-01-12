package com.example.travel_guide.ui.signUp;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.travel_guide.R;
import com.example.travel_guide.model.Model;
import com.example.travel_guide.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//Create User Page

public class SignUp extends Fragment {

    EditText email, country, userName, password;
    ImageView avatarPic;
    Bitmap imageBitmap;
    String new_userName, new_email, new_sex, new_country, new_password, new_id;
    List<String>lstSaved,lstUserPosts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        // new_id = Model
        //TODO:: temporary - we need id that is not exist
        //new_id = getRand(0,100);  //


        email = view.findViewById(R.id.email_signup_et);
        //sex = view.findViewById(R.id.sex_signup_et);
        country = view.findViewById(R.id.country_signup_et);
        userName = view.findViewById(R.id.username_signup_et);
        password = view.findViewById(R.id.password_signup_et);
        avatarPic = view.findViewById(R.id.avatar_signup_imv);
        RadioGroup sexRG = view.findViewById(R.id.sex_radioGroup);
        //sex.setText(checkedSexRB.getText());

        ImageButton uploadPicBtn = view.findViewById(R.id.gallery_signup_imb); //TODO:chenge to imgView of gallery??
        uploadPicBtn.setOnClickListener(v -> openGallery());

        Button submitBtn = view.findViewById(R.id.submit_signup_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:: verify if user not exist in DB --> if not update DB with user and navigate to sign in page
                //                                      --> if exist then write proper msg

                submitBtn.setEnabled(false);
                RadioButton checkedSexRB = sexRG.findViewById(sexRG.getCheckedRadioButtonId());

                new_userName = userName.getText().toString();
                new_email = email.getText().toString();
                new_sex = (String) checkedSexRB.getText(); //sex.getText().toString();
                new_country = country.getText().toString();
                new_password = password.getText().toString();
                lstSaved = new ArrayList<>();
                lstUserPosts = new ArrayList<>();
                User user = new User(new_userName, new_email, new_sex, new_country, new_password, lstSaved,lstUserPosts);

                if(imageBitmap!=null) {
                    Model.instance.saveImage(imageBitmap, new_userName+ ".jpg", "user_avatars", url -> {
                        user.setAvatarUrl(url);
                        Model.instance.createUserWithEmail(user, () -> Navigation.findNavController(v).navigate(SignUpDirections.actionSignUpNavToLogInNav()));
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
                    avatarPic.setImageBitmap(imageBitmap);
                }
            }
        }
    }

//    public String getRand(int n1, int n2) {
//        //generating number between n1 - n2
//        Random rand = new Random();
//        int min = n1, max = n2;
//        return String.valueOf(rand.nextInt(max - min + 1) + min);
//    }
}