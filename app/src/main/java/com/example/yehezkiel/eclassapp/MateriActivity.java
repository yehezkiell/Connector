package com.example.yehezkiel.eclassapp;

import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MateriActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {


    private List<String> key = new ArrayList<>();
    private List<MateriKuliah> listMateri = new ArrayList<>();

    private static final int FILE_SELECT_CODE = 0;

    private myAdapterMatkul myAdapter;
    private FirebaseAuth.AuthStateListener authListener;
    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference userRef;
    private DatabaseReference materiRef;
    private DatabaseReference asdosRef;

    Dialog myDialog;
    private FirebaseUser users;
    private DatabaseReference courseRef;
    private String keys, flag, namaMatkulGet;
    private Button btnMateri;
    private Uri filepath;
    private CheckBox silabus_checkbox;
    private EditText inputNama,inputPertemuan;

    private ProgressBar progressbar_materi;
    private TextView mStaticMateri;
    Button btnUpload;
    TextView upload_path;
    FirebaseStorage storage;
    StorageReference storageReference;
    private Dialog AlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storage = FirebaseStorage.getInstance();

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_materi, contentFrameLayout);

        setTitle("Materi");
        progressbar_materi = (ProgressBar) findViewById(R.id.progressBar_materi);
        mStaticMateri = (TextView) findViewById(R.id.materi_static);
        progressbar_materi.setVisibility(View.VISIBLE);

        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.upload_popup);
        btnMateri = (Button) findViewById(R.id.btnMateri);
        upload_path = myDialog.findViewById(R.id.upload_path);



        int REQUEST_CODE = 1;

        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, REQUEST_CODE);




        userRef = FirebaseDatabase.getInstance().getReference("user_course");
        users = FirebaseAuth.getInstance().getCurrentUser();
        materiRef = FirebaseDatabase.getInstance().getReference("materi_course");
        asdosRef = FirebaseDatabase.getInstance().getReference("asisten_course");

        inputNama = (EditText) myDialog.findViewById(R.id.input_nama);
        inputPertemuan = (EditText) myDialog.findViewById(R.id.input_pertemuan);

        inputNama.addTextChangedListener(materiTextWatcher);
        inputPertemuan.addTextChangedListener(materiTextWatcher);

        //Recycler View
        mRecycleView = (RecyclerView) findViewById(R.id.recycler_materi);
        mRecycleView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecycleView.setLayoutManager(mLayoutManager);




        mAdapter = new myAdapterMateri(listMateri);
        mRecycleView.setAdapter(mAdapter);
        mRecycleView.addItemDecoration(new LayoutMarginDecoration(1,15));

        final Spinner areaSpinner = (Spinner) findViewById(R.id.spinner);

        //get intent from activity
        Intent intent = getIntent();
        keys = intent.getStringExtra("keys");
        flag = intent.getStringExtra("flag");
        namaMatkulGet = intent.getStringExtra("namamatkul");





        Log.e("spinnnn", "" + keys + flag);


        areaSpinner.setOnItemSelectedListener(this);

        if (flag.equals("asdos")) {
            btnMateri.setVisibility(View.VISIBLE);
            areaSpinner.setVisibility(View.GONE);
            materiRef.child(namaMatkulGet).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    listMateri.clear();
                    if(dataSnapshot.exists()){
                        for(DataSnapshot dataHasil : dataSnapshot.getChildren()){

                            MateriKuliah materi = dataHasil.getValue(MateriKuliah.class);
                            listMateri.add(materi);
                            progressbar_materi.setVisibility(View.GONE);
                            Collections.sort(listMateri, new Comparator<MateriKuliah>() {
                                public int compare(MateriKuliah o1, MateriKuliah o2) {
                                    boolean b1 = o1.isSilabus();
                                    boolean b2 = o2.isSilabus();

                                    return (b1 != b2) ? (b1) ? -1 : 1 : 0;

                                }
                            });
                            mAdapter.notifyDataSetChanged();
                        }
                    }else {
                        progressbar_materi.setVisibility(View.GONE);
                        mStaticMateri.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else

        {

            userRef.child(users.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataHasil : dataSnapshot.getChildren()) {
                            String nama_matkul = (String) dataHasil.getValue();
                            key.add(nama_matkul);
                            final String idCourses = dataHasil.getKey();


                        }
                        ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(MateriActivity.this, android.R.layout.simple_spinner_item, key);
                        areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                        areaSpinner.setAdapter(areasAdapter);

                        //set spinner from detail activity
                        areaSpinner.setSelection(areasAdapter.getPosition(keys));


                    } else {

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        mRecycleView.addOnItemTouchListener(new RecyclerTouchListener(this, mRecycleView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                final MateriKuliah materi = listMateri.get(position);


                final StorageReference httpsReference = storage.getReferenceFromUrl(materi.getUrl());

                if (isNetworkStatusAvialable(getApplicationContext())) {
                    new SweetAlertDialog(MateriActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Download File ?")
                            .setContentText("File name : " + materi.getNama())
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
                                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "" + storageMetadata.getName());
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

    public void showPopupMateri(View v){
        TextView txtClose;
        txtClose = (TextView) myDialog.findViewById(R.id.txtClose);
        Button btnChoose = (Button) myDialog.findViewById(R.id.buttonChoose);
        btnUpload = (Button) myDialog.findViewById(R.id.buttonUpload);
//        silabus_checkbox = (CheckBox) myDialog.findViewById(R.id.silabus_checkbox);

        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });


        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFileFull();
            }
        });


        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file


                    filepath = data.getData();
                    upload_path.setText(""+filepath.toString());

                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadFileFull(){


           String nama_input = inputNama.getText().toString();
           String pertemuan_input = inputPertemuan.getText().toString();


            if(TextUtils.isEmpty(nama_input)){
                inputNama.setText("");
                new SweetAlertDialog(MateriActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Nama Materi Tidak Boleh Kosong !!")
                        .show();
            }else if(TextUtils.isEmpty(pertemuan_input)){
                inputPertemuan.setText("");
                new SweetAlertDialog(MateriActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Pertemuan Tidak Boleh Kosong !!")
                        .show();

            }else if(filepath == null){
                new SweetAlertDialog(MateriActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("File Tidak Boleh Kosong !!")
                        .show();
            }else{
                final String keyMateri = materiRef.child(namaMatkulGet).push().getKey();


                final StorageReference ref = storage.getReference().child("materi_1/"+filepath.getLastPathSegment());
                final UploadTask uploadTask = ref.putFile(filepath);
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading");
                progressDialog.show();


                ref.putFile(filepath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                    @Override
                                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                        if (!task.isSuccessful()) {

                                            throw task.getException();

                                        }

                                        // Continue with the task to get the download URL
                                        return ref.getDownloadUrl();

                                    }
                                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {


                                            Uri downloadUri = task.getResult();
//                                            if(silabus_checkbox.isChecked()){
//                                                Log.e("cekbox","check box clicked");
//                                                materiRef.child(namaMatkulGet).child(keyMateri).child("silabus").setValue(true);
//
//                                            }else{
//                                                materiRef.child(namaMatkulGet).child(keyMateri).child("silabus").setValue(false);
//
//                                            }
                                            materiRef.child(namaMatkulGet).child(keyMateri).child("url").setValue(downloadUri.toString());
                                            materiRef.child(namaMatkulGet).child(keyMateri).child("nama").setValue(inputNama.getText().toString());
                                            materiRef.child(namaMatkulGet).child(keyMateri).child("pertemuan").setValue(inputPertemuan.getText().toString());
                                            inputNama.setText("");
                                            inputPertemuan.setText("");
                                            upload_path.setText("");
//                                            silabus_checkbox.setChecked(false);

                                        } else {



                                        }
                                    }});

                                progressDialog.dismiss();
                                new SweetAlertDialog(MateriActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Complete")
                                        .setContentText("Upload Success !")
                                        .show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(MateriActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                Log.e("downloadnya","masuk progres");
                                double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                        .getTotalByteCount());
                                progressDialog.setMessage("Uploaded "+(int)progress+"%");
                            }
                        });


            }

    }



    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
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
                        Log.e("itemnya",""+dataHasil.getKey().toString());

                        MateriKuliah materi = dataHasil.getValue(MateriKuliah.class);

                        listMateri.add(materi);
                        progressbar_materi.setVisibility(View.GONE);
                        mStaticMateri.setVisibility(View.GONE);

                        Collections.sort(listMateri, new Comparator<MateriKuliah>() {
                            public int compare(MateriKuliah o1, MateriKuliah o2) {
                                boolean b1 = o1.isSilabus();
                                boolean b2 = o2.isSilabus();

                                return (b1 != b2) ? (b1) ? -1 : 1 : 0;

                            }
                        });

                        mAdapter.notifyDataSetChanged();

                    }
                }else{
                    progressbar_materi.setVisibility(View.GONE);
                    mStaticMateri.setVisibility(View.VISIBLE);
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

    private TextWatcher materiTextWatcher = new TextWatcher(){

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String inputNama_input = inputNama.getText().toString().trim();
            String inputPertemuan_input = inputPertemuan.getText().toString().trim();



            btnUpload.setEnabled(!inputNama_input.isEmpty() || !inputPertemuan_input.isEmpty() || filepath != null );
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}
