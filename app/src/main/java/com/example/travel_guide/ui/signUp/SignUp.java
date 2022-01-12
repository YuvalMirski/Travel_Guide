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
import android.widget.Toast;

import androidx.annotation.NonNull;
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
    String new_userName, new_email, new_sex, new_country, new_password;
    List<String> lstSaved, lstUserPosts;
    RadioGroup sexRG;
    Button submitBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        email = view.findViewById(R.id.email_signup_et);
        country = view.findViewById(R.id.country_signup_et);
        userName = view.findViewById(R.id.username_signup_et);
        password = view.findViewById(R.id.password_signup_et);
        avatarPic = view.findViewById(R.id.avatar_signup_imv);
        sexRG = view.findViewById(R.id.sex_radioGroup);

        ImageButton uploadPicBtn = view.findViewById(R.id.gallery_signup_imb);
        uploadPicBtn.setOnClickListener(v -> openGallery());

        submitBtn = view.findViewById(R.id.submit_signup_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitBtn.setEnabled(false);
                RadioButton checkedSexRB = sexRG.findViewById(sexRG.getCheckedRadioButtonId());

                new_userName = userName.getText().toString();
                new_email = email.getText().toString();
                new_country = country.getText().toString();
                new_password = password.getText().toString();
                lstSaved = new ArrayList<>();
                lstUserPosts = new ArrayList<>();

                if(checkedSexRB!= null) {
                    new_sex = (String) checkedSexRB.getText();
                }
                else {
                    Toast.makeText(getContext(), "You must choose sex", Toast.LENGTH_LONG).show();
                    submitBtn.setEnabled(true);
                }

                String checkRes = checkSignUp(new_email,new_password,new_userName, new_country);
                if(!checkRes.equals("true")){
                    Toast.makeText(getContext(), checkRes, Toast.LENGTH_LONG).show();
                    submitBtn.setEnabled(true);
                }
                else {
                    User user = new User(new_userName, new_email, new_sex, new_country, new_password, lstSaved, lstUserPosts);
                    checkImg(v, user, imageBitmap);
                }
            }
        });
        return view;
    }

    private String checkSignUp(String new_email, String new_password, String new_userName, String new_country)
    {
        if(new_userName.equals(""))
            return "You must enter username";
        if(new_email.equals("") || !(android.util.Patterns.EMAIL_ADDRESS.matcher(new_email).matches()))
            return  "You must enter correct email";
        if(new_country.equals(""))
            return "You must enter country";
        if(new_password.equals("") || new_password.length()<6)
            return "You must correct password";
        return "true";
    }

    private void checkImg(View v, User user, Bitmap imageBitmap) {
        if (imageBitmap != null) {
            Model.instance.saveImage(imageBitmap, new_userName + ".jpg", "user_avatars", url -> {
                user.setAvatarUrl(url);
                Model.instance.createUserWithEmail(user, new Model.AddUserToFBListener() {
                    @Override
                    public void onComplete(String isSuccess) {
                        if (isSuccess.equals("true")) {
                            Navigation.findNavController(v).navigate(SignUpDirections.actionSignUpNavToLogInNav());
                        } else {
                            String msg = isSuccess.split(": ")[1];
                            Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            });
        } else {
            Toast.makeText(getContext(), "You must add profile image", Toast.LENGTH_LONG).show();
            submitBtn.setEnabled(true);
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
                    avatarPic.setImageBitmap(imageBitmap);
                }
            }
        }
    }

}