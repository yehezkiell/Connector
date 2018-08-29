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
    private DatabaseReference userRef;
    private FirebaseUser users;
    private DatabaseReference mataKuliahRef;
    private DatabaseReference daftarTugasRef;
    private DatabaseReference mTugasRef;
    private boolean loaded;
    private int i=0;






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

        LoadData();



        return v;
    }


    private void LoadData(){
//        listTugas.clear();
//        loaded=false;
        listTugas.clear();
        userRef.child(users.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                final ArrayList<Integer> test1 = new ArrayList<>();
//                final ArrayList<Integer> test2 = new ArrayList<>();
                if (dataSnapshot.exists()){
                    Log.e("coba","if pertama");

                    for(final DataSnapshot dataHasil : dataSnapshot.getChildren()){
                        Log.e("datahasil",""+dataHasil.getValue());
                        final String name = (String) dataHasil.getValue();

                        final String idCourses = dataHasil.getKey();

                        mTugasRef.child(idCourses).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(final DataSnapshot dataSnapshot2) {
                                listTugas.clear();
                                if(dataSnapshot2.exists()) {

                                    Log.e("coba","if kedua");

//                                    test1.add(1);
                                    for (DataSnapshot dataHasil2 : dataSnapshot2.getChildren()) {
                                        Log.e("coba","loop kedua");
                                        final String idTugas = dataHasil2.getKey();
                                        daftarTugasRef.child(idTugas).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot3) {
                                                //cek status 0, pgl lod data true
                                                Log.e("coba","hasil");
                                                DaftarTugas tugas = new DaftarTugas();
                                                tugas = dataSnapshot3.getValue(DaftarTugas.class);
                                                listTugas.add(tugas);
//                                                test2.add(1);
//                                                if(test1.size() == test2.size()){
                                                    mAdapter.notifyDataSetChanged();
//                                                    set true
//                                                    if(loaded){
//                                                        LoadData();
//                                                    }
//                                                    loaded = true;
//                                                    Log.e("coba","masuk count dan hrus paling blkng");
//                                                }



                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    //cek loop seleseai
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
