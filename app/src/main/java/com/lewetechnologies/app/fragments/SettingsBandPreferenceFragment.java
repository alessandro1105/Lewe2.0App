package com.lewetechnologies.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.lewetechnologies.app.R;

/**
 * Created by alessandro on 22/05/16.
 */
public class SettingsBandPreferenceFragment extends PreferenceFragment {

    public static SettingsBandPreferenceFragment newInstance(boolean status) {

        //istanzio il fragment
        SettingsBandPreferenceFragment fragment = new SettingsBandPreferenceFragment();

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

                preference.setSummary("HEY COME VA?");

                return false;
            }
        });*/

    }

}
