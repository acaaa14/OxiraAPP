package com.example.oxiraapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.oxiraapp.R;
import com.example.oxiraapp.activities.LoginActivity;
import com.example.oxiraapp.models.User;
import com.example.oxiraapp.utils.DatabaseHelper;
import com.example.oxiraapp.utils.SessionManager;

public class ProfileFragment extends Fragment {

    // Layout
    private LinearLayout layoutPribadi, layoutPerusahaan;

    // Pribadi
    private TextView tvNama, tvNik, tvTtl;

    // Perusahaan
    private TextView tvNamaPerusahaan, tvPenanggungJawab;

    // Umum
    private TextView tvAlamat, tvEmail, tvNoHp;

    private Button btnLogout;

    private DatabaseHelper db;
    private SessionManager session;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        initView(view);

        db = new DatabaseHelper(requireContext());
        session = new SessionManager(requireContext());

        loadProfile();

        btnLogout.setOnClickListener(v -> {
            session.logout();
            startActivity(new Intent(requireActivity(), LoginActivity.class));
            requireActivity().finish();
        });

        return view;
    }

    private void initView(View v) {
        layoutPribadi = v.findViewById(R.id.layoutPribadi);
        layoutPerusahaan = v.findViewById(R.id.layoutPerusahaan);

        tvNama = v.findViewById(R.id.tvNama);
        tvNik = v.findViewById(R.id.tvNik);
        tvTtl = v.findViewById(R.id.tvTtl);

        tvNamaPerusahaan = v.findViewById(R.id.tvNamaPerusahaan);
        tvPenanggungJawab = v.findViewById(R.id.tvPenanggungJawab);

        tvAlamat = v.findViewById(R.id.tvAlamat);
        tvEmail = v.findViewById(R.id.tvEmail);
        tvNoHp = v.findViewById(R.id.tvNoHp);

        btnLogout = v.findViewById(R.id.btnLogout);
    }

    private void loadProfile() {

        String email = session.getEmail();
        if (email == null) return;

        User user = db.getUser(email);
        if (user == null) return;

        // Data umum
        tvAlamat.setText(user.alamat);
        tvEmail.setText(user.email);
        tvNoHp.setText(user.noHp);

        if (user.kategori.equalsIgnoreCase("Pribadi")) {

            layoutPribadi.setVisibility(View.VISIBLE);
            layoutPerusahaan.setVisibility(View.GONE);

            tvNama.setText(user.nama);
            tvNik.setText(user.nik);
            tvTtl.setText(user.ttl);

        } else {

            layoutPerusahaan.setVisibility(View.VISIBLE);
            layoutPribadi.setVisibility(View.GONE);

            tvNamaPerusahaan.setText(user.namaPerusahaan);
            tvPenanggungJawab.setText(user.penanggungJawab);

        }
    }
}
