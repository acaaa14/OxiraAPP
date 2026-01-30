package com.example.oxiraapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.oxiraapp.R;
import com.example.oxiraapp.models.LokasiTabung;
import com.example.oxiraapp.repositories.LokasiRepository;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.osmdroid.config.Configuration;
import org.osmdroid.views.MapView;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;

import java.util.List;

public class MapsActivity extends AppCompatActivity {

    private MapView mapView;
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private LinearLayout containerLokasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // INIT OSM
        Configuration.getInstance().load(this, getSharedPreferences("osm_prefs", MODE_PRIVATE));

        mapView = findViewById(R.id.map);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        // INIT BOTTOM SHEET
        View bottomSheet = findViewById(R.id.bottomSheetLokasi);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        // INIT LIST LOKASI
        containerLokasi = bottomSheet.findViewById(R.id.containerLokasi);
        
        if (containerLokasi != null) {
            List<LokasiTabung> daftarLokasi = LokasiRepository.getDaftarLokasi();
            for (LokasiTabung lokasi : daftarLokasi) {
                TextView tv = new TextView(this);
                // Menampilkan stok sewa dan stok beli secara terpisah
                tv.setText(lokasi.nama + "\nSewa: " + lokasi.stokSewa + " tabung | Beli: " + lokasi.stokBeli + " tabung");
                tv.setPadding(12, 12, 12, 12);
                tv.setTextSize(14);
                containerLokasi.addView(tv);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
}
