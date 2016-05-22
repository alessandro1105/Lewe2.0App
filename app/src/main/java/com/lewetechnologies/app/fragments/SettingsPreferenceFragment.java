package com.lewetechnologies.app.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.lewetechnologies.app.R;
import com.lewetechnologies.app.configs.Config;

/**
 * Created by alessandro on 22/05/16.
 */
public class SettingsPreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings_main);


        //on click listener su "Exit" (richiede di chiudere l'app)
        Preference exitPreference = (Preference) findPreference("exit"); //search by key
        exitPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {

                //chiudo l'app
                sendExitRequest();

                return true;
            }
        });

        //on click listener su "Exit" (richiede di chiudere l'app
        final Preference factoryReset = (Preference) findPreference("factory_reset"); //search by key
        factoryReset.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {

                //eseguo il factory reset
                factoryReset();

                //chiudo l'app
                sendExitRequest();

                return true;
            }
        });

    }

    private void sendExitRequest() {
        //ritorno la richiesta di chiusura dell'app
        Intent exitIntent = new Intent();
        getActivity().setResult(Config.RESULT_EXIT_CODE, exitIntent);
        getActivity().finish();
    }

    private void factoryReset() {
        //accedo alle shared preference
        SharedPreferences preferences = getActivity().getApplicationContext().getSharedPreferences(Config.SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        //cancello tutte le preferenze
        editor.clear();

        //salvo
        editor.commit();

        //cancello tutto dal DB
        //DA SCRIVERE
    }
}
