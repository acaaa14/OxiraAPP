package com.example.oxiraapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.oxiraapp.models.User;

public class DatabaseHelper extends SQLiteOpenHelper {

    // ================= DATABASE =================
    private static final String DATABASE_NAME = "oxira.db";
    private static final int DATABASE_VERSION = 4;

    // ================= TABLE =================
    public static final String TABLE_USER = "users";

    // ================= COLUMN =================
    public static final String COL_ID = "id";
    public static final String COL_NAMA = "nama";
    public static final String COL_NIK = "nik";
    public static final String COL_TTL = "ttl";
    public static final String COL_NAMA_PERUSAHAAN = "nama_perusahaan";
    public static final String COL_PENANGGUNG_JAWAB = "penanggung_jawab";
    public static final String COL_ALAMAT = "alamat";
    public static final String COL_NOHP = "no_hp";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";
    public static final String COL_KATEGORI = "kategori";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // ================= CREATE TABLE =================
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_USER + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAMA + " TEXT, " +
                COL_NIK + " TEXT, " +
                COL_TTL + " TEXT, " +
                COL_NAMA_PERUSAHAAN + " TEXT, " +
                COL_PENANGGUNG_JAWAB + " TEXT, " +
                COL_ALAMAT + " TEXT NOT NULL, " +
                COL_NOHP + " TEXT NOT NULL, " +
                COL_EMAIL + " TEXT UNIQUE NOT NULL, " +
                COL_PASSWORD + " TEXT NOT NULL, " +
                COL_KATEGORI + " TEXT NOT NULL" +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    // ================= REGISTER USER =================
    public boolean registerUser(
            String nama,
            String nik,
            String ttl,
            String alamat,
            String noHp,
            String email,
            String password,
            String namaPerusahaan,
            String penanggungJawab,
            String kategori
    ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_ALAMAT, alamat);
        values.put(COL_NOHP, noHp);
        values.put(COL_EMAIL, email);
        values.put(COL_PASSWORD, password);
        values.put(COL_KATEGORI, kategori);

        if (kategori.equalsIgnoreCase("Perusahaan")) {
            values.put(COL_NAMA_PERUSAHAAN, namaPerusahaan);
            values.put(COL_PENANGGUNG_JAWAB, penanggungJawab);
        } else {
            values.put(COL_NAMA, nama);
            values.put(COL_NIK, nik);
            values.put(COL_TTL, ttl);
        }

        return db.insert(TABLE_USER, null, values) != -1;
    }

    // ================= LOGIN =================
    public boolean loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT " + COL_ID + " FROM " + TABLE_USER +
                        " WHERE " + COL_EMAIL + "=? AND " + COL_PASSWORD + "=?",
                new String[]{email, password}
        );

        boolean success = c.moveToFirst();
        c.close();
        return success;
    }

    // ================= CHECK EMAIL =================
    public boolean checkEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT " + COL_ID + " FROM " + TABLE_USER +
                        " WHERE " + COL_EMAIL + "=?",
                new String[]{email}
        );

        boolean exists = c.moveToFirst();
        c.close();
        return exists;
    }

    // ================= GET USER (AUTO PROFILE) =================
    public User getUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT * FROM " + TABLE_USER + " WHERE " + COL_EMAIL + "=?",
                new String[]{email}
        );

        if (c.moveToFirst()) {
            User u = new User();
            u.id = c.getInt(c.getColumnIndexOrThrow(COL_ID));
            u.nama = c.getString(c.getColumnIndexOrThrow(COL_NAMA));
            u.nik = c.getString(c.getColumnIndexOrThrow(COL_NIK));
            u.ttl = c.getString(c.getColumnIndexOrThrow(COL_TTL));
            u.namaPerusahaan = c.getString(c.getColumnIndexOrThrow(COL_NAMA_PERUSAHAAN));
            u.penanggungJawab = c.getString(c.getColumnIndexOrThrow(COL_PENANGGUNG_JAWAB));
            u.alamat = c.getString(c.getColumnIndexOrThrow(COL_ALAMAT));
            u.noHp = c.getString(c.getColumnIndexOrThrow(COL_NOHP));
            u.email = c.getString(c.getColumnIndexOrThrow(COL_EMAIL));
            u.kategori = c.getString(c.getColumnIndexOrThrow(COL_KATEGORI));
            c.close();
            return u;
        }
        c.close();
        return null;
    }

    // ================= UPDATE PROFILE =================
    public boolean updateProfile(User u) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();

        v.put(COL_NAMA, u.nama);
        v.put(COL_NIK, u.nik);
        v.put(COL_TTL, u.ttl);
        v.put(COL_NAMA_PERUSAHAAN, u.namaPerusahaan);
        v.put(COL_PENANGGUNG_JAWAB, u.penanggungJawab);
        v.put(COL_ALAMAT, u.alamat);
        v.put(COL_NOHP, u.noHp);

        return db.update(
                TABLE_USER,
                v,
                COL_EMAIL + "=?",
                new String[]{u.email}
        ) > 0;
    }

    // ================= UPDATE PASSWORD =================
    public boolean updatePassword(String email, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(COL_PASSWORD, newPassword);

        return db.update(
                TABLE_USER,
                v,
                COL_EMAIL + "=?",
                new String[]{email}
        ) > 0;
    }
}
