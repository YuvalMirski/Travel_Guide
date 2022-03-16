package com.example.travel_guide;

import static com.example.travel_guide.MyApplication.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.travel_guide.model.Model;
import com.example.travel_guide.model.User;
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
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {//implements NavigationView.OnNavigationItemSelectedListener

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private DrawerLayout drawer;
    private NavController navController;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.homePage_nav, R.id.account_nav)
                .setOpenableLayout(drawer)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //Update drawer header
        NavigationView navigationViewDrawer = (NavigationView) findViewById(R.id.nav_view);
        TextView userName = navigationViewDrawer.getHeaderView(0).findViewById(R.id.userName_tv);
        TextView userEmail = navigationViewDrawer.getHeaderView(0).findViewById(R.id.userEmail_tv);
        ImageView userAvatar = navigationViewDrawer.getHeaderView(0).findViewById(R.id.userPic_imv);

        User currentUser = Model.instance.getCurrentUser();
        userName.setText("Hello " + currentUser.getUserName() + "!");
        userEmail.setText(currentUser.getEmail());
        Picasso.get()
                .load(currentUser.getAvatarUrl())
                .into(userAvatar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
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

        if (!super.onOptionsItemSelected(item)) {
            switch (item.getItemId()) {
//                case android.R.id.home:
//                    navController.navigateUp();
//                    NavigationUI.onNavDestinationSelected(item, navController);
//                    return true;
                case R.id.action_logout:
                    Model.instance.signOut(userId);
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                    drawer.closeDrawers();
                    return true;
            }
        }
        else{
            return true;
        }
        return false;
    }
}