package com.example.yehezkiel.eclassapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by Yehezkiel on 7/15/2018.
 */



public class myAdapterTugas extends RecyclerView.Adapter<myAdapterTugas.MyViewHolder>{
    private List<DaftarTugas> listTugas;
    private FirebaseUser users = FirebaseAuth.getInstance().getCurrentUser();
    public TwoFragment.Counter counter;





    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tanggal_tugas,judul_tugas,tanggal_kumpul,nama_matkul,jam_tugas;
        public RelativeLayout mCustomTugas;
        public ImageView notif_tugas;

        public MyViewHolder(View view) {
            super(view);
            mCustomTugas = (RelativeLayout) view.findViewById(R.id.custom_tugas);
            nama_matkul = (TextView) view.findViewById(R.id.judul_p);
            judul_tugas = (TextView) view.findViewById(R.id.deskripsi_p);
            tanggal_tugas = (TextView) view.findViewById(R.id.tanggal_tugas);
            notif_tugas = (ImageView) view.findViewById(R.id.notif_tugas);
            jam_tugas = (TextView) view.findViewById(R.id.jam_tugas);
        }
    }

    public myAdapterTugas(List<DaftarTugas> listTugas,TwoFragment.Counter counter) {
        this.listTugas = listTugas;
        this.counter = counter;
    }

    public myAdapterTugas(List<DaftarTugas> listTugas) {
        this.listTugas = listTugas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_row_tugas, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final DaftarTugas tugas = listTugas.get(position);
        holder.nama_matkul.setText(tugas.getNama_tugas());
        holder.judul_tugas.setText(tugas.getJudul_tugas());

        String strCurrentDate = tugas.getTanggal_tugas();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat outputdate = new SimpleDateFormat("EEE , dd MMM yyyy");
        SimpleDateFormat outputhour = new SimpleDateFormat("hh:mm aa");

        String date = null;
        String hour = null;


        try {
            Date newDate  = format.parse(strCurrentDate);
            date= outputdate.format(newDate);
            hour = outputhour.format(newDate);
            holder.tanggal_tugas.setText(date);
            holder.jam_tugas.setText(hour);

        } catch (ParseException e) {
            e.printStackTrace();
        }







        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), ReadActivity.class);
                DatabaseReference flag_tugas = FirebaseDatabase.getInstance().getReference("flag_tugas");

                if(tugas.getFlag().equals("a")){
                    Log.e("Ser_Counter","didalem adapter");
                    counter.setCounter(-1);
                }

                flag_tugas.child(tugas.getIdCourse()).child(tugas.getIdTugas()).child(users.getUid()).setValue("o");
                intent.putExtra("flag", "tugas");
                intent.putExtra("namamatkultugas", tugas.getNama_tugas());
                intent.putExtra("deskripsitugas", tugas.getDeskripsi_tugas());
                intent.putExtra("judultugas", tugas.getJudul_tugas());
                intent.putExtra("tanggalkumpul", tugas.getTanggal_kumpul());
                intent.putExtra("tanggaltugas", tugas.getTanggal_tugas());
                view.getContext().startActivity(intent);

            }
        });

        if(tugas.getFlag() != null) {

            if (tugas.getFlag().equals("a")) {
                Log.e("mantul", " masuk a");
                holder.itemView.setBackgroundColor(Color.parseColor("#e3f2fd"));
                holder.notif_tugas.setVisibility(View.VISIBLE);

            } else {
                Log.e("mantul", " masuk o");
                holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
                holder.notif_tugas.setVisibility(View.GONE);
            }
        }else {

        }




    }



    @Override
    public int getItemCount() {
        return listTugas.size();
    }

}
