package com.example.travel_guide.ui.login;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.travel_guide.MainActivity;
import com.example.travel_guide.R;
import com.example.travel_guide.model.Model;
import com.example.travel_guide.model.User;
import com.google.android.material.navigation.NavigationView;


public class LogIn extends Fragment {

    private int attemptLogIn = 3;

    public LogIn() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //initialize the FirebaseAuth instance
        Model.instance.initFireBaseAuto();

        View view = inflater.inflate(R.layout.fragment_log_in, container, false);

        EditText email = view.findViewById(R.id.email_login_et);
        EditText password = view.findViewById(R.id.password_login_et);

        Button loginBtn = (Button) view.findViewById(R.id.login_login_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(true)
                {
                    Log.d("Tag","success");
                    Model.instance.userSignIn(email.getText().toString().trim(), password.getText().toString().trim(), new Model.OnCompleteGeneralListener() {
                        @Override
                        public void onComplete(User user) { //String userId
                            System.out.println("before navigation");

                            NavigationView navigationView = (NavigationView)getActivity().findViewById(R.id.nav_view);
                            TextView userName = navigationView.getHeaderView(0).findViewById(R.id.userName_tv);
                            TextView userEmail = navigationView.getHeaderView(0).findViewById(R.id.userEmail_tv);

                            userName.setText(user.getUserName());
                            userEmail.setText(user.getEmail());

                            Menu menu = navigationView.getMenu();
                            MenuItem nav_Login = menu.findItem(R.id.logIn_nav);
                            MenuItem nav_signUp = menu.findItem(R.id.signUp_nav);
                            MenuItem nav_Logout = menu.findItem(R.id.logOut_nav);
                            nav_Login.setVisible(false);
                            nav_signUp.setVisible(false);
                            nav_Logout.setVisible(true);

                            MainActivity.SaveUserId.setId(user.getId());

                            //TODO:: to change unabled to press
                            //navigationView.setNavigationItemSelectedListener(this);

                            Navigation.findNavController(v).navigate(LogInDirections.actionLogInNavToHomePageNav(user.getId()));
                        }
                    });


                    //Navigation.findNavController(v).navigate(LogInDirections.actionLogInNavToHomePageNav(userID));

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

}
//https://www.tutorialspoint.com/android/android_login_screen.htm