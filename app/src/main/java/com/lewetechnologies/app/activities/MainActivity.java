package com.lewetechnologies.app.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.lewetechnologies.app.R;
import com.lewetechnologies.app.configs.Config;
import com.lewetechnologies.app.fragments.GSRMainFragment;
import com.lewetechnologies.app.fragments.TemperatureMainFragment;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                //cambio l'elemento selezionato nella bottombar se cambia la pagina
                @Override
                public void onPageSelected(int position) {
                    if (position == 0) { //selezionata temperatura

                        MainActivity.this.setTemperatureSelected();

                    } else if (position == 1) { //selezionata pagina gsr
                        MainActivity.this.setGSRSelected();
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            }

        );


        //on click listener exitButton
        ((ImageButton) findViewById(R.id.exitButton)).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //chiudo l'app al click
                MainActivity.this.shutdownApp();
            }

        });

        //inizializzo la bottom bar
        setTemperatureSelected();

        //onclick bottom bar
        //temperature
        ((RelativeLayout) findViewById(R.id.bottombar_action_temperature)).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                /*//selezione temperatura nella bottom bar
                MainActivity.this.setTemperatureSelected();

                //visualizzo il fragment scelto
                mViewPager.setCurrentItem(0, true);*/

                //mSectionsPagerAdapter.update();
                //mSectionsPagerAdapter.notifyDataSetChanged();

                ((TemperatureMainFragment) mSectionsPagerAdapter.getItem(0)).uodate();
            }

        });

        //gsr
        ((RelativeLayout) findViewById(R.id.bottombar_action_gsr)).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                //selezione GSR nella bottom bar
                MainActivity.this.setGSRSelected();

                //visualizzo il fragment scelto
                mViewPager.setCurrentItem(1, true);
            }

        });
        //settings
        ((RelativeLayout) findViewById(R.id.bottombar_action_settings)).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //avvio l'activity SettingsActivity
                Intent settingsActivity = new Intent(MainActivity.this, SettingsActivity.class);
                startActivityForResult(settingsActivity, Config.REQUEST_EXIT_CODE);
            }

        });


        //TEST PREFERENZE
        /*SharedPreferences preferences = getApplicationContext().getSharedPreferences(Config.SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean("status", true);

        editor.commit();*/


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            //avvio l'activity SettingsActivity
            Intent activity = new Intent(this, SettingsActivity.class);
            startActivityForResult(activity, Config.REQUEST_EXIT_CODE);

            return true;

        } else if (id == R.id.action_credits) {

            //avvio l'activity CreditsActivity
            Intent activity = new Intent(this, CreditsActivity.class);
            startActivity(activity);

            return true;

        } else if (id == R.id.action_about) {

            //avvio l'activity AboutActivity
            Intent activity = new Intent(this, AboutActivity.class);
            startActivity(activity);

            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //controllo richiesta di chiusura app
        if (requestCode == Config.REQUEST_EXIT_CODE && resultCode == Config.RESULT_EXIT_CODE) {
            //chiudo l'app
            shutdownApp();
        }
    }

    //metodo che chiude l'app e tutti i suoi componenti
    private void shutdownApp() {
        //chiudo i servizi attivi


        //chiudo l'activity
        finish();
    }

    //Handler bottom bar
    public void setTemperatureSelected() {
        //setto la nuova icona e il colore del testo della bottombar dell'elemento selezionato
        ((ImageView) findViewById(R.id.bottombar_icon_temperature)).setImageDrawable(MainActivity.this.getDrawable(R.drawable.ic_temperature_selected));
        ((TextView) findViewById(R.id.bottombar_text_temperature)).setTextColor(ContextCompat.getColor(getApplication(), R.color.activity_main_bottombar_element_description_text_color_selected));

        //resetto la bottombar
        //setto la nuova icona e il colore del testo della bottombar dell'elemento selezionato
        ((ImageView) findViewById(R.id.bottombar_icon_gsr)).setImageDrawable(MainActivity.this.getDrawable(R.drawable.ic_gsr));
        ((TextView) findViewById(R.id.bottombar_text_gsr)).setTextColor(ContextCompat.getColor(getApplication(), R.color.activity_main_bottombar_element_description_text_color));
    }

    public void setGSRSelected() {
        //setto la nuova icona e il colore del testo della bottombar dell'elemento selezionato
        ((ImageView) findViewById(R.id.bottombar_icon_gsr)).setImageDrawable(MainActivity.this.getDrawable(R.drawable.ic_gsr_selected));
        ((TextView) findViewById(R.id.bottombar_text_gsr)).setTextColor(ContextCompat.getColor(getApplication(), R.color.activity_main_bottombar_element_description_text_color_selected));

        //resetto la bottombar
        //setto la nuova icona e il colore del testo della bottombar dell'elemento selezionato
        ((ImageView) findViewById(R.id.bottombar_icon_temperature)).setImageDrawable(MainActivity.this.getDrawable(R.drawable.ic_temperature));
        ((TextView) findViewById(R.id.bottombar_text_temperature)).setTextColor(ContextCompat.getColor(getApplication(), R.color.activity_main_bottombar_element_description_text_color));

    }







    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        TemperatureMainFragment tf;

        private String test = "hey";

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);

            tf = new TemperatureMainFragment();
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.

            switch (position) {

                //Temperature fragment
                case 0:
                    return tf; //ritorno il fragment della temperatura

                //GSR fragment
                case 1:
                    return new GSRMainFragment(); //ritorno il fragment del GSR
            }

            //ritorno il fragment della temperatura nel caso di errore
            return null;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2; //due fragment
        }

        public void update() {
            test = "ciao";
        }
    }

}
