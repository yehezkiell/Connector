package com.example.yehezkiel.eclassapp;

/**
 * Created by Yehezkiel on 9/13/2018.
 */

public class ListNilaiDetail {

    private int nomer,nim;
    private double nilai;
    private String name;


    public ListNilaiDetail(int nomer,double nilai, String name, int nim) {
        this.nilai = nilai;
        this.name = name;
        this.nim = nim;
    }

    public ListNilaiDetail() {
    }


    public int getNomer() {
        return nomer;
    }

    public void setNomer(int nomer) {
        this.nomer = nomer;
    }

    public double getNilai() {
        return nilai;
    }

    public void setNilai(double nilai) {
        this.nilai = nilai;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNim() {
        return nim;
    }

    public void setNim(int nim) {
        this.nim = nim;
    }
}
