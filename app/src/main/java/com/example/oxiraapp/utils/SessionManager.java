package com.example.oxiraapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "oxira_session";

    private static final String KEY_LOGIN = "is_login";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_KATEGORI = "kategori";

    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    // ================= CREATE LOGIN =================
    public void createLoginSession(String email, String kategori) {
        editor.putBoolean(KEY_LOGIN, true);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_KATEGORI, kategori);
        editor.apply();
    }

    // ================= CHECK LOGIN =================
    public boolean isLogin() {
        return pref.getBoolean(KEY_LOGIN, false);
    }

    // ================= GET DATA =================
    public String getEmail() {
        return pref.getString(KEY_EMAIL, null);
    }

    public String getKategori() {
        return pref.getString(KEY_KATEGORI, null);
    }

    // ================= LOGOUT =================
    public void logout() {
        editor.clear();
        editor.apply();
    }
}
