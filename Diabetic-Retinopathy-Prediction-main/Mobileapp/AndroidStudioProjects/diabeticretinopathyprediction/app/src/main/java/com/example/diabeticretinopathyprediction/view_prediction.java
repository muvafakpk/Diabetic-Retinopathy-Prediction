package com.example.diabeticretinopathyprediction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class view_prediction extends AppCompatActivity {
    String[] id, date, prediction, image;
    ListView li;
    SharedPreferences sh;
    ProgressDialog pd;
    FloatingActionButton fab;

    @Override
    public void onBackPressed() {
        Intent ij = new Intent(getApplicationContext(), Homepage.class);
        startActivity(ij);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_prediction);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        li = findViewById(R.id.li);
        fab = findViewById(R.id.floatingActionButton3);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ij = new Intent(getApplicationContext(), prediction.class);
                startActivity(ij);
            }
        });

        String url = sh.getString("url", "") + "/android_view_prediction";

        pd = new ProgressDialog(view_prediction.this);
        pd.setMessage("Fetching....");
        pd.show();

        new Handler().postDelayed(() -> pd.dismiss(), 3000);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("VIEW_PREDICTION_RESPONSE", response);
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
                            JSONArray js = jsonObj.getJSONArray("users");
                            id = new String[js.length()];
                            date = new String[js.length()];
                            prediction = new String[js.length()];
                            image = new String[js.length()];

                            for (int i = 0; i < js.length(); i++) {
                                JSONObject u = js.getJSONObject(i);
                                id[i] = u.optString("id", "");
                                date[i] = u.optString("date", "");
                                prediction[i] = u.optString("pred", "");
                                image[i] = u.optString("image", "");
                            }

                            li.setAdapter(new custom_view_prediction(getApplicationContext(), id, date, prediction, image));
                        } else {
                            Toast.makeText(getApplicationContext(), "Not found", Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        Log.e("VIEW_PREDICTION_ERROR", e.getMessage());
                        Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(getApplicationContext(), "Error: " + error.toString(), Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("lid", sh.getString("lid", ""));
                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }
}
