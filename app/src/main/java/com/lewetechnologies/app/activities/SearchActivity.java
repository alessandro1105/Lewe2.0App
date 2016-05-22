package com.lewetechnologies.app.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lewetechnologies.app.R;
import com.lewetechnologies.app.configs.Config;

public class SearchActivity extends AppCompatActivity {

    //creo il ricevitore di quando è stato trovato un device BT
    BroadcastReceiver onBTDeviceFoundReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            //se è stato trovato un dispositivo
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                //recupero il device trovato
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                onBTDeviceFound(device);


            }
        }
    };

    //bluetooth adapter
    BluetoothAdapter mBluetoothAdapter;

    //indica se è stato registrato il receiver
    private boolean onBTDeviceFoundReceiverRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //imposto la toolbar personalizzata
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //imposto il tasto indietro
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //inizializzo le variabili
        onBTDeviceFoundReceiverRegistered = false; //non ancora registrato il receiver

        //creao bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //se il bt non è abilitato chiedo l'attivazione
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, Config.REQUEST_ENABLE_BT);

        //il bluetooth è abilitato avvio la ricerca
        } else {
            startDiscovery();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //se il ricevitore è stato registrato lo disregistro
        if (onBTDeviceFoundReceiverRegistered) {
            stopDiscovery(); //fermo la ricerca
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //se la richiesta è quella di abilitare il bluetooth
        if (requestCode == Config.REQUEST_ENABLE_BT) {

            //se il bt è stato abilitato
            if (mBluetoothAdapter.isEnabled()) {
                startDiscovery();

            //il bt non è stato abilitato
            } else {
                stopProgressBarError(); //blocco la progress bar e metto l'immagine di errore

                //mostro toast con l'errore
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.activity_search_bt_not_enabled), Toast.LENGTH_LONG).show();
            }
        }
    }

    //metodo che avvia la ricerca tramite BT
    private void startDiscovery() {
        //registro il ricevitore
        registerReceiver(onBTDeviceFoundReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        onBTDeviceFoundReceiverRegistered = true;

        //inizio la ricerca del bluetooth
        mBluetoothAdapter.startDiscovery();

    }

    //metodo che avvia la ricerca tramite BT
    private void stopDiscovery() {
        //registro il ricevitore
        unregisterReceiver(onBTDeviceFoundReceiver);
        onBTDeviceFoundReceiverRegistered = false;
    }

    //metodo chiamato quando viene trovato un dispositivo BT
    private void onBTDeviceFound(BluetoothDevice device) {

        //se il nome del device trovato è quello di un leweband
        if (device.getName().equals(Config.LEWEBAND_BT_NAME)) {
            //bluetooth trovato

            //fermo al progress bar e metto l'icona di successo
            stopProgressBarSuccess();

            //mostro toast per indicare che il device è stato trovato
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.activity_search_leweband_found), Toast.LENGTH_LONG).show();

            //stoppo la ricerca
            stopDiscovery();

            //salvo nelle preferenze il device trovato
            SharedPreferences preferences = getApplicationContext().getSharedPreferences(Config.SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            //salvo i valori nelle preferenze
            editor.putString(Config.SHARED_PREFERENCE_KEY_DEVICE_MAC, device.getAddress()); //salvo il mac del device
            editor.putBoolean(Config.SHARED_PREFERENCE_KEY_FIRST_ASSOCIATION, true); //indico che è stato effettuata la prima associazione

            //salvo i cambiamenti
            editor.commit();

        }
    }


    //gestione della grafica
    private void stopProgressBarError() {
        //stop progress bar
        ProgressBar progress = (ProgressBar) findViewById(R.id.progress);
        progress.setIndeterminateDrawable(null);

        //change image icon
        ImageView image = (ImageView) findViewById(R.id.icon);
        image.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_error));
    }

    private void stopProgressBarSuccess() {
        //stop progress bar
        ProgressBar progress = (ProgressBar) findViewById(R.id.progress);
        progress.setIndeterminateDrawable(null);

        //change image icon
        ImageView image = (ImageView) findViewById(R.id.icon);
        image.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_success));
    }


}
