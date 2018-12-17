package com.example.yehezkiel.eclassapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
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




    private DatabaseReference userRef;
    private DatabaseReference nilaiRef ;
    private DatabaseReference nilaiInfoRef ;
    private FirebaseUser users;
    private List<NilaiMatakuliah> listNilai = new ArrayList<>();

    private ProgressBar progressBar_nilai;


    private TextView static_null,static_graph,static_nilai,nilai_akhir,capaian_maksimal,huruf_nilai_akhir,huruf_capaian_maksimal;

    //recyclerview
    private RecyclerView mRecycleView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private float bobot;
    private float bobotMath;
    private PieChart pieChart;
    private double temp;
    private int jumlah;
    double hasilAkhir = Float.valueOf(0);
    String hasilAkhirChar,bobotAkhirChar;

    private LinearLayout nilai_akhir_layout,graph_layout;

    double mathAkhir;
    double jumlahMath;
    int count;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_nilai, contentFrameLayout);

        final Intent intent = getIntent();
        final String keys = intent.getStringExtra("keys");
        String flag = intent.getStringExtra("flag");
        setTitle("Nilai");


        userRef = FirebaseDatabase.getInstance().getReference("users");
        users = FirebaseAuth.getInstance().getCurrentUser();
        nilaiRef = FirebaseDatabase.getInstance().getReference("nilai_course");
        nilaiInfoRef = FirebaseDatabase.getInstance().getReference("keterangan_nilai");

//        pie chart setting
        pieChart = (PieChart) findViewById(R.id.piechart_nilai);


        static_graph = (TextView) findViewById(R.id.static_graph);
        static_nilai = (TextView) findViewById(R.id.nilai_layout);

        mRecycleView = (RecyclerView) findViewById(R.id.recycler_nilai);
        mRecycleView.setHasFixedSize(true);

        nilai_akhir_layout = (LinearLayout) findViewById(R.id.nilai_akhir_layout);
        graph_layout = (LinearLayout) findViewById(R.id.graph_layout);

        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecycleView.setLayoutManager(mLayoutManager);

        progressBar_nilai = (ProgressBar) findViewById(R.id.progressBar_Nilai);
        static_null = (TextView) findViewById(R.id.static_null);

        mAdapter = new myAdapterNilai(listNilai);
        mRecycleView.setAdapter(mAdapter);
        mRecycleView.addItemDecoration(new LayoutMarginDecoration(1, 15));

        nilai_akhir = (TextView) findViewById(R.id.nilai_akhir);
        capaian_maksimal = (TextView) findViewById(R.id.capaian_maksimal);
        huruf_nilai_akhir = (TextView) findViewById(R.id.huruf_nilai_akhir);
        huruf_capaian_maksimal = (TextView) findViewById(R.id.huruf_capaian_maksimal);



        final ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();
        final Legend pieLegend = pieChart.getLegend();
        final PieDataSet dataSet = new PieDataSet(yvalues, "");


        if(flag == null){
            QueryNilai(keys,yvalues,dataSet,pieLegend);
        }else {


            if (flag.equals("asdos")) {
                nilai_akhir_layout.setVisibility(View.GONE);
                graph_layout.setVisibility(View.GONE);
                nilaiRef.child(keys).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot nilaiHasil : dataSnapshot.getChildren()) {
                                final String nilaiHasilKey = nilaiHasil.getKey();

                                nilaiInfoRef.child(keys).child(nilaiHasilKey).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        NilaiMatakuliah nilai = dataSnapshot.getValue(NilaiMatakuliah.class);
                                        listNilai.add(nilai);
                                        nilai.setKeys(nilaiHasilKey);
                                        nilai.setFlag("asdos");
                                        nilai.setNamaMatkul(keys);
                                        progressBar_nilai.setVisibility(View.GONE);
                                        mAdapter.notifyDataSetChanged();


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }


                        } else {
                            //tidak ada nilai
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } else {

                QueryNilai(keys,yvalues,dataSet,pieLegend);

            }
        }



    }

    private void QueryNilai(final String keys, final ArrayList<PieEntry> yvalues, final PieDataSet dataSet, final Legend pieLegend){

        nilaiRef.child(keys).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listNilai.clear();
                yvalues.clear();
                final ArrayList<Integer> test1 = new ArrayList<>();
                final ArrayList<Integer> test2 = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataHasil : dataSnapshot.getChildren()) {


                        if (dataHasil.child(users.getUid()).exists()) {
                            final String nilai_temp = (String) dataHasil.child(users.getUid()).getValue().toString();


                            final String nilai_key = dataHasil.getKey();
                            final double etKids = Double.parseDouble(nilai_temp);

                            test1.add(1);
                            nilaiInfoRef.child(keys).child(dataHasil.getKey()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        jumlah++;

                                        NilaiMatakuliah nilai = dataSnapshot.getValue(NilaiMatakuliah.class);
                                        nilai.setNilai(etKids);
                                        nilai.setKeys(nilai_key);
                                        nilai.setFlag("asdos");
                                        nilai.setNamaMatkul(keys);
                                        nilai.setFlag("-");

                                        listNilai.add(nilai);
                                        test2.add(1);


                                        if (etKids == 0) {
                                            yvalues.add(new PieEntry(Float.parseFloat(String.valueOf(nilai.getSkala())), nilai.getNama()));
                                            int newcolor = Color.GRAY;
                                            dataSet.addColor(newcolor);

                                        } else {
                                            yvalues.add(new PieEntry(Float.parseFloat(etKids * (nilai.getBobot() / 100) + "f"), nilai.getNama()));
                                            hasilAkhir = hasilAkhir + etKids * (nilai.getBobot() / 100);
                                            bobotMath = (float) (nilai.getBobot() - hasilAkhir);

                                            bobot = (float) (bobot+nilai.getBobot());
                                            nilai_akhir.setText(""+hasilAkhir);
                                            capaian_maksimal.setText(""+bobot+"%");

                                            if (hasilAkhir <= 100 && hasilAkhir >= 85) {
                                                hasilAkhirChar = "A";
                                            } else if (hasilAkhir < 85 && hasilAkhir >= 75) {
                                                hasilAkhirChar = "A-";
                                            } else if (hasilAkhir < 75 && hasilAkhir >= 70) {
                                                hasilAkhirChar = "B";
                                            } else if (hasilAkhir < 70 && hasilAkhir >= 65) {
                                                hasilAkhirChar = "B-";
                                            } else if (hasilAkhir < 65 && hasilAkhir >= 60) {
                                                hasilAkhirChar = "C+";
                                            } else if (hasilAkhir < 60 && hasilAkhir >= 55) {
                                                hasilAkhirChar = "C";
                                            } else if (hasilAkhir < 55 && hasilAkhir >= 45) {
                                                hasilAkhirChar = "D";
                                            } else {
                                                hasilAkhirChar = "E";
                                            }

                                            if (bobot <= 100 && bobot >= 85) {
                                                bobotAkhirChar = "A";
                                            } else if (bobot < 85 && bobot >= 75) {
                                                bobotAkhirChar = "A-";
                                            } else if (bobot < 75 && bobot >= 70) {
                                                bobotAkhirChar = "B";
                                            } else if (bobot < 70 && bobot >= 65) {
                                                bobotAkhirChar = "B-";
                                            } else if (bobot < 65 && bobot >= 60) {
                                                bobotAkhirChar = "C+";
                                            } else if (bobot < 60 && bobot >= 55) {
                                                bobotAkhirChar = "C";
                                            } else if (bobot < 55 && bobot >= 45) {
                                                bobotAkhirChar = "D";
                                            } else {
                                                bobotAkhirChar = "E";
                                            }

                                            huruf_nilai_akhir.setText(""+hasilAkhirChar);
                                            huruf_capaian_maksimal.setText(""+bobotAkhirChar);

                                        }

                                        if (test1.size() == test2.size()) {
                                            pieChart.getDescription().setText("Jumlah : " + jumlah);
                                            hasilAkhir = 0;
                                            bobot=0;
                                            bobotMath=0;
                                            jumlah=0;
                                            pieChart.setExtraOffsets(5, 10, 5, 5);
                                            pieChart.setDragDecelerationFrictionCoef(0.9f);
                                            pieChart.setTransparentCircleRadius(61f);
                                            pieChart.setHoleColor(Color.WHITE);
                                            pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);


                                            if (yvalues.contains(0))
                                                dataSet.setSliceSpace(3f);
                                            dataSet.setSelectionShift(5f);

                                            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                                            PieData pieData = new PieData((dataSet));
                                            pieData.setValueTextSize(14f);
                                            pieData.setValueTextColor(Color.WHITE);

                                            pieLegend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
                                            pieLegend.setWordWrapEnabled(true);
                                            pieChart.setEntryLabelColor(Color.BLACK);
                                            pieChart.setCenterText("" + hasilAkhirChar);
                                            pieChart.setCenterTextSize(40f);
                                            pieData.setValueFormatter(new myValueFormater());

                                            pieChart.setCenterTextColor(Color.BLUE);
                                            pieChart.setData(pieData);

                                            progressBar_nilai.setVisibility(View.GONE);
                                            mAdapter.notifyDataSetChanged();

                                        }


                                    } else {
                                        Log.e("nilai", "masuk else 1");


                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        } else {
                            if (dataSnapshot.getChildrenCount() == 0) {
                                Log.e("nilai", "masuk else 2");
                                progressBar_nilai.setVisibility(View.GONE);
                                static_null.setVisibility(View.VISIBLE);
                                huruf_nilai_akhir.setText("-");
                                nilai_akhir.setText("-");
                                capaian_maksimal.setText("-");
                                huruf_capaian_maksimal.setText("-");

                            }


                        }


                    }
                } else {
                    progressBar_nilai.setVisibility(View.GONE);
                    static_null.setVisibility(View.VISIBLE);
                    huruf_nilai_akhir.setText("-");
                    nilai_akhir.setText("-");
                    capaian_maksimal.setText("-");
                    huruf_capaian_maksimal.setText("-");


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
