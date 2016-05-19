package com.lewetechnologies.app.activities;

import android.content.Intent;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lewetechnologies.app.R;
import com.lewetechnologies.app.configs.Config;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsMainPreferenceFragment()).commit();

    }

    public static class SettingsMainPreferenceFragment extends PreferenceFragment {

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
                    SettingsMainPreferenceFragment.this.getActivity().setResult(Config.RESULT_EXIT_CODE, exitIntent);
                    SettingsMainPreferenceFragment.this.getActivity().finish();


                    return true;
                }
            });

        }
    }
}
