package com.lewetechnologies.app;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.text.DecimalFormat;
import java.util.ArrayList;

public class TemperatureChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature_chart);

        LineChart chart = (LineChart) findViewById(R.id.chart);

        //inizializzo il grafico
        initializeChartView(chart);

        //popolo il grafico
        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        getData(xVals, yVals);

        //creo il dataset
        LineDataSet set = new LineDataSet(yVals, "Temperature");

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

    private void initializeChartView(LineChart chart) {
        //description
        chart.setDescription("");
        chart.setNoDataTextDescription("");

        //background
        chart.setBackgroundColor(Color.TRANSPARENT); //background color

        //legends
        chart.getLegend().setEnabled(false); //disabilita la leggenda

        //bordi
        chart.setDrawBorders(false); //elimina i bordi

        //animazione
        chart.animateXY(1500, 1500); //animazione di riempimento

        //settings asse x
        chart.getXAxis().setDrawGridLines(false); //disabilita la grid dell'asse x
        chart.getXAxis().setDrawLabels(true); //abilita le label dell'asse x
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM); //posizione asse x
        chart.setExtraLeftOffset(20f);
        chart.setExtraRightOffset(50f);
        chart.getXAxis().setTextColor(Color.parseColor("#FFFFFF"));

        //settings asse y
        chart.getAxisLeft().setDrawGridLines(false);//disabilita la griglia dell'asse y
        chart.getAxisRight().setEnabled(false); //disabilita l'asse di destra (right y)
        chart.setScaleYEnabled(false); //disabilita lo zoom dell'asse y
        chart.getAxisLeft().setTextColor(Color.parseColor("#FFFFFF")); //colore delle label

        //formatter
        chart.getAxisLeft().setValueFormatter(new TemperatureYAxisValueFormatter());

        //maker view
        chart.setMarkerView(new TooltipMarkerView(getApplicationContext(), R.layout.tooltip_chart));
    }

    private void initializeDataSet(LineDataSet set) {
        //line settings
        set.setLineWidth(2f); //line width

        //color
        set.setColor(Color.parseColor("#FFFFFF"));
        set.setCircleColor(Color.parseColor("#FFFFFF"));

        set.setDrawValues(false); //disabilito la visualizzazione dei valori

        set.setCircleRadius(5f); //dimensione del cerchietto
        set.setDrawCircleHole(false); //niente buco


        //dataset settings
        set.setDrawHighlightIndicators(false); //elimina le linee di highlight
        set.setDrawFilled(false); //elimina la colorazione dell'area sottostante al grafico
    }

    //getData
    private void getData(ArrayList<String> xVals, ArrayList<Entry> yVals) {
        //X labels
        xVals.add("16/05/2016 15:30");
        xVals.add("16/05/2016 15:35");
        xVals.add("16/05/2016 15:40");
        xVals.add("16/05/2016 15:45");
        xVals.add("16/05/2016 15:50");
        xVals.add("16/05/2016 15:55");
        xVals.add("16/05/2016 16:00");
        xVals.add("16/05/2016 16:05");
        xVals.add("16/05/2016 16:10");
        xVals.add("16/05/2016 16:15");

        //Y data
        yVals.add(new Entry(34.5f, 0));
        yVals.add(new Entry(37.3f, 1));
        yVals.add(new Entry(37.5f, 2));
        yVals.add(new Entry(33.1f, 3));
        yVals.add(new Entry(32.1f, 4));
        yVals.add(new Entry(35.0f, 5));
        yVals.add(new Entry(37.3f, 6));
        yVals.add(new Entry(37.5f, 7));
        yVals.add(new Entry(33.1f, 8));
        yVals.add(new Entry(32.1f, 9));
    }


    private static class TooltipMarkerView extends MarkerView {

        private TextView tvContent;

        public TooltipMarkerView (Context context, int layoutResource) {
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
}
