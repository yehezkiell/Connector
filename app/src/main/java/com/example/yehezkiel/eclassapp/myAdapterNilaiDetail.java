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

public class myAdapterNilaiDetail extends RecyclerView.Adapter<myAdapterNilaiDetail.MyViewHolder> {

    private List<ListNilaiDetail> listNilaiDetail;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,nim,nilai,nomer;


        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.nama_nilai_detail);
            nim = (TextView) view.findViewById(R.id.nim_nilai);
            nilai = (TextView) view.findViewById(R.id.nilai_detail);
            nomer = (TextView) view.findViewById(R.id.nomer);

        }

    }

    public myAdapterNilaiDetail(List<ListNilaiDetail> listNilaiDetail) {

        this.listNilaiDetail = listNilaiDetail;
    }

    @NonNull
    @Override
    public myAdapterNilaiDetail.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_nilai_detail, parent, false);
        return new MyViewHolder(itemView);
    }

    public void onBindViewHolder(myAdapterNilaiDetail.MyViewHolder holder, int position) {
        ListNilaiDetail nilai = listNilaiDetail.get(position);
        holder.name.setText(nilai.getName());
        holder.nim.setText(""+nilai.getNim());
        holder.nilai.setText(""+nilai.getNilai());
        holder.nomer.setText(Integer.toString(position+1));
    }

    @Override
    public int getItemCount() {
        return listNilaiDetail.size();
    }
}
