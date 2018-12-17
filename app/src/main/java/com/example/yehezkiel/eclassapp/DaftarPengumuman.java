package com.example.yehezkiel.eclassapp;

/**
 * Created by Yehezkiel on 7/23/2018.
 */

public class DaftarPengumuman {
    private String nama_p,deskripsi,judul,tanggal_peng;
    private Long date_milisecond;
    private String idCourse;
    private String idPengumuman;
    private String flag;
    private String header;
    private String tanggal_peng_2;
    private String hour;




    public DaftarPengumuman(String nama_p, String deskripsi, String judul, String header, String tanggal_peng, String hour) {
        this.nama_p = nama_p;
        this.deskripsi = deskripsi;
        this.judul = judul;
        this.header = header;
        this.tanggal_peng = tanggal_peng;
        this.hour = hour;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getTanggal_peng_2() {
        return tanggal_peng_2;
    }

    public void setTanggal_peng_2(String tanggal_peng_2) {
        this.tanggal_peng_2 = tanggal_peng_2;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getFlag() {
        return flag;
    }


    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getIdCourse() {
        return idCourse;
    }


    public void setIdCourse(String idCourse) {
        this.idCourse = idCourse;
    }

    public String getIdPengumuman() {
        return idPengumuman;
    }

    public void setIdPengumuman(String idPengumuman) {
        this.idPengumuman = idPengumuman;
    }

    public Long getDate_milisecond() {
        return date_milisecond;
    }

    public void setDate_milisecond(Long date_milisecond) {
        this.date_milisecond = date_milisecond;
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
