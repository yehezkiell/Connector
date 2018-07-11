package com.example.yehezkiel.eclassapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String keys = intent.getStringExtra("keys");
        Toast.makeText(getApplicationContext(), keys + " is selected in the new intent!!", Toast.LENGTH_SHORT).show();


        toolbar.setNavigationIcon(R.mipmap.baseline_arrow_back_white_24); // your drawable
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


}
