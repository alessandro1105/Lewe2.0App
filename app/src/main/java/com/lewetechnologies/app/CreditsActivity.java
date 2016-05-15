package com.lewetechnologies.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CreditsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        //visualizzo il back button
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /*
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
    */


}
