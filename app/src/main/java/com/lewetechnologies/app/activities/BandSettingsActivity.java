package com.lewetechnologies.app.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lewetechnologies.app.R;
import com.lewetechnologies.app.fragments.SettingsBandPreferenceFragment;

public class BandSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction().replace(android.R.id.content, SettingsBandPreferenceFragment.newInstance(false)).commit();

        //TEST SOSTITUZIONE VISTA AL RICEVIMENTO DI UN INTENT
        /*registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                getFragmentManager().beginTransaction().replace(android.R.id.content, BandSettingsPreferenceFragment.newInstance(true)).commit();

            }
        }, new IntentFilter("test12345678"));*/

    }
}
