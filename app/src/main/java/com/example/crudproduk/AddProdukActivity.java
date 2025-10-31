package com.example.crudproduk;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.crudproduk.utils.ImageUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddProdukActivity extends AppCompatActivity {

    private TextInputEditText etNamaProduk, etHargaProduk, etNoProduk;
    private ImageView ivFotoProduk;
    private MaterialButton btnSimpan, btnPilih;
    private Uri imageUri;
    private String fotoBase64 = "";
    private FirebaseFirestore db;

    private final ActivityResultLauncher<String> pilihGambarLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    imageUri = uri;
                    ivFotoProduk.setImageURI(uri);
                    try {
                        fotoBase64 = ImageUtils.uriToBase64(this, uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Gagal memproses gambar", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_produk);

        etNamaProduk = findViewById(R.id.etNamaProduk);
        etHargaProduk = findViewById(R.id.etHargaProduk);
        etNoProduk = findViewById(R.id.etNoProduk);
        ivFotoProduk = findViewById(R.id.imgPreview);
        btnSimpan = findViewById(R.id.btnSimpan);
        btnPilih = findViewById(R.id.btnPilihGambar);

        db = FirebaseFirestore.getInstance();

        btnPilih.setOnClickListener(v -> pilihGambarLauncher.launch("image/*"));
        btnSimpan.setOnClickListener(v -> simpanProduk());
    }

    private void simpanProduk() {
        String nama = etNamaProduk.getText() != null ? etNamaProduk.getText().toString().trim() : "";
        String hargaStr = etHargaProduk.getText() != null ? etHargaProduk.getText().toString().trim() : "";
        String noStr = etNoProduk.getText() != null ? etNoProduk.getText().toString().trim() : "";

        if (TextUtils.isEmpty(nama) || TextUtils.isEmpty(hargaStr) || TextUtils.isEmpty(noStr)) {
            Toast.makeText(this, "Lengkapi semua data", Toast.LENGTH_SHORT).show();
            return;
        }

        // fotoBase64 boleh kosong string "" jika user tidak memilih gambar
        ProgressDialog progress = ProgressDialog.show(this, "Menyimpan", "Mohon tunggu...", true);

        Map<String, Object> data = new HashMap<>();
        data.put("namaProduk", nama);
        data.put("harga", hargaStr);         // simpan sebagai String sesuai permintaan
        data.put("fotoBase64", fotoBase64 != null ? fotoBase64 : "");
        data.put("noProduk", noStr);

        // PENTING: gunakan collection "Produk" sesuai DB-mu (case-sensitive)
        db.collection("produk").add(data)
                .addOnSuccessListener(docRef -> {
                    progress.dismiss();
                    Toast.makeText(this, "Produk berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    progress.dismiss();
                    Toast.makeText(this, "Gagal menyimpan: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
