package com.lewetechnologies.app.jack;

import com.lewetechnologies.app.logger.Logger;
import com.lewetechnologies.app.thread.CyclicalThread;

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
    private long timerResendMessage; //tempo (ms) da attendere prima di reinviare i messaggi non confermati
    private long timerPolling; //tempo (ms) da attendere tra un polling e un altro del mezzo di strasmissione

    //tempi
    private long timeLastPolling; //ultimo accesso a mmJTM
    private long timeLastSend; //ultimo invio dei messaggi

    //polling
    private boolean pollingEnabled; //indica se è abilitato il polling




    //---HANDLER USER SPECIFIED---
    public abstract void onReceive(JData message, long id); //handler onReceive
    public abstract void onReceiveAck(long id); //handler onReceiveAck
    public abstract long getMessageID(); //deve ritornare un id valido per il messaggio


    //---PUBLIC---

    //costruttore con tutti i parametri (mmJTM, timerResendMessage, timerPolling)
    public Jack(JTransmissionMethod mmJTM, long timerResendMessage, long timerPolling) {

        //salvo mmJTM
        this.mmJTM = mmJTM;

        //salvo i valori dei timer
        this.timerResendMessage = timerResendMessage;
        this.timerPolling = timerPolling;

        //inizializzo le varibili
        this.timeLastSend = 0; //0 indica non ancora effettuato
        this.timeLastPolling = 0; //0 indica non ancora effettuato

    }

    //costruttore con solo mmJTM
    public Jack(JTransmissionMethod mmJTM) {
        this(mmJTM, JK_TIMER_RESEND_MESSAGE, JK_TIMER_POLLING);
    }


    //funzione di avvio
    public void start() {
        Logger.i("JACK", "Start Jack protocol");

        //abilito il polling del mezzo di trasmissione
        this.pollingEnabled = true;

    }

    //funzione di stop
    public void stop() {
        Logger.i("JACK", "Stop Jack protocol");

        //abilito il polling del mezzo di trasmissione
        this.pollingEnabled = false;

    }

    //funzione per svuotare il buffer di invio
    public void flushBufferSend() {

    }

    //funzione per inviare un messaggio
    public long send(JData message) {

        return 0;

    }


    //---PRIVATE---

    //funzione che elabora il messaggio
    private void execute(String messageJSON) {

    }

    //funzione che invia il messaggio ACK
    private void sendAck(long id) {

    }

    //funzione che controlla un messaggio ACK
    private void checkAck(long id) {

    }


    //thread per controllare periodicamente il mezzo di trasmissione
    public class ThreadLoop extends CyclicalThread {


        public ThreadLoop(long timer) {
            super(timer);
        }

        @Override
        public void execute() {

            //se il polling è abilitato
            if (Jack.this.pollingEnabled) {

                

            }

        }
    }

}
