package com.example.oxiraapp.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.oxiraapp.R;
import com.example.oxiraapp.model.LokasiTabung;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;

public class HomeFragmentPribadi extends Fragment {

    private MapView mapView;
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private TextView tvNamaRuko, tvAlamat;

    // ================== LIFECYCLE ==================

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // WAJIB untuk OSMDroid
        Configuration.getInstance().setUserAgentValue(
                requireContext().getPackageName()
        );
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_home_pribadi,
                container,
                false
        );

        initView(view);
        setupMap();
        loadData();

        return view;
    }

    // ================== INIT ==================

    private void initView(View view) {
        mapView = view.findViewById(R.id.map);

        View bottomSheet = view.findViewById(R.id.bottomSheetLokasi);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        // SESUAI XML
        tvNamaRuko = view.findViewById(R.id.tvNamaRuko);
        tvAlamat = view.findViewById(R.id.tvAlamat);
    }

    // ================== MAP ==================

    private void setupMap() {
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(11.5);
        mapView.getController().setCenter(
                new GeoPoint(-6.200000, 106.816666)
        );
    }

    private void loadData() {
        List<LokasiTabung> list = new ArrayList<>();

        list.add(new LokasiTabung(
                "OXIRA Sudirman",
                "Jl. Jendral Sudirman No. 88, Jakarta Pusat",
                -6.21462, 106.82294, 3
        ));

        list.add(new LokasiTabung(
                "OXIRA Cengkareng",
                "Jl. Lingkar Luar Barat, Jakarta Barat",
                -6.14827, 106.73500, 15
        ));

        for (LokasiTabung lokasi : list) {
            addMarker(lokasi);
        }
    }

    private void addMarker(LokasiTabung lokasi) {

        Marker marker = new Marker(mapView);
        marker.setPosition(new GeoPoint(lokasi.lat, lokasi.lng));
        marker.setAnchor(
                Marker.ANCHOR_CENTER,
                Marker.ANCHOR_BOTTOM
        );

        Drawable icon = ContextCompat.getDrawable(
                requireContext(),
                lokasi.stok <= 5
                        ? R.drawable.tabung_merah
                        : R.drawable.tabung_hijau
        );

        if (icon != null) {
            icon.setBounds(0, 0, 90, 90);
            marker.setIcon(icon);
        }

        marker.setOnMarkerClickListener((m, map) -> {
            tvNamaRuko.setText(lokasi.nama);
            tvAlamat.setText(lokasi.alamat);
            bottomSheetBehavior.setState(
                    BottomSheetBehavior.STATE_EXPANDED
            );
            return true;
        });

        mapView.getOverlays().add(marker);
    }

    // ================== MAP LIFECYCLE ==================

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null) mapView.onPause();
    }
}
