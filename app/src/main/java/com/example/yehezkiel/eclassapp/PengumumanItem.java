package com.example.yehezkiel.eclassapp;

/**
 * Created by Yehezkiel on 7/20/2018.
 */

public class PengumumanItem extends ListItem {

    private String nama_matkul;


    public String getNama_matkul() {

        return nama_matkul;
    }

    public void setNama_matkul(String nama_matkul) {
        this.nama_matkul = nama_matkul;
    }

    @Override
    public int getType() {
        return TYPE_NAME;
    }
}
