package com.example.yehezkiel.eclassapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ThreeFragment extends Fragment {
    View v;
    private List<DaftarPengumuman> listPengumuman = new ArrayList<>();
    private ArrayList<String> obj3 = new ArrayList<>();
    List<ListItem> consolidatedList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter adapter;
    private FirebaseAuth mAuth;

    private DatabaseReference userRef;
    private FirebaseUser users;
    private DatabaseReference mataKuliahRef;
    private DatabaseReference daftarPengumumanRef;

    public ThreeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v =  inflater.inflate(R.layout.fragment_three, container, false);

        userRef = FirebaseDatabase.getInstance().getReference("users");
        users = FirebaseAuth.getInstance().getCurrentUser();
        mataKuliahRef = FirebaseDatabase.getInstance().getReference("courses");
        daftarPengumumanRef = FirebaseDatabase.getInstance().getReference("pengumuman");

        mAuth = FirebaseAuth.getInstance();

        mRecyclerView = (RecyclerView) v.findViewById(R.id.PengumumanRView);
        mRecyclerView.setHasFixedSize(true);

        adapter = new PengumumanAdapter(getActivity().getApplicationContext(), consolidatedList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);


        userRef.child(users.getUid()).child("courses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                obj3.clear();
                if(dataSnapshot.exists()){
                    long datacount = dataSnapshot.getChildrenCount();
                    int i = 0;
                    for(DataSnapshot dataHasil : dataSnapshot.getChildren()){
                        final String idCourse =  dataHasil.getKey();
                        obj3.add(idCourse);
                        if(datacount-1 == i){
                            queryObj3();
                        }
                        i++;
                    }
                }else {

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        HashMap<String, List<DaftarPengumuman>> groupedHashMap = groupDataIntoHashMap(listPengumuman);

        for (String nama_p : groupedHashMap.keySet()) {
            PengumumanItem nameItem = new PengumumanItem();
            nameItem.setNama_matkul(nama_p);
            consolidatedList.add(nameItem);


            for (DaftarPengumuman daftarPengumuman : groupedHashMap.get(nama_p)) {
                GeneralItem generalItem = new GeneralItem();
                generalItem.setDaftarPengumuman(daftarPengumuman);//setBookingDataTabs(bookingDataTabs);
                consolidatedList.add(generalItem);
            }
        }

        return v;
    }

    public void queryObj3(){
        for(int j = 0 ; j<obj3.size() ;j++){
            mataKuliahRef.child(obj3.get(j)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        if (dataSnapshot.hasChild("pengumuman")) {
                            final String name = (String) dataSnapshot.child("name").getValue();
                            for (DataSnapshot idKeyPeng : dataSnapshot.child("pengumuman").getChildren()) {

                                    daftarPengumumanRef.child(idKeyPeng.getKey()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot2) {
                                            DaftarPengumuman pengumuman = new DaftarPengumuman();
                                            pengumuman = dataSnapshot2.getValue(DaftarPengumuman.class);
                                            pengumuman.setNama_p(name);
                                            listPengumuman.add(pengumuman);

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });


                            }

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private HashMap<String, List<DaftarPengumuman>> groupDataIntoHashMap(List<DaftarPengumuman> listOfDaftarPengumuman) {

        HashMap<String, List<DaftarPengumuman>> groupedHashMap = new HashMap<>();

        for (DaftarPengumuman daftarPengumuman : listOfDaftarPengumuman) {

            String hashMapKey = daftarPengumuman.getNama_p();

            if (groupedHashMap.containsKey(hashMapKey)) {
                // The key is already in the HashMap; add the pojo object
                // against the existing key.
                groupedHashMap.get(hashMapKey).add(daftarPengumuman);
            } else {
                // The key is not there in the HashMap; create a new key-value pair
                List<DaftarPengumuman> list = new ArrayList<>();
                list.add(daftarPengumuman);
                groupedHashMap.put(hashMapKey, list);
            }
        }


        return groupedHashMap;
    }



}

