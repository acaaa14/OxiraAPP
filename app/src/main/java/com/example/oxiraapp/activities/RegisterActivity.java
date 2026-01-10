package com.example.oxiraapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText etNama, etNik, etTtl, etAlamat;
    private EditText etNamaPerusahaan, etPenanggungJawab;
    private EditText etNoHp, etEmail, etPassword;
    private Spinner spKategori;
    private Button btnRegister, btnUploadKtp;
    private ImageView imgKtpPreview;

    private Uri ktpUri;
    private String permanentKtpPath = "";
    private DatabaseHelper db;

    private final ActivityResultLauncher<String> pickImage =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    ktpUri = uri;
                    imgKtpPreview.setImageURI(uri);
                    imgKtpPreview.setVisibility(View.VISIBLE);
                    saveImageToInternalStorage(uri);
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
    }

    private void saveImageToInternalStorage(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            File file = new File(getFilesDir(), "ktp_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            permanentKtpPath = file.getAbsolutePath();
        } catch (Exception e) {
            Log.e("Register", "Gagal simpan gambar", e);
        }
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this,
                        R.array.kategori_user,
                        android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spKategori.setAdapter(adapter);

        spKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String k = parent.getItemAtPosition(position).toString();
                boolean isPribadi = k.equalsIgnoreCase("Pribadi");

                btnUploadKtp.setVisibility(isPribadi ? View.VISIBLE : View.GONE);
                etNama.setVisibility(isPribadi ? View.VISIBLE : View.GONE);
                etNik.setVisibility(isPribadi ? View.VISIBLE : View.GONE);
                etTtl.setVisibility(isPribadi ? View.VISIBLE : View.GONE);
                etAlamat.setVisibility(isPribadi ? View.VISIBLE : View.GONE);

                etNamaPerusahaan.setVisibility(isPribadi ? View.GONE : View.VISIBLE);
                etPenanggungJawab.setVisibility(isPribadi ? View.GONE : View.VISIBLE);
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupAction() {
        btnUploadKtp.setOnClickListener(v -> pickImage.launch("image/*"));
        btnRegister.setOnClickListener(v -> handleRegister());
    }

    private void handleRegister() {
        String kategori = spKategori.getSelectedItem().toString();
        if (!isValid(kategori)) return;

        boolean success = db.registerUser(
                etNama.getText().toString().trim(),
                etNik.getText().toString().trim(),
                etTtl.getText().toString().trim(),
                etAlamat.getText().toString().trim(),
                etNoHp.getText().toString().trim(),
                etEmail.getText().toString().trim(),
                etPassword.getText().toString().trim(),
                etNamaPerusahaan.getText().toString().trim(),
                etPenanggungJawab.getText().toString().trim(),
                kategori,
                permanentKtpPath
        );

        Toast.makeText(this,
                success ? "Registrasi berhasil" : "Gagal registrasi",
                Toast.LENGTH_SHORT).show();

        if (success) finish();
    }

    private boolean isValid(String kategori) {
        if (etEmail.getText().toString().isEmpty()) return false;
        if (kategori.equalsIgnoreCase("Pribadi") && permanentKtpPath.isEmpty()) return false;
        return true;
    }

    private void runTextRecognition(Uri uri) {
        try {
            InputImage image = InputImage.fromFilePath(this, uri);
            TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                    .process(image)
                    .addOnSuccessListener(this::extractKTPData)
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Gagal baca KTP", Toast.LENGTH_SHORT).show()
                    );
        } catch (Exception e) {
            Log.e("OCR", "Error", e);
        }
    }

    // ================= OPTIMIZED OCR =================
    private void extractKTPData(Text visionText) {

        String rawText = visionText.getText().toUpperCase()
                .replace("N1K", "NIK")
                .replace("JL.", "JALAN");

        // ===== NIK =====
        Matcher nikMatcher = Pattern.compile("\\b\\d{16}\\b").matcher(rawText);
        if (nikMatcher.find()) {
            etNik.setText(nikMatcher.group());
        }

        List<String> lines = new ArrayList<>();
        for (Text.TextBlock block : visionText.getTextBlocks()) {
            for (Text.Line line : block.getLines()) {
                lines.add(line.getText().toUpperCase().trim());
            }
        }

        // ===== NAMA =====
        boolean nextIsNama = false;
        for (String line : lines) {
            if (line.equals("NAMA")) {
                nextIsNama = true;
                continue;
            }
            if (nextIsNama && isValidNama(line)) {
                etNama.setText(formatNama(line));
                break;
            }
        }

        // ===== TTL =====
        Matcher ttlMatcher =
                Pattern.compile("[A-Z ]+,\\s*\\d{2}-\\d{2}-\\d{4}")
                        .matcher(rawText);
        if (ttlMatcher.find()) {
            etTtl.setText(ttlMatcher.group());
        }

        // ===== ALAMAT =====
        String jalan = "";
        String rtRw = "";

        for (String line : lines) {
            if ((line.contains("JALAN") || line.startsWith("JL")) && jalan.isEmpty()) {
                jalan = line;
            }

            Matcher rtRwMatcher =
                    Pattern.compile("\\bRT\\s*\\d{3}/\\d{3}\\b|\\b\\d{3}/\\d{3}\\b")
                            .matcher(line);
            if (rtRwMatcher.find() && rtRw.isEmpty()) {
                rtRw = rtRwMatcher.group();
            }
        }

        if (!jalan.isEmpty()) {
            etAlamat.setText(rtRw.isEmpty()
                    ? jalan
                    : jalan + " RT/RW " + rtRw);
        }

        Toast.makeText(this, "Data KTP ditarik otomatis", Toast.LENGTH_SHORT).show();
    }

    // ================= OCR HELPERS =================
    private boolean isValidNama(String text) {
        if (!text.matches("[A-Z ]{5,}")) return false;

        String[] blacklist = {
                "PROVINSI", "KABUPATEN", "KOTA",
                "KECAMATAN", "KELURAHAN", "DESA",
                "RT", "RW", "AGAMA", "STATUS",
                "PEKERJAAN", "KEWARGANEGARAAN",
                "BERLAKU", "GOL", "ALAMAT","JENIS KELAMIN"
        };

        for (String b : blacklist) {
            if (text.contains(b)) return false;
        }
        return true;
    }

    private String formatNama(String nama) {
        String[] parts = nama.toLowerCase().split(" ");
        StringBuilder sb = new StringBuilder();
        for (String p : parts) {
            sb.append(Character.toUpperCase(p.charAt(0)))
                    .append(p.substring(1)).append(" ");
        }
        return sb.toString().trim();
    }

}
