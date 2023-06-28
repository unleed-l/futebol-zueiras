package com.example.futebolzueiras;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    public static SQLiteHelper sqLiteHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instancia classe de gerenciamento de banco de dados que é utilizada nos fragments
        sqLiteHelper = new SQLiteHelper(this, "MemeDB.sqlite" , null , 1);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
}
