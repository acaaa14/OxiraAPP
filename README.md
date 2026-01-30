# OxiraAPP - Emergency Oxygen Solution 🏥

OxiraAPP adalah ekosistem digital terintegrasi untuk pemesanan, penyewaan, dan pembelian tabung oksigen secara real-time. Sistem ini menggabungkan kekuatan **Android Native (Java)** sebagai antarmuka pengguna dan **Laravel** sebagai backend engine untuk transaksi keuangan yang aman.

---

## 🛠️ Tech Stack & Dependencies

### Mobile Application (Android)
- **Language:** Java
- **UI:** XML Layouts with Material Design 3
- **Maps:** `osmdroid` (OpenStreetMap) untuk pemetaan lokasi ruko.
- **AI/OCR:** `Google ML Kit Text Recognition` untuk scan KTP otomatis.
- **Scanning:** `ZXing Android Embedded` untuk validasi QR ruko.
- **Network:** `Volley` untuk komunikasi API ke backend.
- **Payment:** `Midtrans SDK (UI Kit)` untuk integrasi gerbang pembayaran.

### Backend (Web API)
- **Framework:** Laravel 10
- **Language:** PHP 8.x
- **Payment Engine:** Midtrans Snap (Sandbox Mode)
- **Database:** MySQL (MariaDB)
- **Tunneling:** Ngrok (untuk mengekspos localhost ke HTTPS publik)

---

## 📂 Struktur Folder Proyek

```text
OxiraAPP/
├── app/                        # [ANDROID STUDIO PROJECT]
│   ├── src/main/java/          # Logika Bisnis (Java)
│   │   └── com/example/oxiraapp/
│   │       ├── activities/     # Splash, Login, Register, Form, Maps, Scan
│   │       ├── fragments/      # Home (Peta), History, Profile
│   │       ├── models/         # Model Data (User, Lokasi)
│   │       └── utils/          # DatabaseHelper (SQLite), SessionManager
│   ├── src/main/res/           # Resource (XML, Drawables, Layouts)
│   │   ├── layout/             # Desain Antarmuka
│   │   └── xml/                # network_security_config.xml (Izin HTTP)
│   └── build.gradle            # Konfigurasi Build & Library
├── oxira-backend/              # [LARAVEL BACKEND PROJECT]
│   ├── app/Http/Controllers/   # API Controller (PaymentController)
│   ├── routes/api.php          # Definisi Endpoint API
│   ├── .env                    # Konfigurasi Server Key Midtrans
│   └── database/migrations/    # Struktur Tabel MySQL
├── daftar_qr_semua_cabang.html # Alat bantu simulasi Scan QR Cabang
└── README.md                   # Dokumentasi Utama
```

---

## 🚀 Fitur Unggulan

1. **Smart Registration (OCR):** Pengguna cukup memotret KTP, dan sistem akan mengisi Nama, NIK, dan Alamat secara otomatis menggunakan kecerdasan buatan.
2. **Dual-Stok Management:** Peta interaktif menampilkan sisa stok tabung untuk **Sewa** dan **Beli** secara terpisah di setiap ruko.
3. **Dynamic Marker:** Icon ruko berubah menjadi **Merah** jika stok kritis/habis dan **Hijau** jika stok melimpah.
4. **Automated Rental Calculator:** Menghitung biaya sewa secara presisi berdasarkan durasi hari (Tarif standar: Rp 25.000/hari).
5. **Secure Payment Gate:** Transaksi dilindungi oleh Midtrans, mendukung metode pembayaran Gopay, Virtual Account, dan lainnya.

---

## ⚙️ Langkah Instalasi

### 1. Konfigurasi Backend (Laravel)
1. Masuk ke terminal folder `oxira-backend`.
2. Jalankan `composer install`.
3. Buat file `.env` dan masukkan:
   ```env
   MIDTRANS_SERVER_KEY=isi_server_key_sandbox_anda
   APP_URL=isi_link_ngrok_anda
   ```
4. Jalankan server:
   ```bash
   php artisan serve --host=0.0.0.0 --port=8080
   ```

### 2. Ekspos Server (Ngrok)
Karena HP Android membutuhkan HTTPS untuk keamanan Midtrans:
1. Jalankan ngrok: `ngrok http 8080`.
2. Salin URL HTTPS yang muncul (misal: `https://abcd.ngrok-free.app`).

### 3. Konfigurasi Mobile (Android Studio)
1. Buka folder `OxiraAPP` di Android Studio.
2. Buka `FormPembelianActivity.java`.
3. Tempel URL ngrok Anda pada variabel `URL_NGROK`.
4. Pastikan `Client Key` di `initMidtransSDK` sudah benar.
5. Klik **Sync Project with Gradle Files** dan **Run**.

---

## 📝 Catatan Pengembangan
- **Jetifier:** Aktif (`android.enableJetifier=true`) untuk mendukung library legacy.
- **Cleartext Traffic:** Diizinkan melalui `network_security_config.xml` untuk mendukung komunikasi `http` selama masa pengembangan lokal.

---
© 2026 OxiraAPP - Teknologi untuk Kemanusiaan.
