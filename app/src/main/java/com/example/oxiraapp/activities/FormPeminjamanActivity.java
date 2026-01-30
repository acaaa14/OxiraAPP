package com.example.oxiraapp.activities;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class FormPeminjamanActivity extends AppCompatActivity {

    private EditText etNama, etNIK, etKeperluan, etTglPinjam, etTglKembali;
    private ImageView ivKtpPeminjam;
    private TextView tvTotalHarga, tvRincianHarga;
    private Spinner spUkuranTabung;
    private Button btnKonfirmasi;

    private SessionManager session;
    private DatabaseHelper db;
    
    private final Calendar calPinjam = Calendar.getInstance();
    private final Calendar calKembali = Calendar.getInstance();
    
    private long hargaPerHari = 0;
    private final String[] ukuranList = {"Pilih Ukuran", "1 m³", "1.5 m³", "2 m³", "6 m³"};
    private final int[] hargaList = {0, 25000, 35000, 45000, 100000};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_peminjaman);

        session = new SessionManager(this);
        db = new DatabaseHelper(this);

        initView();
        setupSpinner();
        loadUserData();
        initDatePicker();
        
        btnKonfirmasi.setOnClickListener(v -> {
            if (hargaPerHari == 0) {
                Toast.makeText(this, "Silakan pilih ukuran tabung", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "Peminjaman Berhasil Dikonfirmasi!", Toast.LENGTH_LONG).show();
            finish();
        });
    }

    private void initView() {
        etNama = findViewById(R.id.etNama);
        etNIK = findViewById(R.id.etNIK);
        ivKtpPeminjam = findViewById(R.id.ivKtpPeminjam);
        etKeperluan = findViewById(R.id.etKeperluan);
        etTglPinjam = findViewById(R.id.etTglPinjam);
        etTglKembali = findViewById(R.id.etTglKembali);
        tvTotalHarga = findViewById(R.id.tvTotalHarga);
        tvRincianHarga = findViewById(R.id.tvRincianHarga);
        spUkuranTabung = findViewById(R.id.spUkuranTabung);
        btnKonfirmasi = findViewById(R.id.btnBayar);
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ukuranList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                TextView tv = (TextView) v;
                
                // Jika masih "Pilih Ukuran", biarkan warna hitam/abu
                if (position == 0) {
                    tv.setTextColor(Color.parseColor("#BBBBBB")); // Warna hint
                } else {
                    // Jika sudah memilih ukuran, ubah jadi PUTIH
                    tv.setTextColor(Color.WHITE);
                }
                return v;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.BLACK); // Daftar pilihan tetap hitam
                return v;
            }
        };
        
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spUkuranTabung.setAdapter(adapter);

        spUkuranTabung.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hargaPerHari = hargaList[position];
                hitungHarga();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadUserData() {
        String email = session.getEmail();
        User user = db.getUser(email);

        if (user != null) {
            etNama.setText(user.nama != null && !user.nama.isEmpty() ? user.nama : user.namaPerusahaan);
            etNIK.setText(user.nik != null && !user.nik.isEmpty() ? user.nik : "ID Perusahaan");
            
            if (user.ktpImageUri != null && !user.ktpImageUri.isEmpty()) {
                File imgFile = new File(user.ktpImageUri);
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    ivKtpPeminjam.setImageBitmap(myBitmap);
                }
            }
        }
    }

    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

        etTglPinjam.setOnClickListener(v -> {
            new DatePickerDialog(this, (view, year, month, day) -> {
                calPinjam.set(year, month, day);
                etTglPinjam.setText(sdf.format(calPinjam.getTime()));
                hitungHarga();
            }, calPinjam.get(Calendar.YEAR), calPinjam.get(Calendar.MONTH), calPinjam.get(Calendar.DAY_OF_MONTH)).show();
        });

        etTglKembali.setOnClickListener(v -> {
            new DatePickerDialog(this, (view, year, month, day) -> {
                calKembali.set(year, month, day);
                etTglKembali.setText(sdf.format(calKembali.getTime()));
                hitungHarga();
            }, calKembali.get(Calendar.YEAR), calKembali.get(Calendar.MONTH), calKembali.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void hitungHarga() {
        if (hargaPerHari == 0 || etTglPinjam.getText().toString().isEmpty() || etTglKembali.getText().toString().isEmpty()) {
            tvTotalHarga.setText("Rp 0");
            tvRincianHarga.setText("Pilih ukuran dan tanggal");
            return;
        }

        long msDiff = calKembali.getTimeInMillis() - calPinjam.getTimeInMillis();
        long days = TimeUnit.MILLISECONDS.toDays(msDiff);

        if (days < 0) {
            tvTotalHarga.setText("Rp 0");
            Toast.makeText(this, "Tgl kembali tidak valid", Toast.LENGTH_SHORT).show();
            return;
        }

        if (days == 0) days = 1;

        long total = days * hargaPerHari;
        tvTotalHarga.setText("Rp " + String.format(Locale.getDefault(), "%,d", total).replace(',', '.'));
        tvRincianHarga.setText(days + " hari x Rp " + String.format(Locale.getDefault(), "%,d", hargaPerHari).replace(',', '.') + " / hari");
    }
}
