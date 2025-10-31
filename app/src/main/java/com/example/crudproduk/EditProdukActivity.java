package com.example.crudproduk;

import android.app.ProgressDialog;
import android.content.Intent;
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

public class EditProdukActivity extends AppCompatActivity {

    private TextInputEditText etNamaProduk, etHargaProduk, etNoEdit;
    private ImageView ivFotoProduk;
    private MaterialButton btnUpdate, btnUbahGambar;
    private Uri imageUri;
    private String idProduk, fotoBase64Existing = "";
    private FirebaseFirestore db;

    private final ActivityResultLauncher<String> pilihGambarLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    imageUri = uri;
                    ivFotoProduk.setImageURI(uri);
                    try {
                        fotoBase64Existing = ImageUtils.uriToBase64(this, uri); // update base64
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Gagal memproses gambar", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_produk);

        etNamaProduk = findViewById(R.id.etNamaEdit);
        etHargaProduk = findViewById(R.id.etHargaEdit);
        etNoEdit = findViewById(R.id.etNoEdit);
        ivFotoProduk = findViewById(R.id.imgPreview);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnUbahGambar = findViewById(R.id.btnUbahGambar);

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        idProduk = intent.getStringExtra("id_produk");
        String nama = intent.getStringExtra("namaProduk");
        String harga = intent.getStringExtra("harga");
        String noProduk = intent.getStringExtra("noProduk");
        String fotoBase64 = intent.getStringExtra("fotoBase64");

        etNamaProduk.setText(nama != null ? nama : "");
        etHargaProduk.setText(harga != null ? harga : "");
        etNoEdit.setText(noProduk != null ? noProduk : "");
        fotoBase64Existing = fotoBase64 != null ? fotoBase64 : "";

        // Tampilkan gambar (decode base64 jika ada)
        if (fotoBase64Existing != null && !fotoBase64Existing.isEmpty()) {
            // gunakan ImageUtils
            android.graphics.Bitmap bmp = ImageUtils.base64ToBitmap(fotoBase64Existing);
            if (bmp != null) ivFotoProduk.setImageBitmap(bmp);
            else ivFotoProduk.setImageResource(R.drawable.ic_menu_gallery);
        } else {
            ivFotoProduk.setImageResource(R.drawable.ic_menu_gallery);
        }

        btnUbahGambar.setOnClickListener(v -> pilihGambarLauncher.launch("image/*"));
        btnUpdate.setOnClickListener(v -> updateProduk());
    }

    private void updateProduk() {
        String nama = etNamaProduk.getText() != null ? etNamaProduk.getText().toString().trim() : "";
        String hargaStr = etHargaProduk.getText() != null ? etHargaProduk.getText().toString().trim() : "";
        String noStr = etNoEdit.getText() != null ? etNoEdit.getText().toString().trim() : "";

        if (TextUtils.isEmpty(nama) || TextUtils.isEmpty(hargaStr) || TextUtils.isEmpty(noStr)) {
            Toast.makeText(this, "Lengkapi semua data", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog progress = ProgressDialog.show(this, "Mengupdate", "Mohon tunggu...", true);

        // fotoBase64Existing sudah updated saat user memilih gambar; jika user tidak pilih gambar tetap pakai fotoBase64Existing
        Map<String, Object> data = new HashMap<>();
        data.put("namaProduk", nama);
        data.put("harga", hargaStr);       // simpan sebagai String
        data.put("fotoBase64", fotoBase64Existing != null ? fotoBase64Existing : "");
        data.put("noProduk", noStr);

        db.collection("produk").document(idProduk)
                .update(data)
                .addOnSuccessListener(unused -> {
                    progress.dismiss();
                    Toast.makeText(this, "Produk diperbarui", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    progress.dismiss();
                    Toast.makeText(this, "Gagal: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
