package com.example.oxiraapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.oxiraapp.R;
import com.example.oxiraapp.models.User;
import com.example.oxiraapp.utils.DatabaseHelper;
import com.example.oxiraapp.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister, tvForgotPassword;

    private DatabaseHelper db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionManager(this);

        // ✅ Jika sudah login → langsung arahkan
        if (session.isLogin()) {
            navigateToMain();
            return;
        }

        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);

        initView();
        initAction();
    }

    // ================= INIT VIEW =================
    private void initView() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
    }

    // ================= INIT ACTION =================
    private void initAction() {

        btnLogin.setOnClickListener(v -> handleLogin());

        tvRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );

        tvForgotPassword.setOnClickListener(v ->
                startActivity(new Intent(this, ForgotPasswordActivity.class))
        );
    }

    // ================= HANDLE LOGIN =================
    private void handleLogin() {

        clearError();

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (!validateInput(email, password)) return;

        // ✅ Login check
        if (!db.loginUser(email, password)) {
            Toast.makeText(this, "Email atau password salah", Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ Ambil user SEKALI saja
        User user = db.getUser(email);
        if (user == null) {
            Toast.makeText(this, "Data user tidak ditemukan", Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ Simpan session
        session.createLoginSession(user.email, user.kategori);

        Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show();
        navigateToMain();
    }

    // ================= VALIDATION =================
    private boolean validateInput(String email, String password) {

        if (email.isEmpty()) {
            etEmail.setError("Email wajib diisi");
            etEmail.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Format email tidak valid");
            etEmail.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password wajib diisi");
            etPassword.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            etPassword.setError("Password minimal 6 karakter");
            etPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void clearError() {
        etEmail.setError(null);
        etPassword.setError(null);
    }

    // ================= NAVIGATION =================
    private void navigateToMain() {

        String email = session.getEmail();
        String kategori = session.getKategori();

        if (email == null || kategori == null) return;

        Intent intent;

        if (kategori.equalsIgnoreCase("Pribadi")) {
            intent = new Intent(this, MainPribadiActivity.class);
        } else {
            intent = new Intent(this, MainPerusahaanActivity.class);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
