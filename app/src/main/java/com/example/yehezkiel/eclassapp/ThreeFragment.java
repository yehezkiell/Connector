package com.example.yehezkiel.eclassapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ThreeFragment extends Fragment {
    View v;
    private List<DaftarPengumuman> listPengumuman = new ArrayList<>();
    private ArrayList<String> obj3 = new ArrayList<>();
    private ArrayList<String> listTugasKey = new ArrayList<>();
    List<ListItem> consolidatedList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter adapter;
    private FirebaseAuth mAuth;

    private DatabaseReference userRef;
    private FirebaseUser users;
    private DatabaseReference mataKuliahRef;
    private DatabaseReference daftarPengumumanRef;
    private DatabaseReference pengumumanRef;

    private boolean loaded;
    private SwipeRefreshLayout swipeLayout;
    private int bobot;
    private TextView mJumlahPengumuman;

    //send extra
    private ArrayList<String> namaMatkulPut = new ArrayList<>();
    private ArrayList<String> namaPengumumanPut = new ArrayList<>();
    private ArrayList<String> judulPengumumanPut = new ArrayList<>();
    private ArrayList<String> tanggalPengumumanPut = new ArrayList<>();
    private ArrayList<String> deskripsiPengumumanPut = new ArrayList<>();




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

        userRef = FirebaseDatabase.getInstance().getReference("user_course");
        users = FirebaseAuth.getInstance().getCurrentUser();
        mataKuliahRef = FirebaseDatabase.getInstance().getReference("courses");
        daftarPengumumanRef = FirebaseDatabase.getInstance().getReference("pengumuman");
        pengumumanRef = FirebaseDatabase.getInstance().getReference("pengumuman_course");

//        swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);

//        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                new Handler().postDelayed(new Runnable() {
//
//                    @Override public void run() {
//                        swipeLayout.setRefreshing(false);
////                        listPengumuman.clear();
////                        consolidatedList.clear();
////                        LoadDataPengumuman();
//
//                    }
//                    private void onLoaded() {
//                        swipeLayout.setEnabled(true);
//                        swipeLayout.setRefreshing(false);
//                    }
//
//                }, 500);
//            }
//        });



        mJumlahPengumuman = (TextView) v.findViewById(R.id.jumlah_pengumuman);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.PengumumanRView);
        mRecyclerView.setHasFixedSize(true);

        adapter = new PengumumanAdapter(getActivity().getApplicationContext(), consolidatedList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        RecyclerViewHeader header = (RecyclerViewHeader) v.findViewById(R.id.header3);
        header.attachTo(mRecyclerView);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new LayoutMarginDecoration(1,15));


        LoadDataPengumuman();

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                if(consolidatedList.size()-1 >= position)
                {
                    GeneralItem generalItem   = (GeneralItem) consolidatedList.get(position);
                    Intent intent = new Intent(getActivity(), DetailPengumuman.class);
                    intent.putExtra("flag", "pengumuman");

                    intent.putExtra("getnama", generalItem.getDaftarPengumuman().getNama_p());

                    intent.putExtra("tanggalpengumuman", generalItem.getDaftarPengumuman().getTanggal_peng());
                    intent.putExtra("judulpengumuman", generalItem.getDaftarPengumuman().getJudul());
                    intent.putExtra("deskripsipengumuman", generalItem.getDaftarPengumuman().getDeskripsi());
                    startActivity(intent);

                }



            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return v;
    }



    private void LoadDataPengumuman(){

        userRef.child(users.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loaded=false;
                listPengumuman.clear();
                consolidatedList.clear();
                final ArrayList<Integer> test1 = new ArrayList<>();
                final ArrayList<Integer> test2 = new ArrayList<>();
                if (dataSnapshot.exists()){
                    for(final DataSnapshot dataHasil : dataSnapshot.getChildren()){
                        final String idCourses = dataHasil.getKey();
                        final String name = (String) dataSnapshot.child(idCourses).getValue();
                        pengumumanRef.child(idCourses).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(final DataSnapshot dataSnapshot2) {
                                if(dataSnapshot2.exists()) {
                                    for (DataSnapshot dataHasil2 : dataSnapshot2.getChildren()) {
                                        test1.add(1);
                                        final String idTugas = dataHasil2.getKey();
                                        daftarPengumumanRef.child(idTugas).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot3) {

                                                if(dataSnapshot3.exists()){
                                                    DaftarPengumuman pengumuman = new DaftarPengumuman();
                                                    pengumuman = dataSnapshot3.getValue(DaftarPengumuman.class);
                                                    pengumuman.setNama_p(name);
                                                    listPengumuman.add(pengumuman);
                                                    test2.add(1);
                                                    bobot= bobot+1;
                                                    mJumlahPengumuman.setText(""+bobot);


                                                    if (test1.size() == test2.size()) {
                                                        hashMap();
                                                        bobot=0;
                                                        if(loaded){
                                                            test2.clear();
                                                            test2.clear();

                                                            LoadDataPengumuman();
                                                        }
                                                        loaded=true;
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

    private void hashMap(){
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
        adapter.notifyDataSetChanged();

    }


}

