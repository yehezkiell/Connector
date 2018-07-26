package com.example.yehezkiel.eclassapp;

/**
 * Created by Yehezkiel on 7/23/2018.
 */

public class DaftarPengumuman {
    private String nama_p,deskripsi,judul,tanggal;

    public DaftarPengumuman(String nama_p, String deskripsi, String judul, String tanggal) {
        this.nama_p = nama_p;
        this.deskripsi = deskripsi;
        this.judul = judul;
        this.tanggal = tanggal;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public DaftarPengumuman() {

    }

    public String getNama_p() {
        return nama_p;
    }

    public void setNama_p(String nama_p) {
        this.nama_p = nama_p;
    }


}
