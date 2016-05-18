package com.lewetechnologies.app;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
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
import android.widget.Toast;

import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.w3c.dom.Text;

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
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

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

                //selezione temperatura nella bottom bar
                MainActivity.this.setTemperatureSelected();

                //visualizzo il fragment scelto
                mViewPager.setCurrentItem(0, true);
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
                startActivityForResult(settingsActivity, Constants.REQUEST_EXIT_CODE);
            }

        });

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
            startActivityForResult(activity, Constants.REQUEST_EXIT_CODE);

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

        //TEST PER GRAFICO
        } else if (id == R.id.chart) {

            //avvio l'activity AboutActivity
            Intent activity = new Intent(this, Chart.class);
            startActivity(activity);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //controllo richiesta di chiusura app
        if (requestCode == Constants.REQUEST_EXIT_CODE && resultCode == Constants.RESULT_EXIT_CODE) {
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


    public static class TemperatureFragment extends Fragment {

        private View rootView;

        public TemperatureFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            //creo la vista
            rootView = inflater.inflate(R.layout.fragment_main_temperature, container, false); //TEMPERATURE

            //cambio il font del valore mostrato
            Typeface blockFonts = Typeface.createFromAsset(TemperatureFragment.this.getContext().getAssets(), "fonts/DINCond-Bold.ttf");
            ((TextView) rootView.findViewById(R.id.data)).setTypeface(blockFonts);

            //on click chart
            ((RelativeLayout) rootView.findViewById(R.id.chart_container)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //avvio l'activity TemperatureChart
                    Intent activity = new Intent(TemperatureFragment.this.getActivity(), TemperatureChartActivity.class);
                    startActivity(activity);
                }
            });

            //ritorno la vista
            return rootView;
        }
    }

    public static class GSRFragment extends Fragment {

        private View rootView;

        public GSRFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            //creo la vista
            rootView = inflater.inflate(R.layout.fragment_main_gsr, container, false); //TEMPERATURE

            //cambio il font del valore mostrato
            Typeface blockFonts = Typeface.createFromAsset(GSRFragment.this.getContext().getAssets(), "fonts/DINCond-Bold.ttf");
            ((TextView) rootView.findViewById(R.id.data)).setTypeface(blockFonts);

            //on click chart
            ((RelativeLayout) rootView.findViewById(R.id.chart_container)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //avvio l'activity TemperatureChart
                    Intent activity = new Intent(GSRFragment.this.getActivity(), GSRChartActivity.class);
                    startActivity(activity);
                }
            });

            //ritorno la vista
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private TemperatureFragment temperatureFragment;
        private GSRFragment gsrFragment;


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);

            //costruisco i fragment
            temperatureFragment = new TemperatureFragment();
            gsrFragment = new GSRFragment();
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.

            switch (position) {

                //Temperature fragment
                case 0:
                    return temperatureFragment; //ritorno il fragment della temperatura

                //GSR fragment
                case 1:
                    return gsrFragment; //ritorno il fragment del GSR
            }

            //ritorno il fragment della temperatura nel caso di errore
            return temperatureFragment;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2; //due fragment
        }
    }

}
