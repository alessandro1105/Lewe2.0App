package com.lewetechnologies.app.jack;

import com.lewetechnologies.app.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alessandro on 20/05/16.
 */
public class JData {

    //root JSON
    private JSONObject root;

    //nested object
    private JSONObject values;

    //indica se è stato
    private boolean nestedObjectExists;


    //---PUBLIC---

    public JData() {

        //creo la root JSON
        root = new JSONObject();

        //imposto che non è ancora stato creato l'oggetto nested
        nestedObjectExists = false;
    }

    //costruttore passando un messaggio in JSONObject
    public JData(JSONObject message) throws JSONException {
        //salvo la root del messaggio
        root = message;

        //prelevo il payload dei dati
        values = root.getJSONObject(Jack.JK_MESSAGE_PAYLOAD);

    }

    //adder
    public boolean add(String key, boolean value) {

        //se non esiste l'oggetto nested lo creo
        if (!nestedObjectExists) {
            createNestedObject();
        }

        try {
            values.put(key, value);

            return true;

        } catch (JSONException e) {
            Logger.e("JData", "add", e, Logger.SEVERE);

            return false;
        }

    }

    public boolean add(String key, float value) {

        //se non esiste l'oggetto nested lo creo
        if (!nestedObjectExists) {
            createNestedObject();
        }

        try {
            values.put(key, value);

            return true;

        } catch (JSONException e) {
            Logger.e("JData", "add", e, Logger.SEVERE);

            return false;
        }

    }

    public boolean add(String key, double value) {

        //se non esiste l'oggetto nested lo creo
        if (!nestedObjectExists) {
            createNestedObject();
        }

        try {
            values.put(key, value);

            return true;

        } catch (JSONException e) {
            Logger.e("JData", "add", e, Logger.SEVERE);

            return false;
        }

    }

    public boolean add(String key, char value) {

        //se non esiste l'oggetto nested lo creo
        if (!nestedObjectExists) {
            createNestedObject();
        }

        try {
            values.put(key, value);

            return true;

        } catch (JSONException e) {
            Logger.e("JData", "add", e, Logger.SEVERE);

            return false;
        }

    }

    public boolean add(String key, long value) {

        //se non esiste l'oggetto nested lo creo
        if (!nestedObjectExists) {
            createNestedObject();
        }

        try {
            values.put(key, value);

            return true;

        } catch (JSONException e) {
            Logger.e("JData", "add", e, Logger.SEVERE);

            return false;
        }

    }

    public boolean add(String key, int value) {

        //se non esiste l'oggetto nested lo creo
        if (!nestedObjectExists) {
            createNestedObject();
        }

        try {
            values.put(key, value);

            return true;

        } catch (JSONException e) {
            Logger.e("JData", "add", e, Logger.SEVERE);

            return false;
        }

    }

    public boolean add(String key, short value) {

        //se non esiste l'oggetto nested lo creo
        if (!nestedObjectExists) {
            createNestedObject();
        }

        try {
            values.put(key, value);

            return true;

        } catch (JSONException e) {
            Logger.e("JData", "add", e, Logger.SEVERE);

            return false;
        }

    }

    public boolean add(String key, String value) {

        //se non esiste l'oggetto nested lo creo
        if (!nestedObjectExists) {
            createNestedObject();
        }

        try {
            values.put(key, value);

            return true;

        } catch (JSONException e) {
            Logger.e("JData", "add", e, Logger.SEVERE);

            return false;
        }

    }

    //get method

    public boolean getBoolean(String key) {
        try {
            return values.getBoolean(key);

        } catch (JSONException e) {
            return false;
        }
    }

    public double getFloat(String key) {
        try {
            return values.getDouble(key);

        } catch (JSONException e) {
            return 0;
        }
    }

    public double getDouble(String key) {
        try {
            return values.getDouble(key);

        } catch (JSONException e) {
            return 0;
        }
    }

    public char getChar(String key) {
        try {
            return values.getString(key).charAt(0);

        } catch (JSONException e) {
            return 0;
        }
    }

    public long getLong(String key) {
        try {
            return values.getLong(key);

        } catch (JSONException e) {
            return 0;
        }
    }

    public int getInt(String key) {
        try {
            return values.getInt(key);

        } catch (JSONException e) {
            return 0;
        }
    }

    public int getShort(String key) {
        try {
            return values.getInt(key);

        } catch (JSONException e) {
            return 0;
        }
    }

    public String getString(String key) {
        try {
            return values.getString(key);

        } catch (JSONException e) {
            return "";
        }
    }


    //get JsonObject
    public JSONObject getRoot() {

        //se è stato creato l'oggetto nested lo inserisco
        if (nestedObjectExists) {
            try {
                root.put(Jack.JK_MESSAGE_PAYLOAD, values);

            } catch (JSONException e){
                Logger.e("JData", "getRoot", e, Logger.SEVERE);
            }
        }

        //ritorno la root del messaggio
        return root;
    }

    //---PRIVATE---

    public void createNestedObject() {

        //creo l'oggetto nested
        values = new JSONObject();

        //indico che è stato creato l'oggetto nested
        nestedObjectExists = true;

    }


}
