package com.example.yehezkiel.eclassapp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.readystatesoftware.viewbadger.BadgeView;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Tugas extends BaseActivity {

    private List<DaftarTugas> listTugas = new ArrayList<>();


    //firebase variable
    private DatabaseReference daftarTugasRef;
    private DatabaseReference mTugasRef;


    //recyclerview variable
    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    private TextView static_null_tugas;
    private ProgressBar progressBarTugas;

    //intent get extra variable
    String keys,namaMatkulGet,flag;



    //asdos
    private Button btnTugas;
    private Dialog myDialog,infoDialog;
    private Button btnUploadTugas,btnUploadTugas2;

    private EditText nama_tugas,judul_tugas,deskripsi_tugas,tanggal_kumpul;
    String nama_t,judul_t,deskripsi_t,tanggal_kumpul_t;
    String keyMateri;
    boolean edited=true;
    boolean kosong =true;
    String prevKey;
//    private Tugas.Counter counter;
    BadgeView badge;


    Button buttonClearTugas;


    //intent put extra variable
    private ArrayList<String> namaMatkulPut = new ArrayList<>();
    private ArrayList<String> deskripsiTugasPut = new ArrayList<>();
    private ArrayList<String> judulTugasPut = new ArrayList<>();
    private ArrayList<String> tanggalKumpulPut = new ArrayList<>();
    private ArrayList<String> tanggalTugasPut = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_tugas, contentFrameLayout);

        setTitle("Tugas");
        daftarTugasRef = FirebaseDatabase.getInstance().getReference("tugas");
        mTugasRef = FirebaseDatabase.getInstance().getReference("tugas_course");


        myDialog = new Dialog(this, R.style.CustomDialog);
        myDialog.setContentView(R.layout.tugas_popup);

        infoDialog = new Dialog(this, R.style.CustomDialog);
        infoDialog.setContentView(R.layout.info_popup_tugas);


        //intent getExtra
        Intent intent = getIntent();
        keys = intent.getStringExtra("keys");
        namaMatkulGet = intent.getStringExtra("namaMatkul");
        flag = intent.getStringExtra("flag");

        nama_tugas = myDialog.findViewById(R.id.nama_tugas);
        judul_tugas = myDialog.findViewById(R.id.judul_tugas);
        deskripsi_tugas = myDialog.findViewById(R.id.deskripsi_tugas);
        tanggal_kumpul = myDialog.findViewById(R.id.tanggal_kumpul);

        nama_tugas.addTextChangedListener(tugasTextWatcher);
        judul_tugas.addTextChangedListener(tugasTextWatcher);
        deskripsi_tugas.addTextChangedListener(tugasTextWatcher);
        tanggal_kumpul.addTextChangedListener(tugasTextWatcher);


        //
        static_null_tugas = (TextView) findViewById(R.id.static_null_tugas);
        progressBarTugas = (ProgressBar) findViewById(R.id.progressBar_tugas);

        //button
        btnTugas = (Button) findViewById(R.id.btnTugas);





        //recyclerview setting
        mRecycleView = (RecyclerView) findViewById(R.id.recyclerViewTugas);
        mRecycleView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(mLayoutManager);
        mAdapter = new myAdapterTugas(listTugas);
        mRecycleView.setAdapter(mAdapter);
        mRecycleView.addItemDecoration(new LayoutMarginDecoration(1,15));



//        counter = new TwoFragment().Counter() {
//            @Override
//            public void setCounter(int count) {
//                Log.e("Ser_Counter", "" + count);
//                badge.setText(count + "");
//            }
//        };



        if(flag.equals("asdos")){
            btnTugas.setVisibility(View.VISIBLE);
        }else{
            btnTugas.setVisibility(View.GONE);

        }

        mTugasRef.child(keys).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listTugas.clear();
                if(dataSnapshot.exists()){
                    progressBarTugas.setVisibility(View.GONE);
                    for(DataSnapshot dataHasil : dataSnapshot.getChildren()){
                        final String idTugas = dataHasil.getKey();
                        daftarTugasRef.child(keys).child(idTugas).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    DaftarTugas tugas = new DaftarTugas();
                                    tugas = dataSnapshot.getValue(DaftarTugas.class);
                                    listTugas.add(tugas);
                                    namaMatkulPut.add(tugas.getNama_tugas());
                                    deskripsiTugasPut.add(tugas.getDeskripsi_tugas());
                                    tanggalKumpulPut.add(tugas.getTanggal_kumpul());
                                    tanggalTugasPut.add(tugas.getTanggal_tugas());
                                    judulTugasPut.add(tugas.getJudul_tugas());
                                    mAdapter.notifyDataSetChanged();
                                }else {

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                }else{
                    static_null_tugas.setVisibility(View.VISIBLE);
                    progressBarTugas.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mRecycleView.addOnItemTouchListener(new RecyclerTouchListener(this, mRecycleView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                DaftarTugas tugas = listTugas.get(position);
                Intent intent = new Intent(Tugas.this, ReadActivity.class);
                intent.putExtra("flag", "tugas");
                intent.putExtra("namamatkultugas", namaMatkulPut.get(position));
                intent.putExtra("deskripsitugas", deskripsiTugasPut.get(position));
                intent.putExtra("judultugas", judulTugasPut.get(position));
                intent.putExtra("tanggalkumpul", tanggalKumpulPut.get(position));
                intent.putExtra("tanggaltugas", tanggalTugasPut.get(position));
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));








    }


    public void showPopupTugas(View v){
        TextView txtClose;

        buttonClearTugas = (Button) myDialog.findViewById(R.id.buttonClearTugas);
        txtClose = (TextView) myDialog.findViewById(R.id.txtClose);
        btnUploadTugas = (Button) myDialog.findViewById(R.id.buttonUploadTugas);
        btnUploadTugas2 = (Button) myDialog.findViewById(R.id.buttonUploadTugas2);
        TextView txtInfo = (TextView) myDialog.findViewById(R.id.txtInfo);

        txtInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfoTugas();
            }
        });

        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });

        buttonClearTugas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDataTugas();
            }
        });


        btnUploadTugas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadDeskripsiTugas();
            }
        });

        btnUploadTugas2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tambahTugas();
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EventBus.getDefault().post(new ViewEvent("updateViews"));

    }

    private void showInfoTugas(){

        Button buttonClose;
        buttonClose = (Button) infoDialog.findViewById(R.id.dismiss_info);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoDialog.dismiss();
            }
        });
        infoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        infoDialog.show();

    }

    private void uploadDeskripsiTugas(){


        nama_t = nama_tugas.getText().toString();
        judul_t = judul_tugas.getText().toString();
        deskripsi_t = deskripsi_tugas.getText().toString();
        tanggal_kumpul_t = tanggal_kumpul.getText().toString();

        if(TextUtils.isEmpty(nama_t)){
            new SweetAlertDialog(Tugas.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Nama Tidak Boleh Kosong !!")
                    .show();
        }else if(TextUtils.isEmpty(judul_t)){
            new SweetAlertDialog(Tugas.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Judul Tidak Boleh Kosong !!")
                    .show();
        }else if(TextUtils.isEmpty(deskripsi_t)){
            new SweetAlertDialog(Tugas.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Deskripsi Tidak Boleh Kosong !!")
                    .show();
        }else if(TextUtils.isEmpty(tanggal_kumpul_t)){
            new SweetAlertDialog(Tugas.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Tanggal Kumpul Tidak Boleh Kosong !!")
                    .show();
        }else{

            if(edited) {
                keyMateri = daftarTugasRef.child(keys).push().getKey();
                prevKey = keyMateri;

                daftarTugasRef.child(keys).child(keyMateri).child("deskripsi_tugas").setValue(deskripsi_t);
                daftarTugasRef.child(keys).child(keyMateri).child("judul_tugas").setValue(judul_t);
                daftarTugasRef.child(keys).child(keyMateri).child("nama_tugas").setValue(nama_t);
                daftarTugasRef.child(keys).child(keyMateri).child("tanggal_kumpul").setValue(tanggal_kumpul_t);

                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String formattedDate = df.format(c);
                daftarTugasRef.child(keys).child(keyMateri).child("tanggal_tugas").setValue(formattedDate.toString());

                new SweetAlertDialog(Tugas.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Selamat")
                        .setContentText("Upload Pengumuman Berhasil")
                        .show();
                edited=false;
                kosong=false;
                btnUploadTugas2.setEnabled(true);
                btnUploadTugas.setBackgroundResource(R.drawable.button_border_success);

            }else{

                daftarTugasRef.child(keys).child(prevKey).child("deskripsi_tugas").setValue(deskripsi_t);
                daftarTugasRef.child(keys).child(prevKey).child("judul_tugas").setValue(judul_t);
                daftarTugasRef.child(keys).child(prevKey).child("nama_tugas").setValue(nama_t);
                daftarTugasRef.child(keys).child(prevKey).child("tanggal_kumpul").setValue(tanggal_kumpul_t);

                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String formattedDate = df.format(c);
                daftarTugasRef.child(keys).child(keyMateri).child("tanggal_tugas").setValue(formattedDate.toString());

                new SweetAlertDialog(Tugas.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Deskripsi Berhasil di Update !")
                        .show();
                kosong=false;
                btnUploadTugas2.setEnabled(true);
                btnUploadTugas.setBackgroundResource(R.drawable.button_border_success);


            }

        }


    }

    private void deleteDataTugas(){
        nama_tugas.setText("");
        judul_tugas.setText("");
        deskripsi_tugas.setText("");
        tanggal_kumpul.setText("");
        btnUploadTugas.setBackgroundResource(R.drawable.button_selector);
        btnUploadTugas2.setEnabled(false);

        edited=true;
        kosong=true;

    }

    private void tambahTugas() {

        if (kosong) {
            new SweetAlertDialog(Tugas.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Tambah Tugas Terlebih Dulu")
                    .show();

        } else {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Publish Tugas ?")
                    .setContentText("Tugas " + nama_t)
                    .setConfirmText("Ya !")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            deleteDataTugas();
                            btnUploadTugas2.setEnabled(false);
                            btnUploadTugas.setBackgroundResource(R.drawable.button_selector);

                            mTugasRef.child(keys).child(keyMateri).setValue(true);
                            sDialog
                                    .setTitleText("Tugas berhasil ditambahkan !!")
                                    .setContentText(namaMatkulGet)
                                    .setConfirmText("OK")
                                    .setConfirmClickListener(null)
                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                            EventBus.getDefault().post(new ViewEvent("updateViews"));

                        }
                    })

                    .show();
                    btnUploadTugas2.setEnabled(false);


        }
    }

    private TextWatcher tugasTextWatcher = new TextWatcher(){

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String nama_tugas_input = nama_tugas.getText().toString().trim();
            String judul_tugas_input = judul_tugas.getText().toString().trim();

            String deskripsi_tugas_input = deskripsi_tugas.getText().toString().trim();
            String tanggal_kumpul_input = tanggal_kumpul.getText().toString().trim();

            if(!nama_tugas_input.isEmpty() || !judul_tugas_input.isEmpty() || !deskripsi_tugas_input.isEmpty() || !tanggal_kumpul_input.isEmpty()){
                buttonClearTugas.setEnabled(true);
            }else {
                buttonClearTugas.setEnabled(false);
            }

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}
