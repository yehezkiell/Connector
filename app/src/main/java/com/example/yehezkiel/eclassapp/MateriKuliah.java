package com.example.yehezkiel.eclassapp;

/**
 * Created by Yehezkiel on 9/5/2018.
 */

public class MateriKuliah {

    private String nama,pertemuan,url;

    public MateriKuliah(String nama, String pertemuan, String url) {
        this.nama = nama;
        this.pertemuan = pertemuan;
        this.url = url;
    }

    public MateriKuliah() {
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPertemuan() {
        return pertemuan;
    }

    public void setPertemuan(String pertemuan) {
        this.pertemuan = pertemuan;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
