package com.example.oxiraapp.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.oxiraapp.R;
import com.example.oxiraapp.activities.ScanBarcodeActivity;
import com.example.oxiraapp.models.LokasiTabung;
import com.example.oxiraapp.repositories.LokasiRepository;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;

public class HomeFragmentPribadi extends Fragment {

    private MapView mapView;
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private EditText etSearch;
    private TextView tvLabelBottomSheet;
    
    private LinearLayout containerLokasi;
    private LinearLayout layoutActionButtons;
    private TextView tvStok;
    private String lokasiTerpilih = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().setUserAgentValue(requireContext().getPackageName());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_pribadi, container, false);

        initView(view);
        initBottomSheet(view);
        setupMap();
        setupSearch();
        loadAllMarkers();

        return view;
    }

    private void initView(View view) {
        mapView = view.findViewById(R.id.map);
        etSearch = view.findViewById(R.id.etSearch);
    }

    private void initBottomSheet(View view) {
        View bottomSheet = view.findViewById(R.id.bottomSheetLokasi);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        
        float density = getResources().getDisplayMetrics().density;
        bottomSheetBehavior.setPeekHeight((int) (140 * density));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        tvLabelBottomSheet = bottomSheet.findViewById(R.id.tvLabelBottomSheet);
        containerLokasi = bottomSheet.findViewById(R.id.containerLokasi);
        layoutActionButtons = bottomSheet.findViewById(R.id.layoutActionButtons);
        tvStok = bottomSheet.findViewById(R.id.tvStok);

        renderRukoCards(LokasiRepository.getDaftarLokasi());

        view.findViewById(R.id.btnSewa).setOnClickListener(v -> openScan("SEWA"));
        view.findViewById(R.id.btnBeli).setOnClickListener(v -> openScan("BELI"));
    }

    private void setupSearch() {
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                cariDanFilter(etSearch.getText().toString().trim());
                return true;
            }
            return false;
        });
    }

    private void cariDanFilter(String keyword) {
        List<LokasiTabung> all = LokasiRepository.getDaftarLokasi();
        List<LokasiTabung> filtered = new ArrayList<>();

        for (LokasiTabung l : all) {
            if (l.nama.toLowerCase().contains(keyword.toLowerCase()) || 
                l.alamat.toLowerCase().contains(keyword.toLowerCase())) {
                filtered.add(l);
            }
        }

        if (filtered.isEmpty()) {
            Toast.makeText(getContext(), "Tidak ada ruko di '" + keyword + "'", Toast.LENGTH_SHORT).show();
            return;
        }

        tvLabelBottomSheet.setText(keyword.isEmpty() ? "Lihat lokasi yang tersedia" : "Ruko tersedia di '" + keyword + "'");
        renderRukoCards(filtered);
        
        mapView.getController().animateTo(new GeoPoint(filtered.get(0).lat, filtered.get(0).lng));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void renderRukoCards(List<LokasiTabung> list) {
        containerLokasi.removeAllViews();
        for (LokasiTabung lokasi : list) {
            View itemView = getLayoutInflater().inflate(R.layout.item_lokasi, containerLokasi, false);
            CardView cardView = (CardView) itemView;
            
            TextView tvNama = itemView.findViewById(R.id.tvNama);
            TextView tvAlamat = itemView.findViewById(R.id.tvAlamat);
            TextView tvStokItem = itemView.findViewById(R.id.tvStok);

            tvNama.setText(lokasi.nama);
            tvAlamat.setText(lokasi.alamat);
            tvStokItem.setText("Stok: " + lokasi.stok + " tabung");

            itemView.setOnClickListener(v -> {
                for (int i = 0; i < containerLokasi.getChildCount(); i++) {
                    ((CardView) containerLokasi.getChildAt(i)).setCardBackgroundColor(Color.WHITE);
                }
                cardView.setCardBackgroundColor(Color.parseColor("#92B4A7"));
                
                lokasiTerpilih = lokasi.nama;
                tvStok.setVisibility(View.VISIBLE);
                tvStok.setText("Stok tersedia: " + lokasi.stok + " tabung");
                layoutActionButtons.setVisibility(View.VISIBLE);
                
                mapView.getController().animateTo(new GeoPoint(lokasi.lat, lokasi.lng));
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            });

            itemView.findViewById(R.id.btnRute).setOnClickListener(v -> bukaPetaRute(lokasi.lat, lokasi.lng));

            containerLokasi.addView(itemView);
        }
    }

    private void setupMap() {
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(12.0);
        mapView.getController().setCenter(new GeoPoint(-6.21462, 106.82294));
    }

    private void loadAllMarkers() {
        List<LokasiTabung> daftar = LokasiRepository.getDaftarLokasi();
        mapView.getOverlays().clear(); // Bersihkan marker lama
        for (LokasiTabung lokasi : daftar) {
            addMarker(lokasi);
        }
        mapView.invalidate();
    }

    private void addMarker(LokasiTabung lokasi) {
        Marker marker = new Marker(mapView);
        marker.setPosition(new GeoPoint(lokasi.lat, lokasi.lng));
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setTitle(lokasi.nama);

        // --- PERBAIKAN ICON: Tabung Merah (stok <= 0) & Tabung Hijau (stok > 5) ---
        int drawableRes = (lokasi.stok <= 0) ? R.drawable.tabung_merah : R.drawable.tabung_hijau;
        Drawable icon = ContextCompat.getDrawable(requireContext(), drawableRes);
        
        if (icon != null) {
            // Sesuaikan ukuran icon (misal 40dp x 40dp)
            int sizePx = (int) (15 * getResources().getDisplayMetrics().density);
            Bitmap bitmap = ((BitmapDrawable) icon).getBitmap();
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, sizePx, sizePx, false);
            marker.setIcon(new BitmapDrawable(getResources(), scaledBitmap));
        }

        marker.setOnMarkerClickListener((m, map) -> {
            cariDanFilter(lokasi.nama);
            return true;
        });
        mapView.getOverlays().add(marker);
    }

    private void openScan(String type) {
        if (lokasiTerpilih.isEmpty()) {
            Toast.makeText(getContext(), "Pilih ruko terlebih dahulu", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(getContext(), ScanBarcodeActivity.class);
        intent.putExtra("TYPE", type);
        intent.putExtra("LOKASI", lokasiTerpilih);
        startActivity(intent);
    }

    private void bukaPetaRute(double lat, double lng) {
        Uri uri = Uri.parse("google.navigation:q=" + lat + "," + lng);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

    @Override public void onResume() { super.onResume(); mapView.onResume(); }
    @Override public void onPause() { super.onPause(); mapView.onPause(); }
}
