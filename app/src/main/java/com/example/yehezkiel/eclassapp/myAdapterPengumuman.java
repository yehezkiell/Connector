package com.example.yehezkiel.eclassapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yehezkiel on 7/26/2018.
 */

public class myAdapterPengumuman extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    List<ListItem> consolidatedList = new ArrayList<>();
    private List<DaftarPengumuman> listPengumuman;
    private FirebaseUser users = FirebaseAuth.getInstance().getCurrentUser();
    public ThreeFragment.Counter counter;




    public myAdapterPengumuman(Context context, List<ListItem> consolidatedList,ThreeFragment.Counter counter) {
        this.consolidatedList = consolidatedList;
        this.mContext = context;
        this.counter=counter;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {

            case ListItem.TYPE_TASK:
                View v1 = inflater.inflate(R.layout.costume_row_pengumuman, parent,
                        false);
                viewHolder = new GeneralViewHolder(v1);
                break;

            case ListItem.TYPE_NAME:
                View v2 = inflater.inflate(R.layout.costum_row_pengumuman_name, parent, false);
                viewHolder = new DateViewHolder(v2);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        switch (viewHolder.getItemViewType()) {

            case ListItem.TYPE_TASK:

                GeneralItem generalItem   = (GeneralItem) consolidatedList.get(position);
                GeneralViewHolder generalViewHolder= (GeneralViewHolder) viewHolder;
                generalViewHolder.txtJudul.setText(generalItem.getDaftarPengumuman().getNama_p());
                generalViewHolder.txtDeskripsi.setText(generalItem.getDaftarPengumuman().getHeader());



                break;
            case ListItem.TYPE_NAME:
                PengumumanItem dateItem = (PengumumanItem) consolidatedList.get(position);
                DateViewHolder dateViewHolder = (DateViewHolder) viewHolder;
                String strCurrentDate = dateItem.getTanggal_peng();
                Log.e("tanggalnya",""+strCurrentDate);


                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//                SimpleDateFormat outputday = new SimpleDateFormat("EEE");
//                SimpleDateFormat outputdate = new SimpleDateFormat("dd MMM yyy");
//                String day = null;
//                String date = null;
//
//
//                try {
//                    Date newDate  = format.parse(strCurrentDate);
//                    day = outputday.format(newDate);
//                    date= outputdate.format(newDate);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }

                dateViewHolder.txtDate.setText(strCurrentDate);
//                dateViewHolder.txtDate2.setText(date);

                break;

        }




        }







    // ViewHolder for date row item
    class DateViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtDate,txtDate2;

        public DateViewHolder(View v) {
            super(v);
            this.txtDate = (TextView) v.findViewById(R.id.name_p);
            this.txtDate2 = (TextView) v.findViewById(R.id.tanggal_p_2);
        }
    }

    // View holder for general row item
    class GeneralViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtJudul, txtDeskripsi, txtTanggal;

        public GeneralViewHolder(View v) {
            super(v);
            this.txtJudul = (TextView) v.findViewById(R.id.judul_p);
            this.txtDeskripsi = (TextView) v.findViewById(R.id.deskripsi_p);


        }

    }

    @Override
    public int getItemViewType(int position) {
        return consolidatedList.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return consolidatedList != null ? consolidatedList.size() : 0;
    }


}
