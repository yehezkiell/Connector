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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andrognito.flashbar.Flashbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PengumumanDetailActivity extends BaseActivity {

    //variabel recyclerview
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter adapter;
    private Flashbar flashbar = null;


    private List<DaftarPengumuman> listPengumuman = new ArrayList<>();


    //variabel intent extra
    String keys,namaMatkulGet,flag;

    //firebase setting
    private DatabaseReference mataKuliahRef;
    private DatabaseReference daftarPengumumanRef;
    private DatabaseReference pengumumanRef;

    //judul static
    private TextView staticPengumuman;

    private Button btnPengumuman;

    //progress bar dan null static
    private TextView static_null_pengumuman;
    private ProgressBar progressBarPengumuman;

    private Dialog myDialog,infoDialog;
    private Button buttonUpload,buttonUpload2;

    private EditText header_pengumuman,judul_pengumuman,deskripsi_pengumuman;
    String header_p,deskripsi_p,judul_p;

    //set array for putextra intent
    private ArrayList<String> deskripsiPengumumanPut = new ArrayList<>();
    private ArrayList<String> judulPengumumanPut = new ArrayList<>();
    private ArrayList<String> tanggalPengumumanPut = new ArrayList<>();
    String keyMateri;
    Button buttonClearPengumuman;

    boolean edited =true;
    boolean kosong =true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_pengumuman_detail, contentFrameLayout);

        setTitle("Pengumuman");

        //Firebase instance setting
        daftarPengumumanRef = FirebaseDatabase.getInstance().getReference("pengumuman");
        pengumumanRef = FirebaseDatabase.getInstance().getReference("pengumuman_course");
        //


        //RecyclerView Setting
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewPengumuman);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);



        myDialog = new Dialog(this,R.style.CustomDialog);
        myDialog.setContentView(R.layout.pengumuman_popup);

        infoDialog = new Dialog(this,R.style.CustomDialog);
        infoDialog.setContentView(R.layout.info_popup_pengumuman);


        adapter = new myAdapterPengumumanDetail(listPengumuman);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new LayoutMarginDecoration(1,15));
        //

        header_pengumuman = myDialog.findViewById(R.id.header_pengumuman);
        judul_pengumuman = myDialog.findViewById(R.id.judul_pengumuman);
        deskripsi_pengumuman = myDialog.findViewById(R.id.deskripsi_pengumuman);

        header_pengumuman.addTextChangedListener(pengumumanTextWatcher);
        judul_pengumuman.addTextChangedListener(pengumumanTextWatcher);
        deskripsi_pengumuman.addTextChangedListener(pengumumanTextWatcher);


        //Get intent Extra
        Intent intent = getIntent();
        keys = intent.getStringExtra("keys");
        namaMatkulGet = intent.getStringExtra("namaMatkul");
        flag = intent.getStringExtra("flag");

        Log.e("uwaw",""+keys+namaMatkulGet+flag);

        btnPengumuman = (Button) findViewById(R.id.btnPengumuman);



        if(flag.equals("asdos")){
            btnPengumuman.setVisibility(View.VISIBLE);
        }else{
            btnPengumuman.setVisibility(View.GONE);

        }

        // set static judul
        staticPengumuman = (TextView) findViewById(R.id.staticPengumuman);

        //static progress bar and static null
        static_null_pengumuman = (TextView) findViewById(R.id.static_null_pengumuman);
        progressBarPengumuman = (ProgressBar) findViewById(R.id.progressBar_Pengumuman);

        pengumumanRef.child(keys).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listPengumuman.clear();
                progressBarPengumuman.setVisibility(View.GONE);
                if(dataSnapshot.exists()){
                    Log.e("coba2",""+dataSnapshot.getValue());

                    for(DataSnapshot dataHasil : dataSnapshot.getChildren()){
                        final String idCourses = dataHasil.getKey();
                        Log.e("coba2",""+dataHasil.getKey());
                        daftarPengumumanRef.child(keys).child(idCourses).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    DaftarPengumuman pengumuman = new DaftarPengumuman();
                                    pengumuman = dataSnapshot.getValue(DaftarPengumuman.class);
                                    pengumuman.setNama_p(namaMatkulGet);
                                    listPengumuman.add(pengumuman);
                                    //populate array for putextra
                                    deskripsiPengumumanPut.add(pengumuman.getDeskripsi());
                                    tanggalPengumumanPut.add(pengumuman.getTanggal_peng());
                                    judulPengumumanPut.add(pengumuman.getJudul());


                                    adapter.notifyDataSetChanged();


                                }else {

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                }else{
                    //if data==empty
                    progressBarPengumuman.setVisibility(View.GONE);
                    static_null_pengumuman.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void showPopupPengumuman(View v){
        TextView txtClose;

        buttonClearPengumuman = (Button) myDialog.findViewById(R.id.buttonClearPengumuman);
        txtClose = (TextView) myDialog.findViewById(R.id.txtClose);
        buttonUpload = (Button) myDialog.findViewById(R.id.buttonUpload);
        buttonUpload2 = (Button) myDialog.findViewById(R.id.buttonUpload2);
        TextView txtInfo = (TextView) myDialog.findViewById(R.id.txtInfo);

        txtInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfoPengumuman();
            }
        });

        buttonClearPengumuman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDataPengumuman();
            }
        });

        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadDeskripsiTugas();
            }
        });

        buttonUpload2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tambahTugas();
            }
        });




        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

    };

    private void deleteDataPengumuman(){
        header_pengumuman.setText("");
        judul_pengumuman.setText("");
        deskripsi_pengumuman.setText("");

        edited=true;
        kosong=true;

    }


    private void uploadDeskripsiTugas() {

        header_p = header_pengumuman.getText().toString();
        judul_p = judul_pengumuman.getText().toString();
        deskripsi_p = deskripsi_pengumuman.getText().toString();

        if (TextUtils.isEmpty(header_p)) {
            new SweetAlertDialog(PengumumanDetailActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Header Tidak Boleh Kosong !!")
                    .show();
        } else if (TextUtils.isEmpty(judul_p)) {
            new SweetAlertDialog(PengumumanDetailActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Judul Tidak Boleh Kosong !!")
                    .show();
        } else if (TextUtils.isEmpty(deskripsi_p)) {
            new SweetAlertDialog(PengumumanDetailActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Deskripsi Tidak Boleh Kosong !!")
                    .show();
        } else {
            if (edited) {
                keyMateri = daftarPengumumanRef.child(keys).push().getKey();
                daftarPengumumanRef.child(keys).child(keyMateri).child("deskripsi").setValue(deskripsi_p);
                daftarPengumumanRef.child(keys).child(keyMateri).child("header").setValue(header_p);
                daftarPengumumanRef.child(keys).child(keyMateri).child("judul").setValue(judul_p);
                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String formattedDate = df.format(c);
                daftarPengumumanRef.child(keys).child(keyMateri).child("tanggal_peng").setValue(formattedDate.toString());

                new SweetAlertDialog(PengumumanDetailActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Selamat")
                        .setContentText("Upload Pengumuman Berhasil")
                        .show();
                edited=false;
                kosong=false;
                buttonUpload2.setEnabled(true);
                buttonUpload.setBackgroundResource(R.drawable.button_border_success);


            } else {
                daftarPengumumanRef.child(keys).child(keyMateri).child("deskripsi").setValue(deskripsi_p);
                daftarPengumumanRef.child(keys).child(keyMateri).child("header").setValue(header_p);
                daftarPengumumanRef.child(keys).child(keyMateri).child("judul").setValue(judul_p);

                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String formattedDate = df.format(c);
                daftarPengumumanRef.child(keys).child(keyMateri).child("tanggal_peng").setValue(formattedDate.toString());

                new SweetAlertDialog(PengumumanDetailActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Upload Pengumuman Berhasil")
                        .show();
                kosong=false;
                buttonUpload2.setEnabled(true);
                buttonUpload.setBackgroundResource(R.drawable.button_border_success);



            }


        }
    }


    private void showInfoPengumuman(){

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

    private void tambahTugas() {

        if (kosong) {
            new SweetAlertDialog(PengumumanDetailActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Tambah Pengumuman Terlebih Dulu")
                    .show();
        } else {

            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Publish Pengumuman ?")
                    .setContentText("Pengumuman " + header_p)
                    .setConfirmText("Ya !")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            deleteDataPengumuman();
                            buttonUpload2.setEnabled(false);
                            buttonUpload.setBackgroundResource(R.drawable.button_selector);

                            pengumumanRef.child(keys).child(keyMateri).setValue(true);
                            sDialog
                                    .setTitleText("Pengumuman berhasil ditambahkan !!")
                                    .setContentText(namaMatkulGet)
                                    .setConfirmText("OK")
                                    .setConfirmClickListener(null)
                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        }
                    })

                    .show();


        }
    }

    private TextWatcher pengumumanTextWatcher = new TextWatcher(){

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String header_pengumuman_input = header_pengumuman.getText().toString().trim();
            String judul_pengumuman_input = judul_pengumuman.getText().toString().trim();
            String deskripsi_pengumuman_input = deskripsi_pengumuman.getText().toString().trim();

            buttonClearPengumuman.setEnabled(!header_pengumuman_input.isEmpty() || !judul_pengumuman_input.isEmpty() || !deskripsi_pengumuman_input.isEmpty());

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

}
