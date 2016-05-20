package com.lewetechnologies.app.jack;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by alessandro on 19/05/16.
 */

public abstract class Jack {

    private long TIME_BEFORE_RESEND = 2000; //tempo del timer in ms
    private boolean SEND_ONE_TIME = false;

    private static final long TIMER_POLLING_RECEIVE = 500; //tempo di sleep thread ricezione
    private static final long TIMER_POLLING_SEND = 300; //tempo sleep thread invio


    public static final String MESSAGE_TYPE = "message_type"; //campo dentro messageJackData che contine il tipo di mex
    //private static final String MESSAGE_DATA = "message_data"; //campo contente dati nel messaggio tipo dati

    private static final String MESSAGE_ID = "id";

    private static final String MESSAGE_TYPE_ACK = "ack"; //messaggio ack per qualcosa inviato
    private static final String MESSAGE_TYPE_DATA = "values"; //messaggio normale contenente dati

    private static final String MESSAGE_BOOLEAN_TRUE = "t"; //simobolo true in booleano nel mex
    private static final String MESSAGE_BOOLEAN_FALSE = "f"; //simbolo false nel mex



    private JTransmissionMethod mmJTM; //contiene il metodo di trasmissione da usare e deve rispettare l'interfaccia



    private GetMessageThread getMessageThread; //thread get message

    private SendMessageThread sendMessageThread; //thread send message



    private boolean stopThread = true;

    private HashMap<Long, String> sendMessageBuffer; //buffer per i mex da inviare
    private HashMap<Long, Long> sendMessageTimer; //buffer dei timer per i mex da inviare
    private HashMap<Long, JData> sendMessageBufferJData; //buffer per i messaggi da inviare nel formato JData

    private HashMap<Long, String> sendAckBuffer; //buffer che contiene i mex ack da inviare (non necessitano di timer)


    private HashMap<Long, Integer> idMessageReceived; //buffer che contiene gli id dei messaggi ricevuti per evitare duplicazioni nei dati


    public Jack(JTransmissionMethod mmJTM) { //normale

        this.mmJTM = mmJTM;


        //buffer
        sendMessageBuffer = new HashMap<Long, String>(); //buffer messaggi

        sendMessageTimer = new HashMap<Long, Long>(); //timer per invio messaggi

        sendMessageBufferJData = new HashMap<Long, JData>(); //buffer messaggi da inviare formato JData

        sendAckBuffer = new HashMap<Long, String>(); //buffer ack

        idMessageReceived = new HashMap<Long, Integer>(); //buffer id messaggi già ricevuti



        //thread
        getMessageThread = new GetMessageThread(TIMER_POLLING_RECEIVE); //creo thread per get message e lo avvio

        sendMessageThread = new SendMessageThread(TIMER_POLLING_SEND); //thread invio messaggi


        //faccio partire i thread per il polling
        getMessageThread.startThread();

        sendMessageThread.startThread();

    }

    public void finalize() {

        //stoppo i thread
        getMessageThread.stopThread();

        sendMessageThread.stopThread();

    }


    public Jack(JTransmissionMethod mmJTM, long timeBeforeResend) {//costruttore con tempo per il reinvio

        this(mmJTM);

        TIME_BEFORE_RESEND = timeBeforeResend;
    }


    public Jack(JTransmissionMethod mmJTM, boolean sendOneTime) {//costruttore con impostazione che invia il mex una volta (no polling se non ricevo ack)

        this(mmJTM);

        SEND_ONE_TIME = sendOneTime;

    }



    public void start() { //start thread

        Logger.d("Jack", "starting...");

        stopThread = false;


    }

    public void stop() { //stop thread

        Logger.d("Jack", "stopping...");

        stopThread = true;

        Logger.d("Jack", "Stopped");
    }

    public void flushBufferSend() { //funziona che cancella il buffer dei mex da inviare

        sendMessageBuffer = new HashMap<Long, String>(); //reset buffer messaggi da inviare
        sendMessageBufferJData = new HashMap<Long, JData>();

    }

    private synchronized void execute(String data) { //corpo centrale della classe e decide cosa deve fare

        final String message = data;

        Thread execute = new Thread() {

            public void run() {

                Logger.d("Jack", "executing...");

                if (validate(message)) { //verifico che il messaggio ricevuto sia conforme al protocollo

                    JData messageJData = getJDataMessage(message);

                    if (messageJData.getValue(MESSAGE_TYPE).equals(MESSAGE_TYPE_DATA)) {

                        if (!checkMessageAlreadyReceived(messageJData)) {

                            //Logger.d("value", "" + messageJData.get("GSR"));

                            Logger.d("Jack", "onReceive");

                            onReceive(messageJData); //richiamo onReceive e passo l'oggetto messageJData che contiene le info del mex + il mex

                        }

                    } else {

                        checkAck(messageJData); //ack elimino il pacchetto da quelli da inviare

                    }

                }

                Logger.d("Jack", "executed");

            }

        };

        execute.start();

    }



    //verifica che il messaggio non sia già stato ricevuto (reinviato per non ricezione ack)
    private boolean checkMessageAlreadyReceived(JData message) {

        //grezza validazione dei dati da sostituire con il metodo validate
        if (!message.containsKey(MESSAGE_ID)) { //se non presente id incorrerei in errori e quindi blocco elaborazione

            return true; //con true blocco l'elaborazione del messaggio come se fosse già stato ricevuto
        }
        // fine grezza validazione del messaggio


        sendAck(message); //invio ack per problema scadenza timer (invio anche se già ricevuto per perdita o ritardo ack precedente)

        if (!idMessageReceived.containsKey(message.getValue(MESSAGE_ID))) {

            idMessageReceived.put((Long) message.getValue(MESSAGE_ID), 0); //è importante solo la chiave non il valore (cast Long dell'id)

            return false; //messaggio non già ricevuto

        } else {

            return true; //messaggio già ricevuto

        }

    }


    //valida il messaggio
    private boolean validate(String message) {

        Logger.d("Jack", "validating...");

        Logger.d("Jack", "validate");

        return true;
    }


    private JData getJDataMessage(String message) {

        Logger.d("Jack", "getting Jdata from message...");

        JData messageJData = new JData();

        //this.messageJData = new JData();



        String temp = "";
        String temp2 = "";

        int nChar = 0;

        boolean value;


        message = message.substring(2); //elimino 2 caratteri iniziali

        for(int i = 0; i < 2; i++) {

            temp = "";


            if (message.startsWith(MESSAGE_ID)) { //indicazione id

                message = message.substring(MESSAGE_ID.length() + 2); //elimino dal mex id + 2 caratteri (":)

                for (int x = 0; message.charAt(x) != ','; x++) { //prelevo l'id e lo memorizzo in temp
                    temp += message.charAt(x);
                }


                if (i < 1) //manca ancora id
                    message = message.substring(temp.length() + 2); //elimino dal mex la lunghezza dell'id + 2

                messageJData.add(MESSAGE_ID, Long.parseLong(temp)); //converto in long l'id

                Logger.d("id", temp);

            } else if (message.startsWith(MESSAGE_TYPE_ACK)) { //indicazione ack  messaggio ack

                Logger.d("message type", "ack");

                messageJData.add(MESSAGE_TYPE, MESSAGE_TYPE_ACK);

                if (i < 1) //sono al primo giro e manca ancora l'id
                    message = message.substring(MESSAGE_TYPE_ACK.length() + 5); //elimino la lunghezza di ack + 5 caratteri
            } else if (message.startsWith(MESSAGE_TYPE_DATA)) { //indicazione values messaggio contenente dati

                Logger.d("message type", "data");

                messageJData.add(MESSAGE_TYPE, MESSAGE_TYPE_DATA);

                message = message.substring(MESSAGE_TYPE_DATA.length() + 5);

                //azzero le variabili prima di entrare nel ciclo
                value = false;
                temp = "";
                temp2 = "";
                nChar = 0;

                for (int x = 0; message.charAt(x) != ']'; x++) { //scorro i caratteri di message

                    nChar++; //serve per contare i carattri che elimenerò da message

                    if (message.charAt(x) == ',' || message.charAt(x) == '}') { //store value nel JData

                        if (temp2.charAt(0) == '"') { //stringa

                            messageJData.add(temp, temp2.substring(1, temp2.length() - 1));

                        } else if (temp2.contains(".")) { //double

                            messageJData.add(temp, Double.parseDouble(temp2));

                        } else if (temp2 == MESSAGE_BOOLEAN_TRUE || temp2 == MESSAGE_BOOLEAN_FALSE) { // boolean

                            if (temp2 == MESSAGE_BOOLEAN_TRUE) { //true

                                messageJData.add(temp, true);

                            } else { //false

                                messageJData.add(temp, false);

                            }

                        } else { //long

                            messageJData.add(temp, Long.parseLong(temp2));

                        } //fine switch tipi


                        Logger.d(temp, "" + messageJData.getValue(temp)); //stampo dal JData

                        //azzero i valori
                        value = false;
                        temp = "";
                        temp2 = "";

                    } else if (message.charAt(x) == ':') { //passo da caratteri della chiave a caratteri del valore

                        value = true;

                    } else if (!value && message.charAt(x) != '"') { //value = true caratteri CHIAVE

                        temp += message.charAt(x);

                        //Logger.d("key", temp);


                    } else if (value) { //caratteri del VALORE value = false

                        temp2 += message.charAt(x);

                        //Logger.d("value", temp2);

                    }

                } //fine for values


                //Log.e("Jack", "" + i);


                if (i < 1) //manca ancora id
                    message = message.substring(nChar + 3);


            } //fine values


        }

        Logger.d("Jack", "JData getted");

        return messageJData;
    }


    public void send(JData data) {

        final JData message = data;

        Thread execute = new Thread() {

            public void run() {

                Logger.d("Jack", "creating message to send...");


                long id = getTimestamp(); //id = timestamp

                Logger.e("Jack send", "id1: " + id);

                String messageString = "{\"" + MESSAGE_ID + "\":" + id + ",\"" + MESSAGE_TYPE_DATA + "\":[{"; //intenstazione id + values


                for(int i = 0; i < message.size(); i++) {

                    messageString += "\"" + message.getKey(i) + "\":";



                    if (message.getValue(i) instanceof Integer) { //type integer

                        messageString += ((Integer) message.getValue(i)).toString();

                    } else if (message.getValue(i) instanceof Double) { //type double

                        messageString += ((Double) message.getValue(i)).toString();

                    } else if (message.getValue(i) instanceof Boolean) { //boolean traducon i valori impostati

                        if ((Boolean) message.getValue(i)) {

                            messageString += MESSAGE_BOOLEAN_TRUE;

                        } else {

                            messageString += MESSAGE_BOOLEAN_FALSE;

                        }

                    } else if (message.getValue(i) instanceof String) { //stringa aggiungo "" inzio e fine

                        messageString += "\"" + ((String) message.getValue(i)).toString() + "\"";


                    } if (message.getValue(i) instanceof Long) { //type long

                        messageString += ((Long) message.getValue(i)).toString();

                    }

                    messageString += ","; //metto la virgola per separaere i valori

                }


                messageString = messageString.substring(0, messageString.length() -1); //elimino l'ultima virgola

                messageString += "}]}"; //messaggio in stringa creato




                sendMessageBuffer.put(id, messageString); //carico il mex nel buffer (sarà spedito automaticamente)
                sendMessageBufferJData.put(id, message);

                Logger.e("Jack send", "id2: " + id);

                Logger.d("Jack", "message put in buffer");
            }

        };


        execute.start();


    }


    //verifico l'ack
    private void checkAck(JData message) {

        final JData data = message;

        Thread execute = new Thread() {

            public void run() {



                //grezza validazione dei dati da sostituire con il metodo validate
                if (!data.containsKey(MESSAGE_ID)) { //se non presente id incorrerei in errori e quindi blocco elaborazione

                    return; //con true blocco l'elaborazione del messaggio come se fosse già stato ricevuto
                }
                // fine grezza validazione del messaggio




                long id = (Long) data.getValue(MESSAGE_ID);

                Logger.d("Jack", "checking ack... id: " + id);

                Logger.d("Jack", "" + sendMessageBufferJData.containsKey(id));

                for (long key: sendMessageBufferJData.keySet()) {

                    Logger.e("Jack", "" + key);

                }


                if (sendMessageBufferJData.size() > 0) { //verifico che esistano messaggi in attesa di conferma


                    if (sendMessageBufferJData.containsKey(id)) { //verifico che l'id conetnuto ack esista

                        if (sendMessageBuffer.containsKey(id)) {
                            sendMessageBuffer.remove(id); //elimino il messaggio (CONFERMO) non verrà più reinviato
                        }

                        onReceiveAck(sendMessageBufferJData.get(id)); //richiamo metodo astratto invocato al ricevimento di un ack

                        sendMessageBufferJData.remove(id);

                        Logger.d("Jack", "message confirmed...");
                    }
                }
            }

        };

        execute.start();

    }

    //creo ack e lo invio
    private void sendAck(JData message) { //invio ack

        Logger.d("JACK", "ack sending...");

        final JData data = message;

        Thread execute = new Thread() {

            public void run() {

                String messageString = "{\"" + MESSAGE_ID + "\":" + data.getValue(MESSAGE_ID) + ",\"" + MESSAGE_TYPE_ACK + "\":1}"; //intenstazione id + values

                sendAckBuffer.put((Long) data.getValue(MESSAGE_ID), messageString); //carico il mex nel buffer (sarà spedito automaticamente)

                Logger.d("JACK", "ack put in buffer");
            }

        };

        execute.start();

    }


    abstract public void onReceive(JData message); //medoto da implementare e deve contere le istruzioni
    //eseguire al ricevimento del messaggio

    abstract public void onReceiveAck(JData messageConfirmed); //metodo invocato al ricev1imento di un ack (contiene messaggio inviato da cui prelevare i dati)

    abstract protected long getTimestamp();




    //thread che continua a contattare il JTM e vedere se ci sono messaggi
    private class GetMessageThread extends ThreadSafe {

        public GetMessageThread(long timerPolling) {
            super(timerPolling);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void execute() {

            if (!stopThread) {

                if (mmJTM.available()) {

                    String message = mmJTM.receive();

                    if (message.length() > 0) {
                        Jack.this.execute(message);
                    }

                }
            }

        }

    }//fine thread




    //thread dedicato all'invio dei messaggi e a reinviare quelli non confermati entro lo scadere di un timer
    private class SendMessageThread extends ThreadSafe {

        public SendMessageThread(long timerPolling) {
            super(timerPolling);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void execute() {

            if (!stopThread) {

                //invio prima gli ack perchè possono scadere
                if (sendAckBuffer.size() > 0) {

                    Iterator<Long> iter = sendAckBuffer.keySet().iterator();

                    while(iter.hasNext()) {

                        long key = iter.next();

                        mmJTM.send("" + sendAckBuffer.get(key));

                        iter.remove();

                        Logger.d("JACK", "ack sent " + key);

                    }

					/*
					for (long key :  sendAckBuffer.keySet())  {

						mmJTM.send("" + sendAckBuffer.get(key));

						sendAckBuffer.remove(key); //elimino ack dal buffer (basta inviarlo 1 volta sola)

						Logger.d("JACK", "ack sent " + key);

					}*/

                }

                //invio messaggi dopo perchè non scadono
                if (sendMessageBuffer.size() > 0) {

                    Iterator<Long> iter = sendMessageBuffer.keySet().iterator();

                    while(iter.hasNext()) {

                        long key = iter.next();


                        if (sendMessageTimer.containsKey(key)) { //verifico la presenza della chiave nell'array del timer
                            if ((System.currentTimeMillis() - (Long) sendMessageTimer.get(key)) >= TIME_BEFORE_RESEND ) {

                                mmJTM.send(sendMessageBuffer.get(key)); //invio il messaggio
                                Logger.e("Jack invio message", sendMessageBuffer.get(key));

                                sendMessageTimer.remove(key); //elimino il vecchio valore del timer
                                sendMessageTimer.put(key, System.currentTimeMillis()); //setto il nuovo valore

                            }
                        } else { //non è ancora stato inviato il mex

                            mmJTM.send(sendMessageBuffer.get(key)); //invio il messaggio

                            if (!SEND_ONE_TIME) {

                                sendMessageTimer.put(key, System.currentTimeMillis()); //setto il nuovo valore
                            } else {

                                iter.remove(); //elimino il mex dal buffer

                            }

                        }


                    }

					/*
					for (long key :  sendMessageBuffer.keySet())  {

						if (sendMessageTimer.containsKey(key)) { //verifico la presenza della chiave nell'array del timer
							if ((System.currentTimeMillis() - (Long) sendMessageTimer.get(key)) >= TIME_BEFORE_RESEND ) {

								mmJTM.send("" + sendMessageBuffer.get(key)); //invio il messaggio

								sendMessageTimer.remove(key); //elimino il vecchio valore del timer
								sendMessageTimer.put(key, System.currentTimeMillis()); //setto il nuovo valore

							}
						} else { //non è ancora stato inviato il mex

							mmJTM.send("" + sendMessageBuffer.get(key)); //invio il messaggio

							if (!SEND_ONE_TIME) {

								sendMessageTimer.put(key, System.currentTimeMillis()); //setto il nuovo valore
							} else {

								sendMessageBuffer.remove(key);

							}

						}



					}*/

                } //fine if sendMessageBuffer

            }

        }



    }//fine thread send




}//fine classe

