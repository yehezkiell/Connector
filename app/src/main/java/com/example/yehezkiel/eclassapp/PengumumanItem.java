package com.example.yehezkiel.eclassapp;

/**
 * Created by Yehezkiel on 7/20/2018.
 */

public class PengumumanItem extends ListItem {

    private String tanggal_peng;

    public String getTanggal_peng() {
        return tanggal_peng;
    }

    public void setTanggal_peng(String tanggal_peng) {
        this.tanggal_peng = tanggal_peng;
    }



    @Override
    public int getType() {
        return TYPE_NAME;
    }
}
