package com.example.yehezkiel.eclassapp;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Yehezkiel on 9/5/2018.
 */

public class myAdapterMateri extends RecyclerView.Adapter<myAdapterMateri.MyViewHolder> {

    private List<MateriKuliah> listMateri;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nama,pertemuan,silabus;


        public MyViewHolder(View view) {
            super(view);
            nama = (TextView) view.findViewById(R.id.judul_materi);
            pertemuan = (TextView) view.findViewById(R.id.pertemuan);
            silabus = (TextView) view.findViewById(R.id.silabus);
        }

    }

    public myAdapterMateri(List<MateriKuliah> listMateri) {

        this.listMateri = listMateri;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.costume_row_materi, parent, false);
        return new MyViewHolder(itemView);
    }

    public void onBindViewHolder(myAdapterMateri.MyViewHolder holder, int position) {
        MateriKuliah materi = listMateri.get(position);
        holder.nama.setText(materi.getNama());
        holder.pertemuan.setText(materi.getPertemuan());


            if(materi.isSilabus()){
                 holder.silabus.setText("SILABUS");
                 holder.silabus.setTextColor(Color.RED);

            }else{
                holder.silabus.setText("MATERI");
                holder.silabus.setTextColor(Color.BLACK);
            }
        }

    @Override
    public int getItemCount() {
        return listMateri.size();
    }


}
