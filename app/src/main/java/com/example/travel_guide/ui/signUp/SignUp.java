package com.example.travel_guide.ui.signUp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.travel_guide.R;
import com.example.travel_guide.model.Model;
import com.example.travel_guide.model.User;

import java.util.Random;

//Create User Page

public class SignUp extends Fragment {

    EditText email, sex, country, userName,password;

    String new_userName, new_email, new_sex, new_country, new_password, new_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        // new_id = Model
        //TODO:: temporary - we need id that is not exist
         //new_id = getRand(0,100);  //


         email = view.findViewById(R.id.email_signup_et);
         sex = view.findViewById(R.id.sex_signup_et);
         country = view.findViewById(R.id.country_signup_et);
         userName = view.findViewById(R.id.username_signup_et);
         password = view.findViewById(R.id.password_signup_et);

         Button submitBtn = view.findViewById(R.id.submit_signup_btn);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:: verify if user not exist in DB --> if not update DB with user and navigate to sign in page
                //                                      --> if exist then write proper msg

                new_userName = userName.getText().toString();
                new_email = email.getText().toString();
                new_sex = sex.getText().toString();
                new_country = country.getText().toString();
                new_password = password.getText().toString();

                User user = new User(new_userName,new_email,new_sex,new_country,new_password);

                user.getLstSaved().add("DEMO ID ");
                user.getLstSaved().add("DEMO ID2 ");

                Model.instance.createUserWithEmail(user,()-> Navigation.findNavController(v).navigate(SignUpDirections.actionSignUpNavToLogInNav()));

//                Model.instance.addUser(user,()->{
//                   // Navigation.findNavController(email).navigateUp();
//                    Navigation.findNavController(v).navigate(SignUpDirections.actionSignUpNavToLogInNav());
//                });
            }
        });
        return view;
    }
    public String getRand(int n1, int n2) {
        //generating number between n1 - n2
        Random rand = new Random();
        int min = n1, max = n2;
        return String.valueOf(rand.nextInt(max - min + 1) + min);
    }
}