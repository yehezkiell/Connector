package com.example.yehezkiel.eclassapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Yehezkiel on 10/1/2018.
 */

public class myAdapterPengumumanDetail extends RecyclerView.Adapter<myAdapterPengumumanDetail.MyViewHolder> {

    private List<DaftarPengumuman> listPengumuman;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public  TextView txtJudul,txtDeskripsi,txtTanggal;

        public MyViewHolder(View view) {
            super(view);
            txtJudul = (TextView) view.findViewById(R.id.judul_p);
            txtDeskripsi = (TextView) view.findViewById(R.id.deskripsi_p);
            txtTanggal = (TextView) view.findViewById(R.id.tanggal_p);
        }
    }

    public myAdapterPengumumanDetail(List<DaftarPengumuman> listPengumuman) {

        this.listPengumuman = listPengumuman;
    }

    @Override
    public myAdapterPengumumanDetail.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.costume_row_pengumuman, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(myAdapterPengumumanDetail.MyViewHolder holder, int position) {
        DaftarPengumuman tugas = listPengumuman.get(position);
        holder.txtJudul.setText(tugas.getJudul());
        holder.txtDeskripsi.setText(tugas.getDeskripsi());
        holder.txtTanggal.setText(tugas.getTanggal_peng());
    }

    @Override
    public int getItemCount() {
        return listPengumuman.size();
    }

}
