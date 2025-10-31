package com.example.crudproduk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crudproduk.adapter.ProdukAdapter;
import com.example.crudproduk.model.Produk;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton fabAdd;
    private ArrayList<Produk> listProduk;
    private ProdukAdapter adapter;
    private FirebaseFirestore db;
    private boolean isFirstLoad = true; // agar tidak load dua kali saat startup

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerProduk);
        fabAdd = findViewById(R.id.btnAddProduk);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listProduk = new ArrayList<>();
        adapter = new ProdukAdapter(listProduk, this);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        // pertama kali load data
        ambilData();

        fabAdd.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, AddProdukActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();

        // agar tidak reload langsung dua kali saat pertama buka app
        if (isFirstLoad) {
            isFirstLoad = false;
            return;
        }

        // reload data setiap kali kembali dari Add/Edit/Delete activity
        ambilData();
    }

    private void ambilData() {
        db.collection("produk").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    listProduk.clear();

                    if (queryDocumentSnapshots.isEmpty()) {
                        Toast.makeText(this, "Belum ada produk", Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                        return;
                    }

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Produk produk = new Produk();
                        produk.setId_produk(doc.getId());
                        produk.setnamaProduk(doc.getString("namaProduk"));
                        produk.setharga(doc.getString("harga"));
                        produk.setnoProduk(doc.getString("noProduk"));
                        produk.setfotoBase64(doc.getString("fotoBase64"));
                        listProduk.add(produk);
                    }

                    adapter.notifyDataSetChanged();
                    Log.d("MainActivity", "Data produk berhasil dimuat: " + listProduk.size());
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Gagal memuat data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("MainActivity", "Error ambil data: ", e);
                });
    }
}
