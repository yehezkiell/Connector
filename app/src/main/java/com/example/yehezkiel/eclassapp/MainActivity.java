package com.example.yehezkiel.eclassapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<MataKuliah> listMatkul = new ArrayList<>();
    private myAdapter myAdapter;
    private Button logoutBtn;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authListener;
    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private TextView mTextName;
    private TextView mTextNim;

    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
    FirebaseUser users = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference mataKuliahRef = FirebaseDatabase.getInstance().getReference("courses");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mAuth = FirebaseAuth.getInstance();

        //Recycler View
        mRecycleView = (RecyclerView) findViewById(R.id.MainRView);
        mRecycleView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(mLayoutManager);
        mAdapter = new myAdapter(listMatkul);
        mRecycleView.setAdapter(mAdapter);

        //Navbar menu
        mToolbar = (Toolbar) findViewById(R.id.navbaraction);
        setSupportActionBar(mToolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawabel_main);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mNavigationView = (NavigationView) findViewById(R.id.nav_menu);
        View header = mNavigationView.getHeaderView(0);
        mTextName = (TextView) header.findViewById(R.id.header_name);
        mTextNim = (TextView) header.findViewById(R.id.header_nim);



        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case(R.id.tugas):
                        Intent accountActivity = new Intent(MainActivity.this, Tugas.class);
                        startActivity(accountActivity);
                        break;
                    case(R.id.logout_menu):
                        signOut();
                        break;
                }
                return true;
            }
        });

        userRef.child(users.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = (String) dataSnapshot.child("name").getValue().toString();
                String nim = (String) dataSnapshot.child("nim").getValue().toString();

                mTextNim.setText(nim);
                mTextName.setText(name);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        userRef.child(users.getUid()).child("courses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listMatkul.clear();
                if (dataSnapshot.exists()){
                    for(DataSnapshot dataHasil : dataSnapshot.getChildren()){
                        String idCourses = dataHasil.getKey();


                        Log.e("Yhz",idCourses);
                        mataKuliahRef.child(idCourses).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot2) {
                                MataKuliah matkul = dataSnapshot2.getValue(MataKuliah.class);
                                listMatkul.add(matkul);
                                mAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                }else{
                    Toast.makeText(getApplicationContext(),"TIDAK ADA MATAKULIAH",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        logoutBtn = findViewById(R.id.logoutBtn);

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void signOut() {
        mAuth.signOut();
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authListener);


    }

}
