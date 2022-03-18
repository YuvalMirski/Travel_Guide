package com.example.travel_guide.view.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.travel_guide.MainActivity;
import com.example.travel_guide.R;
import com.example.travel_guide.model.Model;
import com.example.travel_guide.model.User;


public class LogIn extends Fragment {

    private int attemptLogIn = 3;
    private String userEmail, userPassword;
    Button loginBtn;

    public LogIn() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Model.instance.initFireBaseAuto();

        View view = inflater.inflate(R.layout.fragment_log_in, container, false);

        EditText email = view.findViewById(R.id.email_login_et);
        EditText password = view.findViewById(R.id.password_login_et);

        loginBtn = (Button) view.findViewById(R.id.login_login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginBtn.setEnabled(false);
                userEmail = email.getText().toString();
                userPassword = password.getText().toString();
                if (checkLogin(userEmail,userPassword)) {
                    Model.instance.userSignIn(email.getText().toString().trim(), password.getText().toString().trim(), new Model.OnCompleteGeneralListener() {
                        @Override
                        public void onComplete(User user) {
                            if (user != null) {
                                Model.instance.setCurrentUser(user);

                                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE); //hide the keyboard input
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                                toFeedActivity();
                            } else {
                                wrongLogin(v,"Wrong email or password!");
                                loginBtn.setEnabled(true);
                            }
                        }
                    });
                }
                else {
                    wrongLogin(v,"Email or password isn't valid!");
                    loginBtn.setEnabled(true);
                }
            }
        });

        Button signUpBtn = (Button) view.findViewById(R.id.signup_login_btn);
//        signUpBtn.setOnClickListener(v -> Navigation.findNavController(v).navigate(LogInDirections.actionLogInNavToSignUpNav()));

        return view;
    }

    private void toFeedActivity() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    public boolean checkLogin(String email, String password)
    {
        if (email != null && password != null) {
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length() >= 6)
                return true;
        }
        return false;
    }

    public void wrongLogin(View view, String msg)
    {
        attemptLogIn--;
        if (attemptLogIn == 0) {
            Toast.makeText(getContext(), "You had 3 wrong attempts to log in!", Toast.LENGTH_LONG).show();

            loginBtn.setEnabled(false);
            loginBtn.setVisibility(view.GONE);
        } else {
            Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
        }
    }
}