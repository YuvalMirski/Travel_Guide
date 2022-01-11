package com.example.travel_guide;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;

import com.example.travel_guide.model.Model;
import com.example.travel_guide.ui.account.Account;
import com.example.travel_guide.ui.home.HomePage;
import com.example.travel_guide.ui.login.LogIn;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.travel_guide.databinding.ActivityMainBinding;

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
        MenuItem logIn = menu.findItem(R.id.logIn_nav);
        MenuItem signUp = menu.findItem(R.id.signUp_nav);
        MenuItem logOut = menu.findItem(R.id.logOut_nav);

        logOut.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Model.instance.signOut();
                Fragment logInFragment = new LogIn();
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main,logInFragment).commit();
                drawer.closeDrawers();
                logOut.setVisible(false);
                logIn.setVisible(true);
                signUp.setVisible(true);
                return true;
            }
        });
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
            Model.instance.getUserIdFromFB(new Model.GetUserId() {
                @Override
                public void onComplete(String id) {
                    userId = id;
                }
            });
            switch (item.getItemId()){
                case R.id.homePage_nav:
                    selectedFragment = new HomePage();
                    bundle.putString("userId", userId);
                    selectedFragment.setArguments(bundle);
                    break;
                case R.id.newPostPage:
                    selectedFragment = new NewPostPage();
                    break;
                case R.id.postListRvFragment:
                    selectedFragment = new PostListRvFragment(); //TODO: to change for saved only posts
                    bundle.putString("userId",userId);
                    bundle.putString("categoryName","userSavedPost");
                    selectedFragment.setArguments(bundle);
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

        Model.instance.getUserIdFromFB(id -> userId = id);
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