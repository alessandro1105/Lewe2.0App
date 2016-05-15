package com.lewetechnologies.app;

import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

             /*
            Preference myPref = (Preference) findPreference("exit1"); //search by key
            myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {

                    //getActivity().finish();
                    //Toast.makeText(DataSyncPreferenceFragment.this.getActivity().getApplicationContext(), "TEST", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(DataSyncPreferenceFragment.this.getActivity().getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("EXIT", true);
                    startActivity(intent);

                    return true;
                }
            });
            */
        }
    }
}
