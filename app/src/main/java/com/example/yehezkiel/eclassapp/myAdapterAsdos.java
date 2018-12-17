package com.example.yehezkiel.eclassapp;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Yehezkiel on 10/12/2018.
 */

public class myAdapterAsdos extends RecyclerView.Adapter<myAdapterAsdos.MyViewHolder>  {

    private List<ListAsdos> listAsdos;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView judul, deskripsi,kelas,day,jam,kode,txtAsdos;
        public ImageView imgAsdos;


        public MyViewHolder(View view) {
            super(view);
            judul = (TextView) view.findViewById(R.id.first_line);
            day = (TextView) view.findViewById(R.id.hari);
            jam = (TextView) view.findViewById(R.id.waktu);
            kelas = (TextView) view.findViewById(R.id.kelas);
            kode = (TextView) view.findViewById(R.id.kode);
            txtAsdos = (TextView) view.findViewById(R.id.asdos);
            imgAsdos = (ImageView) view.findViewById(R.id.imgAsdos);
            txtAsdos.setVisibility(View.VISIBLE);
            imgAsdos.setVisibility(View.VISIBLE);

        }

    }
    public myAdapterAsdos(List<ListAsdos> listAsdos) {

        this.listAsdos = listAsdos;
    }

    @Override
    public myAdapterAsdos.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_row_matkul, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(myAdapterAsdos.MyViewHolder holder, final int position) {
        final ListAsdos asdos = listAsdos.get(position);
        holder.judul.setText(asdos.getName());
        holder.day.setText(asdos.getDay());
        holder.jam.setText(asdos.getJam());
        holder.kelas.setText(asdos.getKelas());
        holder.kode.setText(asdos.getKode());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MenuActivity.class);
                intent.putExtra("keys", asdos.getIdCourse());
                intent.putExtra("flag", "asdos");
                intent.putExtra("namamatkul", asdos.getName());
                intent.putExtra("jammatkul", asdos.getJam());
                intent.putExtra("daymatkul", asdos.getDay());
                intent.putExtra("bobotmatkul", String.valueOf(asdos.getBobot()));
                intent.putExtra("kelasmatkul", asdos.getKelas());
                view.getContext().startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {

        return listAsdos.size();
    }
}
