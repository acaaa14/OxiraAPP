package com.example.oxiraapp.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.oxiraapp.R;
import com.example.oxiraapp.fragments.HomeFragmentPribadi;
import com.example.oxiraapp.fragments.HistoryFragment;
import com.example.oxiraapp.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;

public class MainPribadiActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pribadi);
        initMidtrans();
        checkPermissions();

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // default fragment
        if (savedInstanceState == null) {
            loadFragment(new HomeFragmentPribadi());
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_home) {
                loadFragment(new HomeFragmentPribadi());
                return true;
            } else if (id == R.id.menu_history) {
                loadFragment(new HistoryFragment());
                return true;
            } else if (id == R.id.menu_profile) {
                loadFragment(new ProfileFragment());
                return true;
            }
            return false;
        });
    }
    // ===================== MIDTRANS =====================
    private void initMidtrans() {
        SdkUIFlowBuilder.init()
                .setClientKey("Mid-client-IyhJs7bjc_mVQHGS") // ganti client key kamu
                .setContext(this)
                .setMerchantBaseUrl("http://192.168.1.7:8000/")
                .enableLog(true)
                .buildSDK();
    }

    private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Izin diberikan", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Izin lokasi diperlukan untuk peta", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

}
