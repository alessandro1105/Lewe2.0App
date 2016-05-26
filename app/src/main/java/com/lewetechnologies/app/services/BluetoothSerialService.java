package com.lewetechnologies.app.services;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;

import com.lewetechnologies.app.configs.Config;
import com.lewetechnologies.app.jack.JData;
import com.lewetechnologies.app.jack.JTransmissionMethod;
import com.lewetechnologies.app.jack.Jack;
import com.lewetechnologies.app.logger.Logger;

import java.util.UUID;

/**
 * Created by alessandro on 26/05/16.
 */
public class BluetoothSerialService extends Service implements JTransmissionMethod {

    //---COSTANTI--

    //AZIONI PER INTENT IN ENTRATA (COMANDI)
    public final static String COMMAND_CONNECT = "com.lewetechnologies.app.services.COMMAND_CONNECT"; //connetti
    public final static String COMMAND_DISCONNECT = "com.lewetechnologies.app.services.COMMAND_DISCONNECT"; //disconnetti
    public final static String COMMAND_CONNECTION_STATUS = "com.lewetechnologies.app.services.COMMAND_CONNECTION_STATUS"; //status

    //estra per intent in entrata
    public final static  String EXTRA_DEVICE_ADDRESS = "com.lewetechnologies.app.services.EXTRA_DEVICE_ADDRESS"; //devic// e bt address

    //AZIONI PER INTENT IN USCITA
    public final static String ACTION_CONNECTED = "com.lewetechnologies.app.services.ACTION_BAND_CONNECTED"; //band connesso
    public final static String ACTION_DISCONNECTED = "com.lewetechnologies.app.services.ACTION_BAND_DISCONNECTED"; //band disconnesso
    public final static String ACTION_NEW_DATA = "com.lewetechnologies.app.services.ACTION_NEW_DATA"; //nuovi dati
    public final static String ACTION_CONNECTION_STATUS = "com.lewetechnologies.app.services.ACTION_CONNECTION_STATUS"; //band connesso

    //extra per intent in uscita
    public final static  String EXTRA_CONNECTION_STATUS = "com.lewetechnologies.app.services.EXTRA_CONNECTION_STATUS"; //devic// e bt address

    //tag per logger---
    private final static String TAG = BluetoothSerialService.class.getSimpleName();

    //uuid gatt
    private static final String SERIAL_SERVICE_UUID = "0000ffe0-0000-1000-8000-00805f9b34fb"; //servizio seriale
    private static final String SERIAL_CHARACTERISTC_TX = "0000ffe1-0000-1000-8000-00805f9b34fb"; //caratteristica di TX
    private static final String SERIAL_CHARACTERISTC_RX = "0000ffe1-0000-1000-8000-00805f9b34fb"; //caratteristica di RX

    //connection states
    public static final int STATE_DISCONNECTED = 0; //disconnesso
    public static final int STATE_CONNECTING = 1; //in attesa di connessione
    public static final int STATE_CONNECTED = 2; //connesso

    //caratteri per JTM
    private static final String JTM_START_SEQUENCE = "<";
    private static final String JTM_FINISH_SEQUENCE = ">";

    //jack
    Jack jack;


    //---VARIABILI---
    private String buffer; //message buffer

    private BluetoothManager mBluetoothManager; //bluetooth manager
    private BluetoothAdapter mBluetoothAdapter; //blueooth adapter
    private String mBluetoothDeviceAddress; //indirizzo device bluetooth
    private BluetoothGatt mBluetoothGatt; //blueooth GATT
    private int mConnectionState = STATE_DISCONNECTED; //stato del servizio (DISCONNECTED, CONNECTING, CONNECTED)
    private BluetoothGattCharacteristic characteristicTX; //caratteristica TX

    // Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {

                mConnectionState = STATE_CONNECTED; //cambio lo stato della connessione
                broadcastUpdate(ACTION_CONNECTED); //annuncio la connessione
                mBluetoothGatt.discoverServices(); //start discovery services

                Logger.i(TAG, "Device connected.");

                //avvio jack
                jack.start();
                Logger.i(TAG, "JACK start.");

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                mConnectionState = STATE_DISCONNECTED; //cambio lo stato della connessione
                broadcastUpdate(ACTION_DISCONNECTED); //annuncio la connessione

                //stoppo JACK
                jack.stop();
                Logger.i(TAG, "JACK stop.");

                //provo a riconnettere
                connect(mBluetoothDeviceAddress);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {

                //scorro tutti i servizi trovati
                for (BluetoothGattService gattService : gatt.getServices()) {

                    String uuid = gattService.getUuid().toString();

                    if (uuid.equals(SERIAL_SERVICE_UUID)) {
                        // get characteristic when UUID matches RX/TX UUID
                        characteristicTX = gattService.getCharacteristic(UUID.fromString(SERIAL_CHARACTERISTC_TX));

                        //abilita le notifiche sulla caratteristica di RX
                        mBluetoothGatt.setCharacteristicNotification(gattService.getCharacteristic(UUID.fromString(SERIAL_CHARACTERISTC_TX)), true);

                        //è stato trovato il servizio seriale
                        break;

                    }
                }

            } else {
                Logger.e(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS && characteristic.getUuid().toString().equals(SERIAL_CHARACTERISTC_RX)) {
                //leggo i dati
                final byte[] data = characteristic.getValue();

                //se ci sono dati
                if (data != null && data.length > 0) {
                    //aggiungo la stringa ricevuta al buffer
                    buffer += new String(data);
                }
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            if (characteristic.getUuid().toString().equals(SERIAL_CHARACTERISTC_RX)) {
                //leggo i dati
                final byte[] data = characteristic.getValue();

                //se ci sono dati
                if (data != null && data.length > 0) {
                    //aggiungo la stringa ricevuta al buffer
                    buffer += new String(data);
                }
            }
        }
    };


    //broadcast receiver per i command
    BroadcastReceiver commandReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (COMMAND_CONNECT.equals(intent.getAction())) {

                //prelevo l'indirizzo del device bt
                String address = intent.getStringExtra(EXTRA_DEVICE_ADDRESS);

                //connessione
                connect(address);

            } else if (COMMAND_DISCONNECT.equals(intent.getAction())) {

                //disconnessione
                disconnect();

            } else if (COMMAND_CONNECTION_STATUS.equals(intent.getAction())) {
                broadcastUpdate(COMMAND_CONNECTION_STATUS);
            }

        }
    };

    //invia un intent broadcast solo con action
    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);

        if (ACTION_CONNECTION_STATUS.equals(action)) {
            intent.putExtra(EXTRA_CONNECTION_STATUS, mConnectionState);

        }

        sendBroadcast(intent);
    }


    //connect al device bt
    private boolean connect(final String address) {
        //se è stato inizializzato
        if (mBluetoothAdapter == null || address == null) {
            Logger.e(TAG, "BluetoothAdapter not initialized or unspecified address.", Logger.SEVERE);
            return false;
        }

        //connessione ad un devivice precedentemente connesso
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress) && mBluetoothGatt != null) {
            Logger.i(TAG, "Trying to use an existing mBluetoothGatt for connection.");

            //connetto GATT
            if (mBluetoothGatt.connect()) {
                mConnectionState = STATE_CONNECTING; //imposto lo stato in "connessione in corso"
                return true;
            } else {
                return false;
            }
        }

        //connessione ad un nuovo device
        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Logger.e(TAG, "Device not found.  Unable to connect.", Logger.SEVERE);
            return false;
        }

        // We want to directly connect to the device, so we are setting the autoConnect parameter to false.
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);

        Logger.d(TAG, "Trying to create a new connection.");

        mBluetoothDeviceAddress = address; //salvo l'indirizzo
        mConnectionState = STATE_CONNECTING; //imposto che lo stato in "connessione in corso"
        return true;
    }

    //disconnette dal device bt
    private void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Logger.e(TAG, "BluetoothAdapter not initialized or unspecified address.", Logger.SEVERE);
            return;
        }

        //disconnetto GATT
        mBluetoothGatt.disconnect();
    }

    //chiude il servizio
    private void close() {

        //se non è mai stato instanziato gatt
        if (mBluetoothGatt == null) {
            return;
        }

        //chiude GATT
        mBluetoothGatt.close();
        mBluetoothGatt = null; //garbage collector

        //disregistro il commandReceiver
        unregisterReceiver(commandReceiver);
    }

    //inizializza il servizio
    private boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Logger.e(TAG, "Unable to initialize BluetoothManager.", Logger.SEVERE);
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Logger.e(TAG, "Unable to obtain a BluetoothAdapter.", Logger.SEVERE);
            return false;
        }

        //inizializzo Jack (valori di default per i tempi di polling)
        jack = new Jack(this) {

            //al ricevimento di un messaggio valido
            @Override
            public void onReceive(JData message, long id) {

                //preparo l'intent per avvisare l'app dei nuovi dati
                final Intent intent = new Intent(ACTION_NEW_DATA);

                intent.putExtra(Config.EXTRA_DATA_TEMPERATURE, message.getDouble(Config.JACK_TEMPERATURE));
                intent.putExtra(Config.EXTRA_DATA_GSR, message.getLong(Config.JACK_GSR));
                intent.putExtra(Config.EXTRA_DATA_TIMESTAMP, message.getLong(Config.JACK_TIMESTAMP));

                sendBroadcast(intent);

            }

            //al ricevimento di un ACK
            @Override
            public void onReceiveAck(long id) {
                //non deve fare niente
            }

            @Override
            public long getMessageID() {
                //timestamp per avere id unici
                return System.currentTimeMillis();
            }
        };

        //creo intent filter per il command receiver
        IntentFilter commandReceiverFilter = new IntentFilter();
        commandReceiverFilter.addAction(COMMAND_CONNECT);
        commandReceiverFilter.addAction(COMMAND_DISCONNECT);
        commandReceiverFilter.addAction(COMMAND_CONNECTION_STATUS);

        //registro il commandReceiver
        registerReceiver(commandReceiver, commandReceiverFilter);

        return true;
    }

    //---FUNZIONE DI JTM--
    @Override
    public String receive() {

        //se il buffer è vuoto
        if (buffer.equals("")) {
            return null; //non è stato prelevato nessun messaggio
        }

        //se nel buffer non è presente la sequenza di inizio
        if (!buffer.contains(JTM_START_SEQUENCE)) {
            buffer = ""; //azzero il buffer
            return null; //non è stato prelevato nessun messaggio
        }

        //elimino i caratteri errati prima della sequenza di inizio
        buffer = buffer.substring(buffer.indexOf(JTM_START_SEQUENCE) + JTM_FINISH_SEQUENCE.length());

        //se il buffer non contiene la sequenza finale
        if (!buffer.contains(JTM_FINISH_SEQUENCE)) {
            buffer = ""; //azzero il buffer
            return null; //non è stato prelevato nessun messaggio
        }

        //prelevo il messaggio
        String message = buffer.substring(0, buffer.indexOf(JTM_FINISH_SEQUENCE));

        //elimino il messaggio dal buffer
        buffer = buffer.substring(buffer.indexOf(JTM_FINISH_SEQUENCE) + JTM_FINISH_SEQUENCE.length());

        //ritorno il messaggio prelevato
        return message;
    }

    @Override
    public void send(String message) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Logger.e(TAG, "BluetoothAdapter not initialized", Logger.SEVERE);
            return;
        }

        if (mConnectionState == STATE_CONNECTED) {
            Logger.d(TAG, "Sending: " + message);

            //converto in byte il messaggio
            final byte[] tx = message.getBytes();

            //sctivo la caratteristica TX
            characteristicTX.setValue(tx);

            //scrivo la caratteristica
            mBluetoothGatt.writeCharacteristic(characteristicTX);

        }

    }

    @Override
    public boolean available() {
        return buffer.equals("");
    }

    //---FUNZIONI PER IL BINDER

    @Override
    public IBinder onBind(Intent intent) {
        initialize();
        return new LocalBinder();
    }

    //ritorna il binder
    public class LocalBinder extends Binder {
        BluetoothSerialService getService() {
            return BluetoothSerialService.this;
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
        close();
        return super.onUnbind(intent);
    }
}
