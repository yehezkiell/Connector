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
import android.view.Menu;
import android.view.MenuInflater;
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
import java.util.List;


public class TwoFragment extends Fragment {

    View v;
    private List<DaftarTugas> listTugas = new ArrayList<>();

    private ProgressBar mProgressBar4;

    //Recyclerview variable
    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    //firebase variable
    private DatabaseReference userRef;
    private FirebaseUser users;
    private DatabaseReference daftarTugasRef;
    private DatabaseReference mTugasRef;
    private DatabaseReference mFlagTugas;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authListener;

    private boolean loaded;

    private int bobot;
    private TextView mJumlahTugas;

    private ArrayList<String> namaMatkulPut = new ArrayList<>();
    private ArrayList<String> deskripsiTugasPut = new ArrayList<>();
    private ArrayList<String> judulTugasPut = new ArrayList<>();
    private ArrayList<String> tanggalKumpulPut = new ArrayList<>();
    private ArrayList<String> tanggalTugasPut = new ArrayList<>();
    private ArrayList<String> idTugasPut = new ArrayList<>();

    private ProgressBar mProgressBar;
    private ProgressBar mProgressBar2;
    private int jalanBrpKali = 0;
    private TextView mStaticTugas;
    private SwipeRefreshLayout swipeLayout;



    public static View currentTab;
    BadgeView badge;
    public Counter counter;
    int asd;
    public static final String TAG = "TWO_FRAGMENT";


    public TwoFragment() {
        // Required empty public constructor
    }





    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {

        } else {

            mAuth = FirebaseAuth.getInstance();
            userRef = FirebaseDatabase.getInstance().getReference("user_course");
            users = FirebaseAuth.getInstance().getCurrentUser();
            daftarTugasRef = FirebaseDatabase.getInstance().getReference("tugas");
            mTugasRef = FirebaseDatabase.getInstance().getReference("tugas_course");
            mFlagTugas = FirebaseDatabase.getInstance().getReference("flag_tugas");

            LoadData();
        }
    }

//    public void setMenuVisibility(boolean menuVisible) {
//        super.setMenuVisibility(menuVisible);
//        if (menuVisible) {
//            mAuth = FirebaseAuth.getInstance();
//            userRef = FirebaseDatabase.getInstance().getReference("user_course");
//            users = FirebaseAuth.getInstance().getCurrentUser();
//            daftarTugasRef = FirebaseDatabase.getInstance().getReference("tugas");
//            mTugasRef = FirebaseDatabase.getInstance().getReference("tugas_course");
//            mFlagTugas = FirebaseDatabase.getInstance().getReference("flag_tugas");
//
//            LoadData();
//
//        }
//    }


//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mAuth = FirebaseAuth.getInstance();
//            userRef = FirebaseDatabase.getInstance().getReference("user_course");
//            users = FirebaseAuth.getInstance().getCurrentUser();
//            daftarTugasRef = FirebaseDatabase.getInstance().getReference("tugas");
//            mTugasRef = FirebaseDatabase.getInstance().getReference("tugas_course");
//            mFlagTugas = FirebaseDatabase.getInstance().getReference("flag_tugas");
//
//            LoadData();
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        v =  inflater.inflate(R.layout.fragment_two, container, false);
        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("user_course");
        users = FirebaseAuth.getInstance().getCurrentUser();
        daftarTugasRef = FirebaseDatabase.getInstance().getReference("tugas");
        mTugasRef = FirebaseDatabase.getInstance().getReference("tugas_course");
        mFlagTugas = FirebaseDatabase.getInstance().getReference("flag_tugas");

        //Recycler View
        mJumlahTugas = (TextView) v.findViewById(R.id.jumlah_tugas);
        mStaticTugas = (TextView) v.findViewById(R.id.tugas_static);

        View a = ((MainActivity)getActivity()).tabTwo;
        badge = new BadgeView(getActivity(), a);
        badge.setBadgeMargin(75,0);
        badge.setTextSize(10);

        EventBus.getDefault().post(new ViewEvent("updateViews"));

        mProgressBar = (ProgressBar) v.findViewById(R.id.progressBar_tugas1);
        mProgressBar2 = (ProgressBar) v.findViewById(R.id.progressBar_tugas2);

        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar2.setVisibility(View.VISIBLE);
        swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swiperefreshtugas);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override public void run() {
                        LoadData();
                        swipeLayout.setRefreshing(false);
                        listTugas.clear();

                    }
                    private void onLoaded() {
                        swipeLayout.setEnabled(true);
                        swipeLayout.setRefreshing(false);
                    }

                }, 500);
            }
        });






        Log.e("Ser_Counter", "masuk sebelum counter" );

        counter = new Counter() {
            @Override
            public void setCounter(int count) {
                Log.e("Ser_Counter", "total countnya " + asd);
                Log.e("Ser_Counter", "masuk count ");
                asd=asd+count;
                if(asd==0){
                    Log.e("Ser_Counter", "masuk asd=0");
                    badge.hide();

                }else{
                    Log.e("Ser_Counter", "masuk asd=else");

                    badge.setText(asd + "");
                    badge.show();
                }

            }
        };

        mRecycleView = (RecyclerView) v.findViewById(R.id.TugasRView);
        mRecycleView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecycleView.setLayoutManager(mLayoutManager);
        RecyclerViewHeader header = (RecyclerViewHeader) v.findViewById(R.id.header);
        header.attachTo(mRecycleView);
        mAdapter = new myAdapterTugas(listTugas,counter);
        mRecycleView.setAdapter(mAdapter);
        mRecycleView.addItemDecoration(new LayoutMarginDecoration(1,15));

        LoadData();


        return v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

    }


    public interface Counter {
        public void setCounter(int count);
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
        if (viewEvent.getMessage().equals("updateViews")) {
            LoadData();
        }
    }


    public void LoadData(){
        //query ke user course ambil course apa aja yang user ambil

        userRef.child(users.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar2.setVisibility(View.VISIBLE);
                Log.e("masukmana", "0");

                loaded=false;
                asd=0;
                bobot=0;
                listTugas.clear();

                //create counter test1,2,3 to know when query ends
                final ArrayList<Integer> counter1 = new ArrayList<>();
                final ArrayList<Integer> counter2 = new ArrayList<>();

                if (dataSnapshot.exists()){
                    final ArrayList<Integer> counter3 = new ArrayList<>();


                    //Loop to get all courses that users have
                    for(final DataSnapshot dataHasil : dataSnapshot.getChildren()){
                        final String idCourses = dataHasil.getKey();
                        FirebaseMessaging.getInstance().subscribeToTopic(idCourses);

                        queryTugas(idCourses,counter1,counter2,counter3);
                    }// end of looping datasnapshot1
                }else{
                    if(dataSnapshot.getChildrenCount() == 0){
                        noDataStatic();

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void noDataStatic(){
        mJumlahTugas.setText("-");
        mStaticTugas.setVisibility(View.VISIBLE);
        mProgressBar2.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
    }

    private void hideProgressBar(){
        mProgressBar2.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
    }

    public void queryTugas(final String idCourses, final ArrayList<Integer> counter1, final ArrayList<Integer> counter2, final ArrayList<Integer> counter3){
        mTugasRef.child(idCourses).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot2) {

                if(dataSnapshot2.exists()) {
                    mStaticTugas.setVisibility(View.GONE);

                    for (DataSnapshot dataHasil2 : dataSnapshot2.getChildren()) {
                        counter1.add(1);

                        final String idTugas = dataHasil2.getKey();
                        //query to get description of tugas
                        daftarTugasRef.child(idCourses).child(idTugas).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot3) {

                                if (dataSnapshot3.exists()) {

                                    //get the data and send to adapter
                                    DaftarTugas tugas = new DaftarTugas();
                                    tugas = dataSnapshot3.getValue(DaftarTugas.class);
                                    tugas.setIdCourse(idCourses);
                                    tugas.setIdTugas(idTugas);

                                    //format time
                                    formatTime(tugas);


                                    counter2.add(1);
                                    bobot++;
                                    mJumlahTugas.setText("" + bobot);

                                    final DaftarTugas finalTugas = tugas;

                                    queryFlagTugas(idCourses,idTugas,counter1,counter2,counter3,tugas);


                                }else{
                                    hideProgressBar();
                                }

                            }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }//end of loop for datasnapshot 2
                }else{

                    if(dataSnapshot2.getChildrenCount() == 0) {
                        noDataStatic();


                    }
                    hideProgressBar();



                }

            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void formatTime(DaftarTugas tugas){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date mDate = sdf.parse(tugas.getTanggal_tugas());
            long timeInMilliseconds = mDate.getTime();
            tugas.setDate_milisecond(timeInMilliseconds);
        } catch (ParseException e) {
            e.printStackTrace();
        }



    }

    private void queryFlagTugas(String idCourses, String idTugas, final ArrayList<Integer> counter1, final ArrayList<Integer> counter2, final ArrayList<Integer> counter3, final DaftarTugas finalTugas){

        //flag tugas contains course_id - tugas_id - uid:"a/0"
        mFlagTugas.child(idCourses).child(idTugas).child(users.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    counter3.add(1);
                    final String flags = (String) dataSnapshot.getValue();
                    finalTugas.setFlag(flags);
                    listTugas.add(finalTugas);
                    mAdapter.notifyDataSetChanged();


                    //check if new data value=a and set it to interface
                    if(dataSnapshot.getValue().equals("a")){
                        counter.setCounter(+1);
                        Log.e("Ser_Counter","didalem equals");
                    }


                    if (counter1.size() == counter2.size() && counter3.size()==counter2.size() && counter3.size()==counter1.size()){
                        Collections.sort(listTugas, new Comparator<DaftarTugas>() {
                            public int compare(DaftarTugas o1, DaftarTugas o2) {
                                if (o1.getDate_milisecond() > o2.getDate_milisecond()) {
                                    return -1;

                                } else if (o1.getDate_milisecond() < o2.getDate_milisecond()) {

                                    return 1;
                                } else {
                                    return 0;
                                }
                            }
                        });

                        hideProgressBar();
                        mJumlahTugas.setVisibility(View.VISIBLE);
                        mStaticTugas.setVisibility(View.GONE);
                        mAdapter.notifyDataSetChanged();


                        bobot = 0;

                        if (loaded) {
                            counter2.clear();
                            mProgressBar.setVisibility(View.VISIBLE);
                            mProgressBar2.setVisibility(View.VISIBLE);

                            LoadData();
                        }
                        loaded = true;
                    }

                    if (counter1.size() == counter2.size() && counter3.size()-1==counter2.size() && counter3.size()-1==counter1.size()){


                        Collections.sort(listTugas, new Comparator<DaftarTugas>() {
                            public int compare(DaftarTugas o1, DaftarTugas o2) {
                                if (o1.getDate_milisecond() > o2.getDate_milisecond()) {
                                    return -1;

                                } else if (o1.getDate_milisecond() < o2.getDate_milisecond()) {

                                    return 1;
                                } else {
                                    return 0;
                                }
                            }
                        });
                        mJumlahTugas.setVisibility(View.VISIBLE);
                        mAdapter.notifyDataSetChanged();

                        hideProgressBar();

                        bobot = 0;
                        if (loaded) {

                            counter3.clear();
                            mProgressBar.setVisibility(View.VISIBLE);
                            mProgressBar2.setVisibility(View.VISIBLE);
                            LoadData();

                        }
                        loaded = true;
                    }



                } else {
                    counter2.add(1);
                    counter3.add(1);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


    }



}



