package com.example.yehezkiel.eclassapp;

/**
 * Created by Yehezkiel on 9/14/2018.
 */

public class PesertaMatakuliah {

    private String name;
    private int nim,nomer;

    public PesertaMatakuliah(String name, int nim, int nomer) {
        this.name = name;
        this.nim = nim;
        this.nomer = nomer;
    }

    public PesertaMatakuliah() {
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

    public int getNomer() {
        return nomer;
    }

    public void setNomer(int nomer) {
        this.nomer = nomer;
    }
}
