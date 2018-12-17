package com.example.yehezkiel.eclassapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.readystatesoftware.viewbadger.BadgeView;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.nekocode.badge.BadgeDrawable;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    private Toolbar toolbar;
    public TabLayout tabLayout;
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
    public TextView tabTwo;
    public TextView tabThree;
    ViewPagerAdapter adapter;
    private ImageView badge_header;
    private ImageView prodi_header;
    private SwipeRefreshLayout swipeLayout;

    private DatabaseReference mFlagTugas;

    boolean doubleBackToExitPressedOnce = false;



    private int[] tabIcons = {
            R.mipmap.baseline_home_white_24dp,
            R.mipmap.baseline_assignment_late_white_24dp,
            R.drawable.ic_notifications_black_24dp
    };



    static {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    private DatabaseReference userRef;
    private DatabaseReference userCourse;

    private FirebaseUser users;


    private ImageView button_abs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        userRef = FirebaseDatabase.getInstance().getReference("users");
        users = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        mFlagTugas = FirebaseDatabase.getInstance().getReference("flag_tugas");
        userCourse = FirebaseDatabase.getInstance().getReference("user_course");



        //Navbar menu
        mToolbar = (Toolbar) findViewById(R.id.navbaraction);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(getLayoutInflater().inflate(R.layout.abs_layout, null),
                new ActionBar.LayoutParams(
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.MATCH_PARENT,
                        Gravity.CENTER
                )
        );



        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawabel_main);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mNavigationView = (NavigationView) findViewById(R.id.nav_menu);
        View header = mNavigationView.getHeaderView(0);
        badge_header = (ImageView) header.findViewById(R.id.asdos_header);
        prodi_header = (ImageView) header.findViewById(R.id.prodi_header);

        mTextName = (TextView) header.findViewById(R.id.header_name);
        mTextNim = (TextView) header.findViewById(R.id.header_nim);


        SharedPreferences prefs = getSharedPreferences("token_shared", MODE_PRIVATE);
        String restoredText = prefs.getString("token", null);
        Log.e("tokenya",""+restoredText);

        userRef.child(users.getUid()).child("token_id").setValue(restoredText);

        int position = 0;
        Bundle extras = getIntent().getExtras();
        Log.e("extra",""+extras);
        if(extras != null) {
            position = extras.getInt("viewpager_position");
        }

        //Tab Layout
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);
        setupViewPager(viewPager);
        viewPager.setCurrentItem(position);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();




        String dateString = "2010-09-29 08:45:22";

        String givenDateString = "2018-08-05";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        try {
            Date mDate = sdf.parse(givenDateString);
            long timeInMilliseconds = mDate.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }




        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case (R.id.beranda):
                        viewPager.setCurrentItem(0);
                        break;
                    case (R.id.tugas):
                        viewPager.setCurrentItem(1);

                        break;
                    case (R.id.pengumuman):
                        viewPager.setCurrentItem(2);
                        break;

                    case (R.id.materi):
                        Intent go3 = new Intent(MainActivity.this,MateriActivity.class);
                        go3.putExtra("flag","asd");
                        startActivity(go3);
                        break;
                    case (R.id.equ_ukdw):
                        Intent go4 = new Intent(MainActivity.this,EquActivity.class);
                        startActivity(go4);
                        break;
                    case (R.id.logout_menu):
                        new AsyncTask<Void,Void,Void>()
                        {
                            @Override
                            protected Void doInBackground(Void... params)
                            {
                                {
                                    try
                                    {
                                        FirebaseInstanceId.getInstance().deleteInstanceId();
                                    } catch (IOException e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                                return null;
                            }
                            @Override
                            protected void onPostExecute(Void result)
                            {
                                //call your activity where you want to land after log out
                            }
                        }.execute();
                        signOut();
                        break;
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

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


        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        };}



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.button_bar, menu);
        return true;
    }



    private void setupTabIcons() {


        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("Matakuliah");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.baseline_home_white_24dp, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);


        tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Tugas");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.baseline_assignment_late_white_24dp, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("Pengumuman");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_notifications_black_24dp, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);
        BadgeView badge3 = new BadgeView(getApplicationContext(), tabThree);



    }


    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new OneFragment(), "ONE");
        adapter.addFrag(new TwoFragment(), "TWO");
        adapter.addFrag(new ThreeFragment(), "THREE");
        viewPager.setAdapter(adapter);
//        viewPager.getAdapter().notifyDataSetChanged();
    }




    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }



        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }


    @Override
    public void onPageSelected(int position) {
//        viewPager.getAdapter().notifyDataSetChanged();

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.action_refresh) {
            EventBus.getDefault().post(new ViewEvent("updateViews"));
            EventBus.getDefault().post(new ViewEvent("loadDataPengumuman"));
            return true;
        } else if(mToggle.onOptionsItemSelected(item)){
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

    @Override
    protected void onResume() {
        EventBus.getDefault().post(new ViewEvent("updateViews"));
        EventBus.getDefault().post(new ViewEvent("loadDataPengumuman"));
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Klik sekali lagi untuk keluar ", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }



    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }



}
