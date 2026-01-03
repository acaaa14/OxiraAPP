package com.example.oxiraapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.oxiraapp.R;
import com.example.oxiraapp.utils.SessionManager;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY = 1500; // 1.5 detik

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper())
                .postDelayed(this::navigateUser, SPLASH_DELAY);
    }

    private void navigateUser() {
        SessionManager session = new SessionManager(this);
        Intent intent;

        if (!session.isLogin()) {
            // BELUM LOGIN
            intent = new Intent(this, LoginActivity.class);

        } else {
            // SUDAH LOGIN → CEK KATEGORI
            String kategori = session.getKategori(); // pribadi / perusahaan

            if ("perusahaan".equalsIgnoreCase(kategori)) {
                intent = new Intent(this, MainPerusahaanActivity.class);
            } else {
                // default pribadi
                intent = new Intent(this, MainPribadiActivity.class);
            }
        }

        startActivity(intent);
        finish();
    }
}
