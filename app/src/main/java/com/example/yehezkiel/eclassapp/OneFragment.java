package com.example.yehezkiel.eclassapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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


public class OneFragment extends Fragment {

    View v;
    private List<MataKuliah> listMatkul = new ArrayList<>();
    private List<ListAsdos> listAsdos = new ArrayList<>();

    private ArrayList<String> keys = new ArrayList<>();
    private ArrayList<String> namaMatkulPut = new ArrayList<>();
    private ArrayList<String> dayMatkulPut = new ArrayList<>();
    private ArrayList<String> jamMatkulPut = new ArrayList<>();
    private ArrayList<String> bobotMatkulPut = new ArrayList<>();
    private ArrayList<String> kelasMatkulPut = new ArrayList<>();
    private myAdapterMatkul myAdapter;
    private myAdapterAsdos myAdapter2;

    private FirebaseAuth.AuthStateListener authListener;
    private RecyclerView mRecycleView;
    private RecyclerView mRecycleView2;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter mAdapter2;

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.LayoutManager mLayoutManager2;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;

    private ProgressBar mProgressBar;
    private DatabaseReference userRef;
    private DatabaseReference userRefProdi;
    private DatabaseReference mataKuliahRef ;
    private FirebaseUser users;
    private DatabaseReference courseRef;



    private DatabaseReference asdosRef;

    //hitung total SKS recyclerview header
    private TextView mBobot;
    //hitung total matakuliah recyclerview header
    private int total_matkul;
    private TextView mTotalMatkul;
    private View loadingView;
    private View emptyView;
    private View errorView;

    private int bobot=0;
    boolean flagAsdos;
    private TextView mStaticAsdos,mStaticMatkul;

    private CardView mCardEqu;




    public OneFragment() {
        // Required empty public constructor

    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_one, container, false);
        userRef = FirebaseDatabase.getInstance().getReference("users");
        users = FirebaseAuth.getInstance().getCurrentUser();
        mataKuliahRef = FirebaseDatabase.getInstance().getReference("user_course");
        courseRef = FirebaseDatabase.getInstance().getReference("courses");
        asdosRef = FirebaseDatabase.getInstance().getReference("asisten_course");
        //Recycler View


        //equ ukdw
        mCardEqu = (CardView) v.findViewById(R.id.mCardEqu);
        mCardEqu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenEqu();
            }
        });


        //Recycler View
        mRecycleView = (RecyclerView) v.findViewById(R.id.MainRView);
        mRecycleView2 = (RecyclerView) v.findViewById(R.id.MainRView2);
        mRecycleView.setHasFixedSize(true);
        mRecycleView2.setHasFixedSize(true);


        mProgressBar = (ProgressBar) v.findViewById(R.id.progressBar2);
        mProgressBar.setVisibility(View.VISIBLE);


        mStaticAsdos = (TextView) v.findViewById(R.id.asisten_static);

        mBobot = (TextView) v.findViewById(R.id.bobot);
        mTotalMatkul = (TextView) v.findViewById(R.id.total_matkul);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager2 = new LinearLayoutManager(getActivity());

        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView2.setLayoutManager(mLayoutManager2);
        RecyclerViewHeader header = (RecyclerViewHeader) v.findViewById(R.id.header2);
        header.attachTo(mRecycleView);
        mAdapter = new myAdapterMatkul(listMatkul);
        mAdapter2 = new myAdapterAsdos(listAsdos);

        loadingView = getLayoutInflater().inflate(R.layout.view_loading, mRecycleView, false);


        mRecycleView.setAdapter(mAdapter);
        mRecycleView2.setAdapter(mAdapter2);
        //add margin between item in recyclerview
        mRecycleView.addItemDecoration(new LayoutMarginDecoration(1,15));
        mRecycleView2.addItemDecoration(new LayoutMarginDecoration(1,15));



        mStaticMatkul = (TextView) v.findViewById(R.id.matakuliah_static);



        userRef.child(users.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    boolean flag_asdos = (boolean) dataSnapshot.child("asisten").getValue();
                    Log.e("asisten","ada ga "+dataSnapshot.child("asisten").getValue());
                    if(flag_asdos){
                        mStaticAsdos.setVisibility(View.VISIBLE);
                    }else {
                        mStaticAsdos.setVisibility(View.GONE);
                    }
                }else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        queryMatkul();
        queryAsdos();
        onClickRecyclerMatkul();



        return v;

    }




    private void onClickRecyclerMatkul(){
        mRecycleView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), mRecycleView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                MataKuliah matkul = listMatkul.get(position);
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                intent.putExtra("keys", keys.get(position));
                intent.putExtra("flag", "asd");

                intent.putExtra("namamatkul", namaMatkulPut.get(position));
                intent.putExtra("jammatkul", jamMatkulPut.get(position));
                intent.putExtra("daymatkul", dayMatkulPut.get(position));
                intent.putExtra("bobotmatkul", bobotMatkulPut.get(position));
                intent.putExtra("kelasmatkul", kelasMatkulPut.get(position));
                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }




    private void queryMatkul(){
        mataKuliahRef.child(users.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listMatkul.clear();
                keys.clear();
                bobot=0;
                mProgressBar.setVisibility(View.GONE);

                if (dataSnapshot.exists()){
                    for(DataSnapshot dataHasil : dataSnapshot.getChildren()){
                        final String idCourses = dataHasil.getKey();
                        Log.e("anjs",""+dataSnapshot.getChildrenCount());


                        mTotalMatkul.setText(""+dataSnapshot.getChildrenCount());


                        keys.add(idCourses);

                        FirebaseMessaging.getInstance().subscribeToTopic(idCourses);

                        courseRef.child(idCourses).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot2) {
                                MataKuliah matkul = dataSnapshot2.getValue(MataKuliah.class);
                                bobot = bobot + matkul.getBobot();
                                mBobot.setText(""+bobot);
                                mStaticMatkul.setVisibility(View.GONE);



                                // populate array for putextra to detailactivity
                                namaMatkulPut.add(matkul.getName());
                                bobotMatkulPut.add(String.valueOf(matkul.getBobot()));
                                jamMatkulPut.add(matkul.getJam());
                                dayMatkulPut.add(matkul.getDay());
                                kelasMatkulPut.add(matkul.getKelas());



                                listMatkul.add(matkul);
                                mAdapter.notifyDataSetChanged();
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                }else{
                    mProgressBar.setVisibility(View.GONE);
                    Log.e("anjs","else "+dataSnapshot.getChildrenCount());
                    if(dataSnapshot.getChildrenCount() == 0){
                        mTotalMatkul.setText("-");
                        mBobot.setText("-");
                        mStaticMatkul.setVisibility(View.VISIBLE);

                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void queryAsdos(){

        asdosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listAsdos.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot dataHasil : dataSnapshot.getChildren()){

                        if(dataHasil.child(users.getUid()).exists()){
                            flagAsdos=true;

                            final String idCourse = (String) dataHasil.getKey();
                            courseRef.child(dataHasil.getKey()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        ListAsdos asdos = dataSnapshot.getValue(ListAsdos.class);
                                        asdos.setIdCourse(idCourse);



                                        // populate array for putextra to detailactivity


                                        listAsdos.add(asdos);
                                        mAdapter2.notifyDataSetChanged();
                                    }else{

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });



                        }else{
                            Log.e("datahasilnya","masuk beda uid");

                            flagAsdos=false;
                            //berarti bukan asdos

                        }

                    }
                }else{
                    //tidak ada matkul asdos
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void OpenEqu(){
        Intent intent = new Intent(getActivity(), EquActivity.class);
        startActivity(intent);
    }

}
