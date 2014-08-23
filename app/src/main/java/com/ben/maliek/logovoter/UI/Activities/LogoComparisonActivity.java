package com.ben.maliek.logovoter.UI.Activities;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.ben.maliek.logovoter.R;

import java.util.Random;
import java.util.ResourceBundle;

public class LogoComparisonActivity extends Activity {
    private static String[] companies;
    private static String[] feels;
    private String feel, company1, company2;
    private Random r = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo_comparison);
        Resources res = getResources();
        // get the arrays
        companies = res.getStringArray(R.array.company_array);
        feels = res.getStringArray(R.array.feels_array);

        // pick two companies

        // pick the feel


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.logo_comparison, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
