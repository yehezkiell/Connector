package com.example.yehezkiel.eclassapp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    View v;
    Dialog myDialog;
    private String namaMatkulGet;
    private String dayMatkulGet;
    private String jamMatkulGet;
    private String bobotMatkulGet;
    private String kelasMatkulGet;
    private DatabaseReference mataKuliahRef,dosenRef;
    private String nama_dosen;
    private String foto_dosen;
    private List<ListDosen> listDosen2 = new ArrayList<>();
    private List<ListDosen> listDosen = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private String keys;
    private CardView mNilaiCard,mPesertaCard,mMateriCard,mPengumumanCard;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);



        //setting database
        mataKuliahRef = FirebaseDatabase.getInstance().getReference("courses");
        dosenRef = FirebaseDatabase.getInstance().getReference("daftar_dosen");

        //populate custom pop up
        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.custompopup);

        //recyclerView
        mRecyclerView = (RecyclerView) myDialog.findViewById(R.id.mRecyclerDosen);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new myAdapterDosen(listDosen2);
        mRecyclerView.setAdapter(mAdapter);


        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(horizontalLayoutManagaer);


        //put extra
        Intent intent = getIntent();
        keys = intent.getStringExtra("keys");
        namaMatkulGet = intent.getStringExtra("namamatkul");
        dayMatkulGet = intent.getStringExtra("daymatkul");
        jamMatkulGet = intent.getStringExtra("jammatkul");
        bobotMatkulGet = intent.getStringExtra("bobotmatkul");
        kelasMatkulGet = intent.getStringExtra("kelasmatkul");


        mPengumumanCard = (CardView) findViewById(R.id.pengumuman_card);
        mNilaiCard = (CardView) findViewById(R.id.nilai_card);
        mPesertaCard = (CardView) findViewById(R.id.peserta_card);
        mMateriCard = (CardView) findViewById(R.id.materi_card);

        mPengumumanCard.setOnClickListener(this);
        mMateriCard.setOnClickListener(this);
        mNilaiCard.setOnClickListener(this);
        mPesertaCard.setOnClickListener(this);






        TextView namaMatkulPopup = (TextView) myDialog.findViewById(R.id.nama_matkul_popup);
        TextView bobotMatkulPopup = (TextView) myDialog.findViewById(R.id.bobot_matkul_popup);
        TextView kelasMatkulPopup = (TextView) myDialog.findViewById(R.id.kelas_matkul_popup);
        TextView jamMatkulPopup = (TextView) myDialog.findViewById(R.id.jam_matkul_popup);

        Log.e("abce",namaMatkulGet);
        namaMatkulPopup.setText(namaMatkulGet);
        bobotMatkulPopup.setText(bobotMatkulGet);
        kelasMatkulPopup.setText(kelasMatkulGet);
        jamMatkulPopup.setText(jamMatkulGet);


        //title of detailactivity
        TextView namaMatkulJudul = (TextView) findViewById(R.id.nama_matkul_judul);
        namaMatkulJudul.setText(namaMatkulGet);



        mataKuliahRef.child(keys).child("daftar_dosen").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listDosen.clear();
                if(dataSnapshot.exists()){
                for(DataSnapshot dataDosen : dataSnapshot.getChildren()){

                    dosenRef.child(dataDosen.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                                ListDosen listDosen = dataSnapshot.getValue(ListDosen.class);
                                Log.e("listdosen", listDosen.getNama());
                                listDosen2.add(listDosen);
                                mAdapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                }
            }else{

                }}

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //Here is to hide title and show it only when it collapse
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingtoolbar);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbarlay);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(namaMatkulGet);
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });
        //Here is to hide title and show it only when it collapse


        toolbar.setNavigationIcon(R.mipmap.baseline_arrow_back_white_24); // your drawable
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }



    public void showPopup(View v){
        TextView txtClose;
        txtClose = (TextView) myDialog.findViewById(R.id.txtClose);

        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

        };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.nilai_card):
                Intent intent = new Intent(DetailActivity.this, NilaiActivity.class);
                intent.putExtra("keys", keys);
                startActivity(intent);
                break;
            case (R.id.materi_card):
                Intent intent2 = new Intent(DetailActivity.this, MateriActivity.class);
                intent2.putExtra("keys", namaMatkulGet);
                startActivity(intent2);
                break;
            case (R.id.peserta_card):
                Intent intent3 = new Intent(DetailActivity.this, PesertaActivity.class);
                intent3.putExtra("namaMatkul", namaMatkulGet);
                intent3.putExtra("keys", keys);
                startActivity(intent3);
                break;
            case (R.id.pengumuman_card):
                Intent intent4 = new Intent(DetailActivity.this, PengumumanDetailActivity.class);
                intent4.putExtra("namaMatkul", namaMatkulGet);
                intent4.putExtra("keys", keys);
                startActivity(intent4);
                break;


        }
    }
}



