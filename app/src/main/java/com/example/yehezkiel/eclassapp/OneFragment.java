package com.example.yehezkiel.eclassapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
    private ArrayList<String> keys = new ArrayList<>();
    private ArrayList<String> namaMatkulPut = new ArrayList<>();
    private ArrayList<String> dayMatkulPut = new ArrayList<>();
    private ArrayList<String> jamMatkulPut = new ArrayList<>();
    private ArrayList<String> bobotMatkulPut = new ArrayList<>();
    private ArrayList<String> kelasMatkulPut = new ArrayList<>();
    private myAdapter myAdapter;
    private FirebaseAuth.AuthStateListener authListener;
    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
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

    //hitung total SKS recyclerview header
    private int bobot;
    private TextView mBobot;
    //hitung total matakuliah recyclerview header
    private int total_matkul;
    private TextView mTotalMatkul;
    private View loadingView;
    private View emptyView;
    private View errorView;





    public OneFragment() {
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
        v =  inflater.inflate(R.layout.fragment_one, container, false);
        userRef = FirebaseDatabase.getInstance().getReference("users");
        users = FirebaseAuth.getInstance().getCurrentUser();
        mataKuliahRef = FirebaseDatabase.getInstance().getReference("user_course");
        courseRef = FirebaseDatabase.getInstance().getReference("courses");

        //Recycler View
        //Recycler View
        mRecycleView = (RecyclerView) v.findViewById(R.id.MainRView);
        mRecycleView.setHasFixedSize(true);


        mProgressBar = (ProgressBar) v.findViewById(R.id.progressBar2);
        mProgressBar.setVisibility(View.VISIBLE);


        mBobot = (TextView) v.findViewById(R.id.bobot);
        mTotalMatkul = (TextView) v.findViewById(R.id.total_matkul);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecycleView.setLayoutManager(mLayoutManager);
        RecyclerViewHeader header = (RecyclerViewHeader) v.findViewById(R.id.header2);
        header.attachTo(mRecycleView);
        mAdapter = new myAdapter(listMatkul);

        loadingView = getLayoutInflater().inflate(R.layout.view_loading, mRecycleView, false);


        mRecycleView.setAdapter(mAdapter);
        //add margin between item in recyclerview
        mRecycleView.addItemDecoration(new LayoutMarginDecoration(1,15));




        mataKuliahRef.child(users.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listMatkul.clear();
                keys.clear();

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

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        mRecycleView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), mRecycleView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                MataKuliah matkul = listMatkul.get(position);
                Toast.makeText(getActivity(), keys.get(position) + " is selected!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("keys", keys.get(position));
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



        return v;

    }
}
