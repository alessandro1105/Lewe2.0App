package com.lewetechnologies.app.configs;

import com.lewetechnologies.app.logger.Logger;

/**
 * Created by alessandro on 16/05/16.
 */
public class Config {

    //---INTENT REQUEST CODE---
    public static final int REQUEST_EXIT_CODE = 0; //richiede l'uscita dall'app (USATO IN SETTINGS)

    //---INTENT RESULT CODE---
    public static final int RESULT_EXIT_CODE = 1; //ritorna la richiesta di uscita dell'app (USATO IN SETTINGS)


    //---LOGGER ERROR LEVEL---
    public static final int LOGGER_ERROR_LEVEL = Logger.DEBUG;


    //---SHARED PREFERENCE---
    public static final String SHARED_PREFERENCE_FILE = "com.lewetechnologies.com.PREFERENCE_FILE";

}
