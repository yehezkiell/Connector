package com.example.yehezkiel.eclassapp;

/**
 * Created by Yehezkiel on 4/12/2018.
 */

public class ListAsdos {


    private String name;
    private String day;
    private String jam;
    private String kelas;
    private int bobot;
    private String kode;
    private String idCourse;

    public ListAsdos(String name, String day, String jam,String kelas,String kode,int bobot) {
        this.name = name;
        this.day = day;
        this.jam = jam;
        this.kelas = kelas;
        this.kode = kode;
        this.bobot = bobot;
    }


    public ListAsdos(){

    }

    public String getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(String idCourse) {
        this.idCourse = idCourse;
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


    public String getDay() {
        return day;
    }

    public String getJam() {
        return jam;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setDay(String day) {
        this.day = day;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }
}
