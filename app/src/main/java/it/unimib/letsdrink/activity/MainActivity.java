package it.unimib.letsdrink.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import it.unimib.letsdrink.R;
import it.unimib.letsdrink.ui.categories.DrinksFragment;
import it.unimib.letsdrink.ui.favorites.FavoritesFragment;
import it.unimib.letsdrink.ui.home.HomeFragment;
import it.unimib.letsdrink.ui.profile.ProfileFragment;
import it.unimib.letsdrink.ui.shaker.ShakerFragment;

public class MainActivity extends AppCompatActivity {

    Fragment fragment1;
    final Fragment fragment2 = new DrinksFragment();
    final Fragment fragment3 = new ShakerFragment();
    final Fragment fragment4 = new FavoritesFragment();
    final Fragment fragment5 = new ProfileFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragment1 = new HomeFragment();
        active = fragment1;

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.navigation_home);
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        fm.beginTransaction().add(R.id.nav_host_fragment, fragment1, "home").commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, fragment2, "drinks").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, fragment3, "shaker").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, fragment4, "favorites").hide(fragment4).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, fragment5, "profile").hide(fragment5).commit();

        /* Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_categories, R.id.navigation_shaker, R.id.navigation_favorites, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

         */

    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fm.beginTransaction().hide(active).show(fragment1).commit();
                    active = fragment1;
                    return true;

                case R.id.navigation_categories:
                    fm.beginTransaction().hide(active).show(fragment2).commit();
                    active = fragment2;
                    return true;

                case R.id.navigation_shaker:
                    fm.beginTransaction().hide(active).show(fragment3).commit();
                    active = fragment3;
                    return true;
                    // currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    /* if(currentUser == null) {
                        fm.beginTransaction().hide(active).show(fragment3).commit();
                        active = fragment3;
                        return true;
                    } else {
                        fm.beginTransaction().hide(active).detach(fragment4).attach(fragment4).show(fragment4).commit();
                        active = fragment4;
                        return true;
                    } */

                case R.id.navigation_favorites:
                    fm.beginTransaction().hide(active).show(fragment4).commit();
                    active = fragment4;
                    return true;

                case R.id.navigation_profile:
                    fm.beginTransaction().hide(active).show(fragment5).commit();
                    active = fragment5;
                    return true;


            }
            return false;
        }
    };


}