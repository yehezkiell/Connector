package com.example.yehezkiel.eclassapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
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


public class OneFragment extends Fragment {

    View v;
    private List<MataKuliah> listMatkul = new ArrayList<>();
    private ArrayList<String> keys = new ArrayList<>();
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
    private ProgressBar mProgressBar;


    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
    FirebaseUser users = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference mataKuliahRef = FirebaseDatabase.getInstance().getReference("courses");


    public OneFragment() {
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
        v =  inflater.inflate(R.layout.fragment_one, container, false);
        mAuth = FirebaseAuth.getInstance();

        //Recycler View
        mRecycleView = (RecyclerView) v.findViewById(R.id.MainRView);
        mRecycleView.setHasFixedSize(true);

        mProgressBar = (ProgressBar) v.findViewById(R.id.progressBar2);
        mProgressBar.setVisibility(View.VISIBLE);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mAdapter = new myAdapter(listMatkul);
        mRecycleView.setAdapter(mAdapter);

        userRef.child(users.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = (String) dataSnapshot.child("name").getValue().toString();
                String nim = (String) dataSnapshot.child("nim").getValue().toString();


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        userRef.child(users.getUid()).child("courses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                listMatkul.clear();
                keys.clear();
                mProgressBar.setVisibility(View.GONE);
                if (dataSnapshot.exists()){
                    for(DataSnapshot dataHasil : dataSnapshot.getChildren()){
                        final String idCourses = dataHasil.getKey();
                        keys.add(idCourses);

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
                    Bundle bundle=new Bundle();
                    bundle.putStringArrayList("keys", keys);
                    //set Fragmentclass Arguments
                    Fragment fragobj=new TwoFragment();
                    fragobj.setArguments(bundle);

                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.asd, fragobj).commit();



                }else{
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mRecycleView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), mRecycleView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                MataKuliah matkul = listMatkul.get(position);
                Toast.makeText(getActivity(), keys.get(position) + " is selected!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("keys", keys);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));



        return v;

    }
}
