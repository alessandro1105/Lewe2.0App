package com.lewetechnologies.app.jack;

/**
 * Created by alessandro on 19/05/16.
 */
public interface JTransmissionMethod {

    public String receive(); //deve restituire il messaggio da passare a Jack
    public void send(String message); //invia il messaggio
    public boolean available(); //restituisce true se ci sono dati da ricevere nel buffer
}