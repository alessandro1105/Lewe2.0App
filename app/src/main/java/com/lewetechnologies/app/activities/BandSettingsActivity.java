package com.lewetechnologies.app.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lewetechnologies.app.configs.Config;
import com.lewetechnologies.app.fragments.SettingsBandPreferenceFragment;

public class BandSettingsActivity extends AppCompatActivity {

    //shared preference
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //prelevo le shared preference
        preferences = getApplicationContext().getSharedPreferences(Config.SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);

        //Creo il fragment passando lo stato della connessione con il band
        getFragmentManager().beginTransaction().replace(android.R.id.content,
            SettingsBandPreferenceFragment.newInstance())
            .commit();

    }
}
