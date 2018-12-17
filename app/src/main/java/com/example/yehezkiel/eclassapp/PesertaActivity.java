package com.example.yehezkiel.eclassapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.FrameLayout;

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

public class PesertaActivity extends BaseActivity {

    private List<PesertaMatakuliah> listPeserta = new ArrayList<>();
    private ArrayList<String> nilai_key = new ArrayList<>();
    private DatabaseReference userRef;
    private DatabaseReference pesertaRef ;
    private DatabaseReference asistenRef ;

    private FirebaseUser users;

    //recyclerview
    private RecyclerView mRecycleView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_peserta, contentFrameLayout);


        final Intent intent = getIntent();
        final String keys = intent.getStringExtra("keys");
        final String flag = intent.getStringExtra("flag");

        final String namaMatkul = intent.getStringExtra("namaMatkul");

        userRef = FirebaseDatabase.getInstance().getReference("users");
        users = FirebaseAuth.getInstance().getCurrentUser();
        pesertaRef = FirebaseDatabase.getInstance().getReference("course_peserta");
        asistenRef = FirebaseDatabase.getInstance().getReference("asisten_course");


        mRecycleView = (RecyclerView) findViewById(R.id.recycler_peserta);
        mRecycleView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecycleView.setLayoutManager(mLayoutManager);

        mAdapter = new myAdapterPeserta(listPeserta);

        mRecycleView.setAdapter(mAdapter);

        if(flag==null) {
            setTitle("Peserta ");

            queryPeserta(keys);
        }else{
            setTitle("Asisten ");

            queryAsisten(keys);

        }



    }

    private void queryAsisten(String keys){
        asistenRef.child(keys).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listPeserta.clear();
                if(dataSnapshot.exists()){
                    Log.e("Asisten","1"+dataSnapshot.getValue());

                    for(DataSnapshot dataHasil : dataSnapshot.getChildren()){
                        final String dataKey = dataHasil.getKey();
                        userRef.child(dataKey).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    final PesertaMatakuliah peserta = dataSnapshot.getValue(PesertaMatakuliah.class);
                                    listPeserta.add(peserta);

                                    Collections.sort(listPeserta, new Comparator<PesertaMatakuliah>() {
                                        public int compare(PesertaMatakuliah o1, PesertaMatakuliah o2) {
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

                }else{



                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void queryPeserta(String keys){
        pesertaRef.child(keys).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listPeserta.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot dataHasil : dataSnapshot.getChildren()){
                        final String dataKey = dataHasil.getKey();
                        userRef.child(dataKey).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    final PesertaMatakuliah peserta = dataSnapshot.getValue(PesertaMatakuliah.class);
                                    listPeserta.add(peserta);

                                    Collections.sort(listPeserta, new Comparator<PesertaMatakuliah>() {
                                        public int compare(PesertaMatakuliah o1, PesertaMatakuliah o2) {
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

                }else{



                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
