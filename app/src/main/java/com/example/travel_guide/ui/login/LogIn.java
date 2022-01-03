package com.example.travel_guide.ui.login;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.travel_guide.R;


public class LogIn extends Fragment {

    private int attemptLogIn = 3;

    public LogIn() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_log_in, container, false);

        EditText userName = view.findViewById(R.id.username_login_et);
        EditText password = view.findViewById(R.id.password_login_et);

        Button loginBtn = (Button) view.findViewById(R.id.login_login_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchUser(userName.getText().toString(),password.getText().toString()))
                {
                    Log.d("Tag","success");
                    String userID = ""; // we'll get it after verifying the user, go to it's details and get it from there
                    Navigation.findNavController(v).navigate(LogInDirections.actionLogInNavToHomePageNav(userID));

                }
                else{

                    attemptLogIn--;
                    if(attemptLogIn==0){
                        loginBtn.setEnabled(false);
                        loginBtn.setVisibility(view.GONE);
                    }
                    // write mistake
                }
            }
        });

        Button signUpBtn = (Button) view.findViewById(R.id.signup_login_btn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(LogInDirections.actionLogInNavToSignUpNav());
            }
        });

        //TODO:: add button for user without account

        return view;
    }

    private boolean searchUser(String toString, String toString1) {
        //TODO implement logic of verifying user

        return true;
    }
}
//https://www.tutorialspoint.com/android/android_login_screen.htm