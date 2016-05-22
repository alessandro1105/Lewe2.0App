package com.lewetechnologies.app.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.lewetechnologies.app.activities.GSRChartActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by alessandro on 22/05/16.
 */
//---FRAGMENT CLASS GSR---
public class GSRMainFragment extends Fragment {

    //root view
    private View rootView;

    //gsr text
    private TextView data;

    //chart
    private LineChart chart;


    //costruttore
    public GSRMainFragment() {
        //richiede un costruttore vuoto
    }

    //factory method per la costruzuione del fragment
    public static GSRMainFragment newInstance() {

        //istanzio il fragment
        GSRMainFragment fragment = new GSRMainFragment();

        //ritorno il fragment
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //creo la vista
        rootView = inflater.inflate(R.layout.fragment_main_gsr, container, false); //TEMPERATURE

        //ottengo i riferimenti alla view
        data = (TextView) rootView.findViewById(R.id.data);
        chart = (LineChart) rootView.findViewById(R.id.chart);

        //cambio il font del valore mostrato
        Typeface blockFonts = Typeface.createFromAsset(GSRMainFragment.this.getContext().getAssets(), "fonts/DINCond-Bold.ttf");
        data.setTypeface(blockFonts);

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
                Intent activity = new Intent(GSRMainFragment.this.getActivity(), GSRChartActivity.class);
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
        chart.setMarkerView(new GSRTooltipMarkerView(GSRMainFragment.this.getContext(), R.layout.chart_tooltip));
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

    //chart YAxis formatter
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

    //chart tooltip formatter
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