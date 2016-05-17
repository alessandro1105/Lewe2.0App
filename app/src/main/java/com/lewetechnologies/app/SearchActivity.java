package com.lewetechnologies.app;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //stop search
        //ProgressBar progress = (ProgressBar) findViewById(R.id.progress);
        //progress.setIndeterminateDrawable(null);

        /*
        //change image icon
        ImageView image = (ImageView) findViewById(R.id.icon);
        image.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_error));
        */


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                onBackPressed();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
