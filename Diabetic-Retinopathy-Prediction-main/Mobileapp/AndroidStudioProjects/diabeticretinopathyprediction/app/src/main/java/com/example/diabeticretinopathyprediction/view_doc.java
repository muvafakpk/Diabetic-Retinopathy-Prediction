package com.example.diabeticretinopathyprediction;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class view_doc extends AppCompatActivity {
    String[] id, doc_name, doc_email, doc_q, doc_phone, doc_img;
    ListView li;
    SharedPreferences sh;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_doc);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        li = findViewById(R.id.li);
        String url = sh.getString("url", "") + "/android_view_doc";

        pd = new ProgressDialog(view_doc.this);
        pd.setMessage("Fetching....");
        pd.show();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {

                                JSONArray js = jsonObj.getJSONArray("users");
                                id = new String[js.length()];
                                doc_name = new String[js.length()];
                                doc_email = new String[js.length()];
                                doc_q = new String[js.length()];
                                doc_phone = new String[js.length()];
                                doc_img = new String[js.length()];

                                for (int i = 0; i < js.length(); i++) {
                                    JSONObject u = js.getJSONObject(i);
                                    id[i] = u.getString("id");
                                    doc_name[i] = u.getString("doc_name");
                                    doc_email[i] = u.getString("email");
                                    doc_q[i] = u.getString("doc_q");
                                    doc_phone[i] = u.getString("doc_phone");
                                    doc_img[i] = u.getString("doc_img");
                                }

                                li.setAdapter(new custom_view_doc(getApplicationContext(), id, doc_name, doc_email, doc_q, doc_phone, doc_img));
                            } else {
                                Toast.makeText(getApplicationContext(), "Not found", Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                return new HashMap<>(); // No location params
            }
        };

        int MY_SOCKET_TIMEOUT_MS = 100000;
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }
}
