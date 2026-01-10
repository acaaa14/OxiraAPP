package com.example.oxiraapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.oxiraapp.R;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

public class ScanBarcodeActivity extends AppCompatActivity {

    private DecoratedBarcodeView barcodeScanner;
    private String modeTransaksi;
    private String namaLokasi;
    private boolean isTransitioning = false;
    private static final int CAMERA_PERMISSION_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);

        modeTransaksi = getIntent().getStringExtra("TYPE");
        namaLokasi = getIntent().getStringExtra("LOKASI");

        barcodeScanner = findViewById(R.id.barcodeScanner);
        TextView tvInfo = findViewById(R.id.tvInfo);

        if (tvInfo != null) {
            tvInfo.setText("Mode: " + modeTransaksi + "\nLokasi: " + namaLokasi);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            barcodeScanner.decodeContinuous(callback);
        }
    }

    private final BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() != null && !isTransitioning) {
                isTransitioning = true;
                barcodeScanner.pause();
                barcodeScanner.getBarcodeView().stopDecoding();

                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    Intent intent;
                    // ✅ PERBAIKAN: Navigasi sesuai Mode (SEWA atau BELI)
                    if ("BELI".equalsIgnoreCase(modeTransaksi)) {
                        intent = new Intent(ScanBarcodeActivity.this, FormPembelianActivity.class);
                    } else {
                        intent = new Intent(ScanBarcodeActivity.this, FormPeminjamanActivity.class);
                    }
                    
                    intent.putExtra("TYPE", modeTransaksi);
                    intent.putExtra("LOKASI", namaLokasi);
                    intent.putExtra("SCAN_RESULT", result.getText());
                    
                    startActivity(intent);
                    finish();
                }, 100);
            }
        }
        @Override public void possibleResultPoints(List<com.google.zxing.ResultPoint> resultPoints) {}
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            barcodeScanner.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeScanner.pause();
    }
}
