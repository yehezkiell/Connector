package com.example.yehezkiel.eclassapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Yehezkiel on 4/10/2018.
 */

public class myAdapterMatkul extends RecyclerView.Adapter<myAdapterMatkul.MyViewHolder> {
    private List<MataKuliah> listMatkul;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView judul, deskripsi,kelas,day,jam,kode;


        public MyViewHolder(View view) {
            super(view);
            judul = (TextView) view.findViewById(R.id.first_line);
            day = (TextView) view.findViewById(R.id.hari);
            jam = (TextView) view.findViewById(R.id.waktu);
            kelas = (TextView) view.findViewById(R.id.kelas);
            kode = (TextView) view.findViewById(R.id.kode);

        }

    }
    public myAdapterMatkul(List<MataKuliah> listMatkul) {

        this.listMatkul = listMatkul;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_row_matkul, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MataKuliah matkul = listMatkul.get(position);
        holder.judul.setText(matkul.getName());
        holder.day.setText(matkul.getDay());
        holder.jam.setText(matkul.getJam());
        holder.kelas.setText(matkul.getKelas());
        holder.kode.setText(matkul.getKode());
    }

    @Override
    public int getItemCount() {

        return listMatkul.size();
    }


}
