package com.lewetechnologies.app.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lewetechnologies.app.fragments.SettingsBandPreferenceFragment;

public class BandSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction().replace(android.R.id.content, SettingsBandPreferenceFragment.newInstance(false)).commit();

    }
}
