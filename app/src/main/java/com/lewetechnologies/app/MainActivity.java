package com.lewetechnologies.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.w3c.dom.Text;

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


            LineChart chart = (LineChart) rootView.findViewById(R.id.chart);

            //onclick chart
            chart.setTouchEnabled(true); //abilito i gesti touch

            //listener
            chart.setOnChartGestureListener(new OnChartGestureListener() {
                @Override
                public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

                }

                @Override
                public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

                }

                @Override
                public void onChartLongPressed(MotionEvent me) {

                    //avvio l'activity TemperatureChart
                    Intent activity = new Intent(TemperatureFragment.this.getActivity(), TemperatureChartActivity.class);
                    startActivity(activity);

                }

                @Override
                public void onChartDoubleTapped(MotionEvent me) {

                }

                @Override
                public void onChartSingleTapped(MotionEvent me) {

                }

                @Override
                public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

                }

                @Override
                public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

                }

                @Override
                public void onChartTranslate(MotionEvent me, float dX, float dY) {

                }
            });

            //inizializzo il grafico
            initializeChartView(chart);

            //popolo il grafico
            ArrayList<String> xVals = new ArrayList<String>();
            ArrayList<Entry> yVals = new ArrayList<Entry>();

            //se ci sono dati
            if (getData(xVals, yVals)) { //riempio il grafico

                //creo il dataset
                LineDataSet set = new LineDataSet(yVals, "GSR");

                //inizializzo il dataset
                initializeDataSet(set);

                //aggiungo le etichette al dataset
                ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                dataSets.add(set); // add the datasets

                // create a data object with the datasets
                LineData data = new LineData(xVals, dataSets);

                // set data
                chart.setData(data);
            }

            //ritorno la vista
            return rootView;
        }

        private void initializeChartView(LineChart chart) {
            //description
            chart.setDescription("");
            chart.setNoDataText("No data available");
            chart.setNoDataTextDescription("Connect your LeweBand");

            //background
            chart.setBackgroundColor(Color.TRANSPARENT); //background color

            //legends
            chart.getLegend().setEnabled(false); //disabilita la leggenda

            //bordi
            chart.setDrawBorders(false); //elimina i bordi

            //animazione
            chart.animateXY(1500, 1500); //animazione di riempimento

            //chart offset
            chart.setExtraLeftOffset(20f);
            chart.setExtraRightOffset(33f);
            chart.setExtraBottomOffset(20f);
            chart.setExtraTopOffset(28f);

            //settings asse x
            chart.getXAxis().setDrawGridLines(false); //disabilita la grid dell'asse x
            chart.getXAxis().setDrawLabels(true); //abilita le label dell'asse x
            chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM); //posizione asse x
            chart.getXAxis().setTextColor(Color.parseColor("#666666"));
            chart.getXAxis().setAxisLineWidth(2f);

            //settings asse y
            chart.getAxisLeft().setDrawGridLines(true);//abilito la griglia
            chart.getAxisLeft().setGridColor(Color.parseColor("#BBBBBB")); //colore della griglia
            chart.getAxisRight().setEnabled(false); //disabilita l'asse di destra (right y)
            chart.setScaleYEnabled(false); //disabilita lo zoom dell'asse y
            chart.getAxisLeft().setTextColor(Color.parseColor("#666666")); //colore delle label
            chart.getAxisLeft().setAxisLineWidth(2f);

            //formatter
            chart.getAxisLeft().setValueFormatter(new TemperatureYAxisValueFormatter());

            //maker view
            chart.setMarkerView(new TemperatureTooltipMarkerView(TemperatureFragment.this.getContext(), R.layout.tooltip_chart));
        }

        private void initializeDataSet(LineDataSet set) {
            //line settings
            set.setLineWidth(2f); //line width

            //color
            set.setColor(Color.parseColor("#4EBCF4")); //colore linea
            set.setCircleColor(Color.parseColor("#3F51B5")); //colore cerchietti
            set.setDrawCircleHole(false); //cerchietti senza buco

            set.setDrawValues(false); //disabilito la visualizzazione dei valori

            set.setCircleRadius(5f); //dimensione del cerchietto
            set.setDrawCircleHole(false); //niente buco


            //dataset settings
            set.setDrawHighlightIndicators(false); //elimina le linee di highlight
            set.setDrawFilled(false); //elimina la colorazione dell'area sottostante al grafico
        }

        //getData
        private boolean getData(ArrayList<String> xVals, ArrayList<Entry> yVals) {
            //X labels
            xVals.add("16/05/2016 15:30");
            xVals.add("16/05/2016 15:35");
            xVals.add("16/05/2016 15:40");
            xVals.add("16/05/2016 15:45");
            xVals.add("16/05/2016 15:50");

            //Y data
            yVals.add(new Entry(34.5f, 0));
            yVals.add(new Entry(37.3f, 1));
            yVals.add(new Entry(37.5f, 2));
            yVals.add(new Entry(33.1f, 3));
            yVals.add(new Entry(32.1f, 4));

            return true;

        }

        public static class TemperatureYAxisValueFormatter implements YAxisValueFormatter {

            private DecimalFormat mFormat;

            public TemperatureYAxisValueFormatter () {
                mFormat = new DecimalFormat("##0.0"); // use one decimal
            }

            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                // write your logic here
                // access the YAxis object to get more information
                return mFormat.format(value) + " °C"; // e.g. append a dollar-sign
            }
        }

        private static class TemperatureTooltipMarkerView extends MarkerView {

            private TextView tvContent;

            public TemperatureTooltipMarkerView (Context context, int layoutResource) {
                super(context, layoutResource);
                // this markerview only displays a textview
                tvContent = (TextView) findViewById(R.id.tvContent);
            }

            // callbacks everytime the MarkerView is redrawn, can be used to update the
            // content (user-interface)
            @Override
            public void refreshContent(Entry e, Highlight highlight) {
                tvContent.setText("" + e.getVal() + "°C"); // set the entry-value as the display text
            }

            @Override
            public int getXOffset(float xpos) {
                // this will center the marker-view horizontally
                return -(getWidth() / 2);
            }

            @Override
            public int getYOffset(float ypos) {
                // this will cause the marker-view to be above the selected value
                return -getHeight();
            }
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

            LineChart chart = (LineChart) rootView.findViewById(R.id.chart);

            //onclick chart
            chart.setTouchEnabled(true); //abilito i gesti touch

            //listener
            chart.setOnChartGestureListener(new OnChartGestureListener() {
                @Override
                public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

                }

                @Override
                public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

                }

                @Override
                public void onChartLongPressed(MotionEvent me) {

                    //avvio l'activity GSRChart
                    Intent activity = new Intent(GSRFragment.this.getActivity(), GSRChartActivity.class);
                    startActivity(activity);

                }

                @Override
                public void onChartDoubleTapped(MotionEvent me) {

                }

                @Override
                public void onChartSingleTapped(MotionEvent me) {

                }

                @Override
                public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

                }

                @Override
                public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

                }

                @Override
                public void onChartTranslate(MotionEvent me, float dX, float dY) {

                }
            });

            //inizializzo il grafico
            initializeChartView(chart);

            //popolo il grafico
            ArrayList<String> xVals = new ArrayList<String>();
            ArrayList<Entry> yVals = new ArrayList<Entry>();

            //se ci sono dati
            if (getData(xVals, yVals)) { //riempio il grafico

                //creo il dataset
                LineDataSet set = new LineDataSet(yVals, "GSR");

                //inizializzo il dataset
                initializeDataSet(set);

                //aggiungo le etichette al dataset
                ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                dataSets.add(set); // add the datasets

                // create a data object with the datasets
                LineData data = new LineData(xVals, dataSets);

                // set data
                chart.setData(data);
            }

            //ritorno la vista
            return rootView;
        }

        private void initializeChartView(LineChart chart) {
            //description
            chart.setDescription("");
            chart.setNoDataText("No data available");
            chart.setNoDataTextDescription("Connect your LeweBand");

            //background
            chart.setBackgroundColor(Color.TRANSPARENT); //background color

            //legends
            chart.getLegend().setEnabled(false); //disabilita la leggenda

            //bordi
            chart.setDrawBorders(false); //elimina i bordi

            //animazione
            chart.animateXY(1500, 1500); //animazione di riempimento

            //chart offset
            chart.setExtraLeftOffset(20f);
            chart.setExtraRightOffset(33f);
            chart.setExtraBottomOffset(20f);
            chart.setExtraTopOffset(28f);

            //settings asse x
            chart.getXAxis().setDrawGridLines(false); //disabilita la grid dell'asse x
            chart.getXAxis().setDrawLabels(true); //abilita le label dell'asse x
            chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM); //posizione asse x
            chart.getXAxis().setTextColor(Color.parseColor("#666666"));
            chart.getXAxis().setAxisLineWidth(2f);

            //settings asse y
            chart.getAxisLeft().setDrawGridLines(true);//abilito la griglia
            chart.getAxisLeft().setGridColor(Color.parseColor("#BBBBBB")); //colore della griglia
            chart.getAxisRight().setEnabled(false); //disabilita l'asse di destra (right y)
            chart.setScaleYEnabled(false); //disabilita lo zoom dell'asse y
            chart.getAxisLeft().setTextColor(Color.parseColor("#666666")); //colore delle label
            chart.getAxisLeft().setAxisLineWidth(2f);

            //formatter
            chart.getAxisLeft().setValueFormatter(new GSRYAxisValueFormatter());

            //maker view
            chart.setMarkerView(new GSRTooltipMarkerView(GSRFragment.this.getContext(), R.layout.tooltip_chart));
        }

        private void initializeDataSet(LineDataSet set) {
            //line settings
            set.setLineWidth(2f); //line width

            //color
            set.setColor(Color.parseColor("#4EBCF4")); //colore linea
            set.setCircleColor(Color.parseColor("#3F51B5")); //colore cerchietti
            set.setDrawCircleHole(false); //cerchietti senza buco

            set.setDrawValues(false); //disabilito la visualizzazione dei valori

            set.setCircleRadius(5f); //dimensione del cerchietto
            set.setDrawCircleHole(false); //niente buco


            //dataset settings
            set.setDrawHighlightIndicators(false); //elimina le linee di highlight
            set.setDrawFilled(false); //elimina la colorazione dell'area sottostante al grafico
        }

        //getData
        private boolean getData(ArrayList<String> xVals, ArrayList<Entry> yVals) {
            //X labels
            xVals.add("16/05/2016 15:30");
            xVals.add("16/05/2016 15:35");
            xVals.add("16/05/2016 15:40");
            xVals.add("16/05/2016 15:45");
            xVals.add("16/05/2016 15:50");

            //Y data
            yVals.add(new Entry(50f, 0));
            yVals.add(new Entry(15f, 1));
            yVals.add(new Entry(0f, 2));
            yVals.add(new Entry(60f, 3));
            yVals.add(new Entry(25f, 4));

            return true; //ci sono dati

        }

        public static class GSRYAxisValueFormatter implements YAxisValueFormatter {

            private DecimalFormat mFormat;

            public GSRYAxisValueFormatter () {
                mFormat = new DecimalFormat("##0.0"); // use one decimal
            }

            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                // write your logic here
                // access the YAxis object to get more information
                return mFormat.format(value) + "%"; // e.g. append a dollar-sign
            }
        }

        private static class GSRTooltipMarkerView extends MarkerView {

            private TextView tvContent;

            public GSRTooltipMarkerView (Context context, int layoutResource) {
                super(context, layoutResource);
                // this markerview only displays a textview
                tvContent = (TextView) findViewById(R.id.tvContent);
            }

            // callbacks everytime the MarkerView is redrawn, can be used to update the
            // content (user-interface)
            @Override
            public void refreshContent(Entry e, Highlight highlight) {
                tvContent.setText("" + e.getVal() + "%"); // set the entry-value as the display text
            }

            @Override
            public int getXOffset(float xpos) {
                // this will center the marker-view horizontally
                return -(getWidth() / 2);
            }

            @Override
            public int getYOffset(float ypos) {
                // this will cause the marker-view to be above the selected value
                return -getHeight();
            }
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
