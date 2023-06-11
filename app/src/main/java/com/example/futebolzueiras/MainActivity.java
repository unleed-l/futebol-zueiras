package com.example.futebolzueiras;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        drawerLayout = findViewById(R.id.drawer_layout);
        //NavigationView navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this , drawerLayout , toolbar , R.string.open_menu , R.string.close_menu);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        /*
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
        */

        replaceFragment(new HomeFragment());
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            if(item.getItemId() == R.id.home){
                replaceFragment(new HomeFragment());
            } else if(item.getItemId() == R.id.profile){
                replaceFragment(new ProfileFragment());
            } else if(item.getItemId() == R.id.upload){
                replaceFragment(new UploadFragment());
            }
            else if(item.getItemId() == R.id.downloads){
                replaceFragment(new DownloadsFragment());
            } else if(item.getItemId() == R.id.favorites){
                replaceFragment(new FavoritesFragment());
            }

            return true;
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

}
