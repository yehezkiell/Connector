package com.example.yehezkiel.eclassapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.andrognito.flashbar.Flashbar;
import com.andrognito.flashbar.anim.FlashAnim;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReadActivity extends AppCompatActivity{

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

    private Button infoTugasPengDetail;

    private Flashbar flashbar = null;


    private TextView staticText, tanggalPengumpulanTugas;

    private View garis_pengumuman2;

    private TextView judulLayout,namaMatakuliah,tanggalLayout,deskripsiLayout,jam_read;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);




        //setting toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationIcon(R.mipmap.baseline_arrow_back_white_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReadActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //end of setting toolbar

        jam_read = (TextView) findViewById(R.id.jam_read);
        judulLayout = (TextView) findViewById(R.id.judulPengumumanDetail);
        namaMatakuliah = (TextView) findViewById(R.id.namaMatakuliahPengumuman);
        tanggalLayout = (TextView) findViewById(R.id.tanggalMatakuliahPengumuman);
        deskripsiLayout = (TextView) findViewById(R.id.deskripsiPengumumanDetail);
        infoTugasPengDetail = (Button) findViewById(R.id.infoTugasPengDetail);
        garis_pengumuman2 = (View) findViewById(R.id.garis_pengumuman2);

        staticText = (TextView) findViewById(R.id.textStatis);
        tanggalPengumpulanTugas = (TextView) findViewById(R.id.tanggalMatakuliahKumpul);



        //extra intent from three fragment
        Intent intent = getIntent();
        flag = intent.getStringExtra("flag");

        if(flag.equals("tugas")){

            getTanggalKumpulTugas = intent.getStringExtra("tanggalkumpul");
            getJudulTugas = intent.getStringExtra("judultugas");
            getTanggalTugas = intent.getStringExtra("tanggaltugas");
            String strCurrentDate = getTanggalTugas;
            String strTanggalKumpul = getTanggalKumpulTugas;

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");

            SimpleDateFormat outputDate = new SimpleDateFormat("EEE , dd MMM yyyy");
            SimpleDateFormat outputHour = new SimpleDateFormat(" hh:mm aa");

            String date = null;
            String hour = null;
            String date2 = null;


            try {
                Date newDate  = format.parse(strCurrentDate);
                date= outputDate.format(newDate);
                hour= outputHour.format(newDate);
                tanggalLayout.setText(date);
                jam_read.setText(hour);
                tanggalLayout.setText(date);

                jam_read.setText(hour);



            } catch (ParseException e) {
                e.printStackTrace();
            }

            try {
                Date newDate2 = format2.parse(strTanggalKumpul);
                date2= outputDate.format(newDate2);
                tanggalPengumpulanTugas.setText(date2);



            } catch (ParseException e) {
                e.printStackTrace();
            }


            getDeskripsiTugas = intent.getStringExtra("deskripsitugas");
            getNamaTugas = intent.getStringExtra("namamatkultugas");


            namaMatakuliah.setText(getNamaTugas);
            judulLayout.setText(getJudulTugas);
            deskripsiLayout.setText(getDeskripsiTugas);

        }else{
            infoTugasPengDetail.setVisibility(View.GONE);
            garis_pengumuman2.setVisibility(View.GONE);
            // if pengumuman

            tanggalPengumumanGet = intent.getStringExtra("tanggalpengumuman");
            String strCurrentDate = tanggalPengumumanGet;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");

            SimpleDateFormat outputdate = new SimpleDateFormat("EEE , dd MMM yyyy");
            SimpleDateFormat outputHour = new SimpleDateFormat(" hh:mm aa");

            String date = null;
            String hour = null;


            try {
                Date newDate  = format.parse(strCurrentDate);
                date= outputdate.format(newDate);
                hour= outputHour.format(newDate);
                tanggalLayout.setText(date);
                jam_read.setText(hour);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            judulPengumumanGet = intent.getStringExtra("judulpengumuman");
            namaPengumumanGet = intent.getStringExtra("namapengumuman");
            deskripsiPengumumanGet = intent.getStringExtra("deskripsipengumuman");

            namaMatakuliah.setText(namaPengumumanGet);
            judulLayout.setText(judulPengumumanGet);
            deskripsiLayout.setText(deskripsiPengumumanGet);

            staticText.setVisibility(View.GONE);
            tanggalPengumpulanTugas.setVisibility(View.GONE);
        }
    }

    private Flashbar primaryActionListener() {
        return new Flashbar.Builder(this)
                .gravity(Flashbar.Gravity.BOTTOM)
                .title("Info")
                .showIcon()
                .backgroundColorRes(R.color.colorPrimaryDark)
                .enterAnimation(FlashAnim.with(this)
                        .animateBar()
                        .duration(750)
                        .alpha()
                        .overshoot())
                .exitAnimation(FlashAnim.with(this)
                        .animateBar()
                        .duration(400)
                        .accelerateDecelerate())
                .message("Pengumpulan tugas hanya dapat dilakukan pada web E-Class")
                .primaryActionText("DISMISS")
                .primaryActionTapListener(new Flashbar.OnActionTapListener() {
                    @Override
                    public void onActionTapped(@NotNull Flashbar bar) {
                        bar.dismiss();
                    }
                })
                .build();
    }

    public void sbTugasDetail(View v){
        flashbar = primaryActionListener();
        flashbar.show();
    }

}
