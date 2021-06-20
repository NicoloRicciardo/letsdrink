package it.unimib.letsdrink.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import it.unimib.letsdrink.R;

public class MainActivity extends AppCompatActivity {

    NavController navController;

    //Metodo che implementa la gestione della bottom navigation view tramite navController e che imposta il layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_drinks, R.id.navigation_categories, R.id.navigation_shaker, R.id.navigation_favorites, R.id.navigation_profile)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    //Metodo che implementa la gestione della navigazione all'indietro tra fragment
    @Override
    public boolean onSupportNavigateUp() {

        navController.navigateUp();

        return super.onSupportNavigateUp();
    }
}