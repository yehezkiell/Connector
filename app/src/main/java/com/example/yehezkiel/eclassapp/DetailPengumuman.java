package com.example.yehezkiel.eclassapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class DetailPengumuman extends AppCompatActivity {

    //pengumuman
    private String tanggalPengumumanGet;
    private String judulPengumumanGet;
    private String namaPengumumanGet;
    private String deskripsiPengumumanGet;
    private String flag;

    //tugas
    private String getTanggalTugas;
    private String getJudulTugas;
    private String getNamaTugas;
    private String getDeskripsiTugas;
    private String getTanggalKumpulTugas;


    private TextView staticText, tanggalPengumpulanTugas;



    private TextView judulLayout,namaMatakuliah,tanggalLayout,deskripsiLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pengumuman);


        //setting toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationIcon(R.mipmap.baseline_arrow_back_white_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailPengumuman.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //end of setting toolbar


        judulLayout = (TextView) findViewById(R.id.judulPengumumanDetail);
        namaMatakuliah = (TextView) findViewById(R.id.namaMatakuliahPengumuman);
        tanggalLayout = (TextView) findViewById(R.id.tanggalMatakuliahPengumuman);
        deskripsiLayout = (TextView) findViewById(R.id.deskripsiPengumumanDetail);



        staticText = (TextView) findViewById(R.id.textStatis);
        tanggalPengumpulanTugas = (TextView) findViewById(R.id.tanggalMatakuliahKumpul);

        //extra intent from three fragment
        Intent intent = getIntent();
        flag = intent.getStringExtra("flag");

        if(flag.equals("tugas")){

            getJudulTugas = intent.getStringExtra("judultugas");
            getTanggalTugas = intent.getStringExtra("tanggaltugas");
            getDeskripsiTugas = intent.getStringExtra("deskripsitugas");
            getNamaTugas = intent.getStringExtra("namamatkultugas");
            getTanggalKumpulTugas = intent.getStringExtra("tanggalkumpul");


            namaMatakuliah.setText(getNamaTugas);
            judulLayout.setText(getJudulTugas);
            tanggalLayout.setText(getTanggalTugas);
            deskripsiLayout.setText(getDeskripsiTugas);
            tanggalPengumpulanTugas.setText(getTanggalKumpulTugas);

        }else{
            // if pengumuman
            tanggalPengumumanGet = intent.getStringExtra("tanggalpengumuman");
            judulPengumumanGet = intent.getStringExtra("judulpengumuman");
            namaPengumumanGet = intent.getStringExtra("getnama");
            deskripsiPengumumanGet = intent.getStringExtra("deskripsipengumuman");

            namaMatakuliah.setText(namaPengumumanGet);
            judulLayout.setText(judulPengumumanGet);
            tanggalLayout.setText(tanggalPengumumanGet);
            deskripsiLayout.setText(deskripsiPengumumanGet);

            staticText.setVisibility(View.GONE);
            tanggalPengumpulanTugas.setVisibility(View.GONE);

        }


    }
}
