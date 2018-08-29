package com.example.yehezkiel.eclassapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


/**
 * Created by Yehezkiel on 7/15/2018.
 */

public class myAdapterTugas extends RecyclerView.Adapter<myAdapterTugas.MyViewHolder> {
    private List<DaftarTugas> listTugas;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tanggal_tugas,judul_tugas,deskripsi_tugas,tanggal_kumpul,nama_matkul;

        public MyViewHolder(View view) {
            super(view);
            nama_matkul = (TextView) view.findViewById(R.id.judul_p);
            judul_tugas = (TextView) view.findViewById(R.id.deskripsi_p);
            deskripsi_tugas = (TextView) view.findViewById(R.id.tanggal_p);
            tanggal_kumpul = (TextView) view.findViewById(R.id.tanggal_kumpul);
            tanggal_tugas = (TextView) view.findViewById(R.id.tanggal_tugas);
        }
    }

    public myAdapterTugas(List<DaftarTugas> listTugas) {

        this.listTugas = listTugas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.costume_row_tugas, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DaftarTugas tugas = listTugas.get(position);
        holder.nama_matkul.setText(tugas.getNama_tugas());
        holder.judul_tugas.setText(tugas.getJudul_tugas());
        holder.deskripsi_tugas.setText(tugas.getDeskripsi_tugas());
        holder.tanggal_kumpul.setText(tugas.getTanggal_kumpul());
        holder.tanggal_tugas.setText(tugas.getTanggal_tugas());
    }

    @Override
    public int getItemCount() {
        return listTugas.size();
    }

}
