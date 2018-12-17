package com.example.yehezkiel.eclassapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;
import java.util.Map;

/**
 * Created by Yehezkiel on 9/17/2018.
 */

public class myFirebaseInstanceService extends FirebaseMessagingService {


    private static final String TAG = "FirebaseMessagingServce";
    private static final String TAG2 = "MyFirebaseIIDService";


    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    @Override
    public void onNewToken(String token) {
        Log.d(TAG2, "Refreshed token: " + token);

        if(user != null){
            Log.d(TAG2, "user ada: " + token);
        }else{
            Log.d(TAG2, "user tidak ada: " + token);
            SharedPreferences.Editor editor = getSharedPreferences("token_shared", MODE_PRIVATE).edit();
            editor.putString("token", token);
            editor.apply();
        }

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Message data payload: " + remoteMessage.getData());
            Map<String, String> data = remoteMessage.getData();

            String data_type = data.get("data_type");
            Log.e(TAG, "data_type: " + data_type);

            if(data_type.equals("pengumuman")){
                String body_notif_pengumuman = data.get("body");
                String title_notif_pengumuman = data.get("title");
                String judul_pengumuman = data.get("judul_p");
                String deskripsi_pengumuman = data.get("deskripsi_p");
                String tanggal_pengumuman = data.get("tanggal_p");
                String nama_pengumuman = data.get("nama_matkul");


                sendNotificationPengumuman(body_notif_pengumuman, title_notif_pengumuman,judul_pengumuman,deskripsi_pengumuman,tanggal_pengumuman,nama_pengumuman);
                Log.e(TAG, "body pengumuman: " + body_notif_pengumuman);
                Log.e(TAG, "title pengumuman: " + title_notif_pengumuman);
            }else if(data_type.equals("tugas")){
                Log.e(TAG, "masuk tugas: ");

                String deskripsi_tugas = data.get("deskripsi_tugas");
                String judul_tugas = data.get("judul_tugas");
                String tanggal_kumpul = data.get("tanggal_kumpul");
                String tanggal_tugas = data.get("tanggal_tugas");
                String nama_tugas = data.get("nama_tugas");

                String body_notif = data.get("body");
                String title_notif = data.get("title");
                sendNotification(body_notif, title_notif, deskripsi_tugas,judul_tugas,tanggal_kumpul,tanggal_tugas,nama_tugas);
                Log.e(TAG, "body: " + body_notif);
                Log.e(TAG, "title: " + title_notif);
            }else if(data_type.equals("nilai")){
                Log.e(TAG, "masuk nilai");
                String judul_nilai = data.get("judul");
                String title_nilai = data.get("title");
                String keys = data.get("keys");
                if(user != null) {
                    sendNotificationNilai(judul_nilai, title_nilai,keys);
                }
                Log.e(TAG, "body: " + judul_nilai);
                Log.e(TAG, "title: " + title_nilai);
            }


        }
        //for foreground process
        if (remoteMessage.getNotification() != null) {
            if(user != null){
                Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            }
            else{
                Log.d(TAG, "belum login : " + remoteMessage.getNotification().getBody());
            }
        }
    }

    private void sendNotification(String body_notif, String title_notif, String deskripsi_tugas, String judul_tugas, String tanggal_kumpul,String tanggal_tugas, String nama_tugas ) {
        Intent intent = new Intent(this, ReadActivity.class);
        intent.putExtra("judultugas", judul_tugas);
        intent.putExtra("tanggaltugas", tanggal_tugas);
        intent.putExtra("deskripsitugas", deskripsi_tugas);
        intent.putExtra("namamatkultugas", nama_tugas);
        intent.putExtra("tanggalkumpul", tanggal_kumpul);
        intent.putExtra("flag", "tugas");




        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setAutoCancel(true)   //Automatically delete the notification
                .setSmallIcon(R.mipmap.ic_launcher) //Notification icon
                .setContentIntent(pendingIntent)
                .setContentTitle("Connector")
                .setContentText("Anda memiliki Tugas baru!")
                .setVibrate(new long[] { 1000, 1000})
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        notificationManager.notify(m, notificationBuilder.build());
    }

    private void sendNotificationPengumuman(String body_notif, String title_notif, String data_judul,
                                            String data_deskripsi, String data_tanggal,String data_nama) {
        Intent intent = new Intent(this, ReadActivity.class);
        intent.putExtra("flag","pengumuman");
        intent.putExtra("namapengumuman", data_nama);
        intent.putExtra("tanggalpengumuman", data_tanggal);
        intent.putExtra("judulpengumuman", data_judul);
        intent.putExtra("deskripsipengumuman", data_deskripsi);


        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setAutoCancel(true)   //Automatically delete the notification
                .setSmallIcon(R.mipmap.ic_launcher) //Notification icon
                .setContentIntent(pendingIntent)
                .setContentTitle("Connector")
                .setContentText("Anda memiliki Pengumuman baru!")
                .setVibrate(new long[] { 1000, 1000})
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        notificationManager.notify(m, notificationBuilder.build());
    }


    private void sendNotificationNilai(String body_notif, String title_notif, String keys) {
        Intent intent = new Intent(this, NilaiActivity.class);
        intent.putExtra("keys", keys);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setAutoCancel(true)   //Automatically delete the notification
                .setSmallIcon(R.mipmap.ic_launcher) //Notification icon
                .setContentIntent(pendingIntent)
                .setContentTitle("Connector")
                .setContentText("Anda memiliki Nilai baru!")
                .setVibrate(new long[] { 1000, 1000})
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        notificationManager.notify(m, notificationBuilder.build());
    }




}


