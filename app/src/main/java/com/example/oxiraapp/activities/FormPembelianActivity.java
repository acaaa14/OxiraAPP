package com.example.oxiraapp.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.oxiraapp.R;
import com.example.oxiraapp.models.User;
import com.example.oxiraapp.utils.DatabaseHelper;
import com.example.oxiraapp.utils.SessionManager;

import java.util.Locale;

public class FormPembelianActivity extends AppCompatActivity {

    private EditText etNama, etNik, etEmail, etNoHp, etJumlahTabung;
    private Spinner spUkuranTabung;
    private TextView tvHargaSatuan, tvTotalHarga;
    private Button btnBayar;

    private SessionManager session;
    private DatabaseHelper db;

    private long hargaSatuan = 0;
    private final String[] ukuranList = {"Pilih Ukuran", "1 m³", "1.5 m³", "2 m³", "6 m³"};
    private final int[] hargaList = {0, 500000, 750000, 1000000, 2500000}; // Contoh Harga Jual

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_pembelian);

        session = new SessionManager(this);
        db = new DatabaseHelper(this);

        initView();
        setupSpinner();
        loadUserData();
        setupAutoCalculate();

        btnBayar.setOnClickListener(v -> {
            if (hargaSatuan == 0 || etJumlahTabung.getText().toString().isEmpty()) {
                Toast.makeText(this, "Lengkapi data pembelian", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "Lanjut ke Pembayaran...", Toast.LENGTH_LONG).show();
        });
    }

    private void initView() {
        etNama = findViewById(R.id.etNama);
        etNik = findViewById(R.id.etNik);
        etEmail = findViewById(R.id.etEmail);
        etNoHp = findViewById(R.id.etNoHp);
        etJumlahTabung = findViewById(R.id.etJumlahTabung);
        spUkuranTabung = findViewById(R.id.spUkuranTabung);
        tvHargaSatuan = findViewById(R.id.tvHargaSatuan);
        tvTotalHarga = findViewById(R.id.tvTotalHarga);
        btnBayar = findViewById(R.id.btnBayar);
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ukuranList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(position == 0 ? Color.GRAY : Color.BLACK);
                return v;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spUkuranTabung.setAdapter(adapter);

        spUkuranTabung.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hargaSatuan = hargaList[position];
                tvHargaSatuan.setText("Rp " + String.format(Locale.getDefault(), "%,d", hargaSatuan).replace(',', '.'));
                hitungTotal();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadUserData() {
        User user = db.getUser(session.getEmail());
        if (user != null) {
            etNama.setText(user.nama != null && !user.nama.isEmpty() ? user.nama : user.namaPerusahaan);
            etNik.setText(user.nik != null && !user.nik.isEmpty() ? user.nik : "ID Perusahaan");
            etEmail.setText(user.email);
            etNoHp.setText(user.noHp);
        }
    }

    private void setupAutoCalculate() {
        etJumlahTabung.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { hitungTotal(); }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void hitungTotal() {
        String qtyStr = etJumlahTabung.getText().toString();
        int qty = qtyStr.isEmpty() ? 0 : Integer.parseInt(qtyStr);
        long total = qty * hargaSatuan;
        tvTotalHarga.setText("Rp " + String.format(Locale.getDefault(), "%,d", total).replace(',', '.'));
    }
}
