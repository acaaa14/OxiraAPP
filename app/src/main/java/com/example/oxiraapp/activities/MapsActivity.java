package com.example.oxiraapp.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.oxiraapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity {

    private static final double DEFAULT_ZOOM = 11.5;
    private static final int ICON_SIZE_DP = 30;

    private MapView mapView;
    private String typeTransaksi; // SEWA / BELI
    private Object tvNamaRuko;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().load(
                getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(this)
        );

        setContentView(R.layout.activity_maps);

        typeTransaksi = getIntent().getStringExtra("TYPE");

        initMap();
        loadLokasiRuko();
    }

    // ================= MAP SETUP =================
    private void initMap() {
        mapView = findViewById(R.id.map);
        mapView.setMultiTouchControls(true);

        GeoPoint jakarta = new GeoPoint(-6.200000, 106.816666);
        mapView.getController().setZoom(DEFAULT_ZOOM);
        mapView.getController().setCenter(jakarta);
    }

    // ================= DATA LOKASI =================
    private void loadLokasiRuko() {

        List<Lokasi> lokasiList = new ArrayList<>();

        lokasiList.add(new Lokasi("Ruko Jakarta Sudirman", -6.2146, 106.8229, 10, 5));
        lokasiList.add(new Lokasi("Ruko Jakarta Cengkareng", -6.1489, 106.7350, 3, 0));
        lokasiList.add(new Lokasi("Ruko Depok Margonda", -6.3690, 106.8300, 0, 8));
        lokasiList.add(new Lokasi("Ruko Bogor Pajajaran", -6.5944, 106.7891, 7, 4));
        lokasiList.add(new Lokasi("Ruko Bandung Dago", -6.8721, 107.6139, 12, 9));

        for (Lokasi lokasi : lokasiList) {
            addMarker(lokasi);
        }

        mapView.invalidate();
    }

    // ================= MARKER =================
    private void addMarker(Lokasi lokasi) {

        Marker marker = new Marker(mapView);
        marker.setPosition(new GeoPoint(lokasi.lat, lokasi.lng));
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

        marker.setIcon(getMarkerIcon(lokasi));
        marker.setTitle(lokasi.nama);
        marker.setSubDescription(
                "Sewa: " + lokasi.stokSewa + " | Beli: " + lokasi.stokBeli
        );

        marker.setOnMarkerClickListener((m, mapView) -> {
            showBottomSheet(lokasi);
            return true;
        });

        mapView.getOverlays().add(marker);
    }

    // ================= ICON LOGIC =================
    private Drawable getMarkerIcon(Lokasi lokasi) {

        int iconRes =
                (lokasi.stokSewa <= 0 && lokasi.stokBeli <= 0)
                        ? R.drawable.tabung_merah
                        : R.drawable.tabung_hijau;

        Drawable drawable = ContextCompat.getDrawable(this, iconRes);
        return resizeDrawable(drawable, ICON_SIZE_DP);
    }

    // ================= BOTTOM SHEET =================
    private void showBottomSheet(Lokasi lokasi) {

        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_lokasi, null);

        TextView tvNamaRuko = view.findViewById(R.id.tvNamaRuko);
        TextView tvAlamat = view.findViewById(R.id.tvAlamat);
        TextView tvStok = view.findViewById(R.id.tvStok);

        Button btnSewa = view.findViewById(R.id.btnSewa);
        Button btnBeli = view.findViewById(R.id.btnBeli);

        tvNamaRuko.setText(lokasi.nama);
        tvAlamat.setText("Jl. Jenderal Sudirman No.88, Jakarta");

        tvStok.setText(
                "Sewa: " + lokasi.stokSewa +
                        " | Beli: " + lokasi.stokBeli
        );

        // ================= LOGIC SEWA =================
        btnSewa.setEnabled(lokasi.stokSewa > 0);
        btnSewa.setOnClickListener(v -> {
            openScan("SEWA", lokasi.nama);
            dialog.dismiss();
        });

        // ================= LOGIC BELI =================
        btnBeli.setEnabled(lokasi.stokBeli > 0);
        btnBeli.setOnClickListener(v -> {
            openScan("BELI", lokasi.nama);
            dialog.dismiss();
        });

        dialog.setContentView(view);
        dialog.show();
    }

    private void openScan(String type, String lokasi) {
        Intent intent = new Intent(this, ScanBarcodeActivity.class);
        intent.putExtra("TYPE", type);
        intent.putExtra("LOKASI", lokasi);
        startActivity(intent);
    }

    // ================= UTIL =================
    private BitmapDrawable resizeDrawable(Drawable drawable, int dp) {
        if (drawable == null) return null;

        int px = (int) (dp * getResources().getDisplayMetrics().density);
        Bitmap bitmap = Bitmap.createBitmap(px, px, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        drawable.setBounds(0, 0, px, px);
        drawable.draw(canvas);

        return new BitmapDrawable(getResources(), bitmap);
    }

    // ================= MODEL =================
    static class Lokasi {
        String nama;
        double lat, lng;
        int stokSewa, stokBeli;

        Lokasi(String nama, double lat, double lng, int sewa, int beli) {
            this.nama = nama;
            this.lat = lat;
            this.lng = lng;
            this.stokSewa = sewa;
            this.stokBeli = beli;
        }
    }
}
