package com.example.yehezkiel.eclassapp;

import android.content.Intent;
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
        public TextView nama_nilai,bobot_nilai,skala_nilai,nilai_nilai,nilai_layout;
        public View SplitLine_hor1;


        public MyViewHolder(View view) {
            super(view);
            SplitLine_hor1 = (View) view.findViewById(R.id.SplitLine_hor1);
            nilai_layout = (TextView) view.findViewById(R.id.nilai_layout);
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
                .inflate(R.layout.custom_row_nilai_new, parent, false);
        return new MyViewHolder(itemView);
    }

    public void onBindViewHolder(myAdapterNilai.MyViewHolder holder, final int position) {
        NilaiMatakuliah nilai = listNilai.get(position);
        if(nilai.getFlag().equals("asdos")){
            holder.nilai_nilai.setVisibility(View.GONE);
            holder.SplitLine_hor1.setVisibility(View.GONE);
            holder.nilai_layout.setVisibility(View.GONE);
        }else{
            holder.nilai_nilai.setText(""+nilai.getNilai());

        }
        holder.nama_nilai.setText(nilai.getNama());
        holder.bobot_nilai.setText(""+nilai.getBobot());
        holder.skala_nilai.setText(""+nilai.getSkala());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NilaiMatakuliah nilai = listNilai.get(position);
                Intent intent = new Intent(view.getContext(), NilaiDetailActivity.class);
                intent.putExtra("keys", nilai.getKeys());
                intent.putExtra("namamatkul", nilai.getNamaMatkul());
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listNilai.size();
    }
}
