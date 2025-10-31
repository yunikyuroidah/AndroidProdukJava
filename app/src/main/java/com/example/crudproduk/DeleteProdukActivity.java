package com.example.crudproduk;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.crudproduk.utils.ImageUtils;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;

public class DeleteProdukActivity extends AppCompatActivity {

    private ImageView ivFotoProduk;
    private MaterialButton btnHapus, btnBatal;
    private String idProduk, fotoBase64, namaProduk;
    private FirebaseFirestore db;
    private TextView tvNamaDelete, tvHargaDelete, tvNoDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_produk);

        ivFotoProduk = findViewById(R.id.imgProdukDelete);
        btnHapus = findViewById(R.id.btnHapusProduk);
        btnBatal = findViewById(R.id.btnBatalHapus);
        tvNamaDelete = findViewById(R.id.tvNamaDelete);
        tvHargaDelete = findViewById(R.id.tvHargaDelete);
        tvNoDelete = findViewById(R.id.tvNoDelete);

        db = FirebaseFirestore.getInstance();

        idProduk = getIntent().getStringExtra("id_produk");
        fotoBase64 = getIntent().getStringExtra("fotoBase64");
        namaProduk = getIntent().getStringExtra("namaProduk");

        tvNamaDelete.setText(namaProduk != null ? namaProduk : "-");
        // harga/no not strictly required here but could be shown if passed

        // tampilkan foto: decode base64 jika ada
        Bitmap bmp = ImageUtils.base64ToBitmap(fotoBase64);
        if (bmp != null) ivFotoProduk.setImageBitmap(bmp);
        else ivFotoProduk.setImageResource(R.drawable.ic_menu_gallery);

        btnHapus.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Hapus Produk")
                    .setMessage("Yakin ingin menghapus produk \"" + (namaProduk != null ? namaProduk : "") + "\"?")
                    .setPositiveButton("Ya", (dialog, which) -> hapusProduk())
                    .setNegativeButton("Batal", (dialog, which) -> dialog.dismiss())
                    .show();
        });

        btnBatal.setOnClickListener(v -> finish());
    }

    private void hapusProduk() {
        db.collection("produk").document(idProduk)
                .delete()
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Produk dihapus", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Gagal: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
