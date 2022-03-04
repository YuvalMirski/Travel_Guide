package com.example.travel_guide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.travel_guide.databinding.ActivityMainBinding;

public class LoginActivity extends AppCompatActivity {
    NavController navCtl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        NavHost navHost = (NavHost) getSupportFragmentManager().findFragmentById(R.id.login_navhost);
        navCtl = navHost.getNavController();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.login_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        NavigationUI.setupActionBarWithNavController(this, navCtl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.login_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (@NonNull MenuItem item){
        if (!super.onOptionsItemSelected(item)) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    navCtl.navigateUp();
                    return true;
                default:
                    NavigationUI.onNavDestinationSelected(item, navCtl);
            }
        } else {
            return true;
        }
        return false;
    }
}