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
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class custom_view_doc extends BaseAdapter {
   String[] id,doc_name, doc_email, doc_q, doc_phone,doc_img;
    private Context context;
    SharedPreferences sh;
    public  custom_view_doc(Context context, String[] id, String[] doc_name, String[] doc_email, String[] doc_q, String[] doc_phone, String[] doc_img){
        this.context = context;
        this.id=id;
        this.doc_name=doc_name;
        this.doc_email=doc_email;
        this.doc_q=doc_q;
        this.doc_phone=doc_phone;
        this.doc_img=doc_img;
        sh= PreferenceManager.getDefaultSharedPreferences(context);



    }



    @Override
    public int getCount() {
        return doc_q.length;
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
            gridView=inflator.inflate(R.layout.activity_custom_view_doc,null);

        }
        else
        {
            gridView=(View)view;

        }
        TextView tv1=(TextView)gridView.findViewById(R.id.textView5);
        TextView tv2=(TextView)gridView.findViewById(R.id.textView7);
        TextView tv3=(TextView)gridView.findViewById(R.id.textView8);
        TextView tv4=(TextView)gridView.findViewById(R.id.textView9);
        ImageView img = (ImageView)gridView.findViewById(R.id.imageView);
        Button tv5=(Button)gridView.findViewById(R.id.button4);

        tv1.setText(doc_name[i]);
        tv2.setText(doc_email[i]);
        tv3.setText(doc_q[i]);
        tv4.setText(doc_phone[i]);
        tv5.setTag(i);
        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor ed = sh.edit();
                ed.putString("did", id[pos]);
                ed.commit();
                Intent i = new Intent(context, view_schedule.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });

        String url=sh.getString("url", "")+doc_img[i];
        Picasso.with(context).load(url).transform(new CircleTransform()). into(img);

        return gridView;
    }
}