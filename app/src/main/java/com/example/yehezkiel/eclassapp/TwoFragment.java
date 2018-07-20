package com.example.yehezkiel.eclassapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
    FirebaseUser users = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference mataKuliahRef = FirebaseDatabase.getInstance().getReference("courses");
    DatabaseReference daftarTugasRef = FirebaseDatabase.getInstance().getReference("tugas");


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



        //Recycler View
        mRecycleView = (RecyclerView) v.findViewById(R.id.TugasRView);
        mRecycleView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mAdapter = new myAdapterTugas(listTugas);
        mRecycleView.setAdapter(mAdapter);



        Bundle bundle = this.getArguments();
        if(getArguments()!=null)
        {
            obj2 = bundle.getStringArrayList("keys");
            Log.e("nba",obj2.toString());
        }


        for(int i = 0;i<obj2.size();i++){
            final int finalI = i;
            listTugas.clear();
            mataKuliahRef.child(obj2.get(i)).addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.hasChild("tugas")){

                        final String name = (String) dataSnapshot.child("name").getValue();
                        for(DataSnapshot data1 : dataSnapshot.child("tugas").getChildren()){

                            Log.e("wasu", data1.getKey());
                          daftarTugasRef.child(data1.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                              @Override

                              public void onDataChange(DataSnapshot dataSnapshot2) {
                                      DaftarTugas tugas = new DaftarTugas();
                                      tugas = dataSnapshot2.getValue(DaftarTugas.class);
                                      tugas.setName(name);
                                      listTugas.add(tugas);
                                      mAdapter.notifyDataSetChanged();
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


        return v;
    }
}
