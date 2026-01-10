package com.example.oxiraapp.models;

public class LokasiTabung {
    public String nama, alamat;
    public double lat, lng;
    public int stok;

    public LokasiTabung(String nama, String alamat, double lat, double lng, int stok) {
        this.nama = nama;
        this.alamat = alamat;
        this.lat = lat;
        this.lng = lng;
        this.stok = stok;
    }
}
