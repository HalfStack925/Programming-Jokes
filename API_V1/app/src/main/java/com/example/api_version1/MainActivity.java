package com.example.api_version1;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    //initializing
    private TextView setUp;
    private TextView punchLine;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //display the icon in your action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        //assigning
        Button btnJoke = findViewById(R.id.btnJoke);
        setUp = findViewById(R.id.txtSetUP);
        punchLine = findViewById(R.id.txtPunchLine);

        //using volley queue
        queue = Volley.newRequestQueue(this);


        //onclick listener event for json that will print jokes onto main page
        btnJoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                jsonParse();

            }
        });
    }

    //jsonparse pulling api from jokes
    private void jsonParse(){

        // API address
        String url = "https://official-joke-api.appspot.com/jokes/programming/random";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parseData(response);
                    try {
                    //loops through JSON array
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        //populates setup string
                        setUp.setText(jsonObject.getString("setup"));

                        //3 second delay to display the punchline
                        CountDownTimer countDownTimer = new CountDownTimer(3000,1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                try {
                                    //populates punchline string
                                    punchLine.setText(jsonObject.getString("punchline"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                //hides punch line
                                punchLine.setVisibility(View.INVISIBLE);
                            }
                            @Override
                            public void onFinish() {
                                //displays punch line and plays drum sound
                                punchLine.setVisibility(View.VISIBLE);
                                MediaPlayer drum;
                                drum = MediaPlayer.create(MainActivity.this, R.raw.drum);
                                drum.start();
                            }
                       }.start();
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            }
        });
        queue.add(request);
    }

}