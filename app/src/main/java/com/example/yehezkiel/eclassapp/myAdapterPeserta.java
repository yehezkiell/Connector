package com.example.yehezkiel.eclassapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Yehezkiel on 9/14/2018.
 */

public class myAdapterPeserta extends RecyclerView.Adapter<myAdapterPeserta.MyViewHolder> {

    private List<PesertaMatakuliah> listPeserta;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,nim,nomer;


        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.nama_peserta);
            nim = (TextView) view.findViewById(R.id.nim_peserta);
            nomer = (TextView) view.findViewById(R.id.nomer_peserta);

        }

    }

    public myAdapterPeserta(List<PesertaMatakuliah> listPeserta) {

        this.listPeserta = listPeserta;
    }

    @NonNull
    @Override
    public myAdapterPeserta.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_row_peserta, parent, false);
        return new MyViewHolder(itemView);
    }

    public void onBindViewHolder(myAdapterPeserta.MyViewHolder holder, int position) {
        PesertaMatakuliah nilai = listPeserta.get(position);



        holder.name.setText(nilai.getName());
        holder.nim.setText(""+nilai.getNim());
        holder.nomer.setText(Integer.toString(position+1));
    }

    @Override
    public int getItemCount() {
        return listPeserta.size();
    }
}
