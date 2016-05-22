package com.lewetechnologies.app.fragments;

import android.content.Intent;
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


        //on click listener su "Exit" (richiede di chiudere l'app
        Preference exitPreference = (Preference) findPreference("exit"); //search by key
        exitPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {

                //ritorno la richiesta di chiusura dell'app
                Intent exitIntent = new Intent();
                getActivity().setResult(Config.RESULT_EXIT_CODE, exitIntent);
                getActivity().finish();


                return true;
            }
        });

    }
}
