package com.lewetechnologies.app.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by alessandro on 22/05/16.
 */
public class Database {

    //---DATABASE SETTINGS---
    public static final String DATABASE_FILE_NAME = "com.lewetechnologies.com.Database.sql"; //nome del file
    public static final int DATABASE_VERSION = 1; //verisone del database

    //statement di creazione
    private static final String DATABASE_CREATE_STATEMENT = "CREATE TABLE sensors (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "sensor_name TEXT NOT NULL, " +
            "sensor_value TEXT NOT NULL, " +
            "timestamp INTEGER NOT NULL);";

    //field del database
    public static final String FIELD_SENSORS_ID = "id"; //id
    public static final String FIELD_SENSORS_SENSOR_NAME = "sensor_name"; //sensor name
    public static final String FIELD_SENSORS_SENSOR_VALUE = "sensor_value"; //sensor value
    public static final String FIELD_SENSORS_TIMESTAMP = "timestamp"; //timestamp

    //tabella
    public static final String TABLE_SENSORS = "sensors";


    //---VARIABILI---
    private Context context; //context dell'applicazione (per aprire connessione con db)
    private SQLiteDatabase database; //database
    private DatabaseHelper databaseHelper; //helper per la connessione al db


    //costruttore
    public Database(Context context) {

        //salvo il context
        this.context = context;

        //inizializzo le varibili
        database = null;
        databaseHelper = null;

    }


    //apertura database
    public void open() { //apre la connessione con il db

        //apro il database
        databaseHelper = new DatabaseHelper(context, DATABASE_FILE_NAME, DATABASE_VERSION, DATABASE_CREATE_STATEMENT);

        //ottengo il database
        database = databaseHelper.getWritableDatabase();

    }


    //chiusura database
    public void close() { //chiude la connessione con il db

        //se il database è stato aperto
        if (databaseHelper != null)
            databaseHelper.close(); //chiudo il database

        //elimino il riferimento al database
        database = null;

    }

    //execute
    public Cursor executeQuery(String querySQL) {

        //se il database è stato aperto
        if (database != null) {

            //se è una query di select
            if (querySQL.startsWith("SELECT")) {

                //eseguo la query e ritorno il risultato
                return database.rawQuery(querySQL, null);

            //non è una query che necessita risultato
            } else {

                //eseguo la query
                database.execSQL(querySQL);

                //ritorno null
                return null;
            }

        //il database non è stato aperto
        } else {
            throw new DatabaseException();
        }
    }

}
