package com.lewetechnologies.app.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lewetechnologies.app.R;

public class BandSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction().replace(android.R.id.content, BandSettingsPreferenceFragment.newInstance(false)).commit();

        //TEST SOSTITUZIONE VISTA AL RICEVIMENTO DI UN INTENT
        /*registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                getFragmentManager().beginTransaction().replace(android.R.id.content, BandSettingsPreferenceFragment.newInstance(true)).commit();

            }
        }, new IntentFilter("test12345678"));*/

    }

    public static class BandSettingsPreferenceFragment extends PreferenceFragment {

        public static BandSettingsPreferenceFragment newInstance(boolean status) {

            //istanzio il fragment
            BandSettingsPreferenceFragment fragment = new BandSettingsPreferenceFragment();

            //creo il contenitore dei parametri
            Bundle args = new Bundle();

            //inserisco i parametri
            args.putBoolean("status", status);

            //setto i parametri
            fragment.setArguments(args);

            //ritorno il fragment
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.settings_band);

            //prelevo la preferenza
            Preference status = (Preference) findPreference("status");

            //setto il summaty in base al parametro passato
            if (getArguments().getBoolean("status", false)) {
                status.setSummary(getString(R.string.activity_settings_band_status_summary_connected));

            } else {
                status.setSummary(getString(R.string.activity_settings_band_status_summary_disconnected));
            }

            //TEST INTENT PER CAMBIO VISTA
            /*status.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    Intent intent = new Intent("test12345678");

                    getActivity().sendBroadcast(intent);

                    return false;
                }
            });*/

        }

    }
}
