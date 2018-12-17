package com.example.yehezkiel.eclassapp;

/**
 * Created by Yehezkiel on 7/12/2018.
 */

public class DaftarTugas{
    private String judul_tugas;
    private String nama_tugas;
    private String deskripsi_tugas;
    private String tanggal_kumpul;
    private String tanggal_tugas;
    private Long date_milisecond;
    private String flag;
    private String idCourse;
    private String idTugas;


    public DaftarTugas(String nama_tugas,String judul_tugas, String deskripsi_tugas, String tanggal_kumpul, String tanggal_tugas) {
        this.nama_tugas = nama_tugas;
        this.judul_tugas = judul_tugas;
        this.deskripsi_tugas = deskripsi_tugas;
        this.tanggal_kumpul = tanggal_kumpul;
        this.tanggal_tugas = tanggal_tugas;
    }

    public DaftarTugas() {

    }

    public String getIdTugas() {
        return idTugas;
    }

    public void setIdTugas(String idTugas) {
        this.idTugas = idTugas;
    }

    public String getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(String idCourse) {
        this.idCourse = idCourse;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Long getDate_milisecond() {
        return date_milisecond;
    }

    public void setDate_milisecond(Long date_milisecond) {
        this.date_milisecond = date_milisecond;
    }

    public String getNama_tugas() {
        return nama_tugas;
    }

    public void setNama_tugas(String nama_tugas) {
        this.nama_tugas = nama_tugas;
    }

    public String getJudul_tugas() {
        return judul_tugas;
    }

    public void setJudul_tugas(String judul_tugas) {
        this.judul_tugas = judul_tugas;
    }

    public String getDeskripsi_tugas() {
        return deskripsi_tugas;
    }

    public void setDeskripsi_tugas(String deskripsi_tugas) {
        this.deskripsi_tugas = deskripsi_tugas;
    }

    public String getTanggal_kumpul() {
        return tanggal_kumpul;
    }

    public void setTanggal_kumpul(String tanggal_kumpul) {
        this.tanggal_kumpul = tanggal_kumpul;
    }

    public String getTanggal_tugas() {

        return tanggal_tugas;
    }

    public void setTanggal_tugas(String tanggal_tugas) {

        this.tanggal_tugas = tanggal_tugas;
    }
}
