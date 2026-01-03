package com.example.oxiraapp.activities;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.oxiraapp.R;
import com.example.oxiraapp.utils.DatabaseHelper;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText etEmail, etNewPassword;
    Button btnReset;

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etEmail = findViewById(R.id.etEmail);
        etNewPassword = findViewById(R.id.etNewPassword);
        btnReset = findViewById(R.id.btnReset);

        db = new DatabaseHelper(this);

        btnReset.setOnClickListener(v -> resetPassword());
    }

    private void resetPassword() {
        String email = etEmail.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email tidak valid");
            return;
        }

        if (newPassword.length() < 6) {
            etNewPassword.setError("Password minimal 6 karakter");
            return;
        }

        if (!db.checkEmail(email)) {
            Toast.makeText(this, "Email tidak terdaftar", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean updated = db.updatePassword(email, newPassword);
        if (updated) {
            Toast.makeText(this, "Password berhasil diperbarui", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Gagal memperbarui password", Toast.LENGTH_SHORT).show();
        }
    }
}
