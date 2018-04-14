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

public class myAdapter extends RecyclerView.Adapter<myAdapter.MyViewHolder> {
    private List<MataKuliah> listMatkul;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView judul, deskripsi,dosen1,dosen2,day,jam;

        public MyViewHolder(View view) {
            super(view);
            judul = (TextView) view.findViewById(R.id.first_line);
            day = (TextView) view.findViewById(R.id.hari);
            jam = (TextView) view.findViewById(R.id.waktu);
            dosen1 = (TextView) view.findViewById(R.id.first_dosen);
            dosen2 = (TextView) view.findViewById(R.id.second_dosen);

        }
    }


    public myAdapter(List<MataKuliah> listMatkul) {
        this.listMatkul = listMatkul;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.costume_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MataKuliah matkul = listMatkul.get(position);
        holder.judul.setText(matkul.getName());
        holder.dosen1.setText(matkul.getDosen_1());
        holder.dosen2.setText(matkul.getDosen_2());
        holder.day.setText(matkul.getDay());
        holder.jam.setText(matkul.getJam());


    }

    @Override
    public int getItemCount() {
        return listMatkul.size();
    }
}
