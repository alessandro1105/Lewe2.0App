package com.lewetechnologies.app.logger;

import android.util.Log;
import com.lewetechnologies.app.configs.Config;

/**
 * Created by alessandro on 19/05/16.
 */

//LOGGER class used to handle better log info
public class Logger {

    //---LOGGER ERROR LEVEL---
    public static final int NO_LOG = -1; //severe level
    public static final int DEBUG = 0; //debug level
    public static final int INFO = 1; //info level
    public static final int WARNING = 2; //warning level
    public static final int SEVERE = 3; //severe level

    //---METHOD FOR LOGGING USING DEBUG ERROR LEVEL---
    public static void i(String tag, String msg) { i(tag, msg, DEBUG); }

    public static void d(String tag, String msg) { d(tag, msg, DEBUG); }

    public static void e(String tag, String msg) { e(tag, msg, DEBUG); }

    public static void e(String tag, String msg, Exception e) { e(tag, msg, DEBUG); }


    //---METHOD FOR LOGGING USING USER SPECIFIED ERROR LEVEL--
    public static void i(String tag, String msg, int errorLevel) {
        if (errorLevel >= Config.LOGGER_ERROR_LEVEL) {
            Log.i(tag, msg);
        }
    }

    public static void d(String tag, String msg, int errorLevel) {
        if (errorLevel >= Config.LOGGER_ERROR_LEVEL) {
            Log.d(tag, msg);
        }
    }

    public static void e(String tag, String msg, int errorLevel) {
        if (errorLevel >= Config.LOGGER_ERROR_LEVEL) {
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Exception e, int errorLevel) {
        if (errorLevel >= Config.LOGGER_ERROR_LEVEL) {
            Log.e(tag, msg, e);
        }
    }

}
