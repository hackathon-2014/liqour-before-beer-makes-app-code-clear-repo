package com.ben.maliek.logovoter.UI.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ben.maliek.logovoter.R;

import java.util.Random;

public class LogoComparisonActivity extends Activity {
    private static final String COMPANY_1_KEY = "Slide to the right";
    private static final String COMPANY_2_KEY = "Take it back, now, ya'll";
    private static final String FEEL_KEY = "Turn around";
    private static final String CHOICE_MADE_KEY = "Reverse!";

    private String[] companies;
    private String[] feels;
    private int feelInt, company1Int, company2Int;
    private boolean choiceMade;
    private boolean isActive;
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

        final ImageView company1LogoView = (ImageView)findViewById(R.id.logo1);
        ImageView company2LogoView = (ImageView)findViewById(R.id.logo2);

        Button neitherButton = (Button)findViewById(R.id.neitherButton);

        company1Int = company2Int = feelInt = -1;
        choiceMade = false;
        if(savedInstanceState != null)
        {
            company1Int = savedInstanceState.getInt(COMPANY_1_KEY, -1);
            company2Int = savedInstanceState.getInt(COMPANY_2_KEY, -1);
            feelInt = savedInstanceState.getInt(FEEL_KEY, -1);

            choiceMade = savedInstanceState.getBoolean(CHOICE_MADE_KEY, false);
        }

        if(company1Int < 0 || company2Int < 0 || feelInt < 0)
        {
            // pick two companies
            company1Int = getRandomFromArray(companies, -1);
            company2Int = getRandomFromArray(companies, company1Int);

            // pick the feel
            feelInt = getRandomFromArray(feels, -1);
        }

        if(choiceMade) {
            final View loading = findViewById(R.id.loading_box);
            loading.setVisibility(View.VISIBLE);
        }

        // set the question
        question.setText(Html.fromHtml("Which Brand Makes You Feel <font color=\"red\">" + feels[feelInt] + "</font>?"));

        Log.d("whatchamacallit guys", companies[company1Int] + " " + companies[company2Int] + " " + feels[feelInt]);

        try {
            // fill in the logos
            int resID = res.getIdentifier(makesafe(companies[company1Int]), "drawable", LogoComparisonActivity.this.getPackageName());
            company1LogoView.setImageDrawable(res.getDrawable(resID));
        }
        catch (Exception e)
        {
            Log.d("FAILURE","NO PICTURES FOR YOU, IMAGE "+company1Int);
        }

        try {
            int resID = res.getIdentifier(makesafe(companies[company2Int]), "drawable", LogoComparisonActivity.this.getPackageName());
            company2LogoView.setImageDrawable(res.getDrawable(resID));
        }
        catch (Exception e)
        {
            Log.d("FAILURE","NO PICTURES FOR YOU, IMAGE "+company2Int);
        }

         // set the onlicks
        company1LogoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send server ping
                sendResult(company1Int, company2Int, feelInt, R.anim.fade_away_left);
            }
        });

        company2LogoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResult(company2Int, company1Int, feelInt, R.anim.fade_away_right);
            }
        });

        neitherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToNext(R.anim.fade_away_down);
            }
        });
    }

    private String makesafe(String company) {
        String safeCompany = company.toLowerCase();
        safeCompany = safeCompany.replace("'", "");
        return safeCompany;

    }

    private void sendResult(final int winner, int loser, int feelInt, final int animDir) {

        //If we already sent our choice, they just tapped it again.
        if(choiceMade){return;}

        choiceMade = true;
        final View loading = findViewById(R.id.loading_box);
        loading.setVisibility(View.VISIBLE);

        String url = HomeActivity.SERVER_ROOT+"/logomotion/service/battle/fight.php?winner=" + (winner + 1) + "&loser=" + (loser+1) + "&category=" + feels[feelInt];
        Log.d("DAT URL", url);
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest req = new StringRequest(url, new Response.Listener<String>(){

            @Override
            public void onResponse(String s) {
                Log.d("SUCCESS", s);
                loading.setVisibility(View.INVISIBLE);
                moveToNext(animDir);
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // whoops
                Log.e("BEN DONE GOOFED", volleyError.getLocalizedMessage());
            }
        });

        mRequestQueue.add(req);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        isActive = false;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        isActive = true;
    }

    private void moveToNext(int fadeAnim) {
        company1Int = -1;
        company2Int = -1;
        feelInt = -1;
        choiceMade = false;
        // on to the next one
        if(isActive) {
            Intent i = new Intent(LogoComparisonActivity.this, LogoComparisonActivity.class);
            startActivity(i);
            finish();
            overridePendingTransition(0,fadeAnim);
        }
    }

    private int getRandomFromArray(String[] array, int exclude) {
        int rando = r.nextInt(array.length);
        while(rando == exclude){

            // do it again
            rando = r.nextInt(array.length);
        }
        return rando;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        isActive = true;
        outState.putInt(COMPANY_1_KEY, company1Int);
        outState.putInt(COMPANY_2_KEY, company2Int);
        outState.putInt(FEEL_KEY, feelInt);
        outState.putBoolean(CHOICE_MADE_KEY, choiceMade);
    }
}
