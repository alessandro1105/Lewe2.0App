package com.lewetechnologies.app.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.lewetechnologies.app.database.Database;
import com.lewetechnologies.app.database.DatabaseResult;
import com.lewetechnologies.app.logger.Logger;

/**
 * Created by alessandro on 27/05/16.
 */
public class DatabaseService extends Service {

    private final static String TAG = DatabaseService.class.getSimpleName();

    //---COSTANTI---

    //comandi
    public static final String COMMAND_EXECUTE_QUERY = "com.lewetechnologies.app.services.DatabaseService.COMMAND_EXECUTE_QUERY";
    //extra per i comandi
    public static final String EXTRA_QUERY = "com.lewetechnologies.app.services.DatabaseService.EXTRA_QUERY"; //query
    public static final String EXTRA_DESTINATION_ACTION = "com.lewetechnologies.app.services.DatabaseService.EXTRA_DESTINATION_ACTION"; //anction di destinazione per il risultato

    //extra per i risultati
    public static final String EXTRA_DATABASE_RESULT = "com.lewetechnologies.app.services.DatabaseService.EXTRA_DATABASE_RESULT";


    //---VARIABILI---
    Database database; //database

    //receiver command
    BroadcastReceiver commandReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (COMMAND_EXECUTE_QUERY.equals(intent.getAction())) {

                //prelevo la query
                String query = intent.getStringExtra(EXTRA_QUERY);

                //show the query
                Logger.e(TAG, "" + query);

                //eseguo la query
                DatabaseResult result = executeQuery(query);

                //se la query ha un risultato
                if (result != null) {

                    //prelevo l'action di destinazione per l'intent dei risultati
                    String destinationAction = intent.getStringExtra(EXTRA_DESTINATION_ACTION);

                    //se non è nullo
                    if (!destinationAction.equals("")) {

                        //invio i rislutati
                        Intent resultIntent = new Intent(destinationAction);
                        resultIntent.putExtra(EXTRA_DATABASE_RESULT, result);

                        sendBroadcast(resultIntent);
                    }
                }
            }

        }
    };


    //funzioni per gestire il databse
    private DatabaseResult executeQuery(String query) {

        //eseguo la query
        Cursor cursor = database.executeQuery(query); //eseguo la query

        //se non c'è risultato
        if (cursor == null) {
            return null;
        }

        //creo il contenitore dei risultati
        DatabaseResult result = new DatabaseResult(); //coontenitore dati db

        //sposto il cursore all'inzio
        cursor.moveToFirst();

        //se ci sono dati nel cursore
        if (cursor.getCount() > 0) {

            //scorro il cursore
            do {

                //aggiungo un record nel contenitore
                int recordIndex = result.addRecord(); //indice del record nel contenitore

                //scorro le colonne nel cursore
                for (String key : cursor.getColumnNames()) {

                    //prelevo l'indice del campo nel cursore
                    int fieldIndex = cursor.getColumnIndex(key); //indice campo nel db

                    //aggiungo il valore nel record del contenitore dei dati

                    //stringa
                    if (cursor.getType(fieldIndex) == Cursor.FIELD_TYPE_STRING) { //string
                        result.addRecordField(recordIndex, key, cursor.getString(fieldIndex));

                    //float
                    } else if (cursor.getType(fieldIndex) == Cursor.FIELD_TYPE_FLOAT) { //float
                        result.addRecordField(recordIndex, key, cursor.getFloat(fieldIndex));

                    //long
                    } else if (cursor.getType(fieldIndex) == Cursor.FIELD_TYPE_INTEGER) { //integer
                        result.addRecordField(recordIndex, key, cursor.getLong(fieldIndex));
                    }

                }


            } while (cursor.moveToNext()); //scorro i dati

        }

        //chiudo il cursore del db
        cursor.close();

        //ritorno il contenitore dei dati
        return result;
    }

    //---FUNZIONI PER IL SERVIZIO
    @Override
    public void onCreate() {
        super.onCreate();

        //creo il database
        database = new Database(this);

        //apro il database
        database.open();

        //registro il receiver
        IntentFilter commandReceiverFilter = new IntentFilter();
        commandReceiverFilter.addAction(COMMAND_EXECUTE_QUERY);

        registerReceiver(commandReceiver, commandReceiverFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        database.close();

        //disregistro il receiver
        unregisterReceiver(commandReceiver);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
