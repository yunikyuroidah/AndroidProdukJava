package com.example.crudproduk.model;

public class Produk {
    private String id_produk;
    private String namaProduk;
    private String harga;
    private String noProduk;
    private String fotoBase64;

    // Konstruktor kosong WAJIB untuk Firestore
    public Produk() { }

    // Konstruktor opsional yang benar (assign parameter ke field)
    public Produk(String id_produk, String namaProduk, String harga, String fotoBase64, String noProduk) {
        this.id_produk = id_produk;
        this.namaProduk = namaProduk;
        this.harga = harga;
        this.fotoBase64 = fotoBase64;
        this.noProduk = noProduk;
    }

    public String getId_produk() {
        return id_produk;
    }

    public void setId_produk(String id_produk) {
        this.id_produk = id_produk;
    }

    public String getnamaProduk() {
        return namaProduk;
    }

    public void setnamaProduk(String namaProduk) {
        this.namaProduk = namaProduk;
    }

    public String getharga() {
        return harga;
    }

    public void setharga(String harga) {
        this.harga = harga;
    }

    public String getfotoBase64() {
        return fotoBase64;
    }

    public void setfotoBase64(String fotoBase64) {
        this.fotoBase64 = fotoBase64;
    }

    public String getnoProduk() {
        return noProduk;
    }

    public void setnoProduk(String noProduk) {
        this.noProduk = noProduk;
    }
}
