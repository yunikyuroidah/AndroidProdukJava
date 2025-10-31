package com.example.crudproduk.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.crudproduk.DeleteProdukActivity;
import com.example.crudproduk.EditProdukActivity;
import com.example.crudproduk.R;
import com.example.crudproduk.model.Produk;
import com.example.crudproduk.utils.ImageUtils;

import java.util.ArrayList;

public class ProdukAdapter extends RecyclerView.Adapter<ProdukAdapter.ViewHolder> {

    private ArrayList<Produk> listProduk;
    private Context context;

    public ProdukAdapter(ArrayList<Produk> listProduk, Context context) {
        this.listProduk = listProduk;
        this.context = context;
    }

    @NonNull
    @Override
    public ProdukAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_produk, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdukAdapter.ViewHolder holder, int position) {
        Produk produk = listProduk.get(position);

        holder.tvNama.setText(produk.getnamaProduk() != null ? produk.getnamaProduk() : "-");
        holder.tvHarga.setText(produk.getharga() != null ? ("Rp " + produk.getharga()) : "-");

        // Decode base64 ke Bitmap (jika empty -> placeholder)
        Bitmap bmp = ImageUtils.base64ToBitmap(produk.getfotoBase64());
        if (bmp != null) {
            holder.imgProduk.setImageBitmap(bmp);
        } else {
            holder.imgProduk.setImageResource(R.drawable.ic_menu_gallery);
        }

        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditProdukActivity.class);
            intent.putExtra("id_produk", produk.getId_produk());
            intent.putExtra("namaProduk", produk.getnamaProduk());
            intent.putExtra("harga", produk.getharga());
            intent.putExtra("fotoBase64", produk.getfotoBase64());
            intent.putExtra("noProduk", produk.getnoProduk());
            context.startActivity(intent);
        });

        holder.btnDelete.setOnClickListener(v -> {
            Intent intent = new Intent(context, DeleteProdukActivity.class);
            intent.putExtra("id_produk", produk.getId_produk());
            intent.putExtra("fotoBase64", produk.getfotoBase64());
            intent.putExtra("namaProduk", produk.getnamaProduk());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listProduk != null ? listProduk.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduk, btnEdit, btnDelete;
        TextView tvNama, tvHarga;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduk = itemView.findViewById(R.id.imgProduk);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvHarga = itemView.findViewById(R.id.tvHarga);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
