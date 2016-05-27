package com.lewetechnologies.app.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.lewetechnologies.app.activities.MainActivity;

/**
 * Created by alessandro on 27/05/16.
 */
public class MainService extends Service {

    //---COSTANTI---
    private static final int ONGOING_NOTIFICATION_ID = 1105;

    //connection states
    private static final int STATE_DISCONNECTED = 0; //disconnesso
    private static final int STATE_CONNECTING = 1; //in attesa di connessione
    private static final int STATE_CONNECTED = 2; //connesso


    //---FUNZIONI DI GESTIONE PER I SERVIZI---
    //chiude i servizi
    private void stopServices() {
        //chiudo il servizio di bluetooth
        Intent bluetoothSerialService = new Intent(this, BluetoothSerialService.class);
        stopService(bluetoothSerialService);

        //chiudo il servizio di database
        Intent databaseService = new Intent(this, DatabaseService.class);
        stopService(databaseService);
    }

    //avvia i servizi
    private void startServices() {

        //avvio il servizio bluetooth
        Intent bluetoothSerialService = new Intent(this, BluetoothSerialService.class);
        startService(bluetoothSerialService);

        //avvio il servizio di database
        Intent databaseService = new Intent(this, DatabaseService.class);
        startService(databaseService);
    }


    //---NOTIFICATION FUNCTION---
    private void setNotification(int state) {

        Notification.Builder builder = new Notification.Builder(this); //creo builder

        //intent apertura main
        Intent intent = new Intent(this, MainActivity.class); //creo intent apertura main

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0); //pending intent

        builder.setContentIntent(pendingIntent);  //imposto l'intent

        startForeground(ONGOING_NOTIFICATION_ID, builder.build());

    }


    //---FUNZIONI PER IL SERVIZIO---
    @Override
    public void onCreate() {
        super.onCreate();

        //imposto la notifica e il servizio come foreground
        setNotification(STATE_DISCONNECTED);

        //avvio i servizi
        startServices();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //chiudo i servizi
        stopServices();
    }

    //---FUNZIONI PER IL BINDER---
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
