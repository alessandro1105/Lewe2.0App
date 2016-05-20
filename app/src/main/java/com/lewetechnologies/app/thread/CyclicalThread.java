package com.lewetechnologies.app.thread;

/**
 * Created by alessandro on 20/05/16.
 */

//classe che gestisce un thread ciclico
public abstract class CyclicalThread extends Thread {

    private long timerPolling; //indica ogni quanto eseguire il metodo execute
    private boolean threadEnabled; //variabile che se true fa terminare naturalmente il thread

    //costruttore
    public CyclicalThread(long timer) {
        super();

        //imposto il timer di polling
        timerPolling = timer;

        //indico che il thread non è abilitato
        threadEnabled = false;
    }

    //stop thread
    public void startThread() { //avvia il thread

        //dico che il thread è abilitato
        threadEnabled = true;

        //faccio iniziare il thread
        super.start();
    }


    public void stopThread() { //stoppa il thread

        //fermo il thread
        threadEnabled = false;
    }

    //metodo run
    public void run() {

        new Runnable() {

            @Override
            public void run() {
                while (!threadEnabled) {

                    //richiamo il metodo user specified
                    execute();

                    //provo a mettere in pausa il thread
                    try {
                        Thread.sleep(timerPolling); //metto in  pausa il thread

                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }

        }.run(); //avvio il runnable

    }

    //metodo che contiene il corpo del thread da eseguire ripetutamente
    abstract public void execute();

}
