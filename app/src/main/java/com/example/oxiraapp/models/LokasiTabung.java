package com.example.oxiraapp.models;

public class LokasiTabung {
    public String nama, alamat;
    public double lat, lng;
    public int stokSewa; // Stok khusus sewa
    public int stokBeli; // Stok khusus beli

    public LokasiTabung(String nama, String alamat, double lat, double lng, int stokSewa, int stokBeli) {
        this.nama = nama;
        this.alamat = alamat;
        this.lat = lat;
        this.lng = lng;
        this.stokSewa = stokSewa;
        this.stokBeli = stokBeli;
    }
}
