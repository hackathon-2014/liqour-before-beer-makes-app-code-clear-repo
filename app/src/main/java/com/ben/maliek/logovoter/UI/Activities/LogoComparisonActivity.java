package com.ben.maliek.logovoter.UI.Activities;

import android.app.Activity;
import android.content.res.Resources;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.ben.maliek.logovoter.R;

import java.util.Random;

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
        TextView question = (TextView)findViewById(R.id.QuestioningTextview);
        // get the arrays
        companies = res.getStringArray(R.array.company_array);
        feels = res.getStringArray(R.array.feels_array);

        ImageView company1LogoView = (ImageView)findViewById(R.id.logo1);
        ImageView company2LogoView = (ImageView)findViewById(R.id.logo2);

        // pick two companies
        company1 = getRandomFromArray(companies, null);
        company2 = getRandomFromArray(companies, company1);

        // pick the feel
        feel = getRandomFromArray(feels, null);

        // set the question
        question.setText("Which Brand Makes You Feel " + feel + "?");

        Log.d("whatchamacallit guys", company1 + " " + company2 + " " + feel);
        // fill in the logos
        int resID = res.getIdentifier(company1.toLowerCase(), "drawable", LogoComparisonActivity.this.getPackageName());
        company1LogoView.setImageDrawable(res.getDrawable(resID));
        resID = res.getIdentifier(company2.toLowerCase(), "drawable", LogoComparisonActivity.this.getPackageName());
        company2LogoView.setImageDrawable(res.getDrawable(resID));



    }

    private String getRandomFromArray(String[] array, String exclude) {
        String rando = array[r.nextInt(array.length)];
        if(exclude != null && exclude.equals(rando)){
            // do it again
            getRandomFromArray(array, exclude);
        }
        return rando;
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
