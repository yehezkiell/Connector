package com.example.yehezkiel.eclassapp;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.readystatesoftware.viewbadger.BadgeView;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
    private DatabaseReference mFlagPengumuman;

    private boolean loaded;
    private SwipeRefreshLayout swipeLayout;
    private int bobot;
    private TextView mJumlahPengumuman;
    private ProgressBar mProgressBar;


    //send extra
    private ArrayList<String> namaMatkulPut = new ArrayList<>();
    private ArrayList<String> namaPengumumanPut = new ArrayList<>();
    private ArrayList<String> judulPengumumanPut = new ArrayList<>();
    private ArrayList<String> tanggalPengumumanPut = new ArrayList<>();
    private ArrayList<String> deskripsiPengumumanPut = new ArrayList<>();
    BadgeView badge3;
    public Counter counter;
    int asd;
    private TextView mStaticPengumuman;



    public ThreeFragment() {
        // Required empty public constructor
    }




    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            userRef = FirebaseDatabase.getInstance().getReference("user_course");
            users = FirebaseAuth.getInstance().getCurrentUser();
            mataKuliahRef = FirebaseDatabase.getInstance().getReference("courses");
            daftarPengumumanRef = FirebaseDatabase.getInstance().getReference("pengumuman");
            pengumumanRef = FirebaseDatabase.getInstance().getReference("pengumuman_course");
            mFlagPengumuman = FirebaseDatabase.getInstance().getReference("flag_pengumuman");
            LoadDataPengumuman();
        } else {


        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }




    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        v =  inflater.inflate(R.layout.fragment_three, container, false);


        userRef = FirebaseDatabase.getInstance().getReference("user_course");
        users = FirebaseAuth.getInstance().getCurrentUser();
        mataKuliahRef = FirebaseDatabase.getInstance().getReference("courses");
        daftarPengumumanRef = FirebaseDatabase.getInstance().getReference("pengumuman");
        pengumumanRef = FirebaseDatabase.getInstance().getReference("pengumuman_course");
        mFlagPengumuman = FirebaseDatabase.getInstance().getReference("flag_pengumuman");

        mProgressBar = (ProgressBar) v.findViewById(R.id.progressBar_pengumuman1);
        mStaticPengumuman = (TextView) v.findViewById(R.id.pengumuman_static);
        mProgressBar.setVisibility(View.VISIBLE);

        View b= ((MainActivity)getActivity()).tabThree;
        badge3 = new BadgeView(getActivity(), b);
        badge3.setBadgeMargin(90,0);
        badge3.setTextSize(10);


        counter = new Counter() {
            @Override
            public void setCounter(int count) {
                Log.e("Ser_Counter", "total countnya " + asd);
                Log.e("Ser_Counter", "masuk count ");
                asd=asd+count;
                if(asd==0){
                    Log.e("Ser_Counter", "masuk asd=0");
                    badge3.hide();

                }else{
                    Log.e("Ser_Counter", "masuk asd=else");

                    badge3.setText(asd + "");
                    badge3.show();
                }

            }
        };





        mJumlahPengumuman = (TextView) v.findViewById(R.id.jumlah_pengumuman);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.PengumumanRView);
        mRecyclerView.setHasFixedSize(true);

        adapter = new myAdapterPengumumanNew(listPengumuman,counter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        RecyclerViewHeader header = (RecyclerViewHeader) v.findViewById(R.id.header3);
        header.attachTo(mRecyclerView);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new LayoutMarginDecoration(1,15));

        swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {

                    @Override public void run() {
                        consolidatedList.clear();
                        listPengumuman.clear();

                        swipeLayout.setRefreshing(false);

                        LoadDataPengumuman();
                    }

                    private void onLoaded() {

                        swipeLayout.setEnabled(true);
                        swipeLayout.setRefreshing(false);
                    }

                }, 1000);
            }
        });

        LoadDataPengumuman();

        return v;
    }


    public interface Counter {
        public void setCounter(int count);
    }





    public void LoadDataPengumuman(){
        Log.e("hashmapnya","ada");
        userRef.child(users.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loaded=false;
                bobot=0;
                asd=0;
                listPengumuman.clear();
                consolidatedList.clear();

                final ArrayList<Integer> test1 = new ArrayList<>();
                final ArrayList<Integer> test2 = new ArrayList<>();
                if (dataSnapshot.exists()){
                    final ArrayList<Integer> test3 = new ArrayList<>();

                    for(final DataSnapshot dataHasil : dataSnapshot.getChildren()){
                        final String idCourses = dataHasil.getKey();
                        final String name = (String) dataSnapshot.child(idCourses).getValue();

                        pengumumanRef.child(idCourses).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(final DataSnapshot dataSnapshot2) {
                                FirebaseMessaging.getInstance().subscribeToTopic(idCourses);

                                if(dataSnapshot2.exists()) {
                                    for (final DataSnapshot dataHasil2 : dataSnapshot2.getChildren()) {
                                        test1.add(1);
                                        final String idTugas = dataHasil2.getKey();
                                        daftarPengumumanRef.child(idCourses).child(idTugas).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot3) {
                                                if(dataSnapshot3.exists()){
                                                    DaftarPengumuman pengumuman = new DaftarPengumuman();
                                                    pengumuman = dataSnapshot3.getValue(DaftarPengumuman.class);
                                                    pengumuman.setNama_p(name);
                                                    pengumuman.setIdCourse(idCourses);
                                                    pengumuman.setIdPengumuman(idTugas);

                                                    test2.add(1);
                                                    bobot= bobot+1;
                                                    mJumlahPengumuman.setText(""+bobot);

                                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                                    try {
                                                        Date mDate = sdf.parse(pengumuman.getTanggal_peng());
                                                        long timeInMilliseconds = mDate.getTime();
                                                        pengumuman.setDate_milisecond(timeInMilliseconds);
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }

                                                    final DaftarPengumuman finalPengumuman = pengumuman;
                                                    final DaftarPengumuman finalPengumuman1 = pengumuman;
                                                    mFlagPengumuman.child(idCourses).child(idTugas).child(users.getUid()).addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            if(dataSnapshot.exists()){
                                                                test3.add(1);
                                                                final String flags = (String) dataSnapshot.getValue();
                                                                finalPengumuman.setFlag(flags);

                                                                if(dataSnapshot.getValue().equals("a")){
                                                                    counter.setCounter(+1);

                                                                }


                                                                listPengumuman.add(finalPengumuman1);

                                                                //query triger data normal
                                                                if (test1.size() == test2.size() && test3.size()==test2.size() && test3.size()==test1.size() ) {
                                                                    Collections.sort(listPengumuman, new Comparator<DaftarPengumuman>() {
                                                                        public int compare(DaftarPengumuman o1, DaftarPengumuman o2) {
                                                                            if (o1.getDate_milisecond() > o2.getDate_milisecond()) {
                                                                                return -1;

                                                                            } else if (o1.getDate_milisecond() < o2.getDate_milisecond()) {

                                                                                return 1;
                                                                            } else {
                                                                                return 0;
                                                                            }
                                                                        }
                                                                    });
                                                                    hashMap();

                                                                    mStaticPengumuman.setVisibility(View.GONE);


                                                                    if(loaded){
                                                                        test2.clear();
                                                                        LoadDataPengumuman();
                                                                    }
                                                                    loaded=true;
                                                                }

                                                                //query triger flag

                                                                if (test1.size() == test2.size() && test3.size()-1==test2.size() && test3.size()-1==test1.size() ) {

                                                                    Collections.sort(listPengumuman, new Comparator<DaftarPengumuman>() {
                                                                        public int compare(DaftarPengumuman o1, DaftarPengumuman o2) {
                                                                            if (o1.getDate_milisecond() > o2.getDate_milisecond()) {
                                                                                return -1;

                                                                            } else if (o1.getDate_milisecond() < o2.getDate_milisecond()) {

                                                                                return 1;
                                                                            } else {
                                                                                return 0;
                                                                            }
                                                                        }
                                                                    });
                                                                    hashMap();

//                                                                    if(count==0){
//                                                                        badge3.setText(null);
//                                                                        badge3.show();
//                                                                    }
//                                                                    else{
//                                                                        badge3.setText(""+count);
//                                                                        badge3.show();
//                                                                    }
                                                                    mStaticPengumuman.setVisibility(View.GONE);


                                                                    if(loaded){
                                                                        test3.clear();

                                                                        LoadDataPengumuman();
                                                                    }
                                                                    loaded=true;
                                                                }

                                                            }else {
                                                                test2.add(1);
                                                                test3.add(1);
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });




                                                }else{
                                                    mProgressBar.setVisibility(View.GONE);


                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    }//end of loop for datasnapshot 2
                                }else{
                                    mStaticPengumuman.setVisibility(View.VISIBLE);
                                    mProgressBar.setVisibility(View.GONE);
                                    mJumlahPengumuman.setText("-");
                                }
                            }



                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                    }// end of looping datasnapshot1
                }else{
                    if(dataSnapshot.getChildrenCount() == 0){
                        mStaticPengumuman.setVisibility(View.VISIBLE);
                        mProgressBar.setVisibility(View.GONE);

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ViewEvent viewEvent) {
        if (viewEvent.getMessage().equals("loadDataPengumuman")) {
            LoadDataPengumuman();
        }
    }




    private HashMap<String, List<DaftarPengumuman>> groupDataIntoHashMap(List<DaftarPengumuman> listOfDaftarPengumuman) {

        HashMap<String, List<DaftarPengumuman>> groupedHashMap = new HashMap<>();

        for (DaftarPengumuman daftarPengumuman : listOfDaftarPengumuman) {

            String hashMapKey = daftarPengumuman.getTanggal_peng();

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

        for (String tanggal_peng : groupedHashMap.keySet()) {
            PengumumanItem nameItem = new PengumumanItem();
            nameItem.setTanggal_peng(tanggal_peng);
            consolidatedList.add(nameItem);



            for (DaftarPengumuman daftarPengumuman : groupedHashMap.get(tanggal_peng)) {
                GeneralItem generalItem = new GeneralItem();
                generalItem.setDaftarPengumuman(daftarPengumuman);//setBookingDataTabs(bookingDataTabs);
                consolidatedList.add(generalItem);
            }
        }

        mProgressBar.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();


    }


}

