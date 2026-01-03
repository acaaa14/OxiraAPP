# 🫁 OXIRA – Oxygen Rental & Inventory Application

OXIRA adalah aplikasi mobile berbasis **Android (Android Studio)** yang dirancang untuk meningkatkan efisiensi pengelolaan tabung oksigen melalui **pelacakan otomatis berbasis QR Code**, sistem **sewa & beli tabung**, serta **pembayaran digital**.

Aplikasi ini dikembangkan oleh **pengembang pemula** dengan fokus pada sistem yang **sederhana, terstruktur, dan dapat dikembangkan secara bertahap**.

---

## 📌 Latar Belakang

Pengelolaan tabung oksigen secara manual sering menimbulkan masalah seperti:
- Kesalahan pencatatan stok
- Sulitnya pelacakan tabung
- Proses administrasi yang lambat
- Risiko kehilangan dan kerusakan tanpa bukti yang jelas

OXIRA hadir sebagai solusi digital dengan memanfaatkan:
- QR Code
- Maps (OpenStreetMap)
- Sistem transaksi digital
- Riwayat transaksi otomatis

---

## 🎯 Tujuan Pengembangan

Tujuan utama aplikasi OXIRA:
1. Meningkatkan efisiensi pengelolaan tabung oksigen
2. Mempermudah proses sewa dan beli tabung
3. Menyediakan pelacakan tabung berbasis QR Code
4. Meningkatkan keamanan transaksi dengan pembayaran digital
5. Mengurangi kesalahan administrasi manual

---

## 🧩 Ruang Lingkup Sistem

### Platform
- 📱 **Mobile Android** → Pelanggan
- 🌐 **Website** → Admin (pengembangan tahap lanjutan)

### Fokus Pengembangan Awal
- Aplikasi pelanggan (Android)
- Website admin akan dikembangkan setelah fitur pelanggan stabil

---

## 🏗️ Arsitektur Sistem
Android App (Pelanggan)
|
| REST API (FastAPI)
|
Database MySQL


---

## 🗄️ Desain Database (Awal)

Tabel utama:
- `users` → data pelanggan
- `tabung` → data tabung oksigen
- `branch` → data cabang/ruko (awal 3 cabang, dapat ditambah)

---

## 🔐 Alur Sistem Pelanggan

### 1️⃣ Registrasi Pelanggan

Pelanggan melakukan registrasi melalui aplikasi Android.

**Form Registrasi:**
- Input KTP  
  - Menggunakan FastAPI
  - Data otomatis terisi:
    - Nama pelanggan
    - NIK
    - Tanggal lahir (TTL)
    - Alamat (dapat diubah)
- Kategori pelanggan:
  - Pribadi
  - Company
- Email
- Password

Data akan disimpan ke database MySQL.

---

### 2️⃣ Login Pelanggan
- Login menggunakan email dan password
- Validasi ke database
- Tersedia fitur **Lupa Password**

---

### 3️⃣ Navigasi Berdasarkan Kategori

| Kategori | Halaman |
|--------|---------|
| Pribadi | Homepage Pribadi |
| Company | Homepage Company |

---

## 🏠 Homepage Pribadi

### 🔹 Sewa Tabung
- Menampilkan peta lokasi (OpenStreetMap)
- Marker menunjukkan:
  - Lokasi branch
  - Jumlah tabung tersedia
- Klik lokasi → detail stok
- Untuk menyewa:
  - Scan QR Code di lokasi
  - QR Code berisi:
    - Kode tabung
    - Berat tabung
    - ID branch
- Setelah scan:
  - Upload foto kondisi awal tabung
  - Pilih tanggal pengembalian
  - Hitung harga sewa per hari
- Lanjut ke pembayaran (Midtrans)

---

### 🔹 Beli Tabung
- Menampilkan lokasi branch
- Scan QR Code tabung
- Tampilkan harga
- Pembayaran menggunakan Midtrans

---

## 🏢 Homepage Company

Digunakan untuk pelanggan perusahaan (kerjasama).

**Form Kerjasama:**
- Nama perusahaan
- Alamat perusahaan
- Kontak penanggung jawab

Fitur tambahan:
- Sistem pengantaran dan penjemputan tabung
- Digunakan untuk pengisian ulang tabung
- Pembayaran via Midtrans

---

## 🧭 Struktur Navigasi Aplikasi

Main Page berisi:
- Home
- History
- Profile
- Logout

---

## 📜 History Transaksi

### 🔹 History Baru
- Transaksi aktif
- Menampilkan QR Code pengembalian

### 🔹 History Lama
- Transaksi selesai
- Arsip data

---

## 🔁 Sistem Pengembalian Tabung

- Pelanggan scan QR Code pengembalian
- Sistem menampilkan upload foto kondisi tabung
- Jika terdapat kerusakan:
  - Sistem menghitung denda sesuai kerusakan
- Konsep mirip sistem tap KRL (GoTransit)

---

## 🗺️ Teknologi yang Digunakan

### Android (Frontend)
- Java
- Android Studio
- Activity & Fragment
- Bottom Navigation
- Bottom Sheet
- OSMDroid (OpenStreetMap)
- Camera & QR Scanner

### Backend
- FastAPI (REST API)

### Database
- MySQL

### Payment Gateway
- Midtrans

