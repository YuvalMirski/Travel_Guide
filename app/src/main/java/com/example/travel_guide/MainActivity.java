package com.example.travel_guide;

import android.app.Application;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.example.travel_guide.model.Model;
import com.example.travel_guide.ui.LogOut;
import com.example.travel_guide.ui.account.Account;
import com.example.travel_guide.ui.home.HomePage;
import com.example.travel_guide.ui.login.LogIn;
import com.example.travel_guide.ui.login.LogInDirections;
import com.example.travel_guide.ui.signUp.SignUp;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.travel_guide.databinding.ActivityMainBinding;
import com.google.api.SystemParameterOrBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity  {//implements NavigationView.OnNavigationItemSelectedListener

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private DrawerLayout drawer;
    private NavController navController;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.homePage_nav, R.id.account_nav, R.id.logIn_nav,R.id.signUp_nav, R.id.logOut_nav)
                .setOpenableLayout(drawer)
                .build();


        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        Menu menu = navigationView.getMenu();
        MenuItem logOut = menu.findItem(R.id.logOut_nav);
        logOut.setVisible(false);

        BottomNavigationView bottomNav = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        //bottomNav.animate(); TODO:animation
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }



    BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selectedFragment = null;
            Bundle bundle = new Bundle();
            switch (item.getItemId()){
                case R.id.homePage_nav:
                    selectedFragment = new HomePage();
                    Model.instance.getUserIdFromFB(new Model.GetUserId() {
                        @Override
                        public void onComplete(String id) {
                            userId = id;
                        }
                    });
                    bundle.putString("userId", userId);
                    selectedFragment.setArguments(bundle);
                    break;
                case R.id.newPostPage:
                    selectedFragment = new NewPostPage();
                    break;
                case R.id.postListRvFragment:
                    selectedFragment = new PostListRvFragment(); //TODO: to change for saved only posts
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main,selectedFragment).commit();
            return true;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Model.instance.getUserIdFromFB(new Model.GetUserId() {
            @Override
            public void onComplete(String id) {
                userId = id;
            }
        });
        Bundle bundle = new Bundle();
        if (!super.onOptionsItemSelected(item)){
            switch (item.getItemId()){
//                case android.R.id.home:
//                    navController.navigateUp();
//                    return true;
                case R.id.action_account:
                    Fragment accountFragment = new Account();
                    bundle.putString("userId", userId);
                    accountFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main,accountFragment).commit();
                    return true;
            }
        }else{
            return true;
        }
        return false;
    }

//    public static class SaveUserId {
//        private static String id;
//        public static String getId(){
//            return id;
//        }
//        public static void setId(String id)
//        {
//            SaveUserId.id =id;
//        }
//    }
}