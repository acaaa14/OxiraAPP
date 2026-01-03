package com.example.oxiraapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.oxiraapp.R;
import com.example.oxiraapp.models.User;
import com.example.oxiraapp.utils.DatabaseHelper;
import com.example.oxiraapp.utils.SessionManager;

public class KerjasamaActivity extends AppCompatActivity {

    private EditText etNamaPerusahaan, etPenanggungJawab, etAlamat, etNoHp;
    private Button btnLanjut;

    private SessionManager session;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kerjasama);

        session = new SessionManager(this);
        db = new DatabaseHelper(this);

        initView();
        loadDataPerusahaan();
        initAction();
    }

    // ================= INIT VIEW =================
    private void initView() {
        etNamaPerusahaan = findViewById(R.id.etNamaPerusahaan);
        etPenanggungJawab = findViewById(R.id.etPenanggungJawab);
        etAlamat = findViewById(R.id.etAlamat);
        etNoHp = findViewById(R.id.etNoHp);
        btnLanjut = findViewById(R.id.btnLanjut);
    }

    // ================= AUTO ISI DARI REGISTRASI =================
    private void loadDataPerusahaan() {
        String email = session.getEmail();

        if (email == null || email.isEmpty()) return;

        User user = db.getUser(email);
        if (user == null) return;

        etNamaPerusahaan.setText(user.namaPerusahaan);
        etPenanggungJawab.setText(user.penanggungJawab);
        etAlamat.setText(user.alamat);
        etNoHp.setText(user.noHp);

        // Biar tidak bisa diedit (opsional)
        etNamaPerusahaan.setEnabled(false);
        etPenanggungJawab.setEnabled(false);
    }

    // ================= ACTION =================
    private void initAction() {
        btnLanjut.setOnClickListener(v -> {

            Toast.makeText(
                    this,
                    "Data kerjasama disimpan\nLanjut ke Maps",
                    Toast.LENGTH_SHORT
            ).show();

            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        });
    }
}
