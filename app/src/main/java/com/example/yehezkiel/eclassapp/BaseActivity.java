package com.example.yehezkiel.eclassapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import cn.nekocode.badge.BadgeDrawable;

public class BaseActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<MataKuliah> listMatkul = new ArrayList<>();
    private ArrayList<String> keys = new ArrayList<>();
    private myAdapterMatkul myAdapter;
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
    private FirebaseUser users;

    private ImageView badge_header;
    private ImageView prodi_header;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        users = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("users");

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

        badge_header = (ImageView) header.findViewById(R.id.asdos_header);
        prodi_header = (ImageView) header.findViewById(R.id.prodi_header);



        userRef.child(users.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    String name = (String) dataSnapshot.child("name").getValue().toString();
                    String nim = (String) dataSnapshot.child("nim").getValue().toString();
                    String prodi = (String) dataSnapshot.child("prodi").getValue().toString();
                    boolean asdos = (boolean) dataSnapshot.child("asisten").getValue();

                    float scale = getResources().getDisplayMetrics().density;
                    int dpAsPixels = (int) (5*scale + 0.5f);

                    if(asdos){
                        final BadgeDrawable drawable6 =
                                new BadgeDrawable.Builder()
                                        .strokeWidth(1)
                                        .padding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels)
                                        .type(BadgeDrawable.TYPE_WITH_TWO_TEXT_COMPLEMENTARY)
                                        .badgeColor(0xff336633)
                                        .textSize(sp2px(getApplicationContext(), 14))
                                        .typeFace(Typeface.createFromAsset(getAssets(),"fonts/code-bold.otf"))                                        .text1("ASDOS")
                                        .text2("YES")
                                        .build();

                        if (badge_header != null) {
                            badge_header.setImageDrawable(drawable6);
                        }
                    }else {
                        badge_header.setVisibility(View.GONE);
                    }


                    final BadgeDrawable drawable2 =
                            new BadgeDrawable.Builder()
                                    .type(BadgeDrawable.TYPE_ONLY_ONE_TEXT)
                                    .badgeColor(0xff336699)
                                    .typeFace(Typeface.createFromAsset(getAssets(),"fonts/code-bold.otf"))                                        .text1("ASDOS")
                                    .padding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels)
                                    .text1(prodi)
                                    .textSize(sp2px(getApplicationContext(), 14))

                                    .build();

                    prodi_header.setImageDrawable(drawable2);
                    mTextNim.setText(nim);
                    mTextName.setText(name);
                } else {

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case (R.id.beranda):
                        Intent go0 = new Intent(BaseActivity.this,MainActivity.class);

                        // you pass the position you want the viewpager to show in the extra,
                        // please don't forget to define and initialize the position variable
                        // properly
                        go0.putExtra("viewpager_position", 0);
                        go0.setFlags(
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(go0);
                        finish();
                        break;
                    case (R.id.tugas):
                        Intent go = new Intent(BaseActivity.this,MainActivity.class);

                        // you pass the position you want the viewpager to show in the extra,
                        // please don't forget to define and initialize the position variable
                        // properly
                        go.setFlags(
                                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        go.putExtra("viewpager_position", 1);

                        startActivity(go);
                        finish();
                        break;
                    case (R.id.pengumuman):
                        Intent go2 = new Intent(BaseActivity.this,MainActivity.class);

                        // you pass the position you want the viewpager to show in the extra,
                        // please don't forget to define and initialize the position variable
                        // properly

                        go2.setFlags(
                                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        go2.putExtra("viewpager_position", 2);

                        startActivity(go2);
                        finish();
                        break;

                    case (R.id.materi):
                        Intent go3 = new Intent(BaseActivity.this,MateriActivity.class);
                        go3.putExtra("flag","asd");
                        startActivity(go3);
                        finish();
                        break;
                    case (R.id.equ_ukdw):
                        Intent go4 = new Intent(BaseActivity.this,EquActivity.class);
                        startActivity(go4);
                        break;
                    case (R.id.logout_menu):
                        signOut();
                        break;
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });


        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        };}


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

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


}
