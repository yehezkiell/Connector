package com.example.yehezkiel.eclassapp;

/**
 * Created by Yehezkiel on 9/5/2018.
 */

public class MateriKuliah {

    private String nama,pertemuan,url;
    private boolean silabus;

    public MateriKuliah(String nama, String pertemuan, String url, boolean silabus) {
        this.nama = nama;
        this.pertemuan = pertemuan;
        this.url = url;
        this.silabus = silabus;
    }

    public MateriKuliah() {
    }

    public boolean isSilabus() {
        return silabus;
    }

    public void setSilabus(boolean silabus) {
        this.silabus = silabus;
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
