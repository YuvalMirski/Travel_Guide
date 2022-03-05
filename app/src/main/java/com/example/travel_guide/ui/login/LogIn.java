package com.example.travel_guide.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.travel_guide.MainActivity;
import com.example.travel_guide.R;
import com.example.travel_guide.model.Model;
import com.example.travel_guide.model.User;
import com.google.android.material.navigation.NavigationView;


public class LogIn extends Fragment {

    LoginViewModel loginViewModel;
    private int attemptLogIn = 3;
    private String userEmail, userPassword;
    boolean isConnected;
    Button loginBtn;

    public LogIn() {
        // Required empty public constructor
    }

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        //loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //initialize the FirebaseAuth instance
        Model.instance.initFireBaseAuto();

        View view = inflater.inflate(R.layout.fragment_log_in, container, false);

        EditText email = view.findViewById(R.id.email_login_et);
        EditText password = view.findViewById(R.id.password_login_et);

        loginBtn = (Button) view.findViewById(R.id.login_login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEmail = email.getText().toString();
                userPassword = password.getText().toString();
                if (checkLogin(userEmail,userPassword)) {

                    Model.instance.userSignIn(email.getText().toString().trim(), password.getText().toString().trim(), new Model.OnCompleteGeneralListener() {
                        @Override
                        public void onComplete(User user) {
                            if (user != null) {
                                Model.instance.setCurrentUser(user);
//                                View inflatedView = getLayoutInflater().inflate(R.layout.activity_main, null);
//                                NavigationView navigationView = (NavigationView) inflatedView.findViewById(R.id.nav_view); //gets  null!!! TODO:: to check WHY??
//                                TextView userName = navigationView.getHeaderView(0).findViewById(R.id.userName_tv);
//                                TextView userEmail = navigationView.getHeaderView(0).findViewById(R.id.userEmail_tv);
//                                System.out.println(user.getUserName()+"!!!!!!!!!!!");
//                                System.out.println(user.getEmail()+"!!!!!!!!!!!!!!!");
//                                userName.setText(user.getUserName());
//                                userEmail.setText(user.getEmail());
//
//                                Menu menu = navigationView.getMenu();
//                                MenuItem nav_Login = menu.findItem(R.id.logIn_nav);
//                                MenuItem nav_signUp = menu.findItem(R.id.signUp_nav);
//                                MenuItem nav_Logout = menu.findItem(R.id.logOut_nav);
//                                nav_Login.setVisible(false);
//                                nav_signUp.setVisible(false);
//                                nav_Logout.setVisible(true);

                                //TODO:: to change unabled to press
                                //navigationView.setNavigationItemSelectedListener(this);

                                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE); //hide the keyboard input
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                                toFeedActivity();
//                              Navigation.findNavController(v).navigate(LogInDirections.actionGlobalHomePageNav(user.getId()));

                            } else {
                                wrongLogin(v,"Wrong email or password!");
                            }
                        }
                    });
                }
                else {
                    wrongLogin(v,"Email or password isn't valid!");
                }
            }
        });

        Button signUpBtn = (Button) view.findViewById(R.id.signup_login_btn);
//        signUpBtn.setOnClickListener(v -> Navigation.findNavController(v).navigate(LogInDirections.actionLogInNavToSignUpNav()));

        //TODO:: add button for user without account

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
//https://www.tutorialspoint.com/android/android_login_screen.htm