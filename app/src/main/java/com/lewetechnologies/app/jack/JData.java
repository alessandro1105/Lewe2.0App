package com.lewetechnologies.app.jack;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alessandro on 20/05/16.
 */
public class JData {

    //root JSON
    private JSONObject root;

    //nested object
    private JSONObject value;

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
        value = root.getJSONObject(Jack.JK_MESSAGE_PAYLOAD);

    }

    //adder
    public void add(String key, boolean value) {

    }

    public void add(String key, float value) {

    }

    public void add(String key, double value) {

    }

    public void add(String key, char value) {

    }
    public void add(String key, long value) {

    }

    public void add(String key, int value) {

    }

    public void add(String key, short value) {

    }

    public void add(String key, String value) {

    }

    //get JsonObject
    public JSONObject getRoot() {

        //ritorno la root del messaggio
        return root;
    }

    //---PRIVATE---




}
