package com.example.yehezkiel.eclassapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;


public class TwoFragment extends Fragment {

    View v;
    private ProgressBar mProgressBar;

    public TwoFragment() {
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

        mProgressBar = (ProgressBar) v.findViewById(R.id.progressBar4);
        mProgressBar.setVisibility(View.VISIBLE);

        Bundle bundle = this.getArguments();
        if(getArguments()!=null)
        {
            ArrayList<String> obj2 = bundle.getStringArrayList("keys");
            Log.e("nba",obj2.toString());
        }


        return inflater.inflate(R.layout.fragment_two, container, false);
    }
}
