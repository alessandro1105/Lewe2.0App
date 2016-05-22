package com.lewetechnologies.app.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lewetechnologies.app.fragments.SettingsPreferenceFragment;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsPreferenceFragment()).commit();

    }


}
