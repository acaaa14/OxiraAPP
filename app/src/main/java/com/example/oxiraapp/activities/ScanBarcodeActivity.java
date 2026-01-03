package com.example.oxiraapp.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.oxiraapp.R;

public class ScanBarcodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);

        String type = getIntent().getStringExtra("TYPE");
        String lokasi = getIntent().getStringExtra("LOKASI");

        TextView tv = findViewById(R.id.tvInfo);
        tv.setText(
                "Mode: " + type +
                        "\nLokasi: " + lokasi +
                        "\n\nSilakan scan barcode\nhanya di ruko Ozira"
        );
    }
}
