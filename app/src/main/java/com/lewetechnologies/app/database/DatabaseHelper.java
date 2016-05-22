package com.lewetechnologies.app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lewetechnologies.app.logger.Logger;

/**
 * Created by alessandro on 22/05/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    //query di creazione del db se non esiste (passata nel costruttore)
    private String databaseCreateStatement;

    //costruttore
    public DatabaseHelper(Context context, String databaseName, int databaseVersion, String databaseCreateStatement) {
        super(context, databaseName, null, databaseVersion);

        //salvo la query di costruzione
        this.databaseCreateStatement = databaseCreateStatement;
    }

    //creo il database se non esiste
    @Override
    public void onCreate(SQLiteDatabase database) {

        //creo il database
        database.execSQL(databaseCreateStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        //non deve effettuare upgrade
    }

}