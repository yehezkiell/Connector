package com.example.yehezkiel.eclassapp;

/**
 * Created by Yehezkiel on 7/23/2018.
 */

public class DaftarPengumuman {
    private String nama_p,deskripsi,judul,tanggal_peng;

    public DaftarPengumuman(String nama_p, String deskripsi, String judul, String tanggal_peng) {
        this.nama_p = nama_p;
        this.deskripsi = deskripsi;
        this.judul = judul;
        this.tanggal_peng = tanggal_peng;
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

    public String getTanggal_peng() {
        return tanggal_peng;
    }

    public void setTanggal_peng(String tanggal_peng) {
        this.tanggal_peng = tanggal_peng;
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
