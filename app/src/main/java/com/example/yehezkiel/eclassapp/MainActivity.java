package com.example.yehezkiel.eclassapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
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


    static {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    private DatabaseReference userRef;
    private FirebaseUser users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        userRef = FirebaseDatabase.getInstance().getReference("users");
        users = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();

        //Navbar menu
        mToolbar = (Toolbar) findViewById(R.id.navbaraction);
        setSupportActionBar(mToolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawabel_main);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mNavigationView = (NavigationView) findViewById(R.id.nav_menu);
        View header = mNavigationView.getHeaderView(0);
        mTextName = (TextView) header.findViewById(R.id.header_name);
        mTextNim = (TextView) header.findViewById(R.id.header_nim);


        //Tab Layout
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case (R.id.tugas):
                        break;
                    case (R.id.logout_menu):
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


    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private Context mContext;

        public ViewPagerAdapter(Context context, FragmentManager fm) {
            super(fm);
            mContext = context;
        }


        // This determines the fragment for each tab



        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new OneFragment();
            } else if (position == 1){
                return new TwoFragment();
            } else{
                return new ThreeFragment();
            }
        }

        // This determines the number of tabs
        @Override
        public int getCount() {
            return 3;
        }

        // This determines the title for each tab
        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            switch (position) {
                case 0:
                    return mContext.getString(R.string.matkul);
                case 1:
                    return mContext.getString(R.string.tugas);
                case 2:
                    return mContext.getString(R.string.pengumuman);
                default:
                    return null;
            }
        }
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
