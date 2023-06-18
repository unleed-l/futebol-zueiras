package com.example.futebolzueiras;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    public static SQLiteHelper sqLiteHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instancia classe de gerenciamento de banco de dados que é utilizada nos fragments
        sqLiteHelper = new SQLiteHelper(this, "MemeDB.sqlite" , null , 1);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        drawerLayout = findViewById(R.id.drawer_layout);

        // Funcional Navigation Drawer que ainda não foi implementado.
        //NavigationView navigationView = findViewById(R.id.nav_view);

        // Toogle da UI do Navigation Drawer não funcional
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this , drawerLayout , toolbar , R.string.open_menu , R.string.close_menu);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Aplicar highlight no primeiro item do Navigation Drawer
        /*
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
        */

        // Determina o primeiro fragment como fragment inicial
        replaceFragment(new HomeFragment());

        bottomNavigationView.setBackground(null);
        // Alterna de fragment de acordo com item clicado
        bottomNavigationView.setOnItemSelectedListener(item -> {

            if(item.getItemId() == R.id.home){
                replaceFragment(new HomeFragment());
            } else if(item.getItemId() == R.id.profile){
                replaceFragment(new ProfileFragment());
            } else if(item.getItemId() == R.id.upload){
                replaceFragment(new UploadFragment());
            } else if(item.getItemId() == R.id.downloads){
                replaceFragment(new DownloadsFragment());
            } else if(item.getItemId() == R.id.favorites){
                replaceFragment(new FavoritesFragment());
            }

            return true;
        });
    }

    // Função responsável pelo gerenciamento dos fragments
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.config, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            // Lógica para abrir a tela de configurações aqui
            //Toast.makeText(this, "Cliquei em config", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
