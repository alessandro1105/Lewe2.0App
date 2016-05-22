package com.lewetechnologies.app.database;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by alessandro on 22/05/16.
 */
public class DatabaseResult implements Serializable {

    //contenitore dei dati
    private HashMap<Integer, HashMap<String, Object>> result;

    //indica il numero di record
    private int size;

    //costruttore
    public DatabaseResult() {

        //creo il contenitore dei dati
        result = new HashMap<Integer, HashMap<String, Object>>();
    }

    //aggiunta record di risultati
    public int addRecord() {

        //nuovo record
        HashMap<String, Object> record = new HashMap<String, Object>();

        //inserisco il record
        result.put(size, record);

        //incremento size
        size++;

        //ritorno l'indice del record
        return size -1;

    }

    //aggiunta di un field nel record
    public void addRecordField(int index, String key, Object value) {
        result.get(index).put(key, value);
    }

    //get field dal record
    public Object getRecordField(int index, String key) {
        return result.get(index).get(key);
    }

    //ritorna il numero di record contenuti
    public int size() {
        return result.size();
    }

}
