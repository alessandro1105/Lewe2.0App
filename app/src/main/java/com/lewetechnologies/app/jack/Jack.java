package com.lewetechnologies.app.jack;

import com.lewetechnologies.app.logger.Logger;
import com.lewetechnologies.app.thread.CyclicalThread;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by alessandro on 20/05/16.
 */
public abstract class Jack {

    //---COSTANTI---
    private static final String JK_MESSAGE_TYPE = "type"; //key tipo messaggio
    private static final String JK_MESSAGE_TYPE_ACK = "ack"; //tipo ack

    private static final String JK_MESSAGE_TYPE_DATA = "data"; //tipo dati
    private static final String JK_MESSAGE_ID = "id"; //id messaggio

    private static final long JK_TIMER_RESEND_MESSAGE = 1000;//tempo (ms) da attendere prima di reinviare i messaggi non confermati
    private static final long JK_TIMER_POLLING  = 100; //tempo (ms) da attendere tra un polling e un altro del mezzo di strasmissione


    //---VARIABILI---
    //mmJTM
    private JTransmissionMethod mmJTM; //mmJTM

    //timer
    private long timerSendMessage; //tempo (ms) da attendere prima di reinviare i messaggi non confermati

    //thread che gestisce il polling
    private PollingThread pollingThread;

    //contenitori dei messaggi
    private HashMap<Long, String> messageBuffer; //buffer per i messaggi da inviare


    //---HANDLER USER SPECIFIED---
    public abstract void onReceive(JData message, long id); //handler onReceive
    public abstract void onReceiveAck(long id); //handler onReceiveAck
    public abstract long getMessageID(); //deve ritornare un id valido per il messaggio


    //---PUBLIC---

    //costruttore con tutti i parametri (mmJTM, timerResendMessage, timerPolling)
    public Jack(JTransmissionMethod mmJTM, long timerSendMessage, long timerPolling) {

        //salvo mmJTM
        this.mmJTM = mmJTM;

        //salvo i valori dei timer
        this.timerSendMessage = timerSendMessage;

        //creo il contenitore dei messaggi
        this.messageBuffer = new HashMap<Long, String>();

        //creo il thread di polling
        this.pollingThread = new PollingThread(timerPolling);

    }

    //costruttore con solo mmJTM
    public Jack(JTransmissionMethod mmJTM) {
        this(mmJTM, JK_TIMER_RESEND_MESSAGE, JK_TIMER_POLLING);
    }


    //funzione di avvio
    public void start() {
        Logger.i("Jack", "Start Jack protocol");

        //avvio il thread del polling
        this.pollingThread.startThread();

    }

    //funzione di stop
    public void stop() {
        Logger.i("Jack", "Stop Jack protocol");

        //stoppo il thread del polling
        this.pollingThread.stopThread();

    }

    //funzione per svuotare il buffer di invio
    public void flushBufferSend() {

        //creo un nuovo contenitore dei dati (Garbage collector elimina il vecchio)
        this.messageBuffer = new HashMap<Long, String>();

    }

    //funzione per inviare un messaggio
    public long send(JData message) {

        return 0;

    }


    //---PRIVATE---

    //funzione che elabora il messaggio
    private void execute(String messageJSON) {

        try {

            //parso il messaggio
            JSONObject root = new JSONObject(messageJSON);

            //verifico se possiede la chiave per il tipo di messaggio
            if (!root.has(JK_MESSAGE_TYPE)) {
                return; //non ha la chiave quindi il messaggio non è valido
            }

            //prelevo la stringa del tipo di messaggio
            String type = root.getString(JK_MESSAGE_TYPE);

            //se è un messaggio dati
            if (type.equals(JK_MESSAGE_TYPE_DATA)) {




            //se è un messaggio di ACK
            } else if (type.equals(JK_MESSAGE_TYPE_ACK)) {

                //verifico se possiede la chiave per l'id del messaggio
                if (!root.has(JK_MESSAGE_ID)) {
                    return; //non ha la chiave quindi il messaggio non è valido
                }

                //ottengo l'id del messaggio
                long id = root.getLong(JK_MESSAGE_ID);

                //chiamo la funzione di gestione degli ack
                checkAck(id);

            }

        } catch (JSONException e) {

            //JSON non valido
            return; //messaggio non valido

        }

    }

    //funzione che invia il messaggio ACK
    private void sendAck(long id) {

        try {
            //creo la root del messaggio JSON
            JSONObject root = new JSONObject();

            //inserisco i dati
            root.put(JK_MESSAGE_ID, id);//id del messaggio da confermare
            root.put(JK_MESSAGE_TYPE, JK_MESSAGE_TYPE_ACK); //il messaggio è un ACK

            //invio il messaggio
            this.mmJTM.send(root.toString());

        } catch (JSONException e) {
            Logger.e("Jack", "onSendAck", e, Logger.SEVERE);
        }

    }

    //funzione che controlla un messaggio ACK
    private void checkAck(long id) {

        //se l'id corrisponde ad un messaggio nel buffer
        if (this.messageBuffer.containsKey(id)) {

            //elimino il messaggio
            this.messageBuffer.remove(id);

            //richiamo l'handler dell'utente
            onReceiveAck(id);
        }

    }


    //thread per controllare periodicamente il mezzo di trasmissione
    public class PollingThread extends CyclicalThread {

        private long timeLastSend;


        public PollingThread(long timer) {
            super(timer);

            //inizializzo le varibili
            timeLastSend = 0; //0 significa non ancora inviato
        }

        @Override
        public void execute() {

            //controllo se ci sono messaggi da ricevere
            if (Jack.this.mmJTM.available()) {

                String message = Jack.this.mmJTM.receive();

                if (!message.equals("")) {
                    Jack.this.execute(message);
                }

            }

            //controllo se posso inviare i messaggi e se ce ne sono da inviare
            if (System.currentTimeMillis() - timeLastSend >= Jack.this.timerSendMessage && Jack.this.messageBuffer.size() > 0) {

                //salvo l'ultimo invio dei messaggi
                timeLastSend = System.currentTimeMillis();

                //creo iteratore del contenitore dei messaggi
                Iterator<Long> iter = Jack.this.messageBuffer.keySet().iterator();

                //finchè ci sono messaggi
                while(iter.hasNext()) {

                    //prelevo la chiave
                    long key = iter.next();

                    //invio il messaggio
                    Jack.this.mmJTM.send("" + Jack.this.messageBuffer.get(key));
                }

            }

        }
    }

}
