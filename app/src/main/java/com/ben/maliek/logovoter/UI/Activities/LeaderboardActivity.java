package com.ben.maliek.logovoter.UI.Activities;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.ben.maliek.logovoter.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Droidweb on 8/23/2014.
 */
public class LeaderboardActivity extends Activity{

    private static final String leaderFeederKey = "I'm Henry the 8th, I am";

    private String[] companies;
    private JSONObject leaderFeeds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard);

        // get the feels
        final Resources res = getResources();
        String[] feels = res.getStringArray(R.array.feels_array);

        final LinearLayout rowContainer = (LinearLayout)findViewById(R.id.feelsrowcontainer);
        companies = res.getStringArray(R.array.company_array);

        leaderFeeds = new JSONObject();

        // for each feel pump out a view to add to the Scrollview
        for(final String feel: feels){
            // set the title
            final View childview = getLayoutInflater().inflate(R.layout.feelsrow, rowContainer, false);
            TextView title = (TextView)childview.findViewById(R.id.titleTextview);
            title.setText(feel);

            // get the json
            JSONArray feelData = null;

            if(savedInstanceState != null){
                String savedObj = savedInstanceState.getString(leaderFeederKey, "");
                if(savedObj.length() > 0)
                {
                    try {
                        leaderFeeds = new JSONObject(savedObj);
                    }
                    catch(Exception e){
                        //rats.
                        Log.d("FAILURE","So Json objects are weird.");
                    }
                }
            }

            try {
                if (leaderFeeds != null) {
                    feelData = leaderFeeds.getJSONArray(feel);
                }
            }
            catch (Exception e)
            {
                Log.d("FAILURE","Guess we didn't cache "+feel);
            }

            if(feelData != null)
            {
                prepareUI(childview, feel);
            }
            else {
                RequestQueue mRequestQueue = Volley.newRequestQueue(this);
                String url = "http://192.168.8.23/logomotion/service/leaderboard/forCategory.php?category=" + feel;
                JsonArrayRequest req = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        Log.d("SUCCESS", jsonArray.toString());
                        try {
                            leaderFeeds.put(feel, jsonArray);
                        }
                        catch (Exception e)
                        {
                            Log.d("FAILURE","Man, couldn't cache json.");
                        }
                        prepareUI(childview, feel);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        // whoops
                        Log.e("BEN DONE GOOFED", volleyError.getLocalizedMessage());
                    }
                });

                mRequestQueue.add(req);
            }
            // add the images into the row


            rowContainer.addView(childview);
        }
    }

    private void prepareUI(View childview, String feel)
    {
        try {
            JSONArray jsonArray = leaderFeeds.getJSONArray(feel);
            Resources res = getResources();

            View spinner = childview.findViewById(R.id.spinner);
            View scrollView = childview.findViewById(R.id.horizontalScrollView);

            spinner.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = null; // lets get stuff
                try {
                    object = jsonArray.getJSONObject(i);
                    LinearLayout innerRowContainer = (LinearLayout) childview.findViewById(R.id.horizontalScrollContainer);
                    // make a new company entry for each
                    View individualView = getLayoutInflater().inflate(R.layout.companyitem, innerRowContainer, false);
                    ImageView companyImage = (ImageView) individualView.findViewById(R.id.companyImageView);

                    int what = object.getInt("company_id");
                    try {
                        String companyName = LogoComparisonActivity.makesafe(companies[what - 1]);
                        int resID = res.getIdentifier(companyName.toLowerCase(), "drawable", LeaderboardActivity.this.getPackageName());
                        companyImage.setImageDrawable(res.getDrawable(resID));
                    } catch (Exception e) {
                        Log.d("FAILURE", "NO IMAGE FOR YOU, "+what);
                    }

                    TextView score = (TextView) individualView.findViewById(R.id.percentageView);
                    String niceFormat = String.format("%.1f%%", object.getDouble("percent") * 100.0);
                    score.setText(niceFormat);

                    innerRowContainer.addView(individualView);

                } catch (JSONException e) {
                    // whoops
                    Log.e("BEN DONE GOOFED", e.getLocalizedMessage());
                }
            }
        }
        catch(Exception e)
        {
            Log.d("FAILURE","Guess that row won't work.");
        }
    }

    @Override
    protected void onSaveInstanceState (Bundle outState)
    {
        outState.putString(leaderFeederKey, leaderFeeds.toString());
    }
}
