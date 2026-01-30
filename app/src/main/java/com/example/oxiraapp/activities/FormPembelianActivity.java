package com.example.oxiraapp.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.oxiraapp.R;
import com.example.oxiraapp.models.User;
import com.example.oxiraapp.utils.DatabaseHelper;
import com.example.oxiraapp.utils.SessionManager;
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.models.BillingAddress;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.ShippingAddress;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class FormPembelianActivity extends AppCompatActivity implements TransactionFinishedCallback {

    private EditText etNama, etNik, etEmail, etNoHp, etJumlahTabung;
    private Spinner spUkuranTabung;
    private TextView tvHargaSatuan, tvTotalHarga;
    private Button btnBayar;
    private String userAlamat = "Alamat Lengkap User";

    private SessionManager session;
    private DatabaseHelper db;

    private long hargaSatuan = 0;
    private long totalHarga = 0;
    private String ukuranTerpilih = "";
    private final String[] ukuranList = {"Pilih Ukuran", "1 m³", "1.5 m³", "2 m³", "6 m³"};
    private final int[] hargaList = {0, 500000, 750000, 1000000, 2500000};

    private final String URL_NGROK = "https://unadministrable-unseceded-juanita.ngrok-free.dev"; 
    private final String URL_LARAVEL = URL_NGROK + "/api/payment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_pembelian);

        session = new SessionManager(this);
        db = new DatabaseHelper(this);

        initView();
        initMidtransSDK();
        setupSpinner();
        loadUserData();
        setupAutoCalculate();

        btnBayar.setOnClickListener(v -> {
            if (totalHarga == 0 || etJumlahTabung.getText().toString().isEmpty()) {
                Toast.makeText(this, "Lengkapi data pembelian", Toast.LENGTH_SHORT).show();
                return;
            }
            getSnapTokenFromLaravel();
        });
    }

    private void initMidtransSDK() {
        SdkUIFlowBuilder.init()
                // ✅ KEY SUDAH DISAMAKAN DENGAN DASHBOARD ANDA
                .setClientKey("SB-Mid-client-eQwhMGTbGcyWCNX-")
                .setContext(getApplicationContext())
                .setTransactionFinishedCallback(this)
                .setMerchantBaseUrl(URL_NGROK + "/api/")
                .enableLog(true)
                .buildSDK();
    }

    private void getSnapTokenFromLaravel() {
        final String finalOrderId = "OXIRA-" + System.currentTimeMillis();
        final int qty = Integer.parseInt(etJumlahTabung.getText().toString());

        JSONObject postData = new JSONObject();
        try {
            postData.put("order_id", finalOrderId);
            postData.put("gross_amount", totalHarga);
            postData.put("name", etNama.getText().toString());
            postData.put("email", etEmail.getText().toString());
            postData.put("phone", etNoHp.getText().toString());
        } catch (JSONException e) { e.printStackTrace(); }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL_LARAVEL, postData,
                response -> {
                    try {
                        if (response.has("snap_token")) {
                            String snapToken = response.getString("snap_token");

                            CustomerDetails customer = new CustomerDetails();
                            customer.setFirstName(etNama.getText().toString());
                            customer.setEmail(etEmail.getText().toString());
                            customer.setPhone(etNoHp.getText().toString());

                            BillingAddress bAddress = new BillingAddress();
                            bAddress.setAddress(userAlamat);
                            bAddress.setCity("Jakarta");
                            bAddress.setCountryCode("IDN");
                            bAddress.setPostalCode("12345");
                            customer.setBillingAddress(bAddress);

                            ItemDetails item = new ItemDetails("ITEM-01", (double) hargaSatuan, qty, "Tabung Oksigen " + ukuranTerpilih);
                            ArrayList<ItemDetails> itemDetailsList = new ArrayList<>();
                            itemDetailsList.add(item);

                            TransactionRequest transactionRequest = new TransactionRequest(finalOrderId, (double) totalHarga);
                            transactionRequest.setCustomerDetails(customer);
                            transactionRequest.setItemDetails(itemDetailsList);

                            MidtransSDK.getInstance().setTransactionRequest(transactionRequest);
                            MidtransSDK.getInstance().startPaymentUiFlow(FormPembelianActivity.this, snapToken);
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "Gagal memproses pembayaran", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    String msg = "Koneksi Gagal ke Laravel";
                    if (error.networkResponse != null) msg = "Error: " + error.networkResponse.statusCode;
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                }
        );

        request.setRetryPolicy(new DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(request);
    }

    @Override
    public void onTransactionFinished(TransactionResult result) {

        if (result.getResponse() != null) {

            String status = result.getStatus();
            String message = result.getResponse().getStatusMessage();
            String code = result.getResponse().getStatusCode();

            Log.d("MIDTRANS_DEBUG", "Status: " + status);
            Log.d("MIDTRANS_DEBUG", "Code: " + code);
            Log.d("MIDTRANS_DEBUG", "Message: " + message);

            if (status.equals(TransactionResult.STATUS_SUCCESS)) {
                Toast.makeText(this, "Pembayaran Berhasil!", Toast.LENGTH_LONG).show();
                finish();

            } else if (status.equals(TransactionResult.STATUS_PENDING)) {
                Toast.makeText(this, "Menunggu pembayaran", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(this, "Gagal: " + message, Toast.LENGTH_LONG).show();
            }

        } else if (result.isTransactionCanceled()) {
            Toast.makeText(this, "Transaksi dibatalkan", Toast.LENGTH_LONG).show();

        } else if (result.getStatus().equals(TransactionResult.STATUS_INVALID)) {
            Toast.makeText(this, "Transaksi tidak valid", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, "Transaksi gagal", Toast.LENGTH_LONG).show();
        }
    }


    private void initView() {
        etNama = findViewById(R.id.etNama);
        etNik = findViewById(R.id.etNik);
        etEmail = findViewById(R.id.etEmail);
        etNoHp = findViewById(R.id.etNoHp);
        etJumlahTabung = findViewById(R.id.etJumlahTabung);
        spUkuranTabung = findViewById(R.id.spUkuranTabung);
        tvHargaSatuan = findViewById(R.id.tvHargaSatuan);
        tvTotalHarga = findViewById(R.id.tvTotalHarga);
        btnBayar = findViewById(R.id.btnBayar);
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ukuranList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(position == 0 ? Color.GRAY : Color.BLACK);
                return v;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spUkuranTabung.setAdapter(adapter);
        spUkuranTabung.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hargaSatuan = hargaList[position];
                ukuranTerpilih = ukuranList[position];
                tvHargaSatuan.setText("Rp " + String.format(Locale.getDefault(), "%,d", hargaSatuan).replace(',', '.'));
                hitungTotal();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadUserData() {
        User user = db.getUser(session.getEmail());
        if (user != null) {
            etNama.setText(user.nama != null && !user.nama.isEmpty() ? user.nama : user.namaPerusahaan);
            etNik.setText(user.nik != null && !user.nik.isEmpty() ? user.nik : "ID Perusahaan");
            etEmail.setText(user.email);
            etNoHp.setText(user.noHp);
            if (user.alamat != null && !user.alamat.isEmpty()) userAlamat = user.alamat;
        }
    }

    private void setupAutoCalculate() {
        etJumlahTabung.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { hitungTotal(); }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void hitungTotal() {
        String qtyStr = etJumlahTabung.getText().toString();
        int qty = qtyStr.isEmpty() ? 0 : Integer.parseInt(qtyStr);
        totalHarga = qty * hargaSatuan;
        tvTotalHarga.setText("Rp " + String.format(Locale.getDefault(), "%,d", totalHarga).replace(',', '.'));
    }
}
