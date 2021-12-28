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


public class SignUp extends Fragment {



    public SignUp() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        EditText name = view.findViewById(R.id.name_signup_et);
        EditText email = view.findViewById(R.id.email_signup_et);
        EditText sex = view.findViewById(R.id.sex_signup_et);
        EditText country = view.findViewById(R.id.country_signup_et);
        EditText userName = view.findViewById(R.id.username_signup_et);
        EditText password = view.findViewById(R.id.password_signup_et);
        Button submitBtn = (Button) view.findViewById(R.id.submit_signup_btn);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:: verify if user not exist in DB --> if not update DB with user and navigate to sign in page
                //                                      --> if exist then write proper msg
                Navigation.findNavController(v).navigate(SignUpDirections.actionSignUpNavToLogInNav());
            }
        });
        return view;
    }
}