package com.example.oxiraapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.oxiraapp.R;
import com.example.oxiraapp.utils.DatabaseHelper;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    // ================= VIEW =================
    private EditText etNama, etNik, etTtl, etAlamat;
    private EditText etNamaPerusahaan, etPenanggungJawab;
    private EditText etNoHp, etEmail, etPassword;
    private Spinner spKategori;
    private Button btnRegister, btnUploadKtp;
    private ImageView imgKtpPreview;

    // ================= DATA =================
    private Uri ktpUri;
    private DatabaseHelper db;

    // ================= BLACKLIST OCR =================
    private static final String[] BLACKLIST_NAMA = {
            "PROVINSI", "KABUPATEN", "KOTA",
            "KECAMATAN", "KELURAHAN", "DESA",
            "RT", "RW", "AGAMA", "STATUS",
            "PEKERJAAN", "KEWARGANEGARAAN",
            "BERLAKU", "GOL", "ALAMAT"
    };

    // ================= IMAGE PICKER =================
    private final ActivityResultLauncher<String> pickImage =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    ktpUri = uri;
                    imgKtpPreview.setImageURI(uri);
                    imgKtpPreview.setVisibility(View.VISIBLE);
                    runTextRecognition(uri);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);
        initView();
        setupSpinner();
        setupAction();
    }

    // ================= INIT VIEW =================
    private void initView() {
        etNama = findViewById(R.id.etNama);
        etNik = findViewById(R.id.etNik);
        etTtl = findViewById(R.id.etTtl);
        etAlamat = findViewById(R.id.etAlamat);

        etNamaPerusahaan = findViewById(R.id.etNamaPerusahaan);
        etPenanggungJawab = findViewById(R.id.etPenanggungJawab);

        etNoHp = findViewById(R.id.etNoHp);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        spKategori = findViewById(R.id.spKategori);
        btnRegister = findViewById(R.id.btnRegister);
        btnUploadKtp = findViewById(R.id.btnUploadKtp);
        imgKtpPreview = findViewById(R.id.imgKtpPreview);

        btnUploadKtp.setVisibility(View.GONE);
        imgKtpPreview.setVisibility(View.GONE);
        etNamaPerusahaan.setVisibility(View.GONE);
        etPenanggungJawab.setVisibility(View.GONE);
    }

    // ================= SPINNER =================
    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.kategori_user,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spKategori.setAdapter(adapter);

        spKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String kategori = parent.getItemAtPosition(position).toString();

                if (kategori.equalsIgnoreCase("Pribadi")) {
                    btnUploadKtp.setVisibility(View.VISIBLE);

                    etNama.setVisibility(View.VISIBLE);
                    etNik.setVisibility(View.VISIBLE);
                    etTtl.setVisibility(View.VISIBLE);

                    etNamaPerusahaan.setVisibility(View.GONE);
                    etPenanggungJawab.setVisibility(View.GONE);
                } else {
                    btnUploadKtp.setVisibility(View.GONE);
                    imgKtpPreview.setVisibility(View.GONE);
                    ktpUri = null;

                    etNama.setVisibility(View.GONE);
                    etNik.setVisibility(View.GONE);
                    etTtl.setVisibility(View.GONE);

                    etNamaPerusahaan.setVisibility(View.VISIBLE);
                    etPenanggungJawab.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    // ================= ACTION =================
    private void setupAction() {
        btnUploadKtp.setOnClickListener(v -> pickImage.launch("image/*"));
        btnRegister.setOnClickListener(v -> handleRegister());
    }

    // ================= REGISTER =================
    private void handleRegister() {

        String kategori = spKategori.getSelectedItem().toString();

        if (!isValid(kategori)) return;

        String nama = kategori.equalsIgnoreCase("Perusahaan")
                ? etNamaPerusahaan.getText().toString().trim()
                : etNama.getText().toString().trim();

        boolean success = db.registerUser(
                kategori.equalsIgnoreCase("Pribadi")
                        ? etNama.getText().toString().trim()
                        : "",

                kategori.equalsIgnoreCase("Pribadi")
                        ? etNik.getText().toString().trim()
                        : "",

                kategori.equalsIgnoreCase("Pribadi")
                        ? etTtl.getText().toString().trim()
                        : "",

                etAlamat.getText().toString().trim(),
                etNoHp.getText().toString().trim(),
                etEmail.getText().toString().trim(),
                etPassword.getText().toString().trim(),

                kategori.equalsIgnoreCase("Perusahaan")
                        ? etNamaPerusahaan.getText().toString().trim()
                        : "",

                kategori.equalsIgnoreCase("Perusahaan")
                        ? etPenanggungJawab.getText().toString().trim()
                        : "",

                kategori
        );


        if (success) {
            Toast.makeText(this, "Registrasi berhasil", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Registrasi gagal", Toast.LENGTH_SHORT).show();
        }
    }

    // ================= VALIDATION =================
    private boolean isValid(String kategori) {

        if (etAlamat.getText().toString().isEmpty()
                || etNoHp.getText().toString().isEmpty()
                || etEmail.getText().toString().isEmpty()
                || etPassword.getText().toString().isEmpty()) {
            Toast.makeText(this, "Semua field wajib diisi", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {
            etEmail.setError("Email tidak valid");
            return false;
        }

        if (etPassword.getText().toString().length() < 6) {
            etPassword.setError("Password minimal 6 karakter");
            return false;
        }

        if (kategori.equalsIgnoreCase("Pribadi")) {
            if (ktpUri == null) {
                Toast.makeText(this, "Upload KTP wajib", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (etNik.getText().toString().length() != 16) {
                etNik.setError("NIK harus 16 digit");
                return false;
            }
        }

        if (kategori.equalsIgnoreCase("Perusahaan")) {
            if (etNamaPerusahaan.getText().toString().isEmpty()
                    || etPenanggungJawab.getText().toString().isEmpty()) {
                Toast.makeText(this, "Data perusahaan wajib diisi", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }

    // ================= OCR =================
    private void runTextRecognition(Uri uri) {
        try {
            InputImage image = InputImage.fromFilePath(this, uri);
            TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                    .process(image)
                    .addOnSuccessListener(this::extractKtpData)
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Gagal membaca KTP", Toast.LENGTH_SHORT).show()
                    );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= OCR PARSER =================
    private void extractKtpData(Text text) {

        String allText = text.getText().toUpperCase();

        // ===== NIK =====
        Matcher nikMatcher = Pattern.compile("\\b\\d{16}\\b").matcher(allText);
        if (nikMatcher.find()) {
            etNik.setText(nikMatcher.group());
        }

        // ===== NAMA =====
        for (Text.TextBlock block : text.getTextBlocks()) {
            String line = block.getText().toUpperCase();
            if (isValidNama(line)) {
                etNama.setText(formatNama(line));
                break;
            }
        }

        // ===== TTL =====
        Matcher ttlMatcher = Pattern.compile("[A-Z ]+,\\s*\\d{2}-\\d{2}-\\d{4}")
                .matcher(allText);
        if (ttlMatcher.find()) {
            etTtl.setText(ttlMatcher.group());
        }

        // ===== ALAMAT (JL + RT/RW) =====
        String jalan = "";
        String rtRw = "";

        for (Text.TextBlock block : text.getTextBlocks()) {
            for (Text.Line line : block.getLines()) {
                String teks = line.getText().toUpperCase();

                if ((teks.contains("JL") || teks.contains("JALAN")) && jalan.isEmpty()) {
                    jalan = teks;
                }

                Matcher m = Pattern.compile("\\b\\d{3}/\\d{3}\\b").matcher(teks);
                if (m.find() && rtRw.isEmpty()) {
                    rtRw = m.group();
                }
            }
        }

        if (!jalan.isEmpty()) {
            etAlamat.setText(rtRw.isEmpty() ? jalan : jalan + " RT/RW " + rtRw);
        } else {
            etAlamat.setText("Alamat tidak terbaca, isi manual");
        }

        Toast.makeText(this, "Data KTP terisi otomatis", Toast.LENGTH_SHORT).show();
    }

    private boolean isValidNama(String text) {
        if (!text.matches("[A-Z ]{5,}")) return false;
        for (String b : BLACKLIST_NAMA) {
            if (text.contains(b)) return false;
        }
        return true;
    }

    private String formatNama(String nama) {
        String[] parts = nama.toLowerCase().split(" ");
        StringBuilder result = new StringBuilder();
        for (String p : parts) {
            result.append(Character.toUpperCase(p.charAt(0)))
                    .append(p.substring(1)).append(" ");
        }
        return result.toString().trim();
    }
}
