package com.example.yehezkiel.eclassapp;

/**
 * Created by Yehezkiel on 9/10/2018.
 */

public class NilaiMatakuliah {

    private String nama;
    private double skala,bobot;
    private double nilai;
    private String flag;
    private String keys,namaMatkul;

    public NilaiMatakuliah(String nama, double nilai, double skala, double bobot) {
        this.nama = nama;
        this.nilai = nilai;
        this.skala = skala;
        this.bobot = bobot;
    }

    public String getNamaMatkul() {
        return namaMatkul;
    }

    public void setNamaMatkul(String namaMatkul) {
        this.namaMatkul = namaMatkul;
    }


    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public double getBobot() {
        return bobot;
    }


    public void setBobot(long bobot) {
        this.bobot = bobot;
    }

    public NilaiMatakuliah() {
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }


    public double getNilai() {
        return nilai;
    }

    public void setNilai(double nilai) {
        this.nilai = nilai;
    }

    public double getSkala() {
        return skala;
    }

    public void setSkala(double skala) {
        this.skala = skala;
    }
}
