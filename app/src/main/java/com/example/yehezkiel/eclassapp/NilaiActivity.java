package com.example.yehezkiel.eclassapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;

import java.util.ArrayList;
import java.util.List;

public class NilaiActivity extends BaseActivity {



    private ArrayList<String> nilai_key = new ArrayList<>();
    private DatabaseReference userRef;
    private DatabaseReference nilaiRef ;
    private DatabaseReference nilaiInfoRef ;
    private FirebaseUser users;
    private List<NilaiMatakuliah> listNilai = new ArrayList<>();

    private ProgressBar progressBar_nilai;


    private TextView static_null;

    //recyclerview
    private RecyclerView mRecycleView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_nilai, contentFrameLayout);

        setTitle("Nilai");


        userRef = FirebaseDatabase.getInstance().getReference("users");
        users = FirebaseAuth.getInstance().getCurrentUser();
        nilaiRef = FirebaseDatabase.getInstance().getReference("nilai_course");
        nilaiInfoRef = FirebaseDatabase.getInstance().getReference("keterangan_nilai");


        mRecycleView = (RecyclerView) findViewById(R.id.recycler_nilai);
        mRecycleView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecycleView.setLayoutManager(mLayoutManager);

        progressBar_nilai = (ProgressBar) findViewById(R.id.progressBar_Nilai);
        static_null = (TextView) findViewById(R.id.static_null);

        mAdapter = new myAdapterNilai(listNilai);
        mRecycleView.setAdapter(mAdapter);
        mRecycleView.addItemDecoration(new LayoutMarginDecoration(1,15));



        final Intent intent = getIntent();
        final String keys = intent.getStringExtra("keys");
        Log.e("nilai",""+keys);


        nilaiRef.child(keys).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listNilai.clear();
                final ArrayList<Integer> test1 = new ArrayList<>();
                final ArrayList<Integer> test2 = new ArrayList<>();
                if(dataSnapshot.exists()){
                    for(DataSnapshot dataHasil : dataSnapshot.getChildren()){
                        nilai_key.add(dataHasil.getKey());
                        Log.e("nilais","here "+dataHasil.getKey());

                        if(dataHasil.child(users.getUid()).exists()){
                            final String nilai_temp = (String) dataHasil.child(users.getUid()).getValue().toString();

                            final double etKids = Double.parseDouble(nilai_temp);
                            Log.e("etKids","here "+etKids);


                            test1.add(1);
                            nilaiInfoRef.child(keys).child(dataHasil.getKey()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        NilaiMatakuliah nilai = dataSnapshot.getValue(NilaiMatakuliah.class);
                                        nilai.setNilai(etKids);
                                        listNilai.add(nilai);

                                        test2.add(1);


                                        if(test1.size() == test2.size()){
                                            progressBar_nilai.setVisibility(View.GONE);

                                            mAdapter.notifyDataSetChanged();

                                        }



                                    }else{

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }else{

                        }


                    }
                }else{
                    progressBar_nilai.setVisibility(View.GONE);
                    static_null.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mRecycleView.addOnItemTouchListener(new RecyclerTouchListener(this, mRecycleView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                NilaiMatakuliah nilai = listNilai.get(position);
                Toast.makeText(NilaiActivity.this, nilai_key.get(position) + " is selected!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(NilaiActivity.this, NilaiDetailActivity.class);
                intent.putExtra("keys", nilai_key.get(position));
                intent.putExtra("namamatkul", keys);

                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));



    }
}
