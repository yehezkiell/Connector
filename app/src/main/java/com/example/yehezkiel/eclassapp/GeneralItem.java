package com.example.yehezkiel.eclassapp;

/**
 * Created by Yehezkiel on 7/20/2018.
 */

public class GeneralItem extends ListItem {


    private DaftarPengumuman daftarPengumuman;

    public DaftarPengumuman getDaftarPengumuman() {
        return daftarPengumuman;
    }


    public void setDaftarPengumuman(DaftarPengumuman daftarPengumuman) {
        this.daftarPengumuman = daftarPengumuman;
    }

    public int getType(){
        return TYPE_TASK;
    }




}
