package com.example.oxiraapp.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.oxiraapp.R;
import com.example.oxiraapp.fragments.HomeFragmentPribadi;
import com.example.oxiraapp.fragments.HistoryFragment;
import com.example.oxiraapp.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainPribadiActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    // Fragment khusus pribadi
    private final Fragment homeFragment = new HomeFragmentPribadi();
    private final Fragment historyFragment = new HistoryFragment();
    private final Fragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pribadi);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // default fragment
        loadFragment(homeFragment);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.menu_home) {
                loadFragment(homeFragment);
                return true;
            }
            else if (id == R.id.menu_history) {
                loadFragment(historyFragment);
                return true;
            }
            else if (id == R.id.menu_profile) {
                loadFragment(profileFragment);
                return true;
            }

            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
