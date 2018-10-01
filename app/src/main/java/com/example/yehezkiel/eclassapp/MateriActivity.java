package com.example.yehezkiel.eclassapp;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MateriActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {


    private List<String> key = new ArrayList<>();
    private List<MateriKuliah> listMateri = new ArrayList<>();


    private myAdapter myAdapter;
    private FirebaseAuth.AuthStateListener authListener;
    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference userRef;
    private DatabaseReference materiRef ;
    private FirebaseUser users;
    private DatabaseReference courseRef;
    private String keys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_materi, contentFrameLayout);

        setTitle("Materi");

        int REQUEST_CODE=1;

        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, REQUEST_CODE);



        userRef = FirebaseDatabase.getInstance().getReference("user_course");
        users = FirebaseAuth.getInstance().getCurrentUser();
        materiRef = FirebaseDatabase.getInstance().getReference("materi_course");

        //Recycler View
        mRecycleView = (RecyclerView) findViewById(R.id.recycler_materi);
        mRecycleView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecycleView.setLayoutManager(mLayoutManager);


        mAdapter = new myAdapterMateri(listMateri);
        mRecycleView.setAdapter(mAdapter);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);






        final Spinner areaSpinner = (Spinner) findViewById(R.id.spinner);

        //get intent from activity
        Intent intent = getIntent();
        keys = intent.getStringExtra("keys");
        Log.e("spinnnn","" +keys);



        areaSpinner.setOnItemSelectedListener(this);





        userRef.child(users.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot dataHasil :dataSnapshot.getChildren()){
                        String nama_matkul = (String) dataHasil.getValue();
                        key.add(nama_matkul);
                        final String idCourses = dataHasil.getKey();



                    }
                    ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(MateriActivity.this, android.R.layout.simple_spinner_item, key);
                    areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);



                    areaSpinner.setAdapter(areasAdapter);

                    //set spinner from detail activity
                    areaSpinner.setSelection(areasAdapter.getPosition(keys));





                }else{

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mRecycleView.addOnItemTouchListener(new RecyclerTouchListener(this, mRecycleView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                final MateriKuliah materi = listMateri.get(position);



                FirebaseStorage storage = FirebaseStorage.getInstance();
                final StorageReference httpsReference = storage.getReferenceFromUrl(materi.getUrl());

                if(isNetworkStatusAvialable (getApplicationContext())) {
                    new SweetAlertDialog(MateriActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Download File ?")
                            .setContentText("File name : "+ materi.getNama())
                            .setConfirmText("Yes, Download It")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(final SweetAlertDialog sDialog) {
                                    httpsReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                                        @Override
                                        public void onSuccess(final StorageMetadata storageMetadata) {



                                            sDialog
                                                    .setTitleText("Downloading...")
                                                    .setContentText("Check your notification bar !")
                                                    .setConfirmText("OK")
                                                    .setConfirmClickListener(null)
                                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);


                                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(materi.getUrl()));
                                            request.setDescription("Downloading...");
                                            request.setTitle(materi.getNama());

                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                                request.allowScanningByMediaScanner();
                                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                            }
                                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, ""+storageMetadata.getName());
                                            DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                                            manager.enqueue(request);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            sDialog
                                                    .setTitleText("Download Error!")
                                                    .setContentText("Please Wait...")
                                                    .setConfirmText("OK")
                                                    .setConfirmClickListener(null)
                                                    .changeAlertType(SweetAlertDialog.ERROR_TYPE);



                                        }
                                    });




                                }
                            })
                            .show();

                } else {
                    new SweetAlertDialog(MateriActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Check your internet Connection !")
                            .show();
                }









            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));



    }

    public static boolean isNetworkStatusAvialable (Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
        {
            NetworkInfo netInfos = connectivityManager.getActiveNetworkInfo();
            if(netInfos != null)
                if(netInfos.isConnected())
                    return true;
        }
        return false;
    }




    @Override
    public void onItemSelected(AdapterView<?> parent, final View view, int position, long l) {
        final String item = parent.getItemAtPosition(position).toString();
        listMateri.clear();

        materiRef.child(item).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot dataHasil : dataSnapshot.getChildren()){

                        MateriKuliah materi = dataHasil.getValue(MateriKuliah.class);
                        listMateri.add(materi);
                        mAdapter.notifyDataSetChanged();

                    }
                }else{
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
