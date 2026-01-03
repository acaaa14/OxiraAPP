package com.example.oxiraapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.oxiraapp.R;
import com.example.oxiraapp.activities.KerjasamaActivity;
import com.example.oxiraapp.activities.MapsActivity;

public class HomePerusahaanFragment extends Fragment {

    private Button btnKerjasama, btnMaps;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_home_perusahaan,
                container,
                false
        );

        btnKerjasama = view.findViewById(R.id.btnKerjasama);
        btnMaps = view.findViewById(R.id.btnMaps);

        // 👉 Ajukan Kerjasama
        btnKerjasama.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), KerjasamaActivity.class);
            startActivity(intent);
        });

        // 👉 Buka Maps
        btnMaps.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MapsActivity.class);
            startActivity(intent);
        });

        return view;
    }
}
