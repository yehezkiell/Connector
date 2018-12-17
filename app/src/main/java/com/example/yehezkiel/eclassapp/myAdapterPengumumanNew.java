package com.example.yehezkiel.eclassapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Yehezkiel on 11/9/2018.
 */

public class myAdapterPengumumanNew extends RecyclerView.Adapter<myAdapterPengumumanNew.MyViewHolder>
{

    private List<DaftarPengumuman> listPengumuman;
    public ThreeFragment.Counter counter;
    private FirebaseUser users = FirebaseAuth.getInstance().getCurrentUser();
    private Context mContext;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtJudul,txtDeskripsi,txtTanggal,jam_pengumuman;

        public MyViewHolder(View view) {
            super(view);
            txtJudul = (TextView) view.findViewById(R.id.judul_p);
            txtDeskripsi = (TextView) view.findViewById(R.id.deskripsi_p);
            txtTanggal = (TextView) view.findViewById(R.id.tanggal_pengumuman);
            jam_pengumuman = (TextView) view.findViewById(R.id.jam_pengumuman);
        }
    }

    public myAdapterPengumumanNew(List<DaftarPengumuman> listPengumuman,ThreeFragment.Counter counter) {
        this.counter=counter;
        this.listPengumuman = listPengumuman;
    }

    public myAdapterPengumumanNew(List<DaftarPengumuman> listPengumuman) {
        this.listPengumuman = listPengumuman;
    }

    @Override
    public myAdapterPengumumanNew.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.costume_row_pengumuman, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(myAdapterPengumumanNew.MyViewHolder holder, final int position) {
        final DaftarPengumuman pengumuman = listPengumuman.get(position);
        holder.txtJudul.setText(pengumuman.getJudul());
        holder.txtDeskripsi.setText(pengumuman.getHeader());

        String strCurrentDate = pengumuman.getTanggal_peng();
        Log.e("tanggalnya",""+strCurrentDate);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat outputday = new SimpleDateFormat("EEE , dd MMM yyyy");
        SimpleDateFormat outputhour = new SimpleDateFormat("hh:mm aa");
        String day = null;
        String hour = null;


                try {
                    Date newDate  = format.parse(strCurrentDate);
                    day = outputday.format(newDate);
                    hour= outputhour.format(newDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

        holder.txtTanggal.setText(day);
        holder.jam_pengumuman.setText(hour);


        final DatabaseReference mFlagPengumuman = FirebaseDatabase.getInstance().getReference("flag_pengumuman");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ReadActivity.class);

                if(pengumuman.getFlag().equals("a")){
                    counter.setCounter(-1);
                }

                intent.putExtra("flag","pengumuman");
                mFlagPengumuman.child(pengumuman.getIdCourse()).child(pengumuman.getIdPengumuman()).child(users.getUid()).setValue("o");

                intent.putExtra("namapengumuman", pengumuman.getNama_p());
                intent.putExtra("tanggalpengumuman", pengumuman.getTanggal_peng());
                intent.putExtra("judulpengumuman", pengumuman.getJudul());
                Log.e("pengumuman","as "+pengumuman.getJudul());
                intent.putExtra("deskripsipengumuman", pengumuman.getDeskripsi());
                notifyDataSetChanged();
                view.getContext().startActivity(intent);

            }
        });

        if(pengumuman.getFlag() != null) {
            if (pengumuman.getFlag().equals("a")) {
                Log.e("mantul", " masuk a");
                holder.itemView.setBackgroundColor(Color.parseColor("#e3f2fd"));

            } else {
                Log.e("mantul", " masuk o");
                holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
            }
        }else {

        }
    }

    @Override
    public int getItemCount() {
        return listPengumuman.size();
    }

}
