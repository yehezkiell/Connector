package com.example.yehezkiel.eclassapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Yehezkiel on 8/23/2018.
 */

public class myAdapterDosen extends RecyclerView.Adapter<myAdapterDosen.MyViewHolder> {
    @NonNull
    private Context context;
    private List<ListDosen> listDosen;

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.costume_row_popup, parent, false);
        context = parent.getContext();
        return new myAdapterDosen.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull myAdapterDosen.MyViewHolder holder, int position) {
        ListDosen dosen = listDosen.get(position);
        holder.nama.setText(dosen.getNama());
        Glide.with(context.getApplicationContext())
                .load(dosen.getFoto())
                .into(holder.foto);
    }

    public myAdapterDosen(List<ListDosen> listDosen) {

        this.listDosen = listDosen;
    }

    @Override
    public int getItemCount() {
        return listDosen.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nama;
        public ImageView foto;

        public MyViewHolder(View view) {
            super(view);
            nama = (TextView) view.findViewById(R.id.nama_dosen_1);
            foto = (ImageView) view.findViewById(R.id.gambar_dosen_1);

        }
    }
}
