package com.lewetechnologies.app.jack;
import java.util.HashMap;

/**
 * Created by alessandro on 19/05/16.
 */

//classe contenitore per i dati da inviare con jack
public class JData {

    private int size = 0; //numero di elementi
    private HashMap<String, Object> data = new HashMap<String, Object>(); //buffer key value
    private HashMap<Integer, String> index = new HashMap<Integer, String>(); //buffer index key

    //costruttore
    public JData() {
        ; //costruttore vuoto
    }

    //metodo per aggiungere un valore
    public void add(String key, Object value) { //add
        data.put(key, value); //metto key value nel buffer 1
        index.put(this.size, key); //metto index key nel buffer 2
        this.size++; //incremento la size
    }

    //metodo per ottenere un valore (chiave string)
    public Object getValue(String key) {
        return data.get(key); //get from key
    }

    //metodo per ottenere un valore (chiave indice)
    public Object getValue(int index) {
        return data.get(this.index.get(index)); //prelevo la chiave tramite indice e la uso per prelevare l'oggetto value
    }

    //metodo per ottenere una chiave string a partire dal suo indice
    public Object getKey(int index) { //prelevo la chiave con index
        return this.index.get(index);
    }

    //indica il numero di elementi contenuti
    public int size() { //restituisco la dimensione
        return this.size;
    }

    //verifica se il contenitore contiene una specifica chiave string
    public boolean containsKey(String key) { //metodo che controlla l'esistenza di una chiave
        return data.containsKey(key);
    }

}
