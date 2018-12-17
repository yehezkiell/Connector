package com.example.yehezkiel.eclassapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NilaiDetailActivity extends BaseActivity {

    private String keys;
    private String nama_matkul;

    //recyclerview
    private RecyclerView mRecycleView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private List<ListNilaiDetail> listNilaiDetail = new ArrayList<>();



    //firebase
    private DatabaseReference userRef;
    private DatabaseReference nilaiRef ;
    private DatabaseReference nilaiInfoRef ;
    private FirebaseUser users;

    private int count;
    private double mathAkhir;
    private TextView averageNilai;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_nilai_detail, contentFrameLayout);

        setTitle("Daftar Nilai Kelas");

        userRef = FirebaseDatabase.getInstance().getReference("users");
        users = FirebaseAuth.getInstance().getCurrentUser();
        nilaiRef = FirebaseDatabase.getInstance().getReference("nilai_course");
        nilaiInfoRef = FirebaseDatabase.getInstance().getReference("keterangan_nilai");


        averageNilai = (TextView) findViewById(R.id.averageNilai);
        Intent intent = getIntent();
        keys = intent.getStringExtra("keys");
        nama_matkul = intent.getStringExtra("namamatkul");

        mRecycleView = (RecyclerView) findViewById(R.id.recycler_nilai_detail);
        mRecycleView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecycleView.setLayoutManager(mLayoutManager);

        mAdapter = new myAdapterNilaiDetail(listNilaiDetail);

        mRecycleView.setAdapter(mAdapter);


        nilaiRef.child(nama_matkul).child(keys).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listNilaiDetail.clear();
                if(dataSnapshot.exists()){
                    int count = 0;
                    for(DataSnapshot dataHasil : dataSnapshot.getChildren()){
                        final String uid = dataHasil.getKey();
                        Log.e("news",""+uid);

                        final String nilai_temp = (String) dataHasil.getValue().toString();
                        final int count2 = (int) dataSnapshot.getChildrenCount();
                        final double etKids = Double.parseDouble(nilai_temp);
                        //ambil nilai disini

                        count++;
                        final int finalCount = count;
                        mathAkhir=0;
                        userRef.child(uid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    ListNilaiDetail nilai = dataSnapshot.getValue(ListNilaiDetail.class);
                                    nilai.setNilai(etKids);

                                    mathAkhir = (mathAkhir+nilai.getNilai());
                                    Log.e("countnya",""+mathAkhir);

                                    averageNilai.setText(""+mathAkhir/count2);
                                    Log.e("countnya","pembagian "+mathAkhir/count2);



                                    listNilaiDetail.add(nilai);

                                    Collections.sort(listNilaiDetail, new Comparator<ListNilaiDetail>() {
                                        public int compare(ListNilaiDetail o1, ListNilaiDetail o2) {
                                            if (o1.getNim() < o2.getNim()) {
                                                return -1;

                                            } else if (o1.getNim() > o2.getNim()) {

                                                return 1;
                                            } else {
                                                return 0;
                                            }
                                        }

                                    });

                                    mAdapter.notifyDataSetChanged();
                                }else {

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                }else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }




}
