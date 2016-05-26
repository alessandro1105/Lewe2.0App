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
    public boolean add(String key, boolean value) throws JDataException {

        //se non esiste l'oggetto nested lo creo
        if (!nestedObjectExists) {
            createNestedObject();
        }

        try {
            values.put(key, value);

            return true;

        } catch (JSONException e) {
            Logger.e("JData", "add", e, Logger.SEVERE);

            //lancio l'eccezione
            throw new JDataException();
        }

    }

    public boolean add(String key, float value) throws JSONException {

        //se non esiste l'oggetto nested lo creo
        if (!nestedObjectExists) {
            createNestedObject();
        }

        try {
            values.put(key, value);

            return true;

        } catch (JSONException e) {
            Logger.e("JData", "add", e, Logger.SEVERE);

            //lancio l'eccezione
            throw new JDataException();
        }

    }

    public boolean add(String key, double value) throws JSONException {

        //se non esiste l'oggetto nested lo creo
        if (!nestedObjectExists) {
            createNestedObject();
        }

        try {
            values.put(key, value);

            return true;

        } catch (JSONException e) {
            Logger.e("JData", "add", e, Logger.SEVERE);

            //lancio l'eccezione
            throw new JDataException();
        }

    }

    public boolean add(String key, char value) throws JSONException {

        //se non esiste l'oggetto nested lo creo
        if (!nestedObjectExists) {
            createNestedObject();
        }

        try {
            values.put(key, value);

            return true;

        } catch (JSONException e) {
            Logger.e("JData", "add", e, Logger.SEVERE);

            //lancio l'eccezione
            throw new JDataException();
        }

    }

    public boolean add(String key, long value) throws JSONException {

        //se non esiste l'oggetto nested lo creo
        if (!nestedObjectExists) {
            createNestedObject();
        }

        try {
            values.put(key, value);

            return true;

        } catch (JSONException e) {
            Logger.e("JData", "add", e, Logger.SEVERE);

            //lancio l'eccezione
            throw new JDataException();
        }

    }

    public boolean add(String key, int value) throws JSONException {

        //se non esiste l'oggetto nested lo creo
        if (!nestedObjectExists) {
            createNestedObject();
        }

        try {
            values.put(key, value);

            return true;

        } catch (JSONException e) {
            Logger.e("JData", "add", e, Logger.SEVERE);

            //lancio l'eccezione
            throw new JDataException();
        }

    }

    public boolean add(String key, short value) throws JSONException {

        //se non esiste l'oggetto nested lo creo
        if (!nestedObjectExists) {
            createNestedObject();
        }

        try {
            values.put(key, value);

            return true;

        } catch (JSONException e) {
            Logger.e("JData", "add", e, Logger.SEVERE);

            //lancio l'eccezione
            throw new JDataException();
        }

    }

    public boolean add(String key, String value) throws JSONException {

        //se non esiste l'oggetto nested lo creo
        if (!nestedObjectExists) {
            createNestedObject();
        }

        try {
            values.put(key, value);

            return true;

        } catch (JSONException e) {
            Logger.e("JData", "add", e, Logger.SEVERE);

            //lancio l'eccezione
            throw new JDataException();
        }

    }

    //get method

    public boolean getBoolean(String key) {
        try {
            return values.getBoolean(key);

        } catch (JSONException e) {
            //lancio l'eccezione
            throw new JDataException();
        }
    }

    public double getFloat(String key) {
        try {
            return values.getDouble(key);

        } catch (JSONException e) {
            //lancio l'eccezione
            throw new JDataException();
        }
    }

    public double getDouble(String key) {
        try {
            return values.getDouble(key);

        } catch (JSONException e) {
            //lancio l'eccezione
            throw new JDataException();
        }
    }

    public char getChar(String key) {
        try {
            return values.getString(key).charAt(0);

        } catch (JSONException e) {
            //lancio l'eccezione
            throw new JDataException();
        }
    }

    public long getLong(String key) {
        try {
            return values.getLong(key);

        } catch (JSONException e) {
            //lancio l'eccezione
            throw new JDataException();
        }
    }

    public int getInt(String key) {
        try {
            return values.getInt(key);

        } catch (JSONException e) {
            //lancio l'eccezione
            throw new JDataException();
        }
    }

    public int getShort(String key) {
        try {
            return values.getInt(key);

        } catch (JSONException e) {
            //lancio l'eccezione
            throw new JDataException();
        }
    }

    public String getString(String key) {
        try {
            return values.getString(key);

        } catch (JSONException e) {
            //lancio l'eccezione
            throw new JDataException();
        }
    }


    //get JsonObject
    public JSONObject getRoot() throws JDataException {

        //se è stato creato l'oggetto nested lo inserisco
        if (nestedObjectExists) {
            try {
                root.put(Jack.JK_MESSAGE_PAYLOAD, values);

            } catch (JSONException e){
                Logger.e("JData", "getRoot", e, Logger.SEVERE);

                //lancio l'eccezione
                throw new JDataException();
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
