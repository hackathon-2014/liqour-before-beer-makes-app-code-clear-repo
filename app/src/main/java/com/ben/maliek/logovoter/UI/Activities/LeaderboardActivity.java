package com.ben.maliek.logovoter.UI.Activities;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.ben.maliek.logovoter.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Droidweb on 8/23/2014.
 */
public class LeaderboardActivity extends Activity{

    private static String[] companies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard);

        // get the feels
        final Resources res = getResources();
        String[] feels = res.getStringArray(R.array.feels_array);

        final LinearLayout rowContainer = (LinearLayout)findViewById(R.id.feelsrowcontainer);
        companies = res.getStringArray(R.array.company_array);

        // for each feel pump out a view to add to the Scrollview
        for(String feel: feels){
            // set the title
            final View childview = getLayoutInflater().inflate(R.layout.feelsrow, rowContainer, false);
            TextView title = (TextView)childview.findViewById(R.id.titleTextview);
            title.setText(feel);

            // get the json
            RequestQueue mRequestQueue = Volley.newRequestQueue(this);
            String url = "http://192.168.8.23/logomotion/service/leaderboard/forCategory.php?category=" + feel;
            JsonArrayRequest req = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray jsonArray) {
                    Log.d("SUCCESS", jsonArray.toString());

                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject object = null; // lets get stuff
                        try {
                            object = jsonArray.getJSONObject(i);
                            LinearLayout innerRowContainer = (LinearLayout)childview.findViewById(R.id.horizontalScrollContainer);
                            // make a new company entry for each
                            View individualView = getLayoutInflater().inflate(R.layout.companyitem, innerRowContainer, false);
                            ImageView companyImage = (ImageView)individualView.findViewById(R.id.companyImageView);

                            int what = object.getInt("company_id");
                            try {
                                String companyName = companies[what - 1];
                                int resID = res.getIdentifier(companyName.toLowerCase(), "drawable", LeaderboardActivity.this.getPackageName());
                                companyImage.setImageDrawable(res.getDrawable(resID));
                            }
                            catch(Exception e)
                            {
                                Log.d("FAILURE","NO IMAGE FOR YOU");
                            }

                            TextView score = (TextView)individualView.findViewById(R.id.percentageView);
                            String niceFormat = String.format("%.1f%%", object.getDouble("percent")*100.0);
                            score.setText(niceFormat);

                            innerRowContainer.addView(individualView);

                        } catch (JSONException e) {
                            // whoops
                            Log.e("BEN DONE GOOFED", e.getLocalizedMessage());
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    // whoops
                    Log.e("BEN DONE GOOFED", volleyError.getLocalizedMessage());
                }
            });

            mRequestQueue.add(req);
            // add the images into the row


            rowContainer.addView(childview);
        }
    }
}
