package com.example.yehezkiel.eclassapp;

/**
 * Created by Yehezkiel on 8/23/2018.
 */

public class ListDosen {

    private String nama;
    private String foto;

    public ListDosen(){

    }

    public ListDosen(String nama, String foto) {
        this.nama = nama;
        this.foto = foto;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
