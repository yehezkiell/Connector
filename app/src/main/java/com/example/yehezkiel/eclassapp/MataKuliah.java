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

    public MataKuliah(String name, String dosen_1, String dosen_2, String day, String jam) {
        this.name = name;
        this.dosen_1 = dosen_1;
        this.dosen_2 = dosen_2;
        this.day = day;
        this.jam = jam;
    }


    public MataKuliah(){

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
