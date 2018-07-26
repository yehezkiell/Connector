package com.example.yehezkiel.eclassapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yehezkiel on 7/26/2018.
 */

public class PengumumanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    List<ListItem> consolidatedList = new ArrayList<>();

    public PengumumanAdapter(Context context, List<ListItem> consolidatedList) {
        this.consolidatedList = consolidatedList;
        this.mContext = context;

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
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        switch (viewHolder.getItemViewType()) {

            case ListItem.TYPE_TASK:

                GeneralItem generalItem   = (GeneralItem) consolidatedList.get(position);
                GeneralViewHolder generalViewHolder= (GeneralViewHolder) viewHolder;
                generalViewHolder.txtJudul.setText(generalItem.getDaftarPengumuman().getJudul());
                generalViewHolder.txtDeskripsi.setText(generalItem.getDaftarPengumuman().getDeskripsi());
                generalViewHolder.txtTanggal.setText(generalItem.getDaftarPengumuman().getTanggal());


                break;

            case ListItem.TYPE_NAME:
                PengumumanItem dateItem = (PengumumanItem) consolidatedList.get(position);
                DateViewHolder dateViewHolder = (DateViewHolder) viewHolder;

                dateViewHolder.txtName.setText(dateItem.getNama_matkul());
                // Populate date item data here

                break;
        }
    }





    // ViewHolder for date row item
    class DateViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtName;

        public DateViewHolder(View v) {
            super(v);
            this.txtName = (TextView) v.findViewById(R.id.name_p);

        }
    }

    // View holder for general row item
    class GeneralViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtJudul,txtDeskripsi,txtTanggal;

        public GeneralViewHolder(View v) {
            super(v);
            this.txtJudul = (TextView) v.findViewById(R.id.judul_p);
            this.txtDeskripsi = (TextView) v.findViewById(R.id.deskripsi_p);
            this.txtTanggal = (TextView) v.findViewById(R.id.tanggal_p);


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
