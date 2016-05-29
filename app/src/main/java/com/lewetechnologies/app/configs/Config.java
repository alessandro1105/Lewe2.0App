package com.lewetechnologies.app.configs;

import com.lewetechnologies.app.logger.Logger;

/**
 * Created by alessandro on 16/05/16.
 */
public class Config {

    //---INTENT REQUEST CODE---
    public static final int REQUEST_EXIT_CODE = 0; //richiede l'uscita dall'app (USATO IN SETTINGS)
    public static final int REQUEST_ENABLE_BT = 1; //richiede l'attivazione del bt

    //---INTENT RESULT CODE---
    public static final int RESULT_EXIT_CODE = 1; //ritorna la richiesta di uscita dell'app (USATO IN SETTINGS)


    //---LOGGER ERROR LEVEL---
    public static final int LOGGER_ERROR_LEVEL = Logger.DEBUG;


    //---BT NAME LEWEBAND---
    public static final String LEWEBAND_BT_NAME = "LW2v0";

    //---SHARED PREFERENCE---
    public static final String SHARED_PREFERENCE_FILE = "com.lewetechnologies.com.SharedPreferences";

    //---DATABASE SETTINGS---
    public static final String DATABASE_FILE_NAME = "com.lewetechnologies.com.Database.sql"; //nome del file
    public static final int DATABASE_VERSION = 1; //verisone del database

    //---SHARED PREFERENCE KEY---
    public static final String SHARED_PREFERENCE_KEY_DEVICE_MAC = "shared_preference_key_device_mac";
    public static final String SHARED_PREFERENCE_KEY_FIRST_ASSOCIATION = "shared_preference_key_first_association";
    public static final String SHARED_PREFERENCE_KEY_BAND_NAME_ASSOCIATED = "shared_preference_key_band_name_associated";
    //public static final String SHARED_PREFERENCE_KEY_BAND_STATUS = "shared_preference_key_band_status";


    //---CHIAVI PER EXTRA INTENT CONDIVISI--
    public final static String EXTRA_DATA_GSR = "com.lewetechnologies.app.services.EXTRA_DATA_GSR"; //dato gsr
    public final static String EXTRA_DATA_TEMPERATURE = "com.lewetechnologies.app.services.EXTRA_DATA_TEMPERATURE"; //dato temperature
    public final static String EXTRA_DATA_TIMESTAMP = "com.lewetechnologies.app.services.EXTRA_DATA_TIMESTAMP"; //dato gsr


    //---CHIAVI PER JACK--
    public final static String JACK_TEMPERATURE = "TME";
    public final static String JACK_GSR = "GSR";
    public final static String JACK_TIMESTAMP = "TMP";


    //---CHIAVI PER DB---
    public final static String DATABASE_KEY_TEMPERATURE = "TME";
    public final static String DATABASE_KEY_GSR = "GSR";
}
