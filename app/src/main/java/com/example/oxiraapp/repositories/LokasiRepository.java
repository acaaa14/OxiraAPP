package com.example.oxiraapp.repositories;

import com.example.oxiraapp.models.LokasiTabung;
import java.util.ArrayList;
import java.util.List;

public class LokasiRepository {

    public static List<LokasiTabung> getDaftarLokasi() {
        List<LokasiTabung> list = new ArrayList<>();

        // JAKARTA (10 Lokasi)
        list.add(new LokasiTabung("Oxira Jakarta Sudirman", "Jl. Jenderal Sudirman No. 88, Jakarta Pusat", -6.21462, 106.82294, 8));
        list.add(new LokasiTabung("Oxira Jakarta Cengkareng", "Jl. Lingkar Luar Barat No. 15, Jakarta Barat", -6.14827, 106.73500, 12));
        list.add(new LokasiTabung("Oxira Jakarta Senayan", "Kawasan Senayan, Jakarta Pusat", -6.21342, 106.81153, 10));
        list.add(new LokasiTabung("Oxira Jakarta Kelapa Gading", "Jl. Boulevard Raya, Jakarta Utara", -6.1586, 106.9014, 15));
        list.add(new LokasiTabung("Oxira Jakarta Kemang", "Jl. Kemang Raya No. 10, Jakarta Selatan", -6.2734, 106.8113, 7));
        list.add(new LokasiTabung("Oxira Jakarta Pluit", "Jl. Pluit Selatan Raya, Jakarta Utara", -6.1214, 106.7854, 0));
        list.add(new LokasiTabung("Oxira Jakarta Blok M", "Kawasan Melawai, Jakarta Selatan", -6.2444, 106.7982, 11));
        list.add(new LokasiTabung("Oxira Jakarta Sunter", "Jl. Danau Sunter, Jakarta Utara", -6.1389, 106.8761, 6));
        list.add(new LokasiTabung("Oxira Jakarta Grogol", "Jl. Dr. Susilo, Jakarta Barat", -6.1667, 106.7892, 14));
        list.add(new LokasiTabung("Oxira Jakarta Tebet", "Jl. Tebet Raya, Jakarta Selatan", -6.2281, 106.8485, 10));

        // BANDUNG (10 Lokasi)
        list.add(new LokasiTabung("Oxira Bandung Pasteur", "Jl. Dr. Djunjunan No. 162, Pasteur", -6.8915, 107.5894, 8));
        list.add(new LokasiTabung("Oxira Bandung Dago", "Jl. Ir. H. Juanda, Dago", -6.8868, 107.6153, 12));
        list.add(new LokasiTabung("Oxira Bandung Asia Afrika", "Jl. Asia Afrika, Bandung Kota", -6.9211, 107.6109, 15));
        list.add(new LokasiTabung("Oxira Bandung Antapani", "Jl. Antapani, Bandung Timur", -6.9147, 107.6622, 9));
        list.add(new LokasiTabung("Oxira Bandung Kopo", "Jl. Kopo, Bandung Selatan", -6.9481, 107.5892, 0));
        list.add(new LokasiTabung("Oxira Bandung Buah Batu", "Jl. Buah Batu, Bandung", -6.9456, 107.6234, 11));
        list.add(new LokasiTabung("Oxira Bandung Gedebage", "Kawasan Gedebage, Bandung", -6.9432, 107.6981, 14));
        list.add(new LokasiTabung("Oxira Bandung Setiabudi", "Jl. Dr. Setiabudi, Bandung Utara", -6.8654, 107.5943, 7));
        list.add(new LokasiTabung("Oxira Bandung Lembang", "Jl. Raya Lembang, Bandung Barat", -6.8178, 107.6182, 5));
        list.add(new LokasiTabung("Oxira Bandung Cicadas", "Jl. Ahmad Yani, Cicadas", -6.9082, 107.6421, 10));

        // BEKASI (5 Lokasi)
        list.add(new LokasiTabung("Oxira Bekasi Cyber Park", "Kawasan BCP, Bekasi Selatan", -6.2432, 106.9921, 13));
        list.add(new LokasiTabung("Oxira Bekasi Summarecon", "Kawasan Summarecon Bekasi", -6.2214, 107.0012, 18));
        list.add(new LokasiTabung("Oxira Bekasi Harapan Indah", "Kawasan Medan Satria, Bekasi", -6.1823, 106.9745, 10));
        list.add(new LokasiTabung("Oxira Bekasi Tambun", "Jl. Raya Sultan Hasanuddin, Tambun", -6.2643, 107.0612, 7));
        list.add(new LokasiTabung("Oxira Bekasi Jatiasih", "Jl. Raya Jatiasih, Bekasi", -6.3012, 106.9643, 0));

        // DEPOK (5 Lokasi)
        list.add(new LokasiTabung("Oxira Depok Margonda", "Jl. Margonda Raya, Depok", -6.3721, 106.8312, 12));
        list.add(new LokasiTabung("Oxira Depok Cinere", "Jl. Raya Cinere, Depok", -6.3412, 106.7812, 0));
        list.add(new LokasiTabung("Oxira Depok Sawangan", "Jl. Raya Sawangan, Depok", -6.3943, 106.7712, 11));
        list.add(new LokasiTabung("Oxira Depok UI", "Kawasan Kampus UI, Depok", -6.3612, 106.8243, 15));
        list.add(new LokasiTabung("Oxira Depok Cimanggis", "Jl. Raya Bogor, Cimanggis", -6.3543, 106.8612, 6));

        // BOGOR (5 Lokasi)
        list.add(new LokasiTabung("Oxira Bogor Pajajaran", "Jl. Raya Pajajaran, Bogor Timur", -6.6012, 106.8043, 14));
        list.add(new LokasiTabung("Oxira Bogor Baranangsiang", "Kawasan Terminal Baranangsiang", -6.6043, 106.8112, 9));
        list.add(new LokasiTabung("Oxira Bogor Yasmin", "Jl. KH. Abdullah Bin Nuh, Bogor", -6.5612, 106.7743, 11));
        list.add(new LokasiTabung("Oxira Bogor Ciawi", "Kawasan Simpang Ciawi, Bogor", -6.6543, 106.8512, 7));
        list.add(new LokasiTabung("Oxira Bogor Sentul", "Kawasan Sentul City, Bogor", -6.5412, 106.8812, 13));

        return list;
    }
}
