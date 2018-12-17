package com.example.yehezkiel.eclassapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Yehezkiel on 10/1/2018.
 */

public class myAdapterPengumumanDetail extends RecyclerView.Adapter<myAdapterPengumumanDetail.MyViewHolder> {

    private List<DaftarPengumuman> listPengumuman;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public  TextView txtJudul,txtDeskripsi,txtTanggal,jam_pengumuman;

        public MyViewHolder(View view) {
            super(view);
            txtJudul = (TextView) view.findViewById(R.id.judul_p);
            txtDeskripsi = (TextView) view.findViewById(R.id.deskripsi_p);
            txtTanggal = (TextView) view.findViewById(R.id.tanggal_pengumuman);
            jam_pengumuman = (TextView) view.findViewById(R.id.jam_pengumuman);
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
        final DaftarPengumuman pengumuman = listPengumuman.get(position);
        holder.txtJudul.setText(pengumuman.getJudul());
        holder.txtDeskripsi.setText(pengumuman.getHeader());


        String strCurrentDate = pengumuman.getTanggal_peng();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat outputday = new SimpleDateFormat("EEE , dd MMM yyyy");
        SimpleDateFormat outputhour = new SimpleDateFormat("hh:mm aa");
        String day = null;
        String hour = null;


        try {
            Date newDate  = format.parse(strCurrentDate);
            day = outputday.format(newDate);
            hour= outputhour.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.txtTanggal.setText(day);
        holder.jam_pengumuman.setText(hour);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ReadActivity.class);
                intent.putExtra("flag","pengumuman");
                intent.putExtra("namapengumuman", pengumuman.getNama_p());
                intent.putExtra("tanggalpengumuman", pengumuman.getTanggal_peng());
                intent.putExtra("judulpengumuman", pengumuman.getJudul());
                Log.e("pengumuman","as "+pengumuman.getJudul());
                intent.putExtra("deskripsipengumuman", pengumuman.getDeskripsi());
                notifyDataSetChanged();
                view.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return listPengumuman.size();
    }

}
