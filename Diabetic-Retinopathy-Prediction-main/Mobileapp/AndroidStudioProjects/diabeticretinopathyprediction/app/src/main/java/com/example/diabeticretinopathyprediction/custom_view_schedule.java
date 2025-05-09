package com.example.diabeticretinopathyprediction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class custom_view_schedule extends BaseAdapter {
    String[] id,doc_date,doc_from,doc_to,token_A,amt;
    private Context context;
    public  custom_view_schedule(Context context, String[] id, String[] doc_date, String[] doc_from, String[] doc_to, String[] token_A, String[] amt){
        this.context = context;
        this.id=id;
        this.doc_date=doc_date;
        this.doc_from=doc_from;
        this.doc_to=doc_to;
        this.token_A=token_A;
        this.amt=amt;

    }



    @Override
    public int getCount() {
        return doc_date.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflator=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;
        if(view==null)
        {
            gridView=new View(context);
            //gridView=inflator.inflate(R.layout.customview, null);
            gridView=inflator.inflate(R.layout.activity_custom_view_schedule,null);

        }
        else
        {
            gridView=(View)view;

        }
        TextView tv1=(TextView)gridView.findViewById(R.id.textView24);
        TextView tv2=(TextView)gridView.findViewById(R.id.textView26);
        TextView tv3=(TextView)gridView.findViewById(R.id.textView27);
        TextView tv4=(TextView)gridView.findViewById(R.id.textView29);
        TextView tv6=(TextView)gridView.findViewById(R.id.textView21);
        Button tv5=(Button)gridView.findViewById(R.id.button5);





//        ImageView im=(ImageView) gridView.findViewById(R.id.imageView10);

//        tv6.setTextColor(Color.BLACK);


        tv1.setText(doc_date[i]);
        tv2.setText(doc_from[i]);
        tv3.setText(doc_to[i]);
        tv4.setText(token_A[i]);
        tv6.setText(amt[i]);
        tv5.setTag(i);
        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor ed = sh.edit();
                ed.putString("schid", id[pos]);
                ed.putString("amount",amt[pos]);
                ed.commit();
                Intent i = new Intent(context, PaymentActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
//                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context);
//                String url = sh.getString("url", "")+"/book_appointment";
//                RequestQueue requestQueue = Volley.newRequestQueue(context);
//                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
//
//                                // response
//                                try {
//                                    JSONObject jsonObj = new JSONObject(response);
//                                    if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
//                                        Toast.makeText(context, "booked", Toast.LENGTH_SHORT).show();
//                                    }
//                                    else if (jsonObj.getString("status").equalsIgnoreCase("bkd")) {
//                                        Toast.makeText(context, "already booked", Toast.LENGTH_SHORT).show();
//                                    }
//                                    else if (jsonObj.getString("status").equalsIgnoreCase("No")) {
//                                        Toast.makeText(context, "no more token", Toast.LENGTH_SHORT).show();
//                                    }
////                                    else {
////                                        Toast.makeText(context, "Not found", Toast.LENGTH_LONG).show();
////                                    }
//
//                                }    catch (Exception e) {
//                                    Toast.makeText(context, "Error" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        },
//                        new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                // error
//                                Toast.makeText(context, "eeeee" + error.toString(), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                ) {
//                    @Override
//                    protected Map<String, String> getParams() {
//
//                        Map<String, String> params = new HashMap<String, String>();
//                        params.put("lid",sh.getString("lid", ""));
//                        params.put("sid",id[i]);
//                        return params;
//                    }
//                };
//
//                int MY_SOCKET_TIMEOUT_MS=100000;
//
//                postRequest.setRetryPolicy(new DefaultRetryPolicy(
//                        MY_SOCKET_TIMEOUT_MS,
//                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//                requestQueue.add(postRequest);
            }
        });


//        SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(context);
//        String ip=sh.getString("ip","");
//
//        String url="http://" + ip + ":5000/static/game/"+gamecode[i]+".jpg";
//
//
//        Picasso.with(context).load(url).transform(new CircleTransform()). into(im);

        return gridView;
    }
}