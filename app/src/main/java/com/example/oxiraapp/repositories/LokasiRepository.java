package com.example.oxiraapp.repositories;

import com.example.oxiraapp.models.LokasiTabung;
import java.util.ArrayList;
import java.util.List;

public class LokasiRepository {

    public static List<LokasiTabung> getDaftarLokasi() {
        List<LokasiTabung> list = new ArrayList<>();

        // Format: Nama, Alamat, Lat, Lng, StokSewa, StokBeli
        
        // JAKARTA
        list.add(new LokasiTabung("Oxira Jakarta Sudirman", "Jl. Jenderal Sudirman No. 88, Jakarta Pusat", -6.21462, 106.82294, 8, 5));
        list.add(new LokasiTabung("Oxira Jakarta Cengkareng", "Jl. Lingkar Luar Barat No. 15, Jakarta Barat", -6.14827, 106.73500, 12, 10));
        list.add(new LokasiTabung("Oxira Jakarta Senayan", "Kawasan Senayan, Jakarta Pusat", -6.21342, 106.81153, 10, 8));
        list.add(new LokasiTabung("Oxira Jakarta Kelapa Gading", "Jl. Boulevard Raya, Jakarta Utara", -6.1586, 106.9014, 15, 12));
        list.add(new LokasiTabung("Oxira Jakarta Kemang", "Jl. Kemang Raya No. 10, Jakarta Selatan", -6.2734, 106.8113, 0, 5)); // Stok Sewa Habis
        list.add(new LokasiTabung("Oxira Jakarta Pluit", "Jl. Pluit Selatan Raya, Jakarta Utara", -6.1214, 106.7854, 9, 0)); // Stok Beli Habis
        list.add(new LokasiTabung("Oxira Jakarta Blok M", "Kawasan Melawai, Jakarta Selatan", -6.2444, 106.7982, 11, 15));
        list.add(new LokasiTabung("Oxira Jakarta Sunter", "Jl. Danau Sunter, Jakarta Utara", -6.1389, 106.8761, 6, 4));
        list.add(new LokasiTabung("Oxira Jakarta Grogol", "Jl. Dr. Susilo, Jakarta Barat", -6.1667, 106.7892, 14, 10));
        list.add(new LokasiTabung("Oxira Jakarta Tebet", "Jl. Tebet Raya, Jakarta Selatan", -6.2281, 106.8485, 10, 8));

        // BANDUNG
        list.add(new LokasiTabung("Oxira Bandung Pasteur", "Jl. Dr. Djunjunan No. 162, Pasteur", -6.8915, 107.5894, 8, 6));
        list.add(new LokasiTabung("Oxira Bandung Dago", "Jl. Ir. H. Juanda, Dago", -6.8868, 107.6153, 12, 10));
        list.add(new LokasiTabung("Oxira Bandung Asia Afrika", "Jl. Asia Afrika, Bandung Kota", -6.9211, 107.6109, 15, 12));
        list.add(new LokasiTabung("Oxira Bandung Antapani", "Jl. Antapani, Bandung Timur", -6.9147, 107.6622, 9, 7));
        list.add(new LokasiTabung("Oxira Bandung Kopo", "Jl. Kopo, Bandung Selatan", -6.9481, 107.5892, 0, 4)); // Habis Sewa
        list.add(new LokasiTabung("Oxira Bandung Buah Batu", "Jl. Buah Batu, Bandung", -6.9456, 107.6234, 11, 9));
        list.add(new LokasiTabung("Oxira Bandung Gedebage", "Kawasan Gedebage, Bandung", -6.9432, 107.6981, 14, 10));
        list.add(new LokasiTabung("Oxira Bandung Setiabudi", "Jl. Dr. Setiabudi, Bandung Utara", -6.8654, 107.5943, 7, 5));
        list.add(new LokasiTabung("Oxira Bandung Lembang", "Jl. Raya Lembang, Bandung Barat", -6.8178, 107.6182, 5, 0)); // Habis Beli
        list.add(new LokasiTabung("Oxira Bandung Cicadas", "Jl. Ahmad Yani, Cicadas", -6.9082, 107.6421, 10, 8));

        // Tambahkan kota lainnya dengan pola yang sama...
        // BEKASI, DEPOK, BOGOR (Data simulasi stok)
        list.add(new LokasiTabung("Oxira Bekasi Summarecon", "Bekasi", -6.2214, 107.0012, 18, 12));
        list.add(new LokasiTabung("Oxira Depok UI", "Depok", -6.3612, 106.8243, 15, 10));
        list.add(new LokasiTabung("Oxira Bogor Sentul", "Bogor", -6.5412, 106.8812, 13, 9));

        return list;
    }
}
