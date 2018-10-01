package com.example.yehezkiel.eclassapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;

import java.util.ArrayList;
import java.util.List;


public class TwoFragment extends Fragment {

    View v;
    private ArrayList<String> key2 = new ArrayList<>();

    private ProgressBar mProgressBar4;
    private List<DaftarTugas> listTugas = new ArrayList<>();
    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authListener;
    private ArrayList<String> obj2 = new ArrayList<>();
    private DatabaseReference userRef;
    private FirebaseUser users;
    private DatabaseReference mataKuliahRef;
    private DatabaseReference daftarTugasRef;
    private DatabaseReference mTugasRef;
    private boolean loaded;
    private boolean canBeTriggered;
    private int i=0;
    private int bobot;
    private TextView mJumlahTugas;

    private ArrayList<String> namaMatkulPut = new ArrayList<>();
    private ArrayList<String> deskripsiTugasPut = new ArrayList<>();
    private ArrayList<String> judulTugasPut = new ArrayList<>();
    private ArrayList<String> tanggalKumpulPut = new ArrayList<>();
    private ArrayList<String> tanggalTugasPut = new ArrayList<>();



    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v =  inflater.inflate(R.layout.fragment_two, container, false);
        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("user_course");
        users = FirebaseAuth.getInstance().getCurrentUser();
        mataKuliahRef = FirebaseDatabase.getInstance().getReference("courses");
        daftarTugasRef = FirebaseDatabase.getInstance().getReference("tugas");
        mTugasRef = FirebaseDatabase.getInstance().getReference("tugas_course");





        //Recycler View
        mJumlahTugas = (TextView) v.findViewById(R.id.jumlah_tugas);



        mRecycleView = (RecyclerView) v.findViewById(R.id.TugasRView);
        mRecycleView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecycleView.setLayoutManager(mLayoutManager);
        RecyclerViewHeader header = (RecyclerViewHeader) v.findViewById(R.id.header);
        header.attachTo(mRecycleView);
        mAdapter = new myAdapterTugas(listTugas);
        mRecycleView.setAdapter(mAdapter);
        mRecycleView.addItemDecoration(new LayoutMarginDecoration(1,15));



        LoadData();

        mRecycleView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), mRecycleView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                DaftarTugas tugas = listTugas.get(position);
                Intent intent = new Intent(getActivity(), DetailPengumuman.class);
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

        return v;
    }



    private void LoadData(){
        userRef.child(users.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loaded=false;
                listTugas.clear();
                final ArrayList<Integer> test1 = new ArrayList<>();
                final ArrayList<Integer> test2 = new ArrayList<>();
                if (dataSnapshot.exists()){
                    for(final DataSnapshot dataHasil : dataSnapshot.getChildren()){
                        Log.e("coba","loop pertama");
                        final String idCourses = dataHasil.getKey();
                        FirebaseMessaging.getInstance().subscribeToTopic(idCourses);

                        mTugasRef.child(idCourses).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(final DataSnapshot dataSnapshot2) {
                                if(dataSnapshot2.exists()) {
                                    for (DataSnapshot dataHasil2 : dataSnapshot2.getChildren()) {
                                        Log.e("coba","loop kedua");
                                        test1.add(1);
                                        final String idTugas = dataHasil2.getKey();
                                        daftarTugasRef.child(idTugas).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot3) {
                                                if(dataSnapshot3.exists()){
                                                    DaftarTugas tugas = new DaftarTugas();
                                                    tugas = dataSnapshot3.getValue(DaftarTugas.class);
                                                    listTugas.add(tugas);
                                                    test2.add(1);
                                                    bobot = bobot + 1;
                                                    mJumlahTugas.setText(""+bobot);

                                                    namaMatkulPut.add(tugas.getNama_tugas());
                                                    deskripsiTugasPut.add(tugas.getDeskripsi_tugas());
                                                    tanggalKumpulPut.add(tugas.getTanggal_kumpul());
                                                    tanggalTugasPut.add(tugas.getTanggal_tugas());
                                                    judulTugasPut.add(tugas.getJudul_tugas());



                                                    if(test1.size() == test2.size()){
                                                        mAdapter.notifyDataSetChanged();
                                                        bobot=0;
                                                        if(loaded){
                                                            test2.clear();
                                                            test2.clear();
                                                            LoadData();
                                                            Log.e("coba","loaded");

                                                        }
                                                        loaded = true;
                                                    }

                                                }else{
                                                    test2.add(1);
                                                }


                                                }


                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                        }//end of loop for datasnapshot 2
                                    }else{

                                    }

                                }



                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });



                        }// end of looping datasnapshot1
                    }else{

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    private void LoadData2(){
        listTugas.clear();

        userRef.child(users.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){


                    for(final DataSnapshot dataHasil : dataSnapshot.getChildren()){
                        Log.e("coba","loop pertama");

                        final String idCourses = dataHasil.getKey();
                        mTugasRef.child(idCourses).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(final DataSnapshot dataSnapshot2) {

                                if(dataSnapshot2.exists()) {
                                    for (DataSnapshot dataHasil2 : dataSnapshot2.getChildren()) {
                                        Log.e("coba","loop kedua");

                                        final String idTugas = dataHasil2.getKey();
                                        daftarTugasRef.child(idTugas).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot3) {
                                                DaftarTugas tugas = new DaftarTugas();
                                                tugas = dataSnapshot3.getValue(DaftarTugas.class);
                                                listTugas.add(tugas);


//                                                if(canBeTriggered)
//                                                {
//                                                    if(loaded){
//                                                        LoadData();
//                                                        Log.e("coba","canbetriggered");
//
//                                                    }
//                                                }

                                                mAdapter.notifyDataSetChanged();
                                                Log.e("coba","masuk count dan hrus paling blkng");

                                            }


//                                               if(test1.size() == test2.size()){
//                                                    mAdapter.notifyDataSetChanged();
//                                                    if(loaded){
////                                                        LoadData();
//                                                        Log.e("coba","0");
//
//                                                    }
//                                                   loaded = true;
//                                               }


                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                }//end of loop for datasnapshot 2
                            }else{

                            }

                        }



                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                }// end of looping datasnapshot1
            }else{

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
}
}
