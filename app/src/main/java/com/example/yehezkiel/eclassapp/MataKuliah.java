package com.example.yehezkiel.eclassapp;

/**
 * Created by Yehezkiel on 4/12/2018.
 */

public class MataKuliah {


    private String name;
    private String dosen_1;
    private String dosen_2;
    private String day;
    private String jam;
    private String key;
    private String kelas;
    private int bobot;
    private String kode;


    public MataKuliah(String name, String dosen_1, String dosen_2, String day, String jam,String kelas,String kode) {
        this.name = name;
        this.dosen_1 = dosen_1;
        this.dosen_2 = dosen_2;
        this.day = day;
        this.jam = jam;
        this.kelas = kelas;
        this.kode = kode;

    }


    public MataKuliah(){

    }


    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public int getBobot() {
        return bobot;
    }

    public void setBobot(int bobot) {
        this.bobot = bobot;
    }

    public String getName() {
        return name;
    }

    public String getDosen_1() {
        return dosen_1;
    }

    public String getDosen_2() {
        return dosen_2;
    }

    public String getDay() {
        return day;
    }

    public String getJam() {
        return jam;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDosen1(String dosen_1) {
        this.dosen_1 = dosen_1;
    }

    public void setDosen2(String dosen_2) {
        this.dosen_2 = dosen_2;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }
}
