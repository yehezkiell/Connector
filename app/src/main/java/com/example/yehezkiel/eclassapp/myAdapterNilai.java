package com.example.yehezkiel.eclassapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Yehezkiel on 9/13/2018.
 */

public class myAdapterNilai  extends RecyclerView.Adapter<myAdapterNilai.MyViewHolder>{
    private List<NilaiMatakuliah> listNilai;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nama_nilai,bobot_nilai,skala_nilai,nilai_nilai;


        public MyViewHolder(View view) {
            super(view);
            nama_nilai = (TextView) view.findViewById(R.id.nama_nilai);
            bobot_nilai = (TextView) view.findViewById(R.id.bobot_nilai);
            skala_nilai = (TextView) view.findViewById(R.id.skala_nilai);
            nilai_nilai = (TextView) view.findViewById(R.id.nilai_nilai);
        }

    }

    public myAdapterNilai(List<NilaiMatakuliah> listNilai) {

        this.listNilai = listNilai;
    }

    @NonNull
    @Override
    public myAdapterNilai.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_row_nilai, parent, false);
        return new MyViewHolder(itemView);
    }

    public void onBindViewHolder(myAdapterNilai.MyViewHolder holder, int position) {
        NilaiMatakuliah nilai = listNilai.get(position);
        holder.nama_nilai.setText(nilai.getNama());
        holder.bobot_nilai.setText(""+nilai.getBobot());
        holder.skala_nilai.setText(""+nilai.getSkala());
        holder.nilai_nilai.setText(""+nilai.getNilai());


    }

    @Override
    public int getItemCount() {
        return listNilai.size();
    }
}
