package com.example.yehezkiel.eclassapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;

import java.util.ArrayList;
import java.util.List;

public class PengumumanDetailActivity extends BaseActivity {

    //variabel recyclerview
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter adapter;


    private List<DaftarPengumuman> listPengumuman = new ArrayList<>();


    //variabel intent extra
    String keys;
    String namaMatkulGet;

    //firebase setting
    private DatabaseReference userRef;
    private FirebaseUser users;
    private DatabaseReference mataKuliahRef;
    private DatabaseReference daftarPengumumanRef;
    private DatabaseReference pengumumanRef;

    //judul static
    private TextView staticPengumuman;

    //progress bar dan null static
    private TextView static_null_pengumuman;
    private ProgressBar progressBarPengumuman;


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


        adapter = new myAdapterPengumumanDetail(listPengumuman);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new LayoutMarginDecoration(1,15));
        //

        //Get intent Extra
        Intent intent = getIntent();
        keys = intent.getStringExtra("keys");
        namaMatkulGet = intent.getStringExtra("namaMatkul");
        Log.e("coba2",""+keys);
        Log.e("coba2",""+namaMatkulGet);

        //

        // set static judul
        staticPengumuman = (TextView) findViewById(R.id.staticPengumuman);
        staticPengumuman.setText(namaMatkulGet);

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
                        daftarPengumumanRef.child(idCourses).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    DaftarPengumuman pengumuman = new DaftarPengumuman();
                                    pengumuman = dataSnapshot.getValue(DaftarPengumuman.class);
                                    listPengumuman.add(pengumuman);
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
}
